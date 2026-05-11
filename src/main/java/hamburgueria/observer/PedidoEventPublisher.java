package hamburgueria.observer;

public interface PedidoEventPublisher {

    void registrarObservador(PedidoObserver observer);

    void removerObservador(PedidoObserver observer);

    void notificarObservadores(hamburgueria.state.PedidoState estadoAnterior,
                               hamburgueria.state.PedidoState estadoNovo);
}
