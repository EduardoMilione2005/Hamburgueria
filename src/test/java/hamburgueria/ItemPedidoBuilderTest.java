package hamburgueria;

import hamburgueria.builder.ItemPedidoBuilder;
import hamburgueria.model.Burger;
import hamburgueria.model.Extra;
import hamburgueria.model.ItemPedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoBuilderTest {

    private final Burger burger = new Burger(1, "Smash Classic", "Desc", 28.90, "🍔", "Mais Pedido");
    private final Extra fritas  = new Extra("e1", "Batata Frita", 12.90, "🍟");
    private final Extra refri   = new Extra("e3", "Refrigerante", 7.90, "🥤");

    @Test
    void buildItemSimples() {
        ItemPedido item = new ItemPedidoBuilder(burger).build();

        assertEquals(burger, item.getBurger());
        assertEquals(1, item.getQuantidade());
        assertTrue(item.getExtras().isEmpty());
        assertNull(item.getObservacao());
        assertEquals(28.90, item.calcularTotal(), 0.001);
    }

    @Test
    void buildItemComExtras() {
        ItemPedido item = new ItemPedidoBuilder(burger)
                .comExtra(fritas)
                .comExtra(refri)
                .build();

        assertEquals(2, item.getExtras().size());
        assertEquals(28.90 + 12.90 + 7.90, item.calcularTotal(), 0.001);
    }

    @Test
    void buildItemComQuantidade() {
        ItemPedido item = new ItemPedidoBuilder(burger)
                .comExtra(fritas)
                .comQuantidade(3)
                .build();

        assertEquals(3, item.getQuantidade());
        assertEquals((28.90 + 12.90) * 3, item.calcularTotal(), 0.001);
    }

    @Test
    void buildItemComObservacao() {
        ItemPedido item = new ItemPedidoBuilder(burger)
                .comObservacao("sem cebola")
                .build();

        assertEquals("sem cebola", item.getObservacao());
    }

    @Test
    void builderFluenteCompleto() {
        ItemPedido item = new ItemPedidoBuilder(burger)
                .comExtra(fritas)
                .comExtra(refri)
                .comQuantidade(2)
                .comObservacao("bem passado")
                .build();

        assertEquals(2, item.getQuantidade());
        assertEquals(2, item.getExtras().size());
        assertEquals("bem passado", item.getObservacao());
        assertEquals((28.90 + 12.90 + 7.90) * 2, item.calcularTotal(), 0.001);
    }

    @Test
    void burgerNuloDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> new ItemPedidoBuilder(null));
    }

    @Test
    void extraNuloDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
                new ItemPedidoBuilder(burger).comExtra(null));
    }

    @Test
    void quantidadeZeroDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
                new ItemPedidoBuilder(burger).comQuantidade(0));
    }

    @Test
    void quantidadeNegativaDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
                new ItemPedidoBuilder(burger).comQuantidade(-1));
    }
}
