package hamburgueria;

import hamburgueria.mediator.*;
import hamburgueria.model.Pedido;
import hamburgueria.patterns.bridge.Notificador;
import hamburgueria.patterns.bridge.NotificadorFactory;
import hamburgueria.repository.CardapioRepository;
import hamburgueria.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MediatorTest {

    private CardapioRepository cardapioRepo;
    private ClienteRepository  clienteRepo;
    private CozinhaColleague   cozinha;
    private CaixaColleague     caixa;
    private NotificacaoColleague notificacao;
    private PedidoMediatorImpl mediator;

    @BeforeEach
    void setUp() {
        cardapioRepo = new CardapioRepository();
        clienteRepo  = new ClienteRepository();
        cozinha      = new CozinhaColleague();
        caixa        = new CaixaColleague();
        notificacao  = new NotificacaoColleague(
                NotificadorFactory.clienteViaWhatsApp(),
                NotificadorFactory.cozinhaViaWhatsApp()
        );
        mediator = new PedidoMediatorImpl(cardapioRepo, clienteRepo, cozinha, notificacao, caixa);
    }

    @Test
    void colleaguesDevemTerMediadorRegistrado() {
        assertNotNull(cozinha);
        assertNotNull(caixa);
        assertNotNull(notificacao);
    }


    @Test
    void adicionarItemDeveRefletirNoPedidoAtual() {
        mediator.adicionarItem(1, List.of());
        assertEquals(1, mediator.getPedidoAtual().getItens().size());
    }

    @Test
    void adicionarItemComExtrasDeveCalcularTotalCorreto() {
        mediator.adicionarItem(1, List.of("e1")); // Smash Classic (28.90) + Batata Frita (12.90)
        assertEquals(41.80, mediator.getPedidoAtual().calcularTotal(), 0.001);
    }

    @Test
    void adicionarMultiplosItens() {
        mediator.adicionarItem(1, List.of());
        mediator.adicionarItem(2, List.of());
        assertEquals(2, mediator.getPedidoAtual().getItens().size());
    }

    @Test
    void removerItemDeveReduzirLista() {
        mediator.adicionarItem(1, List.of());
        mediator.adicionarItem(2, List.of());
        mediator.removerItem(0);
        assertEquals(1, mediator.getPedidoAtual().getItens().size());
    }

    @Test
    void burgerInexistenteDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> mediator.adicionarItem(999, List.of()));
    }

    @Test
    void extraInexistenteDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> mediator.adicionarItem(1, List.of("x999")));
    }

    @Test
    void vincularClienteValido() {
        mediator.vincularCliente(1); // Ana Souza (pré-cadastrada)
        assertNotNull(mediator.getPedidoAtual().getCliente());
        assertEquals("Ana Souza", mediator.getPedidoAtual().getCliente().getNome());
    }

    @Test
    void vincularClienteInexistenteDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> mediator.vincularCliente(999));
    }

    @Test
    void confirmarPedidoDeveOrquestrarTodosOsColleagues() {
        mediator.adicionarItem(1, List.of("e1"));

        Pedido confirmado = mediator.confirmarPedido();

        assertEquals(Pedido.Status.CONFIRMADO, confirmado.getStatus());

        assertEquals(1, caixa.getTotalPedidos());
        assertEquals(41.80, caixa.getFaturamentoTotal(), 0.001);

        assertTrue(cozinha.getLog().contains("Novo pedido #" + confirmado.getId()));

        assertTrue(mediator.getPedidoAtual().estaVazio());
    }

    @Test
    void confirmarPedidoDeveAdicionarAoHistorico() {
        mediator.adicionarItem(1, List.of("e1"));
        mediator.confirmarPedido();

        assertEquals(1, mediator.getPedidosConfirmados().size());
    }

    @Test
    void confirmarDoisPedidosAcumulaFaturamento() {
        mediator.adicionarItem(1, List.of());   // 28.90
        mediator.confirmarPedido();

        mediator.adicionarItem(2, List.of());   // 34.90
        mediator.confirmarPedido();

        assertEquals(2, caixa.getTotalPedidos());
        assertEquals(28.90 + 34.90, caixa.getFaturamentoTotal(), 0.001);
    }

    @Test
    void confirmarPedidoVazioDeveLancarExcecao() {
        assertThrows(Exception.class, () -> mediator.confirmarPedido());
    }

    @Test
    void confirmarPedidoAbaixoDoMinimoDeveLancarExcecao() {
        assertThrows(Exception.class, () -> mediator.confirmarPedido());
    }

    @Test
    void cancelarPedidoDeveNotificarCozinha() {
        mediator.adicionarItem(1, List.of());
        mediator.cancelarPedido();

        assertTrue(cozinha.getLog().contains("cancelado"));
    }

    @Test
    void cancelarPedidoDeveAbrirNovoPedidoVazio() {
        mediator.adicionarItem(1, List.of());
        mediator.cancelarPedido();

        assertTrue(mediator.getPedidoAtual().estaVazio());
    }

    @Test
    void cancelarNaoDeveRegistrarNoCaixa() {
        mediator.adicionarItem(1, List.of());
        mediator.cancelarPedido();

        assertEquals(0, caixa.getTotalPedidos());
        assertEquals(0.0, caixa.getFaturamentoTotal(), 0.001);
    }

    @Test
    void cozinhaNaoConheceCaixa() {
        boolean temCampoProibido = false;
        for (var field : CozinhaColleague.class.getDeclaredFields()) {
            if (field.getType().equals(CaixaColleague.class)) {
                temCampoProibido = true;
            }
        }
        assertFalse(temCampoProibido, "CozinhaColleague nao deve referenciar CaixaColleague diretamente");
    }

    @Test
    void caixaNaoConheceCozinha() {
        boolean temCampoProibido = false;
        for (var field : CaixaColleague.class.getDeclaredFields()) {
            if (field.getType().equals(CozinhaColleague.class)) {
                temCampoProibido = true;
            }
        }
        assertFalse(temCampoProibido, "CaixaColleague nao deve referenciar CozinhaColleague diretamente");
    }

    @Test
    void notificacaoNaoConheceCaixaNemCozinha() {
        boolean temCampoProibido = false;
        for (var field : NotificacaoColleague.class.getDeclaredFields()) {
            if (field.getType().equals(CaixaColleague.class) ||
                field.getType().equals(CozinhaColleague.class)) {
                temCampoProibido = true;
            }
        }
        assertFalse(temCampoProibido, "NotificacaoColleague nao deve referenciar outros colleagues diretamente");
    }


    @Test
    void definirObservacaoDeveSerPropagadaAoPedido() {
        mediator.definirObservacao("sem cebola");
        assertEquals("sem cebola", mediator.getPedidoAtual().getObservacao());
    }
}
