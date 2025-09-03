package projetoAcoes;

import io.github.cdimascio.dotenv.Dotenv;
import projetoAcoes.client.EmailClient;
import projetoAcoes.client.IbovFinancialsClient;
import projetoAcoes.model.AlertaPreco;
import projetoAcoes.service.MonitorService;

public class Main {
	
	public static void main(String[] args) { 
		
		verificarArgumentos(args);
		
		try {
			
			// carrega as variaveis do arquivo .env
			Dotenv dotenv = Dotenv.load();
			
			// cliente dos dados da B3
			IbovFinancialsClient apiClient = new IbovFinancialsClient(dotenv.get("IBOV_FINANCIALS_TOKEN"));
		
			// cliente de email
	        EmailClient emailClient = new EmailClient(
	            dotenv.get("EMAIL_HOST"),
	            dotenv.get("EMAIL_PORT"),
	            dotenv.get("EMAIL_USERNAME"),
	            dotenv.get("EMAIL_PASSWORD")
	        );
	        
	        // serviço que fará o monitoramento do ativo
	        MonitorService monitorService = new MonitorService(apiClient, emailClient, dotenv.get("EMAIL_DESTINO"));
	        
	        // alerta que será passado para o monitorService
	        AlertaPreco alerta = new AlertaPreco(args[0], Double.parseDouble(args[1]),
	        									Double.parseDouble(args[2]));
	        
	        monitorService.setAlerta(alerta);
	        monitorService.iniciarMonitoramento();
	        
		
		} catch (Exception e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
		
		return;
		
	}
	
	/**
	 * Verifica os argumentos da entrada para ver se condizem com o esperado
	 * @param args
	 */
	public static void verificarArgumentos(String[] args) {
		if (args.length <= 2) {
            System.err.println("Erro: Argumentos não fornecidos ou insuficientes.");
            System.out.println("Uso: java Main <ativo> <preco de venda> <preco de compra>");
            return;
            
		} else if (args.length > 3) {
			System.err.println("Erro: Argumentos demais.");
            System.out.println("Uso: java Main <ativo> <preco de venda> <preco de compra>");
            
        } else if (!isDouble(args[1]) | !isDouble(args[2])) {
        	System.err.println("Erro na formatação dos valores, use '.' (ponto) como separador decimal");
        	return;
        }
	}
	
	// Função auxiliar para verificar se o numero entrado está correto
	private static boolean isDouble(String texto) {
	    if (texto == null || texto.trim().isEmpty()) {
	        return false;
	    }
	    try {
	        Double.parseDouble(texto);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}

}
