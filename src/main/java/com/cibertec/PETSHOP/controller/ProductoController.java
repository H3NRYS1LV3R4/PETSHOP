package com.cibertec.PETSHOP.controller;

import com.cibertec.PETSHOP.entity.Producto;
import com.cibertec.PETSHOP.service.ProductoService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService; // <--- Inyectamos el Servicio

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        Optional<Producto> producto = productoService.buscarPorId(id);
        return producto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto detalles) {
        Optional<Producto> productoOpt = productoService.buscarPorId(id);
        
        if (productoOpt.isPresent()) {
            Producto prod = productoOpt.get();
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoService.buscarPorId(id);
        
        if (productoOpt.isPresent()) {
            productoService.eliminar(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}