package repuestos.repuestoscloud.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import repuestos.repuestoscloud.entity.Pedido;
import repuestos.repuestoscloud.entity.Usuario;
import repuestos.repuestoscloud.repository.UsuarioRepository;
import repuestos.repuestoscloud.repository.PedidoItemRepository;
import repuestos.repuestoscloud.service.PedidoService;

@Controller
public class HistorialPedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;
    private final PedidoItemRepository pedidoItemRepository;

    public HistorialPedidoController(PedidoService pedidoService,
            UsuarioRepository usuarioRepository,
            PedidoItemRepository pedidoItemRepository) {
        this.pedidoService = pedidoService;
        this.usuarioRepository = usuarioRepository;
        this.pedidoItemRepository = pedidoItemRepository;
    }

    @GetMapping("/mis-pedidos")
    public String verHistorial(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        String login = principal.getName();

        Usuario usuario = usuarioRepository.findByCorreo(login).orElse(null);

        if (usuario == null) {
            return "redirect:/login";
        }

        List<Pedido> pedidos = pedidoService.listarPorUsuario(usuario);
        model.addAttribute("pedidos", pedidos);

        return "pedido/historial";
    }

    @GetMapping("/mis-pedidos/{id}")
    public String verDetalle(@PathVariable Long id, Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        String login = principal.getName();

        Usuario usuario = usuarioRepository.findByCorreo(login).orElse(null);

        if (usuario == null) {
            return "redirect:/login";
        }

        Pedido pedido = pedidoService.obtenerPorId(id);

        if (pedido == null) {
            return "redirect:/mis-pedidos";
        }

        if (pedido.getUsuario() == null
                || !pedido.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return "redirect:/mis-pedidos";
        }

        model.addAttribute("pedido", pedido);
        model.addAttribute("items", pedidoItemRepository.findByIdPedido(pedido.getIdPedido()));

        return "pedido/detallePedido";
    }
}
