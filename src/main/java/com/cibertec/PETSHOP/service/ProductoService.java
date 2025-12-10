package com.cibertec.PETSHOP.service;

import com.cibertec.PETSHOP.dto.ProductoDto;
import com.cibertec.PETSHOP.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
	List<ProductoDto> listarTodos();
    Optional<ProductoDto> buscarPorId(Long id);
    ProductoDto guardar(ProductoDto productoDto);
    void eliminar(Long id);
}