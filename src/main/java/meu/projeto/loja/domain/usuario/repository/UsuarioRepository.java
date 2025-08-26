package meu.projeto.loja.domain.usuario.repository;

import meu.projeto.loja.domain.usuario.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByNomeUsuario(String nomeUsuario);
}
