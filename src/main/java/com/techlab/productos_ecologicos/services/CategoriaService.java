package com.techlab.productos_ecologicos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techlab.productos_ecologicos.exception.CategoriaNoEncontradaException;
import com.techlab.productos_ecologicos.dto.CategoriaDTO;
import com.techlab.productos_ecologicos.dto.CategoriaRequestDTO;
import com.techlab.productos_ecologicos.mapper.CategoriaMapper;
import com.techlab.productos_ecologicos.models.Categoria;
import com.techlab.productos_ecologicos.repository.CategoriaRepository;

@Service // Anotación de Spring que marca esta clase como un "servicio". la ejecuta automaticamente al iniciar, sin esta anotacion Spring no sabe que es un servicio y no la inyecta donde se necesite.
public class CategoriaService {
    
    private final CategoriaRepository repository;
    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }
    public Categoria guardar(Categoria c) {   
        return repository.save(c);
    }
    public List<Categoria> listarTodos() {
        return repository.findAll();
    }
    public Categoria obtenerPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> new CategoriaNoEncontradaException("Categoría con ID " + id + " no encontrada"));
    }
    // Actualiza una categoría existente y guarda los cambios.
    public Categoria actualizar(Integer id, Categoria datos) {
        Categoria existente = obtenerPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setDescripcion(datos.getDescripcion());
        return repository.save(existente);
    }
    public void eliminar(Integer id) {
        Categoria existente = obtenerPorId(id);
        repository.delete(existente);
    }
    // Obtiene todas las categorías y las convierte a DTO para enviarlas al frontend.
    public List<CategoriaDTO> obtenerCategoriasResponse() {
        return CategoriaMapper.toDTOList(
                listarTodos()
        );
    }
    // Obtiene una categoría por id y la convierte a DTO.
    public CategoriaDTO obtenerCategoriaResponse(Integer id) {
        Categoria categoria = obtenerPorId(id);
        return CategoriaMapper.toDTO(categoria);
    }
    // Busca categorías por nombre y convierte el resultado a DTO.
    public List<CategoriaDTO> buscarPorNombreResponse(String nombre) {
        return CategoriaMapper.toDTOList(
                repository.findByNombreContaining(nombre)
        );
    }
    // Crea una categoría utilizando CategoriaRequestDTO.
    public CategoriaDTO crearCategoria(CategoriaRequestDTO dto) {
        Categoria categoria = CategoriaMapper.toEntity(dto);
        Categoria guardada = repository.save(categoria);
        return CategoriaMapper.toDTO(guardada);
    }
    // Actualiza una categoría existente utilizando CategoriaRequestDTO.
    public CategoriaDTO actualizarCategoria(
            Integer id,
            CategoriaRequestDTO dto) {
        Categoria categoria = obtenerPorId(id);
        CategoriaMapper.updateEntity(
                categoria,
                dto
        );
        Categoria actualizada = repository.save(categoria);
        return CategoriaMapper.toDTO(actualizada);
    }
}
