package br.fai.ep.api.service.email;

import br.fai.ep.api.EpApiApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailApi {

    @Autowired
    private JavaMailSender mailSender;

    public final String SENDER = "Lucas lima <lucaslima@fai-mg.br>";

    public void enviar(final Message message) {
        final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(message.getRemetente());
        simpleMailMessage.setTo(message.getDestinarios().toArray(new String[message.getDestinarios().size()]));
        simpleMailMessage.setSubject(message.getAssunto());
        simpleMailMessage.setText(message.getCorpo());

        mailSender.send(simpleMailMessage);
    }

    public boolean send(final String receivers, final String subject, final String message) {
        boolean isMessageSent = false;
        final AnnotationConfigApplicationContext aplication =
                new AnnotationConfigApplicationContext(EpApiApplication.class.getPackage().getName());
        mailSender = aplication.getBean(JavaMailSender.class);

        try {
            final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(SENDER);
            simpleMailMessage.setTo(receivers);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);

            mailSender.send(simpleMailMessage);
            isMessageSent = true;
        } catch (final MailException e) {
            e.printStackTrace();
        }

        return isMessageSent;
    }
}