package repuestos.repuestoscloud.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import repuestos.repuestoscloud.entity.ProductoColor;

public interface ProductoColorRepository extends JpaRepository<ProductoColor, Long> {

    List<ProductoColor> findByProductoIdProductoAndActivoTrue(Long idProducto);

}