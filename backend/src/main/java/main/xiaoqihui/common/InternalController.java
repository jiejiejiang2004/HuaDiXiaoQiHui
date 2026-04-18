package main.xiaoqihui.common;

import jakarta.validation.Valid;
import main.xiaoqihui.common.api.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruit/api/v1/internal")
public class InternalController {

    private final InternalService internalService;

    public InternalController(InternalService internalService) {
        this.internalService = internalService;
    }

    @PostMapping("/message/push")
    public ApiResponse<?> pushMessage(
        @RequestHeader(value = "X-Internal-Secret", required = false) String internalSecret,
        @Valid @RequestBody InternalMessagePushRequest request
    ) {
        internalService.validateInternalSecret(internalSecret);
        return ApiResponse.success(internalService.pushMessage(request));
    }

    @PostMapping("/sso/verify")
    public ApiResponse<?> verifySsoToken(
        @RequestHeader(value = "X-Internal-Secret", required = false) String internalSecret,
        @Valid @RequestBody InternalSsoVerifyRequest request
    ) {
        internalService.validateInternalSecret(internalSecret);
        return ApiResponse.success(internalService.verifySsoToken(request));
    }

    @PostMapping("/email/send")
    public ApiResponse<?> sendEmail(
        @RequestHeader(value = "X-Internal-Secret", required = false) String internalSecret,
        @Valid @RequestBody InternalEmailSendRequest request
    ) {
        internalService.validateInternalSecret(internalSecret);
        return ApiResponse.success(internalService.sendEmail(request));
    }
}
