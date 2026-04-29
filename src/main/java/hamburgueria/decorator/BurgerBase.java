package hamburgueria.patterns.decorator;

public class BurgerBase implements Ingrediente {

    private final String nome;
    private final double precoBase;

    public BurgerBase(String nome, double precoBase) {
        this.nome = nome;
        this.precoBase = precoBase;
    }

    @Override
    public String getDescricao() {
        return nome + " (pão + blend)";
    }

    @Override
    public double getPreco() {
        return precoBase;
    }
}
