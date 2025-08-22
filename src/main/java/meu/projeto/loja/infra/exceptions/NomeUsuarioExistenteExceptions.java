package meu.projeto.loja.infra.exceptions;

public class NomeUsuarioExistenteExceptions extends RuntimeException {
    public NomeUsuarioExistenteExceptions(String mensagem) {
        super(mensagem);
    }
    public NomeUsuarioExistenteExceptions(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
}
