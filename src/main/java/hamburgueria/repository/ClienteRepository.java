package hamburgueria.repository;

import hamburgueria.model.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepository {

    private final List<Cliente> clientes = new ArrayList<>();
    private int proximoId = 1;

    public ClienteRepository() {
        // Clientes pré-cadastrados
        clientes.add(new Cliente(proximoId++, "Ana Souza", "32 99999-0001", "Rua das Flores, 10"));
        clientes.add(new Cliente(proximoId++, "Bruno Lima", "32 98888-1234", ""));
    }

    public Cliente salvar(String nome, String telefone, String endereco) {
        Cliente cliente = new Cliente(proximoId++, nome, telefone, endereco);
        clientes.add(cliente);
        return cliente;
    }

    public Optional<Cliente> buscarPorId(int id) {
        return clientes.stream().filter(c -> c.getId() == id).findFirst();
    }

    public Optional<Cliente> buscarPorNome(String nome) {
        return clientes.stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    public List<Cliente> listarTodos() {
        return new ArrayList<>(clientes);
    }

    public int totalClientes() {
        return clientes.size();
    }
}
