package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="producto")
@Getter @Setter @NoArgsConstructor
public class Producto {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id_producto")
  private Long idProducto;

  @ManyToOne
  @JoinColumn(name="id_marca", nullable=false)
  private MarcaVehiculo marca;

  @ManyToOne
  @JoinColumn(name="id_tipo", nullable=false)
  private TipoRepuesto tipo;

  @Column(nullable=false)
  private String nombre;

  @Column(columnDefinition="TEXT")
  private String descripcion;

  @Column(columnDefinition="TEXT")
  private String compatibilidad;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal precio;

  @Column(nullable=false)
  private Integer stock = 0;

  @Column(name="ruta_imagen")
  private String rutaImagen;

  private Boolean activo = true;

  @Column(name="fecha_creacion")
  private Instant fechaCreacion;

  @Column(name="fecha_modificacion")
  private Instant fechaModificacion;
}