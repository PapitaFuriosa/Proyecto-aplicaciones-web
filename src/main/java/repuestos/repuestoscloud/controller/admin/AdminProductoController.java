package repuestos.repuestoscloud.controller.admin;
import repuestos.repuestoscloud.repository.MarcaVehiculoRepository;
import repuestos.repuestoscloud.repository.ProductoRepository;
import repuestos.repuestoscloud.repository.TipoRepuestoRepository;
import repuestos.repuestoscloud.entity.Producto;
import repuestos.repuestoscloud.service.FirebaseStorageService;
import repuestos.repuestoscloud.util.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {

  private final ProductoRepository productoRepo;
  private final MarcaVehiculoRepository marcaRepo;
  private final TipoRepuestoRepository tipoRepo;
  private final FirebaseStorageService storage;

  public AdminProductoController(ProductoRepository pr, MarcaVehiculoRepository mr, TipoRepuestoRepository tr, FirebaseStorageService st) {
    this.productoRepo = pr; this.marcaRepo = mr; this.tipoRepo = tr; this.storage = st;
  }

  @GetMapping
  public String listado(Model model) {
    model.addAttribute("lista", productoRepo.findAll());
    return "admin/productos";
  }

  @GetMapping("/nuevo")
  public String nuevo(Model model) {
    model.addAttribute("p", new Producto());
    model.addAttribute("marcas", marcaRepo.findAllByOrderByNombreAsc());
    model.addAttribute("tipos", tipoRepo.findAllByOrderByNombreAsc());
    return "admin/ProductoForm";
  }

  @GetMapping("/editar/{id}")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("p", productoRepo.findById(id).orElseThrow());
    model.addAttribute("marcas", marcaRepo.findAllByOrderByNombreAsc());
    model.addAttribute("tipos", tipoRepo.findAllByOrderByNombreAsc());
    return "admin/ProductoForm";
  }

  @PostMapping("/guardar")
  public String guardar(@RequestParam(required=false) Long idProducto,
                        @RequestParam Long idMarca,
                        @RequestParam Long idTipo,
                        @RequestParam String nombre,
                        @RequestParam(required=false) String descripcion,
                        @RequestParam(required=false) String compatibilidad,
                        @RequestParam BigDecimal precio,
                        @RequestParam Integer stock,
                        @RequestParam(defaultValue = "true") Boolean activo,
                        @RequestParam(required=false) MultipartFile imagen) {

    Producto p = (idProducto == null) ? new Producto() : productoRepo.findById(idProducto).orElseThrow();
    p.setMarca(marcaRepo.findById(idMarca).orElseThrow());
    p.setTipo(tipoRepo.findById(idTipo).orElseThrow());
    p.setNombre(nombre);
    p.setDescripcion(descripcion);
    p.setCompatibilidad(compatibilidad);
    p.setPrecio(precio);
    p.setStock(stock);
    p.setActivo(activo);

    if (imagen != null && !imagen.isEmpty()) {
      String url = storage.upload(imagen);
      p.setRutaImagen(url);
    }

    productoRepo.save(p);
    return "redirect:/admin/productos";
  }

  @PostMapping("/desactivar")
  public String desactivar(@RequestParam Long idProducto) {
    Producto p = productoRepo.findById(idProducto).orElseThrow();
    p.setActivo(false);
    productoRepo.save(p);
    return "redirect:/admin/productos";
  }
}