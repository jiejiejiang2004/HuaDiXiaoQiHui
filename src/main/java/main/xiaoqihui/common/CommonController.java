package main.xiaoqihui.common;

import jakarta.validation.Valid;
import main.xiaoqihui.common.api.ApiResponse;
import main.xiaoqihui.common.domain.EmailSendRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

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
}
