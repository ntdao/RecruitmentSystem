package hust.seee.recruitmentsystem.service;

import hust.seee.recruitmentsystem.entity.Job;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Async
    public void sendConfirmEmail(String username, String receipientEmail, String link) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom("recruitmentsystemamj@gmail.com");
            helper.setTo(receipientEmail);

            String subject = "Confirm your email";
            helper.setSubject(subject);

            String content = "<p>Hello " + username + ",</p>"
                    + "<p>Thank you for registering. Please click on the below link to activate your account:</p>"
                    + "<p><a href=\"" + link + "\">Active Now</a></p>"
                    + "<br>"
                    + "<p>Link will expire in 15 minutes.</p>"
                    + "<p>See you soon</p>";
            helper.setText(content, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    public void sendResetPasswordEmail(String username, String recipientEmail, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("recruitmentsystemamj@gmail.com");
            helper.setTo(recipientEmail);

            String subject = "Link to reset your password";
            String content = "<p>Hello " + username + ",</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to change your password:</p>"
                    + "<p><a href=\"" + link + "\">Change my password</a></p>"
                    + "<br>"
                    + "<p>Link will expire in 15 minutes.</p>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    public void sendNotification(String username, String recipientEmail, String body, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("recruitmentsystemamj@gmail.com");
            helper.setTo(recipientEmail);

            String subject = "Kết quả ứng tuyển";
            String content = "<p>Hello " + username + ",</p>"
                    + "<p>" + body + "</p>"
                    + "<p>Nhấn <a href=\"" + link + "\">vào đây</a> để xem thông tin chi tiết</p>"
                    + "<br>"
                    + "<p>Nếu bạn có bất kỳ câu hỏi, vui lòng liên hệ recruitmentsystemamj@gmail.com để được hỗ trợ.</p>"
                    + "<p>Trân trọng\n"
                    + "Đội ngũ ANJ recruitment system</p>";
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    public void sendRecommendJob(String username, String recipientEmail, List<Job> job) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("recruitmentsystemamj@gmail.com");
            helper.setTo(recipientEmail);

            String body = generateHTMLContentForJobs(job);

            String subject = "Việc làm gợi ý cho bạn";
            String content = "<p>Xin chào " + username + ",</p>"
                    + "<p>AMJ Recuitment System mong muốn sẽ giúp bạn tìm được công việc mơ ước của mình. Dựa trên thông tin của bạn, chúng tôi đã tìm được những công việc phù hợp bạn:</p>"
                    + body
                    + "<p>Nếu bạn có bất kỳ câu hỏi, vui lòng liên hệ recruitmentsystemamj@gmail.com để được hỗ trợ.</p>"
                    + "<p>Trân trọng\n"
                    + "Đội ngũ ANJ recruitment system</p>";
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    public String generateHTMLContentForJobs(List<Job> jobs) {
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<ul>");
        for (Job job : jobs) {
            htmlContent.append("<li><a href=\"").append("http://localhost:3000/detail_jobs/").append(job.getJobId()).append("\">").append(job.getJobName()).append("</a></li>");
        }
        htmlContent.append("</ul>");
        return htmlContent.toString();
    }
}
