package repuestos.repuestoscloud.repository;

import repuestos.repuestoscloud.entity.PedidoItem;
import repuestos.repuestoscloud.entity.PedidoItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, PedidoItemId> {

    List<PedidoItem> findByIdPedido(Long idPedido);

    Optional<PedidoItem> findByIdPedidoAndIdProducto(Long idPedido, Long idProducto);

}