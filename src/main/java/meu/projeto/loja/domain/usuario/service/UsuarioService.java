package meu.projeto.loja.domain.usuario.service;

import lombok.RequiredArgsConstructor;
import meu.projeto.loja.domain.usuario.dto.UsuarioRequestDTO;
import meu.projeto.loja.domain.usuario.dto.UsuarioResponseDTO;
import meu.projeto.loja.domain.usuario.dto.UsuarioUpdateDTO;
import meu.projeto.loja.domain.usuario.entity.UsuarioEntity;
import meu.projeto.loja.domain.usuario.repository.UsuarioRepository;
import meu.projeto.loja.infra.exceptions.IdNaoEncontradoExceptions;
import meu.projeto.loja.infra.exceptions.NomeUsuarioComEspacosException;
import meu.projeto.loja.infra.exceptions.NomeUsuarioExistenteExceptions;
import meu.projeto.loja.infra.exceptions.SenhaComEspacosException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO dto) {
        String nomeUsuarioNormalizado = dto.nomeUsuario().replaceAll("\\s+", "").toLowerCase();
        String senhaNormalizada = dto.senha().replaceAll("\\s+", "");
        if (dto.nomeUsuario().contains(" ")) {
            throw new NomeUsuarioComEspacosException("Nome de usuário não pode conter espaços.");
        }
        if (dto.senha().contains(" ")) {
            throw new SenhaComEspacosException("Senha não pode conter espaços.");
        }
        if (usuarioRepository.existsByNomeUsuario(nomeUsuarioNormalizado)) {
            throw new NomeUsuarioExistenteExceptions("Nome de usuário já existe.");
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(dto.nome().trim().replaceAll("\\s+", " "));
        usuario.setNomeUsuario(nomeUsuarioNormalizado);
        usuario.setSenha(passwordEncoder.encode(senhaNormalizada));
        var usuarioSalvo = usuarioRepository.save(usuario);
        return toResponse(usuarioSalvo);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listarUsuarios(Pageable paginacao) {
        return usuarioRepository.findAll(paginacao).map(this::toResponse);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        UsuarioEntity usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new IdNaoEncontradoExceptions("Id não encontrado.")
        );
        if (dto.nome() != null) {
            String nomeNormalizado = dto.nome().trim().replaceAll("\\s+", " ");
            if (nomeNormalizado.isEmpty()) {
                throw new IllegalArgumentException("Nome não pode ser vazio.");
            }
            usuario.setNome(nomeNormalizado);
        }
        if (dto.nomeUsuario() != null) {
            if (dto.nomeUsuario().contains(" ")) {
                throw new NomeUsuarioComEspacosException("Nome de usuário não pode conter espaços.");
            }
            String novoUser = dto.nomeUsuario().replaceAll("\\s+", "").toLowerCase();
            if (novoUser.isEmpty()) {
                throw new IllegalArgumentException("Nome de usuário não pode ser vazio.");
            }
            if (novoUser.length() < 6) {
                throw new IllegalArgumentException("Nome de usuário deve ter pelo menos 6 caracteres.");
            }
            if (!novoUser.equals(usuario.getNomeUsuario()) && usuarioRepository.existsByNomeUsuario(novoUser)) {
                throw new NomeUsuarioExistenteExceptions("Nome de usuário já existe.");
            }
            usuario.setNomeUsuario(novoUser.trim());
        }

        if (dto.senha() != null) {
            if (dto.senha().contains(" ")) {
                throw new SenhaComEspacosException("Senha não pode conter espaços.");
            }
            String senhaNormalizada = dto.senha().replaceAll("\\s+", "").toLowerCase();
            if (senhaNormalizada.isEmpty()) {
                throw new IllegalArgumentException("Senha não pode ser vazia.");
            }
            if (senhaNormalizada.length() < 6 || senhaNormalizada.length() > 12) {
                throw new IllegalArgumentException("A senha deve conter de 6 a 12 caracteres.");
            }
            usuario.setSenha(passwordEncoder.encode(senhaNormalizada).trim());
        }
        UsuarioEntity usuarioSalvo = usuarioRepository.save(usuario);
        return toResponse(usuarioSalvo);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        usuarioRepository.findById(id).orElseThrow(
                () -> new IdNaoEncontradoExceptions("Id não encontrado.")
        );
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO toResponse(UsuarioEntity e) {
        return new UsuarioResponseDTO(e.getId(), e.getNome(), e.getNomeUsuario());
    }
}
