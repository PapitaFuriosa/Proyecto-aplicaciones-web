package repuestos.repuestoscloud.util;

import repuestos.repuestoscloud.entity.Usuario;
import repuestos.repuestoscloud.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    private final UsuarioRepository usuarioRepo;

    public CurrentUser(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public Usuario get() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByCorreo(correo).orElseThrow();
    }
}