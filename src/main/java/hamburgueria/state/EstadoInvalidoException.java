package hamburgueria.state;

public class EstadoInvalidoException extends RuntimeException {

    public EstadoInvalidoException(String estadoAtual, String operacao) {
        super(String.format(
            "❌ Operação '%s' não permitida no estado '%s'.", operacao, estadoAtual
        ));
    }
}
