package meu.projeto.loja.infra.exceptions;

public class NomeUsuarioComEspacosException extends RuntimeException {
    public NomeUsuarioComEspacosException(String message) {
        super(message);
    }
    public NomeUsuarioComEspacosException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}