
package projetoAcoes.client;

import java.util.HashMap;
import java.util.Map;

import projetoAcoes.model.Acao;

public class MockApiClient implements ApiClient {

    private final Map<String, Acao> precosSimulados = new HashMap<>();

    @Override
    public Acao buscarCotacao(String simbolo) throws Exception {
        if (precosSimulados.containsKey(simbolo)) {
            System.out.println("[MOCK API] Retornando preço simulado para " + simbolo);
            return precosSimulados.get(simbolo);
        }
        throw new Exception("Ativo não encontrado no mock: " + simbolo);
    }

    /**
     * Método de controle para nossos testes. Usamos para definir o preço que o mock deve retornar.
     */
    public void setPrecoSimulado(String simbolo, double preco, String variacao) {
        this.precosSimulados.put(simbolo, new Acao(simbolo, preco, variacao));
    }
}