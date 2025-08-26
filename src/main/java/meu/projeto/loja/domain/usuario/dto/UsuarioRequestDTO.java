package meu.projeto.loja.domain.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "Nome é obrigatório.")
        String nome,
        @NotBlank(message = "Nome de usuário é obrigatório.")
        @Size(min = 6, message = "Nome de usuário deve ter pelo menos 6 caracteres.")
        String nomeUsuario,
        @NotBlank(message = "Senha é obrigatória.")
        @Size(min = 6, max = 12, message = "A senha deve conter de 6 a 12 caracteres.")
        String senha
) {}

