package repuestos.repuestoscloud.service;

import repuestos.repuestoscloud.entity.Producto;
import repuestos.repuestoscloud.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
  private final ProductoRepository repo;
  public ProductoService(ProductoRepository repo) { this.repo = repo; }

  public List<Producto> listar(String q, String marca, String tipo) {
    String qq = (q == null || q.isBlank()) ? null : q.trim();
    String mm = (marca == null || marca.isBlank()) ? null : marca.trim();
    String tt = (tipo == null || tipo.isBlank()) ? null : tipo.trim();
    return repo.buscarFiltrar(qq, mm, tt);
  }

  public Producto porId(Long id) {
    return repo.findById(id).orElseThrow();
  }

  public Producto guardar(Producto p) { return repo.save(p); }
}