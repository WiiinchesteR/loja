package meu.projeto.loja.domain.usuario;

import lombok.RequiredArgsConstructor;
import meu.projeto.loja.infra.exceptions.IdNaoEncontradoExceptions;
import meu.projeto.loja.infra.exceptions.NomeUsuarioExistenteExceptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> listarUsuarios(Pageable paginacao) {
        return usuarioRepository.findAll(paginacao).map(UsuarioDTO::new);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        usuarioRepository.findById(id).orElseThrow(
                () -> new IdNaoEncontradoExceptions("Id não encontrado.")
        );
        usuarioRepository.deleteById(id);
    }
}
