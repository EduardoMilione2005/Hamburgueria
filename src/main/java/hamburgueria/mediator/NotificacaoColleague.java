package hamburgueria.mediator;

import hamburgueria.model.Pedido;

public class CozinhaColleague implements ColleagueComponent {

    private PedidoMediator mediator;
    private final StringBuilder log = new StringBuilder();

    @Override
    public void setMediator(PedidoMediator mediator) {
        this.mediator = mediator;
    }

    public void receberNovoPedido(Pedido pedido) {
        String msg = String.format(
            "[Cozinha] Novo pedido #%d recebido! %d item(s) — R$ %.2f%s",
            pedido.getId(),
            pedido.getItens().size(),
            pedido.calcularTotal(),
            pedido.getObservacao() != null ? " | Obs: " + pedido.getObservacao() : ""
        );
        System.out.println(msg);
        log.append(msg).append("\n");
    }

    public void receberCancelamento(Pedido pedido) {
        String msg = String.format("[Cozinha] Pedido #%d cancelado. Descarte os itens.", pedido.getId());
        System.out.println(msg);
        log.append(msg).append("\n");
    }

    public String getLog() {
        return log.toString();
    }

    public void limparLog() {
        log.setLength(0);
    }
}
