package repuestos.repuestoscloud.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PedidoItemId implements Serializable {
  private Long idPedido;
  private Long idProducto;
}