package repuestos.repuestoscloud.repository;

import repuestos.repuestoscloud.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByMarcaNombre(String nombre);

    List<Producto> findByTipoNombre(String nombre);

    @Query("""
        SELECT p FROM Producto p
        WHERE (:q IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')))
          AND (:marca IS NULL OR LOWER(p.marca.nombre) LIKE LOWER(CONCAT('%', :marca, '%')))
          AND (:tipo IS NULL OR LOWER(p.tipo.nombre) LIKE LOWER(CONCAT('%', :tipo, '%')))
          AND p.activo = true
    """)
    List<Producto> buscarFiltrar(String q, String marca, String tipo);

}