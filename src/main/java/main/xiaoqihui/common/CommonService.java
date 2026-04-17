package main.xiaoqihui.common;

import main.xiaoqihui.common.domain.DictItemResponse;
import main.xiaoqihui.common.domain.FileRecordEntity;
import main.xiaoqihui.common.domain.SmsSendRequest;
import main.xiaoqihui.common.exception.BusinessException;
import main.xiaoqihui.common.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommonService {

    private static final Map<String, List<DictItemResponse>> STATIC_DICT = Map.of(
        "industry", List.of(
            new DictItemResponse("INTERNET", "互联网", 1),
            new DictItemResponse("EDUCATION", "教育培训", 2),
            new DictItemResponse("MANUFACTURE", "智能制造", 3)
        ),
        "education", List.of(
            new DictItemResponse("大专", "大专", 1),
            new DictItemResponse("本科", "本科", 2),
            new DictItemResponse("硕士", "硕士", 3)
        ),
        "salary", List.of(
            new DictItemResponse("0-5K", "0-5K", 1),
            new DictItemResponse("5K-10K", "5K-10K", 2),
            new DictItemResponse("10K-20K", "10K-20K", 3)
        ),
        "experience", List.of(
            new DictItemResponse("应届", "应届", 1),
            new DictItemResponse("1-3年", "1-3年", 2),
            new DictItemResponse("3-5年", "3-5年", 3)
        )
    );

    private static final Map<String, SmsCodeCache> SMS_CACHE = new ConcurrentHashMap<>();
    private static final Set<String> IMAGE_TYPES = Set.of("jpg", "jpeg", "png");
    private static final Set<String> RESUME_TYPES = Set.of("pdf", "doc", "docx");
    private static final Set<String> LICENSE_TYPES = Set.of("jpg", "jpeg", "png", "pdf");

    private final CommonMapper commonMapper;
    private final String uploadDir;

    public CommonService(CommonMapper commonMapper, @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.commonMapper = commonMapper;
        this.uploadDir = uploadDir;
    }

    public Map<String, Object> sendSmsCode(SmsSendRequest request) {
        String debugCode = "123456";
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(5);
        SMS_CACHE.put(buildSmsKey(request.mobile(), request.scene()), new SmsCodeCache(debugCode, expireAt));
        return Map.of("expireSeconds", 300, "debugCode", debugCode);
    }

    public void validateSmsCode(String mobile, String scene, String smsCode) {
        SmsCodeCache cache = SMS_CACHE.get(buildSmsKey(mobile, scene));
        if (cache == null || cache.expireAt().isBefore(LocalDateTime.now()) || !cache.code().equals(smsCode)) {
            throw new BusinessException(3004, "验证码错误或已过期");
        }
    }

    public Map<String, Object> uploadFile(MultipartFile file, String bizType) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(7001, "文件上传失败");
        }
        String extension = getExtension(file.getOriginalFilename());
        validateBizTypeAndFile(bizType, extension, file.getSize());

        String fileId = UUID.randomUUID().toString().replace("-", "");
        String storedName = fileId + "." + extension;
        Path targetDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path targetPath = targetDir.resolve(storedName);

        try {
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new BusinessException(7001, "文件上传失败");
        }

        FileRecordEntity entity = new FileRecordEntity();
        entity.setFileId(fileId);
        entity.setBizType(bizType);
        entity.setOriginalName(file.getOriginalFilename());
        entity.setFileUrl("/uploads/" + storedName);
        entity.setFilePath(targetPath.toString());
        entity.setFileSize(file.getSize());
        entity.setFileType(extension);
        entity.setUploaderId(SecurityUtils.getUserId());
        commonMapper.insertFileRecord(entity);

        return Map.of(
            "fileId", fileId,
            "fileName", file.getOriginalFilename(),
            "fileUrl", entity.getFileUrl(),
            "fileSize", file.getSize(),
            "fileType", extension
        );
    }

    public FileRecordEntity requireFileById(String fileId) {
        FileRecordEntity file = commonMapper.findFileById(fileId);
        if (file == null) {
            throw new BusinessException(7001, "文件不存在");
        }
        return file;
    }

    public Map<String, Object> getDict(String dictType) {
        List<DictItemResponse> items;
        if ("jobCategory".equals(dictType)) {
            items = commonMapper.listJobCategories();
        } else {
            items = STATIC_DICT.get(dictType);
        }
        if (items == null) {
            throw new BusinessException(1001, "不支持的字典类型");
        }
        return Map.of("dictType", dictType, "items", items);
    }

    private void validateBizTypeAndFile(String bizType, String extension, long size) {
        if (!StringUtils.hasText(extension)) {
            throw new BusinessException(7002, "文件格式不支持");
        }
        String lowerType = extension.toLowerCase();
        if ("RESUME".equalsIgnoreCase(bizType)) {
            if (!RESUME_TYPES.contains(lowerType)) {
                throw new BusinessException(7002, "文件格式不支持");
            }
            if (size > 10 * 1024 * 1024L) {
                throw new BusinessException(7003, "文件大小超限");
            }
            return;
        }
        if ("LICENSE".equalsIgnoreCase(bizType)) {
            if (!LICENSE_TYPES.contains(lowerType)) {
                throw new BusinessException(7002, "文件格式不支持");
            }
            if (size > 10 * 1024 * 1024L) {
                throw new BusinessException(7003, "文件大小超限");
            }
            return;
        }
        if (Set.of("LOGO", "AVATAR", "BANNER").contains(bizType.toUpperCase())) {
            if (!IMAGE_TYPES.contains(lowerType)) {
                throw new BusinessException(7002, "文件格式不支持");
            }
            if (size > 5 * 1024 * 1024L) {
                throw new BusinessException(7003, "文件大小超限");
            }
            return;
        }
        throw new BusinessException(7002, "不支持的业务类型");
    }

    private String getExtension(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    private String buildSmsKey(String mobile, String scene) {
        return mobile + ":" + scene;
    }

    private record SmsCodeCache(String code, LocalDateTime expireAt) {
    }
}
