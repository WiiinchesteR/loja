package meu.projeto.loja.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
        Long id,
        @NotBlank
        String nome,
        @NotBlank
        String nomeUsuario,
        @NotBlank
        String senha
) {
    public UsuarioDTO(UsuarioEntity entity) {
        this(entity.getId(),
                entity.getNome(),
                entity.getNomeUsuario(),
                entity.getSenha());
    }
}
