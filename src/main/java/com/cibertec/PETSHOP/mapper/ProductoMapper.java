package com.cibertec.PETSHOP.mapper;

import com.cibertec.PETSHOP.dto.ProductoDto;
import com.cibertec.PETSHOP.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDto toDto(Producto entity) {
        ProductoDto dto = new ProductoDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPrecio(entity.getPrecio());
        dto.setStock(entity.getStock());
        dto.setImagenUrl(entity.getImagenUrl());
        return dto;
    }

    public Producto toEntity(ProductoDto dto) {
        Producto entity = new Producto();
        entity.setId(dto.getId()); 
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPrecio(dto.getPrecio());
        entity.setStock(dto.getStock());
        entity.setImagenUrl(dto.getImagenUrl());
        return entity;
    }
}