package hamburgueria.patterns.abstractfactory;

import hamburgueria.model.Burger;
import hamburgueria.model.Extra;

public interface ComboFactory {
    Burger criarBurger();
    Extra  criarAcompanhamento();
    Extra  criarBebida();
    String getNomeCombo();

    default void exibirCombo() {
        Burger b = criarBurger();
        Extra  a = criarAcompanhamento();
        Extra  beb = criarBebida();
        double total = b.getPreco() + a.getPreco() + beb.getPreco();
        System.out.printf("=== Combo %s (Abstract Factory) ===%n", getNomeCombo());
        System.out.printf("  Burger:         %s%n", b);
        System.out.printf("  Acompanhamento: %s%n", a);
        System.out.printf("  Bebida:         %s%n", beb);
        System.out.printf("  Total do combo: R$ %.2f%n%n", total);
    }
}
