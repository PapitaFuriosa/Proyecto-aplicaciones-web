package repuestos.repuestoscloud.controller;
import repuestos.repuestoscloud.service.CarritoService;
import repuestos.repuestoscloud.util.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarritoController {

  private final CarritoService carritoService;
  private final CurrentUser currentUser;

  public CarritoController(CarritoService cs, CurrentUser cu) {
    this.carritoService = cs; this.currentUser = cu;
  }

  @GetMapping("/carrito")
  public String ver(Model model) {
    var data = carritoService.verCarrito(currentUser.get());
    model.addAttribute("items", data.get("items"));
    model.addAttribute("total", data.get("total"));
    return "carrito";
  }

  @PostMapping("/carrito/agregar")
  public String agregar(@RequestParam Long idProducto) {
    carritoService.agregar(currentUser.get(), idProducto);
    return "redirect:/carrito";
  }

  @PostMapping("/carrito/actualizar")
  public String actualizar(@RequestParam Long idProducto, @RequestParam Integer cantidad) {
    carritoService.actualizarCantidad(currentUser.get(), idProducto, cantidad);
    return "redirect:/carrito";
  }

  @PostMapping("/carrito/eliminar")
  public String eliminar(@RequestParam Long idProducto) {
    carritoService.eliminar(currentUser.get(), idProducto);
    return "redirect:/carrito";
  }
}
