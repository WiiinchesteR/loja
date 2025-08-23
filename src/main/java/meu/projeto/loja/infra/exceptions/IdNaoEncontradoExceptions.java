package meu.projeto.loja.infra.exceptions;

public class IdNaoEncontradoExceptions extends RuntimeException {
    public IdNaoEncontradoExceptions(String mensagem) {
        super(mensagem);
    }
    public IdNaoEncontradoExceptions(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
