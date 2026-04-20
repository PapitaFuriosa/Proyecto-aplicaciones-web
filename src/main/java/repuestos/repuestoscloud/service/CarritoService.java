package repuestos.repuestoscloud.service;

import repuestos.repuestoscloud.repository.CarritoItemRepository;
import repuestos.repuestoscloud.repository.CarritoRepository;
import repuestos.repuestoscloud.repository.ProductoRepository;
import repuestos.repuestoscloud.entity.CarritoItem;
import repuestos.repuestoscloud.entity.Producto;
import repuestos.repuestoscloud.entity.CarritoItemId;
import repuestos.repuestoscloud.entity.Usuario;
import repuestos.repuestoscloud.entity.Carrito;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepo;
    private final CarritoItemRepository itemRepo;
    private final ProductoRepository productoRepo;

    public CarritoService(CarritoRepository c, CarritoItemRepository i, ProductoRepository p) {
        this.carritoRepo = c;
        this.itemRepo = i;
        this.productoRepo = p;
    }

    @Transactional
    public Carrito obtenerOCrearCarritoActivo(Usuario u) {
        return carritoRepo.findByUsuarioIdUsuarioAndEstado(u.getIdUsuario(),
                Carrito.EstadoCarrito.ACTIVO
        ).orElseGet(() -> {
            Carrito c = new Carrito();
            c.setUsuario(u);
            c.setEstado(Carrito.EstadoCarrito.ACTIVO);
            return carritoRepo.save(c);
        });
    }

    @Transactional
    public void agregar(Usuario u, Long idProducto) {
        Carrito c = obtenerOCrearCarritoActivo(u);
        Producto p = productoRepo.findById(idProducto).orElseThrow();

        if (!Boolean.TRUE.equals(p.getActivo())) {
            return;
        }
        if (p.getStock() <= 0) {
            return;
        }

        Optional<CarritoItem> existing = itemRepo.findByIdCarritoAndIdProducto(c.getIdCarrito(), idProducto);
        if (existing.isPresent()) {
            CarritoItem it = existing.get();

            if (it.getCantidad() < p.getStock()) {
                it.setCantidad(it.getCantidad() + 1);
                itemRepo.save(it);
            }
        } else {
            CarritoItem it = new CarritoItem();
            it.setIdCarrito(c.getIdCarrito());
            it.setIdProducto(idProducto);
            it.setCantidad(1);
            itemRepo.save(it);
        }
    }

    @Transactional
    public void actualizarCantidad(Usuario u, Long idProducto, int cantidad) {
        Carrito c = obtenerOCrearCarritoActivo(u);

        if (cantidad <= 0) {
            itemRepo.deleteById(new CarritoItemId(c.getIdCarrito(), idProducto));
            return;
        }

        Producto p = productoRepo.findById(idProducto).orElseThrow();

        if (cantidad > p.getStock()) {
            cantidad = p.getStock();
        }

        CarritoItem it = itemRepo.findByIdCarritoAndIdProducto(c.getIdCarrito(), idProducto).orElseThrow();
        it.setCantidad(cantidad);
        itemRepo.save(it);
    }

    @Transactional
    public void eliminar(Usuario u, Long idProducto) {
        Carrito c = obtenerOCrearCarritoActivo(u);
        itemRepo.deleteById(new CarritoItemId(c.getIdCarrito(), idProducto));
    }

    public record ItemVista(
            Long idProducto,
            String nombre,
            BigDecimal precio,
            Integer cantidad,
            BigDecimal subtotal,
            String img,
            Integer stock
    ) {
    }

    public Map<String, Object> verCarrito(Usuario u) {
        Carrito c = carritoRepo.findByUsuarioIdUsuarioAndEstado(
                u.getIdUsuario().longValue(),
                Carrito.EstadoCarrito.ACTIVO
        ).orElse(null);

        if (c == null) {
            return Map.of("items", List.of(), "total", BigDecimal.ZERO);
        }

        List<CarritoItem> items = itemRepo.findByIdCarrito(c.getIdCarrito());
        List<ItemVista> vista = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem it : items) {
            Producto p = productoRepo.findById(it.getIdProducto()).orElse(null);
            if (p == null) {
                continue;
            }

            BigDecimal sub = p.getPrecio().multiply(BigDecimal.valueOf(it.getCantidad()));
            total = total.add(sub);

            vista.add(new ItemVista(
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getPrecio(),
                    it.getCantidad(),
                    sub,
                    p.getRutaImagen(),
                    p.getStock()
            ));
        }

        return Map.of("items", vista, "total", total);
    }
}