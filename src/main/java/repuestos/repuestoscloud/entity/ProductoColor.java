package repuestos.repuestoscloud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="producto_color")
@Getter
@Setter
@NoArgsConstructor
public class ProductoColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColor;

    @ManyToOne
    @JoinColumn(name="id_producto")
    private Producto producto;

    private String nombre;

    @Column(name="codigo_hex")
    private String codigoHex;

    @Column(name="imagen_url")
    private String imagenUrl;

    private Boolean activo;
}