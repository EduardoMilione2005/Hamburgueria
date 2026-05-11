package hamburgueria.mediator;

import hamburgueria.model.Pedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CaixaColleague implements ColleagueComponent {

    private PedidoMediator mediator;
    private final List<Pedido> historico = new ArrayList<>();
    private double faturamentoTotal = 0.0;

    @Override
    public void setMediator(PedidoMediator mediator) {
        this.mediator = mediator;
    }

    public void registrarPedido(Pedido pedido) {
        historico.add(pedido);
        faturamentoTotal += pedido.calcularTotal();
        System.out.printf("[Caixa] Pedido #%d registrado: +R$ %.2f (total do dia: R$ %.2f)%n",
                pedido.getId(), pedido.calcularTotal(), faturamentoTotal);
    }

    public List<Pedido> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    public double getFaturamentoTotal() {
        return faturamentoTotal;
    }

    public int getTotalPedidos() {
        return historico.size();
    }

    public void exibirResumo() {
        System.out.println("=== RESUMO DO CAIXA (Mediator) ===");
        System.out.printf("Total de pedidos confirmados: %d%n", getTotalPedidos());
        System.out.printf("Faturamento total: R$ %.2f%n", faturamentoTotal);
    }
}
