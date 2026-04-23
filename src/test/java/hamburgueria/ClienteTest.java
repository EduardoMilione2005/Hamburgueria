package hamburgueria;

import hamburgueria.model.Cliente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveCriarClienteValido() {
        Cliente c = new Cliente(1, "Ana Souza", "32 99999-0001", "Rua A, 10");
        assertEquals("Ana Souza", c.getNome());
        assertEquals("32 99999-0001", c.getTelefone());
        assertEquals(0, c.getTotalPedidos());
    }

    @Test
    void deveLancarExcecaoNomeVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cliente(1, "", "32 99999-0001", ""));
    }

    @Test
    void deveLancarExcecaoTelefoneVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cliente(1, "Ana", "", ""));
    }

    @Test
    void deveIncrementarTotalPedidosAoRegistrar() {
        Cliente c = new Cliente(1, "Ana", "32 99999-0001", "");
        c.registrarPedido();
        c.registrarPedido();
        assertEquals(2, c.getTotalPedidos());
    }

    @Test
    void devePermitirEnderecoNulo() {
        Cliente c = new Cliente(1, "Bruno", "32 98888-0000", null);
        assertEquals("", c.getEndereco());
    }
}
