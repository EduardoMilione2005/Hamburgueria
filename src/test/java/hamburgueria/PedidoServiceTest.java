package hamburgueria;

import hamburgueria.model.Pedido;
import hamburgueria.repository.CardapioRepository;
import hamburgueria.repository.ClienteRepository;
import hamburgueria.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {

    private PedidoService service;

    @BeforeEach
    void setUp() {
        service = new PedidoService(new CardapioRepository(), new ClienteRepository());
    }

    @Test
    void deveAdicionarItemAoPedidoAtual() {
        service.adicionarItem(1, List.of());
        assertFalse(service.getPedidoAtual().estaVazio());
    }

    @Test
    void deveCalcularTotalCorreto() {
        service.adicionarItem(1, List.of("e1")); // Smash 28.90 + Batata 12.90
        assertEquals(41.80, service.calcularTotalAtual(), 0.01);
    }

    @Test
    void deveRemoverItemDoPedido() {
        service.adicionarItem(1, List.of());
        service.removerItem(0);
        assertTrue(service.getPedidoAtual().estaVazio());
    }

    @Test
    void deveLancarExcecaoBurgerInexistente() {
        assertThrows(IllegalArgumentException.class,
                () -> service.adicionarItem(999, List.of()));
    }

    @Test
    void deveLancarExcecaoExtraInexistente() {
        assertThrows(IllegalArgumentException.class,
                () -> service.adicionarItem(1, List.of("x99")));
    }

    @Test
    void deveConfirmarPedidoEIniciarNovo() {
        service.adicionarItem(1, List.of());
        Pedido confirmado = service.confirmarPedido();
        assertEquals(Pedido.Status.CONFIRMADO, confirmado.getStatus());
        assertTrue(service.getPedidoAtual().estaVazio()); // novo pedido vazio
    }

    @Test
    void deveRegistrarPedidoNoHistorico() {
        service.adicionarItem(1, List.of());
        service.confirmarPedido();
        assertEquals(1, service.getPedidosConfirmados().size());
    }

    @Test
    void deveVincularClienteAoPedido() {
        service.vincularCliente(1); // Ana Souza existe no repo
        assertNotNull(service.getPedidoAtual().getCliente());
        assertEquals("Ana Souza", service.getPedidoAtual().getCliente().getNome());
    }

    @Test
    void deveLancarExcecaoClienteInexistente() {
        assertThrows(IllegalArgumentException.class, () -> service.vincularCliente(999));
    }

    @Test
    void deveCancelarPedidoAtualEIniciarNovo() {
        service.adicionarItem(1, List.of());
        service.cancelarPedidoAtual();
        assertTrue(service.getPedidoAtual().estaVazio());
    }

    @Test
    void deveDefinirObservacaoNoPedido() {
        service.definirObservacao("Sem cebola");
        assertEquals("Sem cebola", service.getPedidoAtual().getObservacao());
    }
}
