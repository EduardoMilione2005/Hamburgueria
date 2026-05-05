package hamburgueria.chainofresponsibility;

public class ValidacaoPedidoException extends RuntimeException {

    public ValidacaoPedidoException(String mensagem) {
        super(mensagem);
    }
}
