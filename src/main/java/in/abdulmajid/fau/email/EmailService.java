package in.abdulmajid.fau.email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.resend.core.exception.ResendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final Resend resend;
    private final String fromAddress;

    public EmailService(
            @Value("${resend.api.key}") String apiKey,
            @Value("${resend.from.address}") String fromAddress
    ) {
        this.resend = new Resend(apiKey);
        this.fromAddress = fromAddress;
    }

    public void sendEmail(String to, String subject, String html) {
        sendEmail(to, subject, html, null);
    }

    public void sendEmail(String to, String subject, String html, String text) {
        CreateEmailOptions.Builder builder = CreateEmailOptions.builder()
                .from(fromAddress)
                .to(to)
                .subject(subject)
                .html(html);

        if (text != null && !text.isBlank()) {
            builder.text(text);
        }

        try {
            CreateEmailResponse response = resend.emails().send(builder.build());
            log.info("Email sent to {}: id={}", to, response.getId());
        } catch (ResendException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
