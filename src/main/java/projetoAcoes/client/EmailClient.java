package projetoAcoes.client;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import projetoAcoes.model.Acao;
import projetoAcoes.model.AlertaPreco;

/**
 * Cliente de email, utilizando jakartaMail. O destinatário dos emails está no arquivo .env
 * com a chave "EMAIL_DESTINO"
 */
public class EmailClient {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private int emailsEnviados;
    private String ultimoAssunto;

    public EmailClient(String host, String port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.emailsEnviados = 0;
    }

    /**
     * Função genérica de enviar um email com um texto e assunto qualquer. Cria uma sessão
     * e faz 3 tentativas de enviar o email.
     * @param destinatario 
     * @param assunto
     * @param corpo
     * @throws MessagingException
     */
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
        
     // Faz 3 tentativas de enviar o email
        for (int i = 0; i < 3; i++) {
	        try {
				// Enviar a mensagem
				Transport.send(message);
				this.emailsEnviados++;
		        this.ultimoAssunto = assunto;
		        
				System.out.println("E-mail enviado!"); // sucesso
				return;
			} catch (MessagingException e) {
				// falha
				System.err.println("Falha ao enviar email, tentando novamente...");
	        }
        }
    }
    
    /**
     * Envia o email de alerta com um assunto e um corpo de mensagem informativos.
     * Faz 3 tentativas.
     * @param alerta
     * @param acao
     * @param emailDestinatario
     * @param compraOuVenda
     */
    public void enviarEmailAlerta(AlertaPreco alerta, Acao acao, String emailDestinatario,
    							String compraOuVenda) {
    	try {
    		
    		// precoAlvo é o preco de compra ou venda do alerta
    		double precoAlvo = compraOuVenda == "compra" ? alerta.getPrecoCompra() : alerta.getPrecoVenda();
    		
            String assunto = "Alerta de Preço para "+ compraOuVenda + " atingido - " + acao.getSimbolo();
            String corpo = String.format(
                "Olá!\n\nSeu alerta de "+ compraOuVenda +" para o ativo " + acao.getSimbolo() + " foi atingido.\n\n" +
                "Condição para a "+ compraOuVenda +": de R$%.2f\n" +
                "Preço Atual: R$%.2f\n\n",
                precoAlvo,
                acao.getPreco()
            );
            this.enviarEmail(emailDestinatario, assunto, corpo);
            
        } catch (Exception e) {
        	// TO-DO adicionar novas tentativas de envio de email
            System.err.println("Falha ao enviar e-mail de notificação: " + e.getMessage());
        }
    }
    
    /**
     * Conta o número de emails enviados. Está aqui por razões de debug
     * @return
     */
    public int getEmailsEnviados() {
        return this.emailsEnviados;
    }

    /**
     * retorna o ultimo assunto, está aqui para debugar também
     * @return
     */
    public String getUltimoAssunto() {
        return this.ultimoAssunto;
    }
    
}