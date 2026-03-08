package repuestos.repuestoscloud.entity;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioRolId implements Serializable {
  private Long idUsuario;
  private Long idRol;
}