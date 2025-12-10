package com.cibertec.PETSHOP.service.impl;

import com.cibertec.PETSHOP.dto.ProductoDto;
import com.cibertec.PETSHOP.entity.Producto;
import com.cibertec.PETSHOP.mapper.ProductoMapper;
import com.cibertec.PETSHOP.repository.ProductoRepository;
import com.cibertec.PETSHOP.service.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper mapper;

	@Override
	public List<ProductoDto> listarTodos() {
		return productoRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
	}

	@Override
	public Optional<ProductoDto> buscarPorId(Long id) {
		return productoRepository.findById(id)
                .map(mapper::toDto);
	}

	@Override
	public ProductoDto guardar(ProductoDto productoDto) {
		Producto entidad = mapper.toEntity(productoDto);
        Producto entidadGuardada = productoRepository.save(entidad);
        return mapper.toDto(entidadGuardada);
	}

	@Override
	public void eliminar(Long id) {
		productoRepository.deleteById(id);
		
	}

    
}