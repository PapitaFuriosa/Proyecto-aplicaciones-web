package repuestos.repuestoscloud.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CarritoItemId implements Serializable {
  private Long idCarrito;
  private Long idProducto;
}
