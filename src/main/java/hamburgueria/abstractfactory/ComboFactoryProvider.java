package hamburgueria.patterns.abstractfactory;

import hamburgueria.model.Burger;
import hamburgueria.model.Extra;

class ComboExecutivoFactory implements ComboFactory {

    @Override
    public Burger criarBurger() {
        return new Burger(1, "Smash Classic", "Blend duplo, queijo americano, picles", 28.90, "🍔", "Mais Pedido");
    }

    @Override
    public Extra criarAcompanhamento() {
        return new Extra("e1", "Batata Frita", 12.90, "🍟");
    }

    @Override
    public Extra criarBebida() {
        return new Extra("e3", "Refrigerante", 7.90, "🥤");
    }

    @Override
    public String getNomeCombo() { return "Executivo"; }
}


class ComboPremiumFactory implements ComboFactory {

    @Override
    public Burger criarBurger() {
        return new Burger(3, "Truffle & Shroom", "Blend angus, cogumelos, queijo brie, maionese trufada", 42.90, "🍄", "Premium");
    }

    @Override
    public Extra criarAcompanhamento() {
        return new Extra("e2", "Onion Rings", 14.90, "🧅");
    }

    @Override
    public Extra criarBebida() {
        return new Extra("e4", "Milk Shake", 19.90, "🥛");
    }

    @Override
    public String getNomeCombo() { return "Premium"; }
}


class ComboVeggieFactory implements ComboFactory {

    @Override
    public Burger criarBurger() {
        return new Burger(5, "Veggie Verde", "Blend grão-de-bico, guacamole, queijo de cabra", 29.90, "🥑", "Veggie");
    }

    @Override
    public Extra criarAcompanhamento() {
        return new Extra("e2", "Onion Rings", 14.90, "🧅");
    }

    @Override
    public Extra criarBebida() {
        return new Extra("e3", "Refrigerante", 7.90, "🥤");
    }

    @Override
    public String getNomeCombo() { return "Veggie"; }
}

public class ComboFactoryProvider {

    public enum TipoCombo { EXECUTIVO, PREMIUM, VEGGIE }

    public static ComboFactory obter(TipoCombo tipo) {
        return switch (tipo) {
            case EXECUTIVO -> new ComboExecutivoFactory();
            case PREMIUM   -> new ComboPremiumFactory();
            case VEGGIE    -> new ComboVeggieFactory();
        };
    }
}
