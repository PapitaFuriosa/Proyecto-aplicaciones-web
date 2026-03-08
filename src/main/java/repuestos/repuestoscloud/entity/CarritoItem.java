package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="carrito_item")
@Getter @Setter @NoArgsConstructor
@IdClass(CarritoItemId.class)
public class CarritoItem {
  @Id @Column(name="id_carrito")
  private Long idCarrito;

  @Id @Column(name="id_producto")
  private Long idProducto;

  @Column(nullable=false)
  private Integer cantidad;
}
