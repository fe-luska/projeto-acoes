package projetoAcoes.model;

public class AlertaPreco {

    private final String ativo;
    private final double precoVenda;
    private final double precoCompra;

    public AlertaPreco(String ativo, double precoVenda, double precoCompra) {
        this.ativo = ativo;
        this.precoVenda = precoVenda;
        this.precoCompra = precoCompra;
    }

    public String getAtivo() {
        return this.ativo;
    }

    public double getPrecoCompra() {
        return this.precoCompra;
    }

    public double getPrecoVenda() {
        return this.precoVenda;
    }
}