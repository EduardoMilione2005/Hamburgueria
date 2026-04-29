package hamburgueria.patterns.bridge;

public class NotificadorFactory {

    public static Notificador clienteViaWhatsApp() { return new NotificadorCliente(new CanalWhatsApp()); }
    public static Notificador clienteViaEmail()    { return new NotificadorCliente(new CanalEmail()); }
    public static Notificador cozinhaViaWhatsApp() { return new NotificadorCozinha(new CanalWhatsApp()); }
    public static Notificador cozinhaViaEmail()    { return new NotificadorCozinha(new CanalEmail()); }

    public static Notificador clienteComCanal(CanalNotificacao canal) { return new NotificadorCliente(canal); }
    public static Notificador cozinhaComCanal(CanalNotificacao canal)  { return new NotificadorCozinha(canal); }
}
