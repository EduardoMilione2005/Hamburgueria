package hamburgueria.model;

public class Burger {

    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private String emoji;
    private String tag;

    public Burger(int id, String nome, String descricao, double preco, String emoji, String tag) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.emoji = emoji;
        this.tag = tag;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public double getPreco() { return preco; }
    public String getEmoji() { return emoji; }
    public String getTag() { return tag; }

    @Override
    public String toString() {
        String tagStr = (tag != null) ? " [" + tag + "]" : "";
        return String.format("%s %s%s - R$ %.2f", emoji, nome, tagStr, preco);
    }
}
