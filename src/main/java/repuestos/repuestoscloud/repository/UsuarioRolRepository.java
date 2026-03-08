package repuestos.repuestoscloud.repository;

import repuestos.repuestoscloud.entity.UsuarioRol;
import repuestos.repuestoscloud.entity.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
    List<UsuarioRol> findByIdUsuario(Long idUsuario);
}