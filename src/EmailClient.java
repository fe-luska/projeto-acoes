import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailClient {

    private final String host;
    private final String port;
    private final String username;
    private final String password;

    public EmailClient(String host, String port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public void enviarEmail(String destinatario, String assunto, String corpo) throws MessagingException {
        // Propriedades do servidor de email
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", this.host);
        props.put("mail.smtp.port", this.port);

        // Criação da sessão
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Criar a mensagem de e-mail
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(this.username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject(assunto);
        message.setText(corpo);

        System.out.println("Enviando e-mail para " + destinatario);
        
        // Enviar a mensagem
        Transport.send(message);
        
        System.out.println("E-mail enviado!");
    }
}