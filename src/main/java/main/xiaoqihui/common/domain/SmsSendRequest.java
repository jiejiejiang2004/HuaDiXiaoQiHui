package main.xiaoqihui.common.domain;

import jakarta.validation.constraints.NotBlank;

public record SmsSendRequest(
    @NotBlank String mobile,
    @NotBlank String scene,
    @NotBlank String captcha,
    @NotBlank String captchaKey
) {
}
