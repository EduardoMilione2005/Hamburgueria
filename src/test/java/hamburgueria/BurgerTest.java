package hamburgueria;

import hamburgueria.model.Burger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BurgerTest {

    @Test
    void deveCriarBurgerComAtributosCorretos() {
        Burger b = new Burger(1, "Smash Classic", "Desc", 28.90, "🍔", "Mais Pedido");
        assertEquals(1, b.getId());
        assertEquals("Smash Classic", b.getNome());
        assertEquals(28.90, b.getPreco());
        assertEquals("🍔", b.getEmoji());
        assertEquals("Mais Pedido", b.getTag());
    }

    @Test
    void devePermitirTagNula() {
        Burger b = new Burger(4, "BBQ Ranch", "Desc", 31.90, "🤠", null);
        assertNull(b.getTag());
    }

    @Test
    void toStringDeveConterNomeEPreco() {
        Burger b = new Burger(1, "Smash Classic", "Desc", 28.90, "🍔", "Mais Pedido");
        String str = b.toString();
        assertTrue(str.contains("Smash Classic"));
        assertTrue(str.contains("28,90") || str.contains("28.90"));
    }
}
