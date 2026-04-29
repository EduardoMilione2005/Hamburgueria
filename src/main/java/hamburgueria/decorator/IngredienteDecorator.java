package hamburgueria.patterns.decorator;

public abstract class IngredienteDecorator implements Ingrediente {

    protected final Ingrediente ingredienteBase;

    public IngredienteDecorator(Ingrediente ingredienteBase) {
        this.ingredienteBase = ingredienteBase;
    }

    @Override
    public String getDescricao() {
        return ingredienteBase.getDescricao();
    }

    @Override
    public double getPreco() {
        return ingredienteBase.getPreco();
    }
}

class BaconExtra extends IngredienteDecorator {
    public BaconExtra(Ingrediente base) { super(base); }

    @Override public String getDescricao() { return super.getDescricao() + " + Bacon Extra"; }
    @Override public double getPreco()     { return super.getPreco() + 5.00; }
}


class QueijoExtra extends IngredienteDecorator {
    public QueijoExtra(Ingrediente base) { super(base); }

    @Override public String getDescricao() { return super.getDescricao() + " + Queijo Extra"; }
    @Override public double getPreco()     { return super.getPreco() + 3.50; }
}


class MolhoEspecial extends IngredienteDecorator {
    public MolhoEspecial(Ingrediente base) { super(base); }

    @Override public String getDescricao() { return super.getDescricao() + " + Molho Especial"; }
    @Override public double getPreco()     { return super.getPreco() + 2.00; }
}


class OvoFrito extends IngredienteDecorator {
    public OvoFrito(Ingrediente base) { super(base); }

    @Override public String getDescricao() { return super.getDescricao() + " + Ovo Frito"; }
    @Override public double getPreco()     { return super.getPreco() + 4.00; }
}
