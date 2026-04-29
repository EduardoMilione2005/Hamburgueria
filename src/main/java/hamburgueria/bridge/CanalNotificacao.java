package hamburgueria.patterns.bridge;

import hamburgueria.model.Pedido;

public interface CanalNotificacao {
    void enviar(String destinatario, String mensagem);
    String getNomeCanal();
}
