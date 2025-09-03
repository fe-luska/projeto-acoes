
package projetoAcoes.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import projetoAcoes.model.Acao;

public class IbovFinancialsClient implements ApiClient {

    private final String apiToken;
    private static final String BASE_URL = "https://www.ibovfinancials.com/api/ibov";
    
    private final HttpClient httpClient;
    private final Gson gson;

    public IbovFinancialsClient(String apiToken) {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
        this.apiToken = apiToken;
    }

    /**
     * Busca a cotação de um ativo da B3 e retorna um objteo "Acao" com os valores
     * @param simbolo simbolo do ativo na B3, ex: PETR4
     */
    @Override
    public Acao buscarCotacao(String simbolo) throws IOException, InterruptedException {
       

    	// Monta a URL
        String url = String.format("%s/quotes/?symbol=%s&token=%s",
                                   BASE_URL, simbolo, this.apiToken);

        // Cria e envia a requisição
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Erro
        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha na requisição HTTP para IbovFinancials: Código " + response.statusCode());
        }

        // parse da resposta
        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

    
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        if (dataObject == null || !dataObject.has(simbolo)) { // nao encotrou o ativo
            throw new RuntimeException("Símbolo '" + simbolo + "' não encontrado na resposta da API IbovFinancials.");
        }
        
        JsonObject ativoObject = dataObject.getAsJsonObject(simbolo);

        // monta a Acao
        double preco = ativoObject.get("last").getAsDouble();
        double variacaoDecimal = ativoObject.get("price_change").getAsDouble();
        String variacaoFormatada = String.format("%.2f%%", variacaoDecimal * 100);

        return new Acao(simbolo, preco, variacaoFormatada);
    }
}