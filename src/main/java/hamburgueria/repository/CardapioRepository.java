package hamburgueria.repository;

import hamburgueria.model.Burger;
import hamburgueria.model.Extra;

import java.util.List;
import java.util.Optional;

public class CardapioRepository {

    private final List<Burger> burgers = List.of(
        new Burger(1, "Smash Classic",    "Blend duplo, queijo americano, picles, cebola caramelizada, molho especial", 28.90, "🍔", "Mais Pedido"),
        new Burger(2, "Bacon Inferno",    "Blend 180g, bacon crocante, queijo cheddar, pimenta jalapeño, aioli defumado", 34.90, "🔥", "Picante"),
        new Burger(3, "Truffle & Shroom", "Blend angus, cogumelos salteados, queijo brie, rúcula, maionese trufada", 42.90, "🍄", "Premium"),
        new Burger(4, "BBQ Ranch",        "Blend 180g, molho barbecue artesanal, cheddar defumado, alface americana", 31.90, "🤠", null),
        new Burger(5, "Veggie Verde",     "Blend de grão-de-bico, guacamole, tomate seco, queijo de cabra, brotos", 29.90, "🥑", "Veggie"),
        new Burger(6, "Double Trouble",   "Dois blends 150g, dois queijos, bacon duplo, cebola crispy, molho secreto", 48.90, "💥", "Especial")
    );

    private final List<Extra> extras = List.of(
        new Extra("e1", "Batata Frita", 12.90, "🍟"),
        new Extra("e2", "Onion Rings",  14.90, "🧅"),
        new Extra("e3", "Refrigerante",  7.90, "🥤"),
        new Extra("e4", "Milk Shake",   19.90, "🥛")
    );

    public List<Burger> listarBurgers() { return burgers; }
    public List<Extra> listarExtras() { return extras; }

    public Optional<Burger> buscarBurgerPorId(int id) {
        return burgers.stream().filter(b -> b.getId() == id).findFirst();
    }

    public Optional<Extra> buscarExtraPorId(String id) {
        return extras.stream().filter(e -> e.getId().equals(id)).findFirst();
    }
}
