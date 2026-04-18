package main.xiaoqihui.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

record InternalMessagePushRequest(
    @NotBlank String receiverType,
    @NotEmpty List<Long> receiverIds,
    @NotBlank String type,
    @NotBlank String title,
    @NotBlank String content,
    Long bizId,
    List<String> channels
) {
}

record InternalSsoVerifyRequest(
    @NotBlank String accessToken
) {
}

record InternalEmailSendRequest(
    @NotEmpty List<@Email String> to,
    @NotBlank String subject,
    @NotBlank String content,
    String templateCode,
    Map<String, Object> params
) {
}
