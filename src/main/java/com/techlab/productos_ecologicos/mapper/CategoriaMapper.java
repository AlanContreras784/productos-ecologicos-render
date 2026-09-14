package com.techlab.productos_ecologicos.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.techlab.productos_ecologicos.dto.CategoriaDTO;
import com.techlab.productos_ecologicos.dto.CategoriaRequestDTO;
import com.techlab.productos_ecologicos.models.Categoria;

public class CategoriaMapper {
    // Convierte una entidad Categoria en un CategoriaDTO.
    // Se utiliza para enviar información al frontend sin exponer la entidad JPA.
    public static CategoriaDTO toDTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
    // Convierte una lista de entidades Categoria en una lista de CategoriaDTO.
    public static List<CategoriaDTO> toDTOList(List<Categoria> categorias) {
        return categorias.stream()
                .map(CategoriaMapper::toDTO)
                .collect(Collectors.toList());
    }
    // Convierte un CategoriaRequestDTO en una entidad Categoria.
    // Se utiliza para crear una nueva categoría.
    public static Categoria toEntity(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return categoria;
    }
    // Actualiza una entidad Categoria existente utilizando los datos
    // recibidos desde CategoriaRequestDTO.
    // Se utiliza en las operaciones PUT.
    public static void updateEntity(
            Categoria categoria,
            CategoriaRequestDTO dto) {
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
    }
}