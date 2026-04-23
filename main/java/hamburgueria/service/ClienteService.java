package hamburgueria.service;

import hamburgueria.model.Cliente;
import hamburgueria.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrar(String nome, String telefone, String endereco) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (telefone == null || telefone.isBlank()) throw new IllegalArgumentException("Telefone é obrigatório.");
        return clienteRepository.salvar(nome, telefone, endereco);
    }

    public Optional<Cliente> buscarPorId(int id) {
        return clienteRepository.buscarPorId(id);
    }

    public Optional<Cliente> buscarPorNome(String nome) {
        return clienteRepository.buscarPorNome(nome);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }
}
