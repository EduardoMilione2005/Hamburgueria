package hamburgueria.chainofresponsibility;

import hamburgueria.model.Pedido;

class ValidarItensPedidoHandler extends ValidacaoPedidoHandler {

    @Override
    protected void checar(Pedido pedido) {
        if (pedido.estaVazio()) {
            throw new ValidacaoPedidoException(
                "❌ [Validação] Pedido vazio: adicione ao menos um item antes de confirmar."
            );
        }
        System.out.println("✔ [Validação] Itens: OK (" + pedido.getItens().size() + " item(s))");
    }
}

class ValidarValorMinimoHandler extends ValidacaoPedidoHandler {

    private static final double VALOR_MINIMO = 20.00;

    @Override
    protected void checar(Pedido pedido) {
        if (pedido.calcularTotal() < VALOR_MINIMO) {
            throw new ValidacaoPedidoException(
                String.format(
                    "❌ [Validação] Valor mínimo não atingido: R$ %.2f (mínimo: R$ %.2f).",
                    pedido.calcularTotal(), VALOR_MINIMO
                )
            );
        }
        System.out.printf("✔ [Validação] Valor mínimo: OK (R$ %.2f)%n", pedido.calcularTotal());
    }
}

class ValidarClienteDeliveryHandler extends ValidacaoPedidoHandler {

    @Override
    protected void checar(Pedido pedido) {
        if (pedido.getCliente() == null) {
            System.out.println("✔ [Validação] Cliente: pedido de balcão (sem vínculo de cliente).");
        } else {
            System.out.println("✔ [Validação] Cliente vinculado: " + pedido.getCliente().getNome());
        }
    }
}

class ValidarLimiteItensHandler extends ValidacaoPedidoHandler {

    private static final int LIMITE_ITENS = 10;

    @Override
    protected void checar(Pedido pedido) {
        if (pedido.getItens().size() > LIMITE_ITENS) {
            throw new ValidacaoPedidoException(
                String.format(
                    "❌ [Validação] Limite de itens excedido: %d itens (máximo: %d).",
                    pedido.getItens().size(), LIMITE_ITENS
                )
            );
        }
        System.out.println("✔ [Validação] Limite de itens: OK");
    }
}

class ValidarStatusPedidoHandler extends ValidacaoPedidoHandler {

    @Override
    protected void checar(Pedido pedido) {
        if (pedido.getStatus() != Pedido.Status.ABERTO) {
            throw new ValidacaoPedidoException(
                "❌ [Validação] Status inválido: o pedido já foi " +
                pedido.getStatus().name().toLowerCase() + " e não pode ser confirmado novamente."
            );
        }
        System.out.println("✔ [Validação] Status: ABERTO — pedido elegível para confirmação.");
    }
}

public class ValidacaoCadeia {

    private ValidacaoCadeia() {}

    public static ValidacaoPedidoHandler obter() {
        ValidacaoPedidoHandler statusHandler      = new ValidarStatusPedidoHandler();
        ValidacaoPedidoHandler itensHandler       = new ValidarItensPedidoHandler();
        ValidacaoPedidoHandler valorMinimoHandler = new ValidarValorMinimoHandler();
        ValidacaoPedidoHandler limiteHandler      = new ValidarLimiteItensHandler();
        ValidacaoPedidoHandler clienteHandler     = new ValidarClienteDeliveryHandler();

        statusHandler
            .setProximo(itensHandler)
            .setProximo(valorMinimoHandler)
            .setProximo(limiteHandler)
            .setProximo(clienteHandler);

        return statusHandler;
    }
}
