package meu.projeto.loja.domain.usuario;

import lombok.RequiredArgsConstructor;
import meu.projeto.loja.infra.exceptions.NomeUsuarioExistenteExceptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioDTO cadastrarUsuario(UsuarioDTO dto) {
        if (usuarioRepository.existsByNomeUsuario(dto.nomeUsuario())) {
            throw  new NomeUsuarioExistenteExceptions("Nome de usuário já existe.");
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(dto.nome());
        usuario.setNomeUsuario(dto.nomeUsuario());
        usuario.setSenha(dto.senha());
        return new UsuarioDTO(usuarioRepository.save(usuario));
    }
}
