package main.xiaoqihui.common.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailSenderService {

    private static final Logger log = LoggerFactory.getLogger(MailSenderService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;
    private final String fromAddress;

    public MailSenderService(
        ObjectProvider<JavaMailSender> mailSenderProvider,
        @Value("${app.mail.from:}") String fromAddress
    ) {
        this.mailSenderProvider = mailSenderProvider;
        this.fromAddress = fromAddress;
    }

    public void sendVerificationCode(String email, String scene, String code) {
        sendHtmlEmail(email, "校企慧邮箱验证码", buildVerificationHtml(scene, code));
    }

    public void sendHtmlEmail(String email, String subject, String html) {
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null) {
            log.warn("邮件发送器未配置，跳过发送: to={}, subject={}", email, subject);
            return;
        }
        if (!StringUtils.hasText(fromAddress)) {
            log.warn("发件人地址未配置，跳过发送: to={}, subject={}", email, subject);
            return;
        }
        try {
            log.info("开始发送邮件: from={}, to={}, subject={}", fromAddress, email, subject);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("邮件发送成功: to={}", email);
        } catch (Exception ex) {
            log.error("邮件发送失败: to={}, subject={}, 错误信息: {}", email, subject, ex.getMessage(), ex);
        }
    }

    private String buildVerificationHtml(String scene, String code) {
        return """
            <html>
            <body style="margin:0;padding:24px;background:#f5f7fa;font-family:Arial,'Microsoft YaHei',sans-serif;color:#1f2937;">
              <div style="max-width:560px;margin:0 auto;background:#ffffff;border-radius:16px;padding:32px;border:1px solid #e5e7eb;">
                <h2 style="margin:0 0 8px;color:#2563eb;">校企慧邮箱验证码</h2>
                <p style="margin:0 0 24px;color:#6b7280;">本次操作场景：%s</p>
                <div style="margin:24px 0;padding:20px;border-radius:12px;background:#eff6ff;text-align:center;">
                  <div style="font-size:14px;color:#6b7280;margin-bottom:8px;">您的验证码</div>
                  <div style="font-size:32px;letter-spacing:8px;font-weight:700;color:#1d4ed8;">%s</div>
                </div>
                <p style="margin:0 0 8px;">验证码 5 分钟内有效，请勿泄露给他人。</p>
                <p style="margin:0;color:#9ca3af;font-size:12px;">如非本人操作，请忽略本邮件。</p>
              </div>
            </body>
            </html>
            """.formatted(scene, code);
    }
}
