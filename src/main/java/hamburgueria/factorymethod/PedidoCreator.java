package hamburgueria.patterns.factorymethod;

import hamburgueria.model.Cliente;
import hamburgueria.model.Pedido;

public abstract class PedidoCreator {

    public abstract Pedido criarPedido(Cliente cliente);

    public Pedido iniciarPedido(Cliente cliente) {
        Pedido pedido = criarPedido(cliente);
        System.out.printf("[%s] Novo pedido #%d iniciado para %s%n",
                getTipoPedido(),
                pedido.getId(),
                cliente != null ? cliente.getNome() : "balcão");
        return pedido;
    }

    public abstract String getTipoPedido();
}
