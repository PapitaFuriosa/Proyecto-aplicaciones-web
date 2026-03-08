package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="carrito")
@Getter @Setter @NoArgsConstructor
public class Carrito {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id_carrito")
  private Long idCarrito;

  @ManyToOne
  @JoinColumn(name="id_usuario", nullable=false)
  private Usuario usuario;

  @Enumerated(EnumType.STRING)
  private EstadoCarrito estado = EstadoCarrito.ACTIVO;

  @Column(name="fecha_creacion")
  private Instant fechaCreacion;

  @Column(name="fecha_modificacion")
  private Instant fechaModificacion;

  public enum EstadoCarrito { ACTIVO, CERRADO }
}
