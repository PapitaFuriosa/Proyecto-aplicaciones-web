package repuestos.repuestoscloud.service;

import repuestos.repuestoscloud.repository.RolRepository;
import repuestos.repuestoscloud.repository.UsuarioRolRepository;
import repuestos.repuestoscloud.repository.UsuarioRepository;
import repuestos.repuestoscloud.entity.Rol;
import repuestos.repuestoscloud.entity.UsuarioRol;
import repuestos.repuestoscloud.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;
    private final UsuarioRolRepository usuarioRolRepo;
    private final RolRepository rolRepo;

    public AuthService(UsuarioRepository u, UsuarioRolRepository ur, RolRepository r) {
        this.usuarioRepo = u;
        this.usuarioRolRepo = ur;
        this.rolRepo = r;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Usuario u = usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Correo no existe"));

        List<UsuarioRol> rolesLink = usuarioRolRepo.findByIdUsuario(u.getIdUsuario());

        List<GrantedAuthority> auths = new ArrayList<>();
        for (UsuarioRol link : rolesLink) {
            Rol rol = rolRepo.findById(link.getIdRol()).orElse(null);
            if (rol != null) {
                auths.add(new SimpleGrantedAuthority(rol.getNombre()));
            }
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getCorreo())
                .password(u.getPasswordHash())
                .authorities(auths)
                .disabled(Boolean.FALSE.equals(u.getActivo()))
                .build();
    }
}