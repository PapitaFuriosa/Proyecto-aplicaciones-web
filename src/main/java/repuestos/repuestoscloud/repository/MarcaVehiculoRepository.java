package repuestos.repuestoscloud.repository;

import repuestos.repuestoscloud.entity.MarcaVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarcaVehiculoRepository extends JpaRepository<MarcaVehiculo, Long> {
    List<MarcaVehiculo> findAllByOrderByNombreAsc();
    Optional<MarcaVehiculo> findByNombre(String nombre);
}