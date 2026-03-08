package repuestos.repuestoscloud.repository;

import repuestos.repuestoscloud.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuarioIdUsuarioAndEstado(Long idUsuario, Carrito.EstadoCarrito estado);

}