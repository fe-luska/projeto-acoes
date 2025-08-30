import java.io.IOException;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
	
	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.load();
		String finnhubKey = dotenv.get("FINNHUB_API_KEY");
		FinnhubClient client = new FinnhubClient(finnhubKey);
		Acao acao;
		
		try {
			acao = client.buscarCotacao("AAPL");
			System.out.println(acao.toString());
		} catch (IOException | InterruptedException e) {
			System.out.print(e);
		}
		
		return;
		
	}

}
