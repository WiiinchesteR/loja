package meu.projeto.loja.infra.exceptions.dto;

public record ApiFieldErrorDTO(
        String field,
        String message,
        Object rejectedValue
) {}
