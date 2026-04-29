package hamburgueria.patterns.bridge;

import hamburgueria.model.Pedido;

public abstract class Notificador {

    protected CanalNotificacao canal;

    public Notificador(CanalNotificacao canal) {
        this.canal = canal;
    }

    public abstract void notificarConfirmacao(String destinatario, Pedido pedido);
    public abstract void notificarCancelamento(String destinatario, Pedido pedido);

    public String getCanalNome() {
        return canal.getNomeCanal();
    }
}



class NotificadorCliente extends Notificador {

    public NotificadorCliente(CanalNotificacao canal) {
        super(canal);
    }

    @Override
    public void notificarConfirmacao(String destinatario, Pedido pedido) {
        String msg = String.format(
            "Olá! Seu pedido #%d foi confirmado. Total: R$ %.2f. Preparo em andamento! 🍔",
            pedido.getId(), pedido.calcularTotal()
        );
        canal.enviar(destinatario, msg);
    }

    @Override
    public void notificarCancelamento(String destinatario, Pedido pedido) {
        String msg = String.format(
            "Seu pedido #%d foi cancelado. Entre em contato se precisar de ajuda.",
            pedido.getId()
        );
        canal.enviar(destinatario, msg);
    }
}


class NotificadorCozinha extends Notificador {

    public NotificadorCozinha(CanalNotificacao canal) {
        super(canal);
    }

    @Override
    public void notificarConfirmacao(String destinatario, Pedido pedido) {
        String msg = String.format(
            "🔔 NOVO PEDIDO #%d | %d itens | Total: R$ %.2f | Obs: %s",
            pedido.getId(),
            pedido.getItens().size(),
            pedido.calcularTotal(),
            pedido.getObservacao() != null ? pedido.getObservacao() : "—"
        );
        canal.enviar(destinatario, msg);
    }

    @Override
    public void notificarCancelamento(String destinatario, Pedido pedido) {
        canal.enviar(destinatario, "❌ Pedido #" + pedido.getId() + " cancelado. Descarte os itens.");
    }
}
