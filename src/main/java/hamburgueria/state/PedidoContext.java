package hamburgueria.state;

import hamburgueria.model.Cliente;
import hamburgueria.model.ItemPedido;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PedidoContext {

    private static int contadorId = 1;

    private final int id;
    private final LocalDateTime dataCriacao;
    private Cliente cliente;
    private String observacao;
    private final List<ItemPedido> itens = new ArrayList<>();

    // Estado atual — único ponto de variação de comportamento
    private PedidoState estadoAtual;

    public PedidoContext() {
        this.id = contadorId++;
        this.dataCriacao = LocalDateTime.now();
        this.estadoAtual = new EstadoAberto();   // estado inicial
    }

    public PedidoContext(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    public void adicionarItem(ItemPedido item) {
        estadoAtual.adicionarItem(this, item);
    }

    public void removerItem(int indice) {
        estadoAtual.removerItem(this, indice);
    }

    public void confirmar() {
        estadoAtual.confirmar(this);
    }

    public void iniciarPreparo() {
        estadoAtual.iniciarPreparo(this);
    }

    public void marcarPronto() {
        estadoAtual.marcarPronto(this);
    }

    public void entregar() {
        estadoAtual.entregar(this);
    }

    public void cancelar() {
        estadoAtual.cancelar(this);
    }

    public void transicionarPara(PedidoState novoEstado) {
        System.out.printf("[State] Pedido #%d: %s %s → %s %s%n",
            id,
            estadoAtual.getEmoji(), estadoAtual.getNome(),
            novoEstado.getEmoji(), novoEstado.getNome()
        );
        this.estadoAtual = novoEstado;
    }

    public void adicionarItemInterno(ItemPedido item) { itens.add(item); }

    public void removerItemInterno(int indice) {
        if (indice < 0 || indice >= itens.size())
            throw new IndexOutOfBoundsException("Índice de item inválido: " + indice);
        itens.remove(indice);
    }

    public void registrarClienteSeVinculado() {
        if (cliente != null) cliente.registrarPedido();
    }

    public int getId()                      { return id; }
    public LocalDateTime getDataCriacao()   { return dataCriacao; }
    public Cliente getCliente()             { return cliente; }
    public void setCliente(Cliente c)       { this.cliente = c; }
    public String getObservacao()           { return observacao; }
    public void setObservacao(String obs)   { this.observacao = obs; }
    public List<ItemPedido> getItens()      { return Collections.unmodifiableList(itens); }
    public PedidoState getEstado()          { return estadoAtual; }
    public String getNomeEstado()           { return estadoAtual.getNome(); }
    public boolean estaVazio()              { return itens.isEmpty(); }

    public double calcularTotal() {
        return itens.stream().mapToDouble(ItemPedido::calcularTotal).sum();
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Pedido #%d | %s %s | %s ===%n",
            id, estadoAtual.getEmoji(), estadoAtual.getNome(), dataCriacao.format(fmt)));
        if (cliente != null)
            sb.append(String.format("Cliente: %s%n", cliente.getNome()));
        sb.append("Itens:\n");
        itens.forEach(sb::append);
        if (observacao != null && !observacao.isBlank())
            sb.append(String.format("Obs: %s%n", observacao));
        sb.append(String.format("TOTAL: R$ %.2f%n", calcularTotal()));
        return sb.toString();
    }
}
