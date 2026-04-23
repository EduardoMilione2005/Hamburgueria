package hamburgueria.model;

import java.util.ArrayList;
import java.util.List;

public class ItemPedido {

    private Burger burger;
    private List<Extra> extras;
    private int quantidade;

    public ItemPedido(Burger burger) {
        this.burger = burger;
        this.extras = new ArrayList<>();
        this.quantidade = 1;
    }

    public void adicionarExtra(Extra extra) {
        extras.add(extra);
    }

    public double calcularTotal() {
        double totalExtras = extras.stream().mapToDouble(Extra::getPreco).sum();
        return (burger.getPreco() + totalExtras) * quantidade;
    }

    public Burger getBurger() { return burger; }
    public List<Extra> getExtras() { return extras; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  %s x%d - R$ %.2f%n", burger.getNome(), quantidade, calcularTotal()));
        if (!extras.isEmpty()) {
            sb.append("    Extras: ");
            extras.forEach(e -> sb.append(e.getNome()).append(", "));
            sb.setLength(sb.length() - 2);
            sb.append("\n");
        }
        return sb.toString();
    }
}
