package repuestos.repuestoscloud.repository;

import repuestos.repuestoscloud.entity.CarritoItem;
import repuestos.repuestoscloud.entity.CarritoItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, CarritoItemId> {

    List<CarritoItem> findByIdCarrito(Long idCarrito);

    Optional<CarritoItem> findByIdCarritoAndIdProducto(Long idCarrito, Long idProducto);
}
