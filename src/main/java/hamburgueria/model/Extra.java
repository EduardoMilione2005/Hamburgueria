package hamburgueria.model;

public class Extra {

    private String id;
    private String nome;
    private double preco;
    private String emoji;

    public Extra(String id, String nome, double preco, String emoji) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.emoji = emoji;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public String getEmoji() { return emoji; }

    @Override
    public String toString() {
        return String.format("%s %s (+R$ %.2f)", emoji, nome, preco);
    }
}
