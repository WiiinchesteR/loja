package meu.projeto.loja.infra.exceptions;

public class SenhaComEspacosException extends RuntimeException {
    public SenhaComEspacosException(String message) {
        super(message);
    }
    public SenhaComEspacosException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}