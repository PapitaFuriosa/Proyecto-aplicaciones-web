package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="rol")
@Getter @Setter @NoArgsConstructor
public class Rol {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id_rol")
  private Long idRol;

  @Column(name="nombre", nullable=false, unique=true)
  private String nombre;
}