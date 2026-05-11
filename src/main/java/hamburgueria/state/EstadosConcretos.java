package hamburgueria.state;

import hamburgueria.model.ItemPedido;

class EstadoAberto implements PedidoState {

    @Override
    public void adicionarItem(PedidoContext ctx, ItemPedido item) {
        ctx.adicionarItemInterno(item);
    }

    @Override
    public void removerItem(PedidoContext ctx, int indice) {
        ctx.removerItemInterno(indice);
    }

    @Override
    public void confirmar(PedidoContext ctx) {
        if (ctx.estaVazio())
            throw new EstadoInvalidoException(getNome(), "confirmar — pedido sem itens");
        ctx.registrarClienteSeVinculado();
        ctx.transicionarPara(new EstadoConfirmado());
    }

    @Override
    public void iniciarPreparo(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "iniciarPreparo");
    }

    @Override
    public void marcarPronto(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "marcarPronto");
    }

    @Override
    public void entregar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "entregar");
    }

    @Override
    public void cancelar(PedidoContext ctx) {
        ctx.transicionarPara(new EstadoCancelado());
    }

    @Override public String getNome()  { return "ABERTO"; }
    @Override public String getEmoji() { return "📝"; }
}

class EstadoConfirmado implements PedidoState {

    @Override
    public void adicionarItem(PedidoContext ctx, ItemPedido item) {
        throw new EstadoInvalidoException(getNome(), "adicionarItem");
    }

    @Override
    public void removerItem(PedidoContext ctx, int indice) {
        throw new EstadoInvalidoException(getNome(), "removerItem");
    }

    @Override
    public void confirmar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "confirmar");
    }

    @Override
    public void iniciarPreparo(PedidoContext ctx) {
        ctx.transicionarPara(new EstadoEmPreparo());
    }

    @Override
    public void marcarPronto(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "marcarPronto");
    }

    @Override
    public void entregar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "entregar");
    }

    @Override
    public void cancelar(PedidoContext ctx) {
        ctx.transicionarPara(new EstadoCancelado());
    }

    @Override public String getNome()  { return "CONFIRMADO"; }
    @Override public String getEmoji() { return "✅"; }
}


class EstadoEmPreparo implements PedidoState {

    @Override
    public void adicionarItem(PedidoContext ctx, ItemPedido item) {
        throw new EstadoInvalidoException(getNome(), "adicionarItem");
    }

    @Override
    public void removerItem(PedidoContext ctx, int indice) {
        throw new EstadoInvalidoException(getNome(), "removerItem");
    }

    @Override
    public void confirmar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "confirmar");
    }

    @Override
    public void iniciarPreparo(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "iniciarPreparo");
    }

    @Override
    public void marcarPronto(PedidoContext ctx) {
        ctx.transicionarPara(new EstadoPronto());
    }

    @Override
    public void entregar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "entregar");
    }

    @Override
    public void cancelar(PedidoContext ctx) {
        ctx.transicionarPara(new EstadoCancelado());
    }

    @Override public String getNome()  { return "EM_PREPARO"; }
    @Override public String getEmoji() { return "🍳"; }
}


class EstadoPronto implements PedidoState {

    @Override
    public void adicionarItem(PedidoContext ctx, ItemPedido item) {
        throw new EstadoInvalidoException(getNome(), "adicionarItem");
    }

    @Override
    public void removerItem(PedidoContext ctx, int indice) {
        throw new EstadoInvalidoException(getNome(), "removerItem");
    }

    @Override
    public void confirmar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "confirmar");
    }

    @Override
    public void iniciarPreparo(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "iniciarPreparo");
    }

    @Override
    public void marcarPronto(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "marcarPronto");
    }

    @Override
    public void entregar(PedidoContext ctx) {
        ctx.transicionarPara(new EstadoEntregue());
    }

    @Override
    public void cancelar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(),
            "cancelar — pedido já pronto, fale com o gerente");
    }

    @Override public String getNome()  { return "PRONTO"; }
    @Override public String getEmoji() { return "🔔"; }
}


class EstadoEntregue implements PedidoState {

    @Override
    public void adicionarItem(PedidoContext ctx, ItemPedido item) {
        throw new EstadoInvalidoException(getNome(), "adicionarItem");
    }

    @Override
    public void removerItem(PedidoContext ctx, int indice) {
        throw new EstadoInvalidoException(getNome(), "removerItem");
    }

    @Override
    public void confirmar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "confirmar");
    }

    @Override
    public void iniciarPreparo(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "iniciarPreparo");
    }

    @Override
    public void marcarPronto(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "marcarPronto");
    }

    @Override
    public void entregar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "entregar");
    }

    @Override
    public void cancelar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "cancelar");
    }

    @Override public String getNome()  { return "ENTREGUE"; }
    @Override public String getEmoji() { return "🎉"; }
}


class EstadoCancelado implements PedidoState {

    @Override
    public void adicionarItem(PedidoContext ctx, ItemPedido item) {
        throw new EstadoInvalidoException(getNome(), "adicionarItem");
    }

    @Override
    public void removerItem(PedidoContext ctx, int indice) {
        throw new EstadoInvalidoException(getNome(), "removerItem");
    }

    @Override
    public void confirmar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "confirmar");
    }

    @Override
    public void iniciarPreparo(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "iniciarPreparo");
    }

    @Override
    public void marcarPronto(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "marcarPronto");
    }

    @Override
    public void entregar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "entregar");
    }

    @Override
    public void cancelar(PedidoContext ctx) {
        throw new EstadoInvalidoException(getNome(), "cancelar");
    }

    @Override public String getNome()  { return "CANCELADO"; }
    @Override public String getEmoji() { return "❌"; }
}
