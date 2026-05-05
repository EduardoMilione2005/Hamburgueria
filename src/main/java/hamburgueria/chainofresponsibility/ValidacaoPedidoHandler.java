package hamburgueria.chainofresponsibility;

import hamburgueria.model.Pedido;

public abstract class ValidacaoPedidoHandler {

    private ValidacaoPedidoHandler proximo;

    public ValidacaoPedidoHandler setProximo(ValidacaoPedidoHandler proximo) {
        this.proximo = proximo;
        return proximo;
    }

    public final void validar(Pedido pedido) {
        checar(pedido);
        if (proximo != null) {
            proximo.validar(pedido);
        }
    }

    protected abstract void checar(Pedido pedido);
}
