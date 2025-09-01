package projetoAcoes.model;

public class Acao {
    private String simbolo;
    private double preco;
    private String variacaoPercentual;

    // Construtor
    public Acao(String simbolo, double preco, String variacaoPercentual) {
        this.simbolo = simbolo;
        this.preco = preco;
        this.variacaoPercentual = variacaoPercentual;
    }

    // Getters e Setters (pode gerar no Eclipse: Source > Generate Getters and Setters)
    public String getSimbolo() { return simbolo; }
    public double getPreco() { return preco; }
    public String getVariacaoPercentual() { return variacaoPercentual; }

    @Override
    public String toString() {
        return String.format("Ação [Símbolo=%s, Preço=R$ %.2f, Variação=%s]",
                             simbolo, preco, variacaoPercentual);
    }
}