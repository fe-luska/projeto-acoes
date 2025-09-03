package projetoAcoes.client;

import projetoAcoes.model.Acao;

/**
 * Interface criada para possibilitar os testes em que se simula uma classe cliente
 * da API que retornará os dados das ações.
 */
public interface ApiClient {
	Acao buscarCotacao(String simbolo) throws Exception;
}