package hamburgueria.observer;

import hamburgueria.state.PedidoContext;
import hamburgueria.state.PedidoState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogDeEstadosObserver implements PedidoObserver {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final List<String> log = new ArrayList<>();

    @Override
    public void onMudancaDeEstado(PedidoContext pedido,
                                  PedidoState estadoAnterior,
                                  PedidoState estadoNovo) {
        String entrada = String.format("[%s] Pedido #%d: %s %s → %s %s",
                LocalDateTime.now().format(FMT),
                pedido.getId(),
                estadoAnterior.getEmoji(), estadoAnterior.getNome(),
                estadoNovo.getEmoji(), estadoNovo.getNome());
        log.add(entrada);
        System.out.println("  📋 [Log] " + entrada);
    }

    public List<String> getLog() {
        return Collections.unmodifiableList(log);
    }

    public void exibirLog() {
        System.out.println("=== Histórico de Estados (Observer/Log) ===");
        log.forEach(System.out::println);
    }
}

class CozinhaObserver implements PedidoObserver {

    @Override
    public void onMudancaDeEstado(PedidoContext pedido,
                                  PedidoState estadoAnterior,
                                  PedidoState estadoNovo) {
        switch (estadoNovo.getNome()) {
            case "CONFIRMADO" ->
                System.out.printf("  🍳 [Cozinha] NOVO PEDIDO #%d recebido! %d item(s) — R$ %.2f%n",
                        pedido.getId(),
                        pedido.getItens().size(),
                        pedido.calcularTotal());
            case "EM_PREPARO" ->
                System.out.printf("  🍳 [Cozinha] Pedido #%d entrou na chapa!%n", pedido.getId());
            case "CANCELADO" ->
                System.out.printf("  🍳 [Cozinha] ⚠ Pedido #%d CANCELADO — interrompa o preparo.%n",
                        pedido.getId());
        }
    }
}

class ClienteObserver implements PedidoObserver {

    @Override
    public void onMudancaDeEstado(PedidoContext pedido,
                                  PedidoState estadoAnterior,
                                  PedidoState estadoNovo) {
        String nomeCliente = pedido.getCliente() != null
                ? pedido.getCliente().getNome()
                : "Cliente";

        switch (estadoNovo.getNome()) {
            case "CONFIRMADO" ->
                System.out.printf("  📱 [App Cliente] %s, seu pedido #%d foi confirmado! " +
                        "Aguarde o preparo. 🍔%n", nomeCliente, pedido.getId());
            case "PRONTO" ->
                System.out.printf("  📱 [App Cliente] %s, seu pedido #%d está PRONTO! " +
                        "Pode retirar no balcão. 🔔%n", nomeCliente, pedido.getId());
            case "ENTREGUE" ->
                System.out.printf("  📱 [App Cliente] Obrigado, %s! Pedido #%d entregue. " +
                        "Bom apetite! 🎉%n", nomeCliente, pedido.getId());
            case "CANCELADO" ->
                System.out.printf("  📱 [App Cliente] %s, seu pedido #%d foi cancelado. " +
                        "Entre em contato se precisar de ajuda.%n", nomeCliente, pedido.getId());
        }
    }
}


class FaturamentoObserver implements PedidoObserver {

    private double faturamentoDia = 0.0;
    private int pedidosConfirmados = 0;
    private int pedidosCancelados = 0;

    @Override
    public void onMudancaDeEstado(PedidoContext pedido,
                                  PedidoState estadoAnterior,
                                  PedidoState estadoNovo) {
        switch (estadoNovo.getNome()) {
            case "CONFIRMADO" -> {
                faturamentoDia += pedido.calcularTotal();
                pedidosConfirmados++;
                System.out.printf("  💰 [Financeiro] Pedido #%d registrado: +R$ %.2f " +
                        "(dia: R$ %.2f)%n",
                        pedido.getId(), pedido.calcularTotal(), faturamentoDia);
            }
            case "CANCELADO" -> {
                if ("CONFIRMADO".equals(estadoAnterior.getNome()) ||
                    "EM_PREPARO".equals(estadoAnterior.getNome())) {
                    faturamentoDia -= pedido.calcularTotal();
                    pedidosCancelados++;
                    System.out.printf("  💰 [Financeiro] Pedido #%d cancelado: -R$ %.2f " +
                            "(dia: R$ %.2f)%n",
                            pedido.getId(), pedido.calcularTotal(), faturamentoDia);
                }
            }
        }
    }

    public void exibirResumo() {
        System.out.println("=== Resumo Financeiro do Dia (Observer/Faturamento) ===");
        System.out.printf("  Pedidos confirmados : %d%n", pedidosConfirmados);
        System.out.printf("  Pedidos cancelados  : %d%n", pedidosCancelados);
        System.out.printf("  Faturamento líquido : R$ %.2f%n", faturamentoDia);
    }

    public double getFaturamentoDia()     { return faturamentoDia; }
    public int getPedidosConfirmados()    { return pedidosConfirmados; }
    public int getPedidosCancelados()     { return pedidosCancelados; }
}


class ObserverFactory {

    private static final FaturamentoObserver faturamento = new FaturamentoObserver();
    private static final LogDeEstadosObserver logGlobal  = new LogDeEstadosObserver();

    private ObserverFactory() {}

    public static LogDeEstadosObserver configurarObservadoresPadrao(
            hamburgueria.state.PedidoContext pedido) {
        pedido.registrarObservador(logGlobal);
        pedido.registrarObservador(new CozinhaObserver());
        pedido.registrarObservador(new ClienteObserver());
        pedido.registrarObservador(faturamento);
        return logGlobal;
    }

    public static FaturamentoObserver getFaturamento() { return faturamento; }
    public static LogDeEstadosObserver getLogGlobal()  { return logGlobal; }
}
