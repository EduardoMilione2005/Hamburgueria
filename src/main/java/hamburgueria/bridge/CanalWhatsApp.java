package hamburgueria.patterns.bridge;

public class CanalWhatsApp implements CanalNotificacao {

    @Override
    public void enviar(String destinatario, String mensagem) {
        System.out.printf("[WhatsApp → %s] %s%n", destinatario, mensagem);
    }

    @Override
    public String getNomeCanal() {
        return "WhatsApp";
    }
}
