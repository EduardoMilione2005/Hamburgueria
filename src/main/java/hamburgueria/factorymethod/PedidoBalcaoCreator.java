package hamburgueria.patterns.factorymethod;

import hamburgueria.model.Cliente;
import hamburgueria.model.Pedido;

class PedidoBalcao extends Pedido {
    public PedidoBalcao() { super(); }
    public PedidoBalcao(Cliente c) { super(c); }

    @Override
    public String toString() {
        return "[BALCÃO] " + super.toString();
    }
}

class PedidoDelivery extends Pedido {

    private static final double TAXA_ENTREGA = 6.90;

    public PedidoDelivery(Cliente cliente) {
        super(cliente);
        if (cliente == null) throw new IllegalArgumentException("Delivery exige cliente cadastrado.");
    }

    @Override
    public double calcularTotal() {
        return super.calcularTotal() + TAXA_ENTREGA;
    }

    @Override
    public String toString() {
        return String.format("[DELIVERY +R$%.2f taxa] %s", TAXA_ENTREGA, super.toString());
    }
}

class PedidoAgendado extends Pedido {

    private final String horarioRetirada;

    public PedidoAgendado(Cliente cliente, String horarioRetirada) {
        super(cliente);
        if (horarioRetirada == null || horarioRetirada.isBlank())
            throw new IllegalArgumentException("Pedido agendado exige horário de retirada.");
        this.horarioRetirada = horarioRetirada;
        setObservacao("Retirada: " + horarioRetirada);
    }

    public String getHorarioRetirada() { return horarioRetirada; }

    @Override
    public String toString() {
        return "[AGENDADO " + horarioRetirada + "] " + super.toString();
    }
}


public class PedidoBalcaoCreator extends PedidoCreator {

    @Override
    public Pedido criarPedido(Cliente cliente) {
        return new PedidoBalcao(cliente);
    }

    @Override
    public String getTipoPedido() { return "BALCÃO"; }
}

class PedidoDeliveryCreator extends PedidoCreator {

    @Override
    public Pedido criarPedido(Cliente cliente) {
        return new PedidoDelivery(cliente);
    }

    @Override
    public String getTipoPedido() { return "DELIVERY"; }
}


class PedidoAgendadoCreator extends PedidoCreator {

    private final String horario;

    public PedidoAgendadoCreator(String horario) {
        this.horario = horario;
    }

    @Override
    public Pedido criarPedido(Cliente cliente) {
        return new PedidoAgendado(cliente, horario);
    }

    @Override
    public String getTipoPedido() { return "AGENDADO"; }
}


class PedidoCreatorFactory {

    public enum TipoPedido { BALCAO, DELIVERY, AGENDADO }

    public static PedidoCreator obter(TipoPedido tipo, String... params) {
        return switch (tipo) {
            case BALCAO   -> new PedidoBalcaoCreator();
            case DELIVERY -> new PedidoDeliveryCreator();
            case AGENDADO -> new PedidoAgendadoCreator(params.length > 0 ? params[0] : "");
        };
    }
}
