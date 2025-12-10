package com.cibertec.PETSHOP.service;

import com.cibertec.PETSHOP.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    public List<Producto> listarTodos();
    public Optional<Producto> buscarPorId(Long id);
    public Producto guardar(Producto producto);
    public void eliminar(Long id);
}