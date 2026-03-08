package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="marca_vehiculo")
@Getter @Setter @NoArgsConstructor
public class MarcaVehiculo {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id_marca")
  private Long idMarca;

  @Column(nullable=false, unique=true)
  private String nombre;
}