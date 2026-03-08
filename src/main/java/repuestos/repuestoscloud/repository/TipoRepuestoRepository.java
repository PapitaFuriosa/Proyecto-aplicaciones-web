package repuestos.repuestoscloud.repository;

import repuestos.repuestoscloud.entity.TipoRepuesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipoRepuestoRepository extends JpaRepository<TipoRepuesto, Long> {
    List<TipoRepuesto> findAllByOrderByNombreAsc();
    Optional<TipoRepuesto> findByNombre(String nombre);
}