package projetoAcoes.client;

import projetoAcoes.model.Acao;

public interface ApiClient {
	Acao buscarCotacao(String simbolo) throws Exception;
}