package hamburgueria;

import hamburgueria.model.*;
import hamburgueria.patterns.bridge.*;
import hamburgueria.patterns.decorator.BurgerPersonalizadoBuilder;
import hamburgueria.patterns.decorator.Ingrediente;
import hamburgueria.repository.CardapioRepository;
import hamburgueria.repository.ClienteRepository;
import hamburgueria.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PadroesTest {

    @Test
    void bridge_canalWhatsAppDeveSerReconhecido() {
        Notificador n = NotificadorFactory.clienteViaWhatsApp();
        assertEquals("WhatsApp", n.getCanalNome());
    }

    @Test
    void bridge_canalEmailDeveSerReconhecido() {
        Notificador n = NotificadorFactory.cozinhaViaEmail();
        assertEquals("Email", n.getCanalNome());
    }

    @Test
    void bridge_notificacaoConfirmacaoNaoDeveLancarExcecao() {
        Notificador n = NotificadorFactory.clienteViaWhatsApp();
        Pedido p = pedidoComBurger();
        p.confirmar();
        assertDoesNotThrow(() -> n.notificarConfirmacao("32 99999-0001", p));
    }

    @Test
    void bridge_notificacaoCancelamentoNaoDeveLancarExcecao() {
        Notificador n = NotificadorFactory.cozinhaViaEmail();
        Pedido p = pedidoComBurger();
        assertDoesNotThrow(() -> n.notificarCancelamento("cozinha@brasa.com", p));
    }

    @Test
    void bridge_pedidoServiceUsaWhatsAppPorPadrao() {
        PedidoService svc = new PedidoService(new CardapioRepository(), new ClienteRepository());
        assertEquals("WhatsApp", svc.getNotificadorCliente().getCanalNome());
        assertEquals("WhatsApp", svc.getNotificadorCozinha().getCanalNome());
    }

    @Test
    void bridge_pedidoServicePermiteTrocarCanalClienteEmRuntime() {
        PedidoService svc = new PedidoService(new CardapioRepository(), new ClienteRepository());
        svc.trocarCanalCliente(new CanalEmail());
        assertEquals("Email", svc.getNotificadorCliente().getCanalNome());
        // cozinha não muda
        assertEquals("WhatsApp", svc.getNotificadorCozinha().getCanalNome());
    }

    @Test
    void bridge_pedidoServicePermiteTrocarCanalCozinhaEmRuntime() {
        PedidoService svc = new PedidoService(new CardapioRepository(), new ClienteRepository());
        svc.trocarCanalCozinha(new CanalEmail());
        assertEquals("Email", svc.getNotificadorCozinha().getCanalNome());
    }

    @Test
    void bridge_confirmarPedidoDisparaNotificacaoSemErro() {
        PedidoService svc = new PedidoService(new CardapioRepository(), new ClienteRepository());
        svc.adicionarItem(1, List.of());
        assertDoesNotThrow(svc::confirmarPedido);
    }

    @Test
    void bridge_cancelarPedidoDisparaNotificacaoSemErro() {
        PedidoService svc = new PedidoService(new CardapioRepository(), new ClienteRepository());
        svc.adicionarItem(1, List.of());
        assertDoesNotThrow(svc::cancelarPedidoAtual);
    }

    @Test
    void bridge_qualquerCombinacaoCanalNotificadorFunciona() {
        assertDoesNotThrow(NotificadorFactory::clienteViaWhatsApp);
        assertDoesNotThrow(NotificadorFactory::clienteViaEmail);
        assertDoesNotThrow(NotificadorFactory::cozinhaViaWhatsApp);
        assertDoesNotThrow(NotificadorFactory::cozinhaViaEmail);
    }
    

    @Test
    void decorator_burgerBaseSemExtrasMantemPreco() {
        Ingrediente b = new BurgerPersonalizadoBuilder("Smash Classic", 28.90).build();
        assertEquals(28.90, b.getPreco(), 0.01);
        assertTrue(b.getDescricao().contains("Smash Classic"));
    }

    @Test
    void decorator_baconExtraAdicionaPrecoEDescricao() {
        Ingrediente b = new BurgerPersonalizadoBuilder("Smash Classic", 28.90)
                .comBaconExtra()
                .build();
        assertEquals(33.90, b.getPreco(), 0.01);
        assertTrue(b.getDescricao().contains("Bacon Extra"));
    }

    @Test
    void decorator_queijoExtraAdicionaPrecoEDescricao() {
        Ingrediente b = new BurgerPersonalizadoBuilder("Smash Classic", 28.90)
                .comQueijoExtra()
                .build();
        assertEquals(32.40, b.getPreco(), 0.01);
        assertTrue(b.getDescricao().contains("Queijo Extra"));
    }

    @Test
    void decorator_molhoEspecialAdicionaPrecoEDescricao() {
        Ingrediente b = new BurgerPersonalizadoBuilder("BBQ Ranch", 31.90)
                .comMolhoEspecial()
                .build();
        assertEquals(33.90, b.getPreco(), 0.01);
        assertTrue(b.getDescricao().contains("Molho Especial"));
    }

    @Test
    void decorator_ovoFritoAdicionaPrecoEDescricao() {
        Ingrediente b = new BurgerPersonalizadoBuilder("BBQ Ranch", 31.90)
                .comOvoFrito()
                .build();
        assertEquals(35.90, b.getPreco(), 0.01);
        assertTrue(b.getDescricao().contains("Ovo Frito"));
    }

    @Test
    void decorator_multiplosExtrasAcumulamPrecoCorretamente() {
        Ingrediente b = new BurgerPersonalizadoBuilder("Smash Classic", 28.90)
                .comBaconExtra()
                .comQueijoExtra()
                .comMolhoEspecial()
                .comOvoFrito()
                .build();
        assertEquals(43.40, b.getPreco(), 0.01);
    }

    @Test
    void decorator_descricaoEmpilhadaContemTodosIngredientes() {
        Ingrediente b = new BurgerPersonalizadoBuilder("Smash Classic", 28.90)
                .comBaconExtra()
                .comQueijoExtra()
                .comMolhoEspecial()
                .comOvoFrito()
                .build();
        String desc = b.getDescricao();
        assertTrue(desc.contains("Bacon Extra"));
        assertTrue(desc.contains("Queijo Extra"));
        assertTrue(desc.contains("Molho Especial"));
        assertTrue(desc.contains("Ovo Frito"));
    }

    @Test
    void decorator_mesmoBuilderPodeSerReusadoParaDiferentesBurgers() {
        Ingrediente s = new BurgerPersonalizadoBuilder("Smash Classic", 28.90).comBaconExtra().build();
        Ingrediente v = new BurgerPersonalizadoBuilder("Veggie Verde", 29.90).comQueijoExtra().build();
        assertNotEquals(s.getPreco(), v.getPreco(), 0.01);
        assertFalse(s.getDescricao().equals(v.getDescricao()));
    }

    private Pedido pedidoComBurger() {
        Burger b = new Burger(1, "Smash Classic", "Desc", 28.90, "🍔", null);
        Pedido p = new Pedido();
        p.adicionarItem(new ItemPedido(b));
        return p;
    }
}

class PadroesTest {
    
    @Test
    void singletonDeveRetornarMesmaInstancia() {
        GerenciadorDePedidos g1 = GerenciadorDePedidos.getInstance();
        GerenciadorDePedidos g2 = GerenciadorDePedidos.getInstance();
        assertSame(g1, g2, "Singleton deve retornar a mesma instância");
    }

    @Test
    void singletonDeveAcumularFaturamento() {
        GerenciadorDePedidos gerenciador = GerenciadorDePedidos.getInstance();
        double antes = gerenciador.getFaturamentoTotal();

        Burger b = new Burger(1, "Smash Classic", "Desc", 28.90, "🍔", null);
        Pedido p = new Pedido();
        p.adicionarItem(new ItemPedido(b));
        p.confirmar();
        gerenciador.registrarPedido(p);

        assertTrue(gerenciador.getFaturamentoTotal() >= antes + 28.90);
    }


    @Test
    void bridgeClienteWhatsAppDeveNotificar() {
        Notificador notificador = NotificadorFactory.clienteViaWhatsApp();
        assertEquals("WhatsApp", notificador.getCanalNome());

        Burger b = new Burger(1, "Smash", "Desc", 28.90, "🍔", null);
        Pedido p = new Pedido();
        p.adicionarItem(new ItemPedido(b));
        p.confirmar();

        assertDoesNotThrow(() -> notificador.notificarConfirmacao("32 99999-0001", p));
    }

    @Test
    void bridgeCozinhaEmailDeveNotificar() {
        Notificador notificador = NotificadorFactory.cozinhaViaEmail();
        assertEquals("Email", notificador.getCanalNome());

        Burger b = new Burger(1, "Smash", "Desc", 28.90, "🍔", null);
        Pedido p = new Pedido();
        p.adicionarItem(new ItemPedido(b));
        p.confirmar();

        assertDoesNotThrow(() -> notificador.notificarConfirmacao("cozinha@brasa.com", p));
    }

    @Test
    void bridgePodeCombinarQualquerNotificadorComQualquerCanal() {
        assertDoesNotThrow(NotificadorFactory::clienteViaWhatsApp);
        assertDoesNotThrow(NotificadorFactory::clienteViaEmail);
        assertDoesNotThrow(NotificadorFactory::cozinhaViaWhatsApp);
        assertDoesNotThrow(NotificadorFactory::cozinhaViaEmail);
    }


    @Test
    void decoratorBurgerBaseDeveTerPrecoCorreto() {
        Ingrediente burger = new BurgerPersonalizadoBuilder("Smash Classic", 28.90).build();
        assertEquals(28.90, burger.getPreco(), 0.01);
        assertTrue(burger.getDescricao().contains("Smash Classic"));
    }

    @Test
    void decoratorComBaconDeveAdicionarPreco() {
        Ingrediente burger = new BurgerPersonalizadoBuilder("Smash Classic", 28.90)
                .comBaconExtra() 
                .build();
        assertEquals(33.90, burger.getPreco(), 0.01);
        assertTrue(burger.getDescricao().contains("Bacon Extra"));
    }

    @Test
    void decoratorEmpilhadoDeveAcumularPrecoEDescricao() {
        Ingrediente burger = new BurgerPersonalizadoBuilder("Smash Classic", 28.90)
                .comBaconExtra()   
                .comQueijoExtra()   
                .comMolhoEspecial() 
                .comOvoFrito()      
                .build();
        assertEquals(43.40, burger.getPreco(), 0.01);
        String desc = burger.getDescricao();
        assertTrue(desc.contains("Bacon Extra"));
        assertTrue(desc.contains("Queijo Extra"));
        assertTrue(desc.contains("Molho Especial"));
        assertTrue(desc.contains("Ovo Frito"));
    }


    @Test
    void abstractFactoryComboExecutivoDeveCriarProdutosCoerentes() {
        ComboFactory factory = ComboFactoryProvider.obter(ComboFactoryProvider.TipoCombo.EXECUTIVO);
        assertEquals("Executivo", factory.getNomeCombo());
        assertNotNull(factory.criarBurger());
        assertNotNull(factory.criarAcompanhamento());
        assertNotNull(factory.criarBebida());
    }

    @Test
    void abstractFactoryComboPremiumDeveTerPrecoMaior() {
        ComboFactory executivo = ComboFactoryProvider.obter(ComboFactoryProvider.TipoCombo.EXECUTIVO);
        ComboFactory premium   = ComboFactoryProvider.obter(ComboFactoryProvider.TipoCombo.PREMIUM);

        double totalExec = executivo.criarBurger().getPreco()
                         + executivo.criarAcompanhamento().getPreco()
                         + executivo.criarBebida().getPreco();

        double totalPrem = premium.criarBurger().getPreco()
                         + premium.criarAcompanhamento().getPreco()
                         + premium.criarBebida().getPreco();

        assertTrue(totalPrem > totalExec, "Combo Premium deve custar mais que Executivo");
    }

    @Test
    void abstractFactoryComboVeggieDeveTerBurgerVeggie() {
        ComboFactory factory = ComboFactoryProvider.obter(ComboFactoryProvider.TipoCombo.VEGGIE);
        Burger burger = factory.criarBurger();
        assertEquals("Veggie Verde", burger.getNome());
        assertEquals("Veggie", burger.getTag());
    }


    @Test
    void factoryMethodBalcaoDeveCriarPedido() {
        PedidoCreator creator = new PedidoBalcaoCreator();
        Cliente cliente = new Cliente(1, "Ana", "32 99999-0001", "");
        Pedido pedido = creator.iniciarPedido(cliente);

        assertNotNull(pedido);
        assertEquals("BALCÃO", creator.getTipoPedido());
    }

    @Test
    void factoryMethodDeliveryDeveExigirCliente() {
        var creator = hamburgueria.patterns.factorymethod.PedidoCreatorFactory
                .obter(hamburgueria.patterns.factorymethod.PedidoCreatorFactory.TipoPedido.DELIVERY);
        assertThrows(IllegalArgumentException.class, () -> creator.criarPedido(null));
    }

    @Test
    void factoryMethodAgendadoDeveRegistrarHorario() {
        var creator = hamburgueria.patterns.factorymethod.PedidoCreatorFactory
                .obter(hamburgueria.patterns.factorymethod.PedidoCreatorFactory.TipoPedido.AGENDADO, "18:30");
        Cliente c = new Cliente(1, "Bruno", "32 98888-0000", "");
        Pedido p = creator.criarPedido(c);
        assertTrue(p.getObservacao().contains("18:30"));
    }
}
