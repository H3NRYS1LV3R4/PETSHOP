package com.cibertec.PETSHOP.mapper;

import com.cibertec.PETSHOP.dto.ProductoDto;
import com.cibertec.PETSHOP.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDto toDto(Producto entity) {
        if (entity == null) return null;
        return new ProductoDto(
            entity.getId(),
            entity.getNombre(),
            entity.getDescripcion(),
            entity.getPrecio(),
            entity.getStock(),
            entity.getImagenUrl()
        );
    }

    public Producto toEntity(ProductoDto dto) {
        if (dto == null) return null;
        Producto entity = new Producto();
        entity.setId(dto.id()); 
        entity.setNombre(dto.nombre());
        entity.setDescripcion(dto.descripcion());
        entity.setPrecio(dto.precio());
        entity.setStock(dto.stock());
        entity.setImagenUrl(dto.imagenUrl());
        return entity;
    }
}