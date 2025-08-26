package meu.projeto.loja.infra.exceptions.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        String path,
        String method,
        LocalDateTime timestamp,
        String traceId,
        List<ApiFieldErrorDTO> fieldErrors
) {}
