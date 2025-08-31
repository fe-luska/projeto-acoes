import java.io.IOException;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.MessagingException;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			
			Dotenv dotenv = Dotenv.load();
			FinnhubClient client = new FinnhubClient(dotenv.get("FINNHUB_API_KEY"));
			
			Acao acao;
			acao = client.buscarCotacao("AAPL");
			System.out.println(acao.toString());
		
	        EmailClient emailClient = new EmailClient(
	            dotenv.get("EMAIL_HOST"),
	            dotenv.get("EMAIL_PORT"),
	            dotenv.get("EMAIL_USERNAME"),
	            dotenv.get("EMAIL_PASSWORD")
	        );
	        
	        // Monta o corpo do e-mail com os dados da ação
	        String assunto = "Teste assunto ";
	        String corpoDoEmail = "Olá!\n\n"
	                            + acao.toString();
	
	        // Envia o e-mail
	        emailClient.enviarEmail(dotenv.get("EMAIL_DESTINO"), assunto, corpoDoEmail);
		
		} catch (IOException | InterruptedException e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		} catch (MessagingException e) {
			System.out.print("Erro ao enviar email");
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
		
		return;
		
	}

}
