package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="tipo_repuesto")
@Getter @Setter @NoArgsConstructor
public class TipoRepuesto {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id_tipo")
  private Long idTipo;

  @Column(nullable=false, unique=true)
  private String nombre;
}