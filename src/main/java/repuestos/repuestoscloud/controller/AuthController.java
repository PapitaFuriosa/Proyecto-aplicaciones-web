package repuestos.repuestoscloud.controller;

import java.util.Optional;
import repuestos.repuestoscloud.repository.RolRepository;
import repuestos.repuestoscloud.repository.UsuarioRolRepository;
import repuestos.repuestoscloud.repository.UsuarioRepository;
import repuestos.repuestoscloud.entity.Rol;
import repuestos.repuestoscloud.entity.UsuarioRol;
import repuestos.repuestoscloud.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final UsuarioRolRepository usuarioRolRepo;
    private final PasswordEncoder encoder;

    public AuthController(UsuarioRepository u, RolRepository r, UsuarioRolRepository ur, PasswordEncoder e) {
        this.usuarioRepo = u;
        this.rolRepo = r;
        this.usuarioRolRepo = ur;
        this.encoder = e;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "registroExitoso", required = false) String registroExitoso,
            Model model) {
        if (registroExitoso != null) {
            model.addAttribute("mensaje", "Cuenta creada correctamente. Ya puedes iniciar sesión.");
        }
        return "login";
    }

    public static class RegistroForm {

        @Email
        @NotBlank
        private String correo;

        @NotBlank
        private String password;

        @NotBlank
        private String confirmarPassword;

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmarPassword() {
            return confirmarPassword;
        }

        public void setConfirmarPassword(String confirmarPassword) {
            this.confirmarPassword = confirmarPassword;
        }
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("form", new RegistroForm());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute("form") RegistroForm form, Model model) {

        if (form.getCorreo() != null && !form.getCorreo().isBlank()) {
            Optional<Usuario> usuarioCorreo = usuarioRepo.findByCorreo(form.getCorreo());
            if (usuarioCorreo.isPresent()) {
                model.addAttribute("error", "Ese correo ya está registrado");
                model.addAttribute("form", form);
                return "registro";
            }
        }

        if (!form.getPassword().equals(form.getConfirmarPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("form", form);
            return "registro";
        }

        Usuario u = new Usuario();
        u.setCorreo(form.getCorreo());
        u.setPasswordHash(encoder.encode(form.getPassword()));
        u.setActivo(true);

        u = usuarioRepo.save(u);

        Rol cliente = rolRepo.findByNombre("ROLE_CLIENTE").orElseThrow();

        UsuarioRol ur = new UsuarioRol();
        ur.setIdUsuario(u.getIdUsuario());
        ur.setIdRol(cliente.getIdRol());
        usuarioRolRepo.save(ur);

        return "redirect:/login?registroExitoso";
    }
}