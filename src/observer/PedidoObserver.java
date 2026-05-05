package hamburgueria.observer;

import hamburgueria.state.PedidoContext;
import hamburgueria.state.PedidoState;

public interface PedidoObserver {

    void onMudancaDeEstado(PedidoContext pedido, PedidoState estadoAnterior, PedidoState estadoNovo);
}
