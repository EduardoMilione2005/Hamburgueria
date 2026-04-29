package hamburgueria.patterns.decorator;

public class BurgerPersonalizadoBuilder {

    private Ingrediente ingrediente;

    public BurgerPersonalizadoBuilder(String nomeBurger, double precoBase) {
        this.ingrediente = new BurgerBase(nomeBurger, precoBase);
    }

    public BurgerPersonalizadoBuilder comBaconExtra()    { ingrediente = new BaconExtra(ingrediente);    return this; }
    public BurgerPersonalizadoBuilder comQueijoExtra()   { ingrediente = new QueijoExtra(ingrediente);   return this; }
    public BurgerPersonalizadoBuilder comMolhoEspecial() { ingrediente = new MolhoEspecial(ingrediente); return this; }
    public BurgerPersonalizadoBuilder comOvoFrito()      { ingrediente = new OvoFrito(ingrediente);      return this; }

    public Ingrediente build() { return ingrediente; }

    public void exibir() {
        System.out.println("=== Burger Personalizado (Decorator) ===");
        System.out.println("Descrição: " + ingrediente.getDescricao());
        System.out.printf("Preço final: R$ %.2f%n%n", ingrediente.getPreco());
    }
}
