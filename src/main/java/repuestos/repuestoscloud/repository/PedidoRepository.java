package repuestos.repuestoscloud.repository;

import repuestos.repuestoscloud.entity.Pedido;
import repuestos.repuestoscloud.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioIdUsuarioOrderByFechaDesc(Long idUsuario);

    List<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario);
}