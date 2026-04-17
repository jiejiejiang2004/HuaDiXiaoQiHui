package main.xiaoqihui.common;

import jakarta.validation.Valid;
import main.xiaoqihui.common.api.ApiResponse;
import main.xiaoqihui.common.domain.FileRecordEntity;
import main.xiaoqihui.common.domain.EmailSendRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/recruit/api/v1/common")
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/dict/{dictType}")
    public ApiResponse<?> getDict(@PathVariable String dictType) {
        return ApiResponse.success(commonService.getDict(dictType));
    }

    @PostMapping("/email/send")
    public ApiResponse<?> sendEmailCode(@Valid @RequestBody EmailSendRequest request) {
        return ApiResponse.success(commonService.sendEmailCode(request));
    }

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> uploadFile(
        @RequestPart("file") MultipartFile file,
        @RequestPart("bizType") String bizType
    ) {
        return ApiResponse.success(commonService.uploadFile(file, bizType));
    }

    @GetMapping("/file/download/{fileId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileId) {
        CommonService.DownloadableFile downloadableFile = commonService.loadDownloadableFile(fileId);
        FileRecordEntity metadata = downloadableFile.metadata();
        String fileName = metadata.getOriginalName();
        MediaType mediaType = resolveMediaType(metadata.getFileType());
        return ResponseEntity.ok()
            .contentType(mediaType)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                    .filename(fileName, StandardCharsets.UTF_8)
                    .build()
                    .toString()
            )
            .contentLength(downloadableFile.content().length)
            .body(new ByteArrayResource(downloadableFile.content()));
    }

    private MediaType resolveMediaType(String fileType) {
        if ("pdf".equalsIgnoreCase(fileType)) {
            return MediaType.APPLICATION_PDF;
        }
        if ("xlsx".equalsIgnoreCase(fileType)) {
            return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }
        if ("csv".equalsIgnoreCase(fileType)) {
            return MediaType.parseMediaType("text/csv");
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
