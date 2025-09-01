package projetoAcoes.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.cdimascio.dotenv.Dotenv;
import projetoAcoes.model.AlertaPreco;
import projetoAcoes.service.MonitorService;

public class MonitorServiceTest {

	private MockApiClient mockApi;
	private EmailClient emailClient;
	private MonitorService monitorService;

	@BeforeEach
	void setUp() {
		// Roda antes de todos os testes
		Dotenv dotenv = Dotenv.load();
		this.mockApi = new MockApiClient();
		this.emailClient = new EmailClient(
	            dotenv.get("EMAIL_HOST"),
	            dotenv.get("EMAIL_PORT"),
	            dotenv.get("EMAIL_USERNAME"),
	            dotenv.get("EMAIL_PASSWORD")
	        );
		
		monitorService = new MonitorService(mockApi, emailClient, dotenv.get("EMAIL_DESTINO"));
		
		// descomente e comente a linha superior se quiser utilizar um email fake para testes
//		this.monitorService = new MonitorService(mockApi, emailClient, "exemplo@teste.com");
	}

	@Test
	void deveEnviarEmailQuandoAlertaDeAltaEAtingido() {
		// ARRANGE (Preparação)
		// Cenário: O alerta é para quando PETR4 subir acima de 240.0.
		monitorService.setAlerta(new AlertaPreco("AAPL", 240, 230));
		
		// Simulação: O preço atual da AAPL é 241.
		mockApi.setPrecoSimulado("AAPL", 241, "+2.5%");

		// ACT (Ação)
		// Executamos a lógica que queremos testar.
		monitorService.verificarAlerta();

		// ASSERT (Verificação)
		// Verificamos se o resultado esperado aconteceu.
		assertEquals(1, emailClient.getEmailsEnviados(), "Um e-mail deveria ter sido enviado.");
		assertTrue(emailClient.getUltimoAssunto().contains("Alerta de Preço para venda atingido - AAPL"));
	}

	@Test
	void naoDeveEnviarEmailQuandoAlertaDeAltaNaoEAtingido() {
	    
	    // Cenário: Alerta para PETR4 acima de 40 e abaixo de 30
	    monitorService.setAlerta(new AlertaPreco("PETR4.SA", 40.0, 30.0));
	    
	    // Simulação: O preço atual é 37.25 e dentro do intervalo.
	    mockApi.setPrecoSimulado("PETR4.SA", 39.0, "-1.2%");
	
	    monitorService.verificarAlerta();
	
	    // Confere se foi enviado email
	    assertEquals(0, emailClient.getEmailsEnviados(), "Nenhum e-mail deveria ter sido enviado.");
	}

	@Test
	void deveEnviarEmailQuandoAlertaDeBaixaEAtingido() {

	    // Cenário: Alerta para NVDA entre 20 e 17
	    monitorService.setAlerta(new AlertaPreco("NVDA", 20, 17));
	    
	    // Simulação: O preço atual é 16.75
	    mockApi.setPrecoSimulado("NVDA", 16.75, "-0.5%");
	
	    monitorService.verificarAlerta();
	
	    // ASSERT
	    assertEquals(1, emailClient.getEmailsEnviados(), "Um e-mail de alerta de baixa deveria ter sido enviado.");
	    assertTrue(emailClient.getUltimoAssunto().contains("Alerta de Preço para compra atingido - NVDA"));
	 }
}