package repuestos.repuestoscloud.controller;

import repuestos.repuestoscloud.entity.Usuario;
import repuestos.repuestoscloud.service.PedidoService;
import repuestos.repuestoscloud.util.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PagoController {

    private final PedidoService pedidoService;
    private final CurrentUser currentUser;

    public PagoController(PedidoService pedidoService, CurrentUser currentUser) {
        this.pedidoService = pedidoService;
        this.currentUser = currentUser;
    }

    public static class PagoForm {

        private String numeroTarjeta;
        private String codigoSeguridad;
        private String fechaVencimiento;
        private String nombrePropietario;

        public String getNumeroTarjeta() {
            return numeroTarjeta;
        }

        public void setNumeroTarjeta(String numeroTarjeta) {
            this.numeroTarjeta = numeroTarjeta;
        }

        public String getCodigoSeguridad() {
            return codigoSeguridad;
        }

        public void setCodigoSeguridad(String codigoSeguridad) {
            this.codigoSeguridad = codigoSeguridad;
        }

        public String getFechaVencimiento() {
            return fechaVencimiento;
        }

        public void setFechaVencimiento(String fechaVencimiento) {
            this.fechaVencimiento = fechaVencimiento;
        }

        public String getNombrePropietario() {
            return nombrePropietario;
        }

        public void setNombrePropietario(String nombrePropietario) {
            this.nombrePropietario = nombrePropietario;
        }
    }

    @GetMapping("/pago")
    public String mostrarPago(Model model) {
        model.addAttribute("form", new PagoForm());
        return "pago";
    }

    @PostMapping("/pago/finalizar")
    public String finalizarPago(@ModelAttribute("form") PagoForm form,
                                @RequestParam("metodoPago") String metodoPago,
                                Model model) {

        if ("tarjeta".equals(metodoPago)) {
            if (form.getNumeroTarjeta() == null || !form.getNumeroTarjeta().matches("\\d{16}")
                    || form.getCodigoSeguridad() == null || form.getCodigoSeguridad().isBlank()
                    || form.getFechaVencimiento() == null || form.getFechaVencimiento().isBlank()
                    || form.getNombrePropietario() == null || form.getNombrePropietario().isBlank()) {

                model.addAttribute("error", "Completa correctamente los datos de la tarjeta.");
                model.addAttribute("form", form);
                return "pago";
            }
        }

        Usuario u = currentUser.get();
        pedidoService.confirmarCompra(u);

        return "redirect:/pedidos";
    }
}