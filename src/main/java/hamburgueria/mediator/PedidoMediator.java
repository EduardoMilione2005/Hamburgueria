package hamburgueria.mediator;

import hamburgueria.model.ItemPedido;
import hamburgueria.model.Pedido;

import java.util.List;

public interface PedidoMediator {

    void adicionarItem(int burgerId, List<String> extrasIds);

    void removerItem(int indice);

    void vincularCliente(int clienteId);

    void definirObservacao(String obs);

    Pedido confirmarPedido();

    void cancelarPedido();

    Pedido getPedidoAtual();
    List<Pedido> getPedidosConfirmados();
}
