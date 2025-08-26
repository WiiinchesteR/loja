package meu.projeto.loja.domain.usuario.dto;

import jakarta.validation.constraints.Size;
import meu.projeto.loja.infra.validation.NotBlankIfPresent;

public record UsuarioUpdateDTO(
        @NotBlankIfPresent(message = "Nome não pode ser vazio.")
        String nome,
        @NotBlankIfPresent(message = "Nome de usuário não pode ser vazio.")
        @Size(min = 6, message = "Nome de usuário deve ter pelo menos 6 caracteres.")
        String nomeUsuario,
        @Size(min = 6, max = 12, message = "A senha deve conter de 6 a 12 caracteres.")
        String senha
) {}
