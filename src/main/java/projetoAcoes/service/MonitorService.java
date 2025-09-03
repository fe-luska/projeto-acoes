package projetoAcoes.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import projetoAcoes.client.ApiClient;
import projetoAcoes.client.EmailClient;
import projetoAcoes.model.Acao;
import projetoAcoes.model.AlertaPreco;

public class MonitorService {

	// de quanto em quanto tempo os dados da ação serão atualizados
	private final long intervaloMonitoramentoSegundos = 20;
	
    private AlertaPreco alerta;
    
    private final ApiClient apiClient;
    private final EmailClient emailClient;
    private final String emailDestinatario;

    // O agendador que executará a verificação periodicamente
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public MonitorService(ApiClient apiClient, EmailClient emailClient, String emailDestinatario) {
        this.apiClient = apiClient;
        this.emailClient = emailClient;
        this.emailDestinatario = emailDestinatario;
    }

    public void setAlerta(AlertaPreco alerta) {
        System.out.println(String.format("Novo alerta adicionado para: " + alerta.getAtivo() +
        					" Preço para venda: R$%.2f preço para compra: R$%.2f",
        					alerta.getPrecoVenda(), alerta.getPrecoCompra()));
        this.alerta = alerta;
    }

    /**
     * Inicia o monitoramento. A verificação será executada em intervalos fixos.
     * @param intervalo é o intervalo em segundos a ser executado a verificação.
     */
    public void iniciarMonitoramento() {
        
        // Encapsula a função verificarAlertas dentro de um runnable pra jogar no scheduler
        Runnable tarefaDeVerificacao = this::verificarAlerta;

        // Agenda a função verificarAlertas para ser executada no intervalo
        scheduler.scheduleAtFixedRate(tarefaDeVerificacao, 0, intervaloMonitoramentoSegundos, TimeUnit.SECONDS);
    }

    /**
     * Função que é executada pelo scheduler no intervalo de tempo definido.
     * Verifica se o preço de compra ou venda foi atingido.
     */
    public void verificarAlerta() {

		try {
			// Busca o preço atual do ativo
			Acao acao = apiClient.buscarCotacao(alerta.getAtivo());
			System.out.println(acao.toString());

			// Preco de compra foi atingido
			if (alerta.getPrecoCompra() >= acao.getPreco()) {
				System.out.println("Alerta atingido para compra" + alerta);
				this.emailClient.enviarEmailAlerta(alerta, acao, this.emailDestinatario, "compra");
				this.pararMonitoramento();
				
			} else if (alerta.getPrecoVenda() <= acao.getPreco()) {
				System.out.println("Alerta atingido para venda" + alerta.toString());
				this.emailClient.enviarEmailAlerta(alerta, acao, this.emailDestinatario, "venda");
				this.pararMonitoramento();
			}

		} catch (Exception e) {
			System.err.println("Erro ao verificar o alerta " + alerta.getAtivo() + "- " + e.getMessage());
		}
    }
    
    public void pararMonitoramento() {
        System.out.println("Parando o monitoramento");
        scheduler.shutdown();
    }
}