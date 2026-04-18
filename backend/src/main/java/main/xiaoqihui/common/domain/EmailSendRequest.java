package main.xiaoqihui.common.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailSendRequest(
    @Email @NotBlank String email,
    @NotBlank String scene,
    @NotBlank String captcha,
    @NotBlank String captchaKey
) {
}
