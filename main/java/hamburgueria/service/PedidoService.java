package hamburgueria.service;

import hamburgueria.model.*;
import hamburgueria.repository.CardapioRepository;
import hamburgueria.repository.ClienteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoService {

    private final CardapioRepository cardapioRepository;
    private final ClienteRepository clienteRepository;
    private final List<Pedido> pedidosConfirmados = new ArrayList<>();
    private Pedido pedidoAtual;

    public PedidoService(CardapioRepository cardapioRepository, ClienteRepository clienteRepository) {
        this.cardapioRepository = cardapioRepository;
        this.clienteRepository = clienteRepository;
        this.pedidoAtual = new Pedido();
    }

    public ItemPedido criarItem(int burgerId, List<String> extrasIds) {
        Burger burger = cardapioRepository.buscarBurgerPorId(burgerId)
                .orElseThrow(() -> new IllegalArgumentException("Burger não encontrado: " + burgerId));
        ItemPedido item = new ItemPedido(burger);
        for (String extraId : extrasIds) {
            Extra extra = cardapioRepository.buscarExtraPorId(extraId)
                    .orElseThrow(() -> new IllegalArgumentException("Extra não encontrado: " + extraId));
            item.adicionarExtra(extra);
        }
        return item;
    }

    public void adicionarItem(int burgerId, List<String> extrasIds) {
        ItemPedido item = criarItem(burgerId, extrasIds);
        pedidoAtual.adicionarItem(item);
    }

    public void removerItem(int indice) {
        pedidoAtual.removerItem(indice);
    }

    public void vincularCliente(int clienteId) {
        Cliente cliente = clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado: " + clienteId));
        pedidoAtual.setCliente(cliente);
    }

    public void definirObservacao(String obs) {
        pedidoAtual.setObservacao(obs);
    }

    public Pedido confirmarPedido() {
        pedidoAtual.confirmar();
        pedidosConfirmados.add(pedidoAtual);
        Pedido confirmado = pedidoAtual;
        pedidoAtual = new Pedido();
        return confirmado;
    }

    public void cancelarPedidoAtual() {
        pedidoAtual.cancelar();
        pedidoAtual = new Pedido();
    }

    public Pedido getPedidoAtual() { return pedidoAtual; }
    public List<Pedido> getPedidosConfirmados() { return new ArrayList<>(pedidosConfirmados); }
    public double calcularTotalAtual() { return pedidoAtual.calcularTotal(); }
}
