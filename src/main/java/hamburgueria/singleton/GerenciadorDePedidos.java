package hamburgueria.patterns.singleton;

import hamburgueria.model.Pedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorDePedidos {

    private static volatile GerenciadorDePedidos instancia;

    private final List<Pedido> historico = new ArrayList<>();
    private double faturamentoTotal = 0.0;

    private GerenciadorDePedidos() {}
    
    public static GerenciadorDePedidos getInstance() {
        if (instancia == null) {
            synchronized (GerenciadorDePedidos.class) {
                if (instancia == null) {
                    instancia = new GerenciadorDePedidos();
                }
            }
        }
        return instancia;
    }

    public void registrarPedido(Pedido pedido) {
        historico.add(pedido);
        faturamentoTotal += pedido.calcularTotal();
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
        System.out.println("=== RESUMO DO CAIXA (Singleton) ===");
        System.out.printf("Total de pedidos confirmados: %d%n", getTotalPedidos());
        System.out.printf("Faturamento total: R$ %.2f%n", faturamentoTotal);
    }
}
