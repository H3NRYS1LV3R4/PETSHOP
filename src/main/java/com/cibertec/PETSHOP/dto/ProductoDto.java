package com.cibertec.PETSHOP.dto;

public record ProductoDto(
    Long id,
    String nombre,
    String descripcion,
    Double precio,
    Integer stock,
    String imagenUrl
) {
}