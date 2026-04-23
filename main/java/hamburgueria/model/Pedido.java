package hamburgueria.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    public enum Status { ABERTO, CONFIRMADO, CANCELADO }

    private static int contadorId = 1;

    private int id;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private String observacao;
    private Status status;
    private LocalDateTime dataCriacao;

    public Pedido() {
        this.id = contadorId++;
        this.itens = new ArrayList<>();
        this.status = Status.ABERTO;
        this.dataCriacao = LocalDateTime.now();
    }

    public Pedido(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    public void adicionarItem(ItemPedido item) {
        if (status != Status.ABERTO) throw new IllegalStateException("Pedido já foi fechado.");
        itens.add(item);
    }

    public void removerItem(int indice) {
        if (indice < 0 || indice >= itens.size()) throw new IndexOutOfBoundsException("Índice inválido.");
        itens.remove(indice);
    }

    public void confirmar() {
        if (itens.isEmpty()) throw new IllegalStateException("Pedido não pode ser confirmado sem itens.");
        this.status = Status.CONFIRMADO;
        if (cliente != null) cliente.registrarPedido();
    }

    public void cancelar() {
        this.status = Status.CANCELADO;
    }

    public double calcularTotal() {
        return itens.stream().mapToDouble(ItemPedido::calcularTotal).sum();
    }

    public boolean estaVazio() { return itens.isEmpty(); }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<ItemPedido> getItens() { return itens; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public Status getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== Pedido #%d | %s | %s ===%n", id, status, dataCriacao.format(fmt)));
        if (cliente != null) sb.append(String.format("Cliente: %s%n", cliente.getNome()));
        sb.append("Itens:\n");
        itens.forEach(sb::append);
        if (observacao != null && !observacao.isBlank())
            sb.append(String.format("Obs: %s%n", observacao));
        sb.append(String.format("TOTAL: R$ %.2f%n", calcularTotal()));
        return sb.toString();
    }
}
