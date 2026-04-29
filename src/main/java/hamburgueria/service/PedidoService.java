package hamburgueria.service;

import hamburgueria.model.*;
import hamburgueria.patterns.bridge.CanalNotificacao;
import hamburgueria.patterns.bridge.CanalWhatsApp;
import hamburgueria.patterns.bridge.Notificador;
import hamburgueria.patterns.bridge.NotificadorFactory;
import hamburgueria.repository.CardapioRepository;
import hamburgueria.repository.ClienteRepository;

import java.util.ArrayList;
import java.util.List;

public class PedidoService {

    private final CardapioRepository cardapioRepository;
    private final ClienteRepository clienteRepository;
    private final List<Pedido> pedidosConfirmados = new ArrayList<>();
    private Pedido pedidoAtual;

    private Notificador notificadorCliente;
    private Notificador notificadorCozinha;

    public PedidoService(CardapioRepository cardapioRepository, ClienteRepository clienteRepository) {
        this(cardapioRepository, clienteRepository,
                NotificadorFactory.clienteViaWhatsApp(),
                NotificadorFactory.cozinhaViaWhatsApp());
    }

    public PedidoService(CardapioRepository cardapioRepository,
                         ClienteRepository clienteRepository,
                         Notificador notificadorCliente,
                         Notificador notificadorCozinha) {
        this.cardapioRepository  = cardapioRepository;
        this.clienteRepository   = clienteRepository;
        this.notificadorCliente  = notificadorCliente;
        this.notificadorCozinha  = notificadorCozinha;
        this.pedidoAtual         = new Pedido();
    }

    public void trocarCanalCliente(CanalNotificacao novoCanal) {
        this.notificadorCliente = NotificadorFactory.clienteComCanal(novoCanal);
    }

    public void trocarCanalCozinha(CanalNotificacao novoCanal) {
        this.notificadorCozinha = NotificadorFactory.cozinhaComCanal(novoCanal);
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
        pedidoAtual.adicionarItem(criarItem(burgerId, extrasIds));
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

        Cliente cliente = confirmado.getCliente();
        String contatoCliente  = (cliente != null) ? cliente.getTelefone() : "balcão";
        notificadorCliente.notificarConfirmacao(contatoCliente, confirmado);
        notificadorCozinha.notificarConfirmacao("cozinha", confirmado);

        pedidoAtual = new Pedido();
        return confirmado;
    }

    public void cancelarPedidoAtual() {
        pedidoAtual.cancelar();

        Cliente cliente = pedidoAtual.getCliente();
        String contatoCliente = (cliente != null) ? cliente.getTelefone() : "balcão";
        notificadorCliente.notificarCancelamento(contatoCliente, pedidoAtual);
        notificadorCozinha.notificarCancelamento("cozinha", pedidoAtual);

        pedidoAtual = new Pedido();
    }

    public Pedido getPedidoAtual()               { return pedidoAtual; }
    public List<Pedido> getPedidosConfirmados()  { return new ArrayList<>(pedidosConfirmados); }
    public double calcularTotalAtual()           { return pedidoAtual.calcularTotal(); }
    public Notificador getNotificadorCliente()   { return notificadorCliente; }
    public Notificador getNotificadorCozinha()   { return notificadorCozinha; }
}
