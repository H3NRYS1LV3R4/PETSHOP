package com.cibertec.PETSHOP.controller;

import com.cibertec.PETSHOP.dto.ProductoDto;
import com.cibertec.PETSHOP.service.ProductoService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService; 

    @GetMapping
    public List<ProductoDto> listarProductos() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> obtenerProducto(@PathVariable Long id) {
        Optional<ProductoDto> producto = productoService.buscarPorId(id);
        return producto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoDto> crearProducto(@RequestBody ProductoDto productoDto) {
    	ProductoDto nuevoProducto = productoService.guardar(productoDto);
        return ResponseEntity.ok(nuevoProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDto detalles) {
        Optional<ProductoDto> productoOpt = productoService.buscarPorId(id);
        
        if (productoOpt.isPresent()) {
            ProductoDto prod = productoOpt.get();
            prod.setNombre(detalles.getNombre());
            prod.setDescripcion(detalles.getDescripcion());
            prod.setPrecio(detalles.getPrecio());
            prod.setStock(detalles.getStock());
            prod.setImagenUrl(detalles.getImagenUrl());
            
            return ResponseEntity.ok(productoService.guardar(prod));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        Optional<ProductoDto> productoOpt = productoService.buscarPorId(id);
        
        if (productoOpt.isPresent()) {
            productoService.eliminar(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}