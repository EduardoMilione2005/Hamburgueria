package hamburgueria.builder;

import hamburgueria.model.Burger;
import hamburgueria.model.Extra;
import hamburgueria.model.ItemPedido;

import java.util.ArrayList;
import java.util.List;

public class ItemPedidoBuilder {

    private final Burger burger;
    private final List<Extra> extras = new ArrayList<>();
    private int quantidade = 1;
    private String observacao;

    public ItemPedidoBuilder(Burger burger) {
        if (burger == null) throw new IllegalArgumentException("Burger não pode ser nulo.");
        this.burger = burger;
    }

    public ItemPedidoBuilder comExtra(Extra extra) {
        if (extra == null) throw new IllegalArgumentException("Extra não pode ser nulo.");
        this.extras.add(extra);
        return this;
    }

    public ItemPedidoBuilder comQuantidade(int quantidade) {
        if (quantidade < 1) throw new IllegalArgumentException("Quantidade deve ser ao menos 1.");
        this.quantidade = quantidade;
        return this;
    }

    public ItemPedidoBuilder comObservacao(String observacao) {
        this.observacao = observacao;
        return this;
    }

    public ItemPedido build() {
        ItemPedido item = new ItemPedido(burger);
        extras.forEach(item::adicionarExtra);
        item.setQuantidade(quantidade);
        if (observacao != null && !observacao.isBlank()) {
            item.setObservacao(observacao);
        }
        return item;
    }
}
