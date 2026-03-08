package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="pedido")
@Getter @Setter @NoArgsConstructor
public class Pedido {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id_pedido")
  private Long idPedido;

  @ManyToOne
  @JoinColumn(name="id_usuario", nullable=false)
  private Usuario usuario;

  @Column(name="fecha")
  private Instant fecha;

  @Enumerated(EnumType.STRING)
  private EstadoPedido estado = EstadoPedido.PENDIENTE;

  @Column(nullable=false, precision=12, scale=2)
  private BigDecimal total = BigDecimal.ZERO;

  public enum EstadoPedido { PENDIENTE, CONFIRMADO, CANCELADO }
}