package br.fai.ep.api.email;

import br.fai.ep.api.EpApiApplication;
import br.fai.ep.epEntities.DTO.MailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String SENDER = "Entenda e Proteja Projeto <supote_projeto_ep@outlook.com>";
    private final String URL_WEBSITE_EP = "http://entenda-e-proteja.us-east-1.elasticbeanstalk.com";

    public String buildMessage(final String username, final long userId) {
        final String message = "<html lang=\"pt-br\">" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>Suporte Projeto E&P </title>" +
                "</head>" +
                "<body>" +
                "Olá Sr(a)." + username + ".\n" +
                "\n" +
                "Recebemos sua solicitação para troca de senha.\n" +
                "Para acessar o link de acesso à página de troca de senha, " +
                "<a href=\"" + URL_WEBSITE_EP + "/account/check-exists-user/" + userId + "\">clique aqui!</a>" +
                "</body>" +
                "</html>";

        return message;
    }

    public String buildMessageContactUs(final MailDto mail) {
        final String message = "<html lang=\"pt-br\">" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">" +
                "    <title>Suporte Projeto E&P </title>" +
                "</head>" +
                "<body>" +
                "<strong>Email de contato</strong>: " + mail.getMailContact() + "<br>" +
                "<strong>Nome para identificação</strong>: " + mail.getUsername() + "<br>" +
                "<strong>Razão do contato</strong>: " + mail.getReason() + "<br><br>" +
                "<p style=\"white-space: pre;\">" +
                mail.getMessage().replaceAll("(\r\n|\n)", "<br />") +
                "</p>" +
                "</body>" +
                "</html>";

        return message;
    }

    public boolean send(final String receivers, final String subject, final String bodyEmail) {
        boolean isMessageSent = false;
        final AnnotationConfigApplicationContext aplication =
                new AnnotationConfigApplicationContext(EpApiApplication.class.getPackage().getName());
        mailSender = aplication.getBean(JavaMailSender.class);

        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(SENDER);
            helper.setTo(receivers);
            helper.setSubject(subject);
            helper.setText(bodyEmail, true);

            mailSender.send(message);
            isMessageSent = true;
        } catch (final MailException e) {
            e.printStackTrace();
        } catch (final MessagingException e) {
            e.printStackTrace();
        }

        return isMessageSent;
    }
}