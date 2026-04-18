package main.xiaoqihui.common;

import main.xiaoqihui.admin.AdminMapper;
import main.xiaoqihui.admin.AdminMessageTemplateEntity;
import main.xiaoqihui.common.exception.BusinessException;
import main.xiaoqihui.common.mail.MailSenderService;
import main.xiaoqihui.common.util.JwtUtil;
import main.xiaoqihui.recruit.CompanyInfoEntity;
import main.xiaoqihui.recruit.MessageEntity;
import main.xiaoqihui.recruit.RecruitmentMapper;
import main.xiaoqihui.recruit.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class InternalService {

    private final RecruitmentMapper recruitmentMapper;
    private final AdminMapper adminMapper;
    private final MailSenderService mailSenderService;
    private final JwtUtil jwtUtil;
    private final String internalSecret;

    public InternalService(
        RecruitmentMapper recruitmentMapper,
        AdminMapper adminMapper,
        MailSenderService mailSenderService,
        JwtUtil jwtUtil,
        @Value("${app.internal.secret}") String internalSecret
    ) {
        this.recruitmentMapper = recruitmentMapper;
        this.adminMapper = adminMapper;
        this.mailSenderService = mailSenderService;
        this.jwtUtil = jwtUtil;
        this.internalSecret = internalSecret;
    }

    public void validateInternalSecret(String providedSecret) {
        if (providedSecret == null || providedSecret.isBlank() || !internalSecret.equals(providedSecret)) {
            throw new BusinessException(2002, "内部接口鉴权失败");
        }
    }

    public Map<String, Object> pushMessage(InternalMessagePushRequest request) {
        List<UserEntity> receivers = resolveReceivers(request.receiverType(), request.receiverIds());
        int successCount = 0;
        for (UserEntity receiver : receivers) {
            insertMessage(receiver.getUserId(), request.type(), request.title(), request.content(), request.bizId());
            successCount++;
            if (request.channels() != null && request.channels().stream().anyMatch("EMAIL"::equalsIgnoreCase)) {
                if (receiver.getEmail() != null && !receiver.getEmail().isBlank()) {
                    mailSenderService.sendHtmlEmail(receiver.getEmail(), request.title(), request.content());
                }
            }
        }
        return Map.of(
            "receiverType", request.receiverType(),
            "receiverCount", receivers.size(),
            "successCount", successCount
        );
    }

    public Map<String, Object> verifySsoToken(InternalSsoVerifyRequest request) {
        if (!jwtUtil.validateAccessToken(request.accessToken())) {
            return Map.of("valid", false);
        }
        String username = jwtUtil.extractUsername(request.accessToken());
        UserEntity user = recruitmentMapper.findUserByMobile(username);
        if (user == null) {
            return Map.of("valid", false);
        }
        return Map.of(
            "valid", true,
            "userId", user.getUserId(),
            "roles", List.of(user.getUserType())
        );
    }

    public Map<String, Object> sendEmail(InternalEmailSendRequest request) {
        String subject = request.subject();
        String content = request.content();
        if (request.templateCode() != null && !request.templateCode().isBlank()) {
            List<AdminMessageTemplateEntity> templates = adminMapper.listMessageTemplates(request.templateCode());
            if (!templates.isEmpty() && "ACTIVE".equalsIgnoreCase(templates.get(0).getEnabled())) {
                AdminMessageTemplateEntity template = templates.get(0);
                subject = renderTemplate(template.getTitleTemplate(), request.params());
                content = renderTemplate(template.getContentTemplate(), request.params());
            }
        }
        int successCount = 0;
        for (String email : request.to()) {
            mailSenderService.sendHtmlEmail(email, subject, content);
            successCount++;
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("successCount", successCount);
        data.put("subject", subject);
        return data;
    }

    private List<UserEntity> resolveReceivers(String receiverType, List<Long> receiverIds) {
        List<UserEntity> users = new ArrayList<>();
        for (Long receiverId : receiverIds) {
            if ("ENTERPRISE".equalsIgnoreCase(receiverType)) {
                CompanyInfoEntity company = recruitmentMapper.findCompanyById(receiverId);
                if (company == null) {
                    throw new BusinessException(6001, "企业不存在: " + receiverId);
                }
                users.add(requireUser(company.getUserId(), "ENTERPRISE"));
                continue;
            }
            if ("ADMIN".equalsIgnoreCase(receiverType)) {
                users.add(requireUser(receiverId, "ADMIN"));
                continue;
            }
            users.add(requireUser(receiverId, "CANDIDATE"));
        }
        return users;
    }

    private UserEntity requireUser(Long userId, String expectedUserType) {
        UserEntity user = recruitmentMapper.findUserById(userId);
        if (user == null || !expectedUserType.equalsIgnoreCase(user.getUserType())) {
            throw new BusinessException(3001, "接收人不存在: " + userId);
        }
        return user;
    }

    private void insertMessage(Long userId, String type, String title, String content, Long bizId) {
        MessageEntity message = new MessageEntity();
        message.setUserId(userId);
        message.setType(type);
        message.setTitle(title);
        message.setContent(content);
        message.setBizId(bizId);
        message.setReadStatus("UNREAD");
        recruitmentMapper.insertMessage(message);
    }

    private String renderTemplate(String template, Map<String, Object> params) {
        if (template == null || params == null || params.isEmpty()) {
            return template;
        }
        String rendered = template;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            rendered = rendered.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        return rendered;
    }
}
