package meu.projeto.loja.infra.exceptions.dto;

import java.time.LocalDateTime;

public record ApiErrorDTO(
        int status,
        String erro,
        LocalDateTime timestamp
) {}