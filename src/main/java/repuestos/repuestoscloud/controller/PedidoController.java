package repuestos.repuestoscloud.controller;

import repuestos.repuestoscloud.entity.Usuario;
import repuestos.repuestoscloud.service.PedidoService;
import repuestos.repuestoscloud.util.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PedidoController {

    private final PedidoService pedidoService;
    private final CurrentUser currentUser;

    public PedidoController(PedidoService pedidoService, CurrentUser currentUser) {
        this.pedidoService = pedidoService;
        this.currentUser = currentUser;
    }

    @PostMapping("/pedido/confirmar")
    public String confirmarCompra() {
        Usuario u = currentUser.get();
        pedidoService.confirmarCompra(u);
        return "redirect:/pedidos";
    }

    @GetMapping("/pedidos")
    public String verPedidos(Model model) {
        Usuario u = currentUser.get();

        model.addAttribute("pedidos",
                pedidoService.obtenerPedidosPorUsuario(u.getIdUsuario()));

        return "pedidos";
    }
}