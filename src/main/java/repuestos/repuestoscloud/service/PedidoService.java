package repuestos.repuestoscloud.service;

import repuestos.repuestoscloud.repository.CarritoItemRepository;
import repuestos.repuestoscloud.repository.CarritoRepository;
import repuestos.repuestoscloud.repository.ProductoRepository;
import repuestos.repuestoscloud.repository.PedidoRepository;
import repuestos.repuestoscloud.repository.PedidoItemRepository;
import repuestos.repuestoscloud.entity.CarritoItem;
import repuestos.repuestoscloud.entity.Producto;
import repuestos.repuestoscloud.entity.Pedido;
import repuestos.repuestoscloud.entity.PedidoItem;
import repuestos.repuestoscloud.entity.Carrito;
import repuestos.repuestoscloud.entity.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final PedidoItemRepository pedidoItemRepo;
    private final CarritoRepository carritoRepo;
    private final CarritoItemRepository carritoItemRepo;
    private final ProductoRepository productoRepo;

    public PedidoService(PedidoRepository pedidoRepo,
            PedidoItemRepository pedidoItemRepo,
            CarritoRepository carritoRepo,
            CarritoItemRepository carritoItemRepo,
            ProductoRepository productoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.pedidoItemRepo = pedidoItemRepo;
        this.carritoRepo = carritoRepo;
        this.carritoItemRepo = carritoItemRepo;
        this.productoRepo = productoRepo;
    }

    public List<Pedido> listarPorUsuario(Usuario usuario) {
        return pedidoRepo.findByUsuarioOrderByFechaDesc(usuario);
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepo.findById(id).orElse(null);
    }

    public List<Pedido> obtenerPedidosPorUsuario(Long idUsuario) {
        return pedidoRepo.findByUsuarioIdUsuarioOrderByFechaDesc(idUsuario);
    }

    @Transactional
    public void confirmarCompra(Usuario u) {

        Carrito carrito = carritoRepo
                .findByUsuarioIdUsuarioAndEstado(u.getIdUsuario(), Carrito.EstadoCarrito.ACTIVO)
                .orElseThrow();

        List<CarritoItem> items = carritoItemRepo.findByIdCarrito(carrito.getIdCarrito());

        if (items.isEmpty()) {
            return;
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(u);
        pedido.setFecha(Instant.now());
        pedido.setEstado(Pedido.EstadoPedido.CONFIRMADO);
        pedido.setTotal(BigDecimal.ZERO);
        pedido = pedidoRepo.save(pedido);

        BigDecimal total = BigDecimal.ZERO;

        for (CarritoItem it : items) {
            Producto p = productoRepo.findById(it.getIdProducto()).orElseThrow();

            int cant = it.getCantidad();

            if (p.getStock() < cant) {
                cant = p.getStock();
            }

            if (cant <= 0) {
                continue;
            }

            p.setStock(p.getStock() - cant);
            productoRepo.save(p);

            PedidoItem pi = new PedidoItem();
            pi.setIdPedido(pedido.getIdPedido());
            pi.setIdProducto(p.getIdProducto());
            pi.setPrecioUnitario(p.getPrecio());
            pi.setCantidad(cant);
            pedidoItemRepo.save(pi);

            total = total.add(p.getPrecio().multiply(BigDecimal.valueOf(cant)));
        }

        pedido.setTotal(total);
        pedidoRepo.save(pedido);

        carrito.setEstado(Carrito.EstadoCarrito.CERRADO);
        carritoRepo.save(carrito);
    }
}
