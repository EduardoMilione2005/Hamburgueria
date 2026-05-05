package hamburgueria.state;

import hamburgueria.model.ItemPedido;

public interface PedidoState {

    void adicionarItem(PedidoContext ctx, ItemPedido item);

    void removerItem(PedidoContext ctx, int indice);

    void confirmar(PedidoContext ctx);

    void iniciarPreparo(PedidoContext ctx);

    void marcarPronto(PedidoContext ctx);

    void entregar(PedidoContext ctx);

    void cancelar(PedidoContext ctx);

    String getNome();

    String getEmoji();
}
