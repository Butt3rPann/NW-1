package sit.integrated.backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private void sendEmail(String to, String token, String subject, String path, String message) {
        try {
            String url = "http://localhost:5173/nw1" + path + "?jwtToken=" + token;

            String content = """
                <div style="font-family: Arial, sans-serif; line-height: 1.5; color: #333;">
                    <h2 style="color: #2E86C1;">Verify Your Email Address</h2>
                    <p>You've entered <strong style="color: #555;">%s</strong> as the email address for your account.</p>
                    <p>%s</p>
                    <p style="margin-top: 20px;">
                        <a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #2E86C1; color: white; text-decoration: none; border-radius: 5px;">
                            Verify Your Email
                        </a>
                    </p>
                    <p style="margin-top: 20px; font-size: 0.9em; color: #888;">
                        Please note: This verification link is only valid for a limited time.\s
                        If the link has expired, you will need to request a new verification email\s
                        to complete your account activation.
                    </p>
                    <p style="margin-top: 20px;">Thank you,<br/>ITB-MSHOP</p>
                </div>
                """.formatted(to, message, url);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            throw new RuntimeException("Failed to send verification email. Please try again later.", ex);
        }
    }

    public void sendVertificationEmail(String to, String vertificationToken) {
        String subject = "Email Vertification";
        String path = "/verify-email";
        String message = "Please verify this emial address by clicking button below.";
        sendEmail(to, vertificationToken, subject, path, message);
    }
}
