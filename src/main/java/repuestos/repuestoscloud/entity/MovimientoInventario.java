package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="movimiento_inventario")
@Getter @Setter @NoArgsConstructor
public class MovimientoInventario {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id_movimiento")
  private Long idMovimiento;

  @ManyToOne @JoinColumn(name="id_producto", nullable=false)
  private Producto producto;

  @ManyToOne @JoinColumn(name="id_usuario_admin", nullable=false)
  private Usuario admin;

  @Enumerated(EnumType.STRING)
  private Tipo tipo;

  @Column(nullable=false)
  private Integer cantidad;

  private String motivo;

  private Instant fecha;

  public enum Tipo { ENTRADA, SALIDA, AJUSTE }
}