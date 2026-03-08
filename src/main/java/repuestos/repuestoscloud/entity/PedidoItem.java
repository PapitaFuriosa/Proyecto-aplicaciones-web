package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name="pedido_item")
@Getter @Setter @NoArgsConstructor
@IdClass(PedidoItemId.class)
public class PedidoItem {
  @Id @Column(name="id_pedido")
  private Long idPedido;

  @Id @Column(name="id_producto")
  private Long idProducto;

  @Column(name="precio_unitario", nullable=false, precision=12, scale=2)
  private BigDecimal precioUnitario;

  @Column(nullable=false)
  private Integer cantidad;
}