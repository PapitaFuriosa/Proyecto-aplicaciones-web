package repuestos.repuestoscloud.controller;

import java.util.Optional;
import repuestos.repuestoscloud.entity.Usuario;
import repuestos.repuestoscloud.repository.UsuarioRepository;
import repuestos.repuestoscloud.util.CurrentUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CuentaController {

    private final CurrentUser currentUser;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public CuentaController(CurrentUser currentUser,
                            UsuarioRepository usuarioRepo,
                            PasswordEncoder passwordEncoder) {
        this.currentUser = currentUser;
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public static class CuentaForm {
        private String nombreUsuario;
        private String correo;
        private String nuevaPassword;
        private String confirmarPassword;
        private String metodoPagoPreferido;

        public String getNombreUsuario() {
            return nombreUsuario;
        }

        public void setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getNuevaPassword() {
            return nuevaPassword;
        }

        public void setNuevaPassword(String nuevaPassword) {
            this.nuevaPassword = nuevaPassword;
        }

        public String getConfirmarPassword() {
            return confirmarPassword;
        }

        public void setConfirmarPassword(String confirmarPassword) {
            this.confirmarPassword = confirmarPassword;
        }

        public String getMetodoPagoPreferido() {
            return metodoPagoPreferido;
        }

        public void setMetodoPagoPreferido(String metodoPagoPreferido) {
            this.metodoPagoPreferido = metodoPagoPreferido;
        }
    }

    @GetMapping("/cuenta")
    public String verCuenta(Model model,
                            @RequestParam(value = "ok", required = false) String ok) {

        Usuario u = currentUser.get();

        CuentaForm form = new CuentaForm();
        form.setNombreUsuario(u.getNombreUsuario());
        form.setCorreo(u.getCorreo());
        form.setMetodoPagoPreferido(u.getMetodoPagoPreferido());

        model.addAttribute("form", form);

        if (ok != null) {
            model.addAttribute("mensaje", "Datos actualizados correctamente.");
        }

        return "cuenta";
    }

    @PostMapping("/cuenta/actualizar")
    public String actualizarCuenta(@ModelAttribute("form") CuentaForm form, Model model) {

        Usuario u = currentUser.get();

        Optional<Usuario> existente = usuarioRepo.findByCorreo(form.getCorreo());
        if (existente.isPresent() && !existente.get().getIdUsuario().equals(u.getIdUsuario())) {
            model.addAttribute("error", "Ese correo ya está en uso.");
            return "cuenta";
        }

        if (form.getNuevaPassword() != null && !form.getNuevaPassword().isBlank()) {
            if (!form.getNuevaPassword().equals(form.getConfirmarPassword())) {
                model.addAttribute("error", "Las contraseñas no coinciden.");
                return "cuenta";
            }
            u.setPasswordHash(passwordEncoder.encode(form.getNuevaPassword()));
        }

        u.setNombreUsuario(form.getNombreUsuario());
        u.setCorreo(form.getCorreo());
        u.setMetodoPagoPreferido(form.getMetodoPagoPreferido());

        usuarioRepo.save(u);

        return "redirect:/cuenta?ok";
    }
}