package main.xiaoqihui.common.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        JavaMailSender mailSender = mailSenderProvider.getIfAvailable();
        if (mailSender == null || !StringUtils.hasText(fromAddress)) {
            log.info("邮件发送模拟: to={}, scene={}, code={}", email, scene, code);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(email);
        message.setSubject("校企慧邮箱验证码");
        message.setText("场景：" + scene + "，验证码：" + code + "，5分钟内有效。");
        mailSender.send(message);
    }
}
