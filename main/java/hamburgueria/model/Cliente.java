package hamburgueria.model;

public class Cliente {

    private int id;
    private String nome;
    private String telefone;
    private String endereco;
    private int totalPedidos;

    public Cliente(int id, String nome, String telefone, String endereco) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (telefone == null || telefone.isBlank()) throw new IllegalArgumentException("Telefone é obrigatório.");
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = (endereco != null) ? endereco : "";
        this.totalPedidos = 0;
    }

    public void registrarPedido() {
        this.totalPedidos++;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public int getTotalPedidos() { return totalPedidos; }

    @Override
    public String toString() {
        return String.format("Cliente{id=%d, nome='%s', telefone='%s', pedidos=%d}",
                id, nome, telefone, totalPedidos);
    }
}
