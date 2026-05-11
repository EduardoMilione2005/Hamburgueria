package hamburgueria.mediator;

import hamburgueria.builder.ItemPedidoBuilder;
import hamburgueria.chainofresponsibility.ValidacaoHandlers;
import hamburgueria.model.*;
import hamburgueria.repository.CardapioRepository;
import hamburgueria.repository.ClienteRepository;

import java.util.ArrayList;
import java.util.List;

public class PedidoMediatorImpl implements PedidoMediator {

    private final CardapioRepository cardapioRepository;
    private final ClienteRepository  clienteRepository;

    private final CozinhaColleague      cozinha;
    private final NotificacaoColleague  notificacao;
    private final CaixaColleague        caixa;

    private Pedido pedidoAtual;
    private final List<Pedido> pedidosConfirmados = new ArrayList<>();

    public PedidoMediatorImpl(CardapioRepository cardapioRepository,
                              ClienteRepository clienteRepository) {
        this(cardapioRepository, clienteRepository,
             new CozinhaColleague(),
             new NotificacaoColleague(),
             new CaixaColleague());
    }

    public PedidoMediatorImpl(CardapioRepository cardapioRepository,
                              ClienteRepository clienteRepository,
                              CozinhaColleague cozinha,
                              NotificacaoColleague notificacao,
                              CaixaColleague caixa) {
        this.cardapioRepository = cardapioRepository;
        this.clienteRepository  = clienteRepository;
        this.cozinha            = cozinha;
        this.notificacao        = notificacao;
        this.caixa              = caixa;
        this.pedidoAtual        = new Pedido();

        cozinha.setMediator(this);
        notificacao.setMediator(this);
        caixa.setMediator(this);
    }
    @Override
    public void adicionarItem(int burgerId, List<String> extrasIds) {
        Burger burger = cardapioRepository.buscarBurgerPorId(burgerId)
                .orElseThrow(() -> new IllegalArgumentException("Burger nao encontrado: " + burgerId));

        ItemPedidoBuilder builder = new ItemPedidoBuilder(burger);
        for (String extraId : extrasIds) {
            Extra extra = cardapioRepository.buscarExtraPorId(extraId)
                    .orElseThrow(() -> new IllegalArgumentException("Extra nao encontrado: " + extraId));
            builder.comExtra(extra);
        }
        pedidoAtual.adicionarItem(builder.build());
    }

    @Override
    public void removerItem(int indice) {
        pedidoAtual.removerItem(indice);
    }

    @Override
    public void vincularCliente(int clienteId) {
        Cliente cliente = clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente nao encontrado: " + clienteId));
        pedidoAtual.setCliente(cliente);
    }

    @Override
    public void definirObservacao(String obs) {
        pedidoAtual.setObservacao(obs);
    }


    @Override
    public Pedido confirmarPedido() {
        System.out.println("--- Validando pedido (Mediator + Chain of Responsibility) ---");
        ValidacaoHandlers.obter().validar(pedidoAtual);
        System.out.println("--- Validacao concluida com sucesso ---");

        pedidoAtual.confirmar();
        Pedido confirmado = pedidoAtual;
        pedidosConfirmados.add(confirmado);

        cozinha.receberNovoPedido(confirmado);

        notificacao.notificarConfirmacao(confirmado);

        caixa.registrarPedido(confirmado);

        pedidoAtual = new Pedido();
        return confirmado;
    }

    @Override
    public void cancelarPedido() {
        pedidoAtual.cancelar();

        cozinha.receberCancelamento(pedidoAtual);
        notificacao.notificarCancelamento(pedidoAtual);

        pedidoAtual = new Pedido();
    }

    @Override
    public Pedido getPedidoAtual()              { return pedidoAtual; }

    @Override
    public List<Pedido> getPedidosConfirmados() { return new ArrayList<>(pedidosConfirmados); }

    public CozinhaColleague     getCozinha()     { return cozinha; }
    public NotificacaoColleague getNotificacao() { return notificacao; }
    public CaixaColleague       getCaixa()       { return caixa; }
}
