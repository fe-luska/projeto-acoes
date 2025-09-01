package projetoAcoes.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import projetoAcoes.model.Acao;

public class FinnhubClient implements ApiClient{

	private final String APIKey;
	private final String BaseURL = "https://finnhub.io/api/v1";
	
	private final HttpClient httpClient;
    private final Gson gson;

    public FinnhubClient(String APIKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
        this.APIKey = APIKey;
    }
    
    /**
     * Busca a cotação atual de uma ação usando a API da Finnhub.
     * @param acao O ticker da ação (ex: PETR4.SA, MGLU3.SA)
     * @return Um objeto Acao com os dados da cotação.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public Acao buscarCotacao(String acao) throws IOException, InterruptedException {
        // 1. Monta a URL específica para a Finnhub
        String url = String.format("%s/quote?symbol=%s&token=%s",
                                   this.BaseURL, acao, this.APIKey);

        // 2. Cria a requisição (essa parte é igual)
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        // 3. Envia a requisição (essa parte é igual)
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Falha na requisição HTTP: Código " + response.statusCode());
        }

        // 4. Analisa (Parse) o JSON da Finnhub
        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
        
        // A Finnhub retorna preço 0 para símbolos não encontrados
        if (jsonObject.get("c").getAsDouble() == 0) {
             throw new RuntimeException("Símbolo não encontrado ou sem cotação: " + acao);
        }

        // 5. Extrai os dados e cria o objeto Acao
        // Note como pegamos os valores de chaves diferentes ("c" e "dp")
        double preco = jsonObject.get("c").getAsDouble();
        double variacaoPercentualNum = jsonObject.get("dp").getAsDouble();

        // Formata a variação percentual para ficar mais legível
        String variacaoFormatada = String.format("%.2f%%", variacaoPercentualNum);

        return new Acao(acao, preco, variacaoFormatada);
    }
	
}
