package hamburgueria.patterns.bridge;

public class CanalEmail implements CanalNotificacao {

    @Override
    public void enviar(String destinatario, String mensagem) {
        System.out.printf("[Email → %s] Assunto: Pedido Sabor na Brasa | %s%n", destinatario, mensagem);
    }

    @Override
    public String getNomeCanal() {
        return "Email";
    }
}
