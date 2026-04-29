package hamburgueria;

import hamburgueria.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private Burger burger;
    private Extra extra;

    @BeforeEach
    void setUp() {
        burger = new Burger(1, "Smash Classic", "Desc", 28.90, "🍔", "Mais Pedido");
        extra = new Extra("e1", "Batata Frita", 12.90, "🍟");
    }

    @Test
    void deveIniciarComoAberto() {
        Pedido p = new Pedido();
        assertEquals(Pedido.Status.ABERTO, p.getStatus());
    }

    @Test
    void deveIniciarVazio() {
        Pedido p = new Pedido();
        assertTrue(p.estaVazio());
    }

    @Test
    void deveAdicionarItem() {
        Pedido p = new Pedido();
        ItemPedido item = new ItemPedido(burger);
        p.adicionarItem(item);
        assertFalse(p.estaVazio());
        assertEquals(1, p.getItens().size());
    }

    @Test
    void deveRemoverItem() {
        Pedido p = new Pedido();
        p.adicionarItem(new ItemPedido(burger));
        p.removerItem(0);
        assertTrue(p.estaVazio());
    }

    @Test
    void deveLancarExcecaoRemoverIndiceInvalido() {
        Pedido p = new Pedido();
        assertThrows(IndexOutOfBoundsException.class, () -> p.removerItem(5));
    }

    @Test
    void deveCalcularTotalSemExtras() {
        Pedido p = new Pedido();
        p.adicionarItem(new ItemPedido(burger));
        assertEquals(28.90, p.calcularTotal(), 0.01);
    }

    @Test
    void deveCalcularTotalComExtras() {
        Pedido p = new Pedido();
        ItemPedido item = new ItemPedido(burger);
        item.adicionarExtra(extra);
        p.adicionarItem(item);
        // 28.90 + 12.90 = 41.80
        assertEquals(41.80, p.calcularTotal(), 0.01);
    }

    @Test
    void deveCalcularTotalComMultiplosItens() {
        Pedido p = new Pedido();
        p.adicionarItem(new ItemPedido(burger)); // 28.90
        Burger burger2 = new Burger(2, "Bacon Inferno", "Desc", 34.90, "🔥", "Picante");
        p.adicionarItem(new ItemPedido(burger2)); // 34.90
        assertEquals(63.80, p.calcularTotal(), 0.01);
    }

    @Test
    void deveConfirmarPedido() {
        Pedido p = new Pedido();
        p.adicionarItem(new ItemPedido(burger));
        p.confirmar();
        assertEquals(Pedido.Status.CONFIRMADO, p.getStatus());
    }

    @Test
    void deveLancarExcecaoConfirmarPedidoVazio() {
        Pedido p = new Pedido();
        assertThrows(IllegalStateException.class, p::confirmar);
    }

    @Test
    void deveIncrementarPedidosDoClienteAoConfirmar() {
        Cliente cliente = new Cliente(1, "Ana", "32 99999-0001", "");
        Pedido p = new Pedido(cliente);
        p.adicionarItem(new ItemPedido(burger));
        p.confirmar();
        assertEquals(1, cliente.getTotalPedidos());
    }

    @Test
    void deveCancelarPedido() {
        Pedido p = new Pedido();
        p.cancelar();
        assertEquals(Pedido.Status.CANCELADO, p.getStatus());
    }

    @Test
    void naoDeveAdicionarItemEmPedidoFechado() {
        Pedido p = new Pedido();
        p.adicionarItem(new ItemPedido(burger));
        p.confirmar();
        assertThrows(IllegalStateException.class, () -> p.adicionarItem(new ItemPedido(burger)));
    }
}
