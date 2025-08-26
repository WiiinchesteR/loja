package meu.projeto.loja.infra.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.slf4j.MDC;

import java.net.URI;
import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ====== EXCEÇÕES CUSTOMIZADAS ======

    @ExceptionHandler(IdNaoEncontradoExceptions.class)
    public ProblemDetail handleIdNaoEncontrado(IdNaoEncontradoExceptions ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "Recurso não encontrado.", ex.getMessage(), req, null);
    }

    @ExceptionHandler(NomeUsuarioExistenteExceptions.class)
    public ProblemDetail handleNomeUsuarioExistente(NomeUsuarioExistenteExceptions ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Conflito de dados.", ex.getMessage(), req, null);
    }

    @ExceptionHandler(NomeUsuarioComEspacosException.class)
    public ProblemDetail handleNomeUsuarioComEspacos(NomeUsuarioComEspacosException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Dados inválidos.", ex.getMessage(), req, null);
    }

    @ExceptionHandler(SenhaComEspacosException.class)
    public ProblemDetail handleSenhaComEspacos(SenhaComEspacosException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Dados inválidos.", ex.getMessage(), req, null);
    }

    // ====== VALIDAÇÃO ======

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<Map<String, Object>> fieldErrors = ex.getBindingResult().getFieldErrors().stream().map(err -> {
            Object rejected = err.getRejectedValue();
            if ("senha".equalsIgnoreCase(err.getField())) rejected = "***"; // nunca vazar senha
            return Map.of(
                    "field", err.getField(),
                    "message", err.getDefaultMessage(),
                    "rejectedValue", rejected
            );
        }).toList();
        return build(HttpStatus.BAD_REQUEST,
                "Erro de validação.",
                "Verifique os campos e tente novamente.",
                req,
                fieldErrors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        List<Map<String, Object>> fieldErrors = ex.getConstraintViolations().stream().map(v ->
                Map.of(
                        "field", v.getPropertyPath().toString(),
                        "message", v.getMessage(),
                        "rejectedValue", v.getInvalidValue()
                )
        ).toList();
        return build(HttpStatus.BAD_REQUEST,
                "Parâmetros inválidos.",
                "Verifique os parâmetros da requisição.",
                req,
                fieldErrors
        );
    }

    // ====== REQUEST COMUM ======

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Bad Request", "Corpo da requisição inválido ou JSON malformado.", req, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String msg = "Parâmetro '%s' inválido: %s".formatted(ex.getName(), ex.getValue());
        return build(HttpStatus.BAD_REQUEST, "Bad Request", msg, req, null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        String msg = "Parâmetro obrigatório ausente: '%s'".formatted(ex.getParameterName());
        return build(HttpStatus.BAD_REQUEST, "Bad Request", msg, req, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", "Método HTTP não suportado.", req, null);
    }

    // ====== BANCO/INTEGRIDADE ======

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Conflict", "Violação de integridade dos dados.", req, null);
    }

    // ====== FALLBACK GENÉRICO ======

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno.", "Erro inesperado. Contate o suporte.", req, null);
    }

    // ====== HELPER ======

    private ProblemDetail build(HttpStatus status,
                                String title,
                                String detail,
                                HttpServletRequest req,
                                List<Map<String, Object>> fieldErrors) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setInstance(URI.create(req.getRequestURI()));
        pd.setProperty("method", req.getMethod());
        pd.setProperty("timestamp", Instant.now());

        // Pega do MDC (injetado pelo filtro)
        String traceId = MDC.get("traceId");
        pd.setProperty("traceId", traceId);
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            pd.setProperty("fieldErrors", fieldErrors);
        }
        return pd;
    }
}
