package meu.projeto.loja.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(
        Long id,
        @NotBlank
        String nome,
        @NotBlank
        String nomeUsuario,
        @NotBlank
        @Size(min = 6, max = 12, message = "A senha deve conter de 6 a 12 caracteres.")
        String senha
) {
    public UsuarioDTO(UsuarioEntity entity) {
        this(entity.getId(),
                entity.getNome(),
                entity.getNomeUsuario(),
                entity.getSenha());
    }
}
