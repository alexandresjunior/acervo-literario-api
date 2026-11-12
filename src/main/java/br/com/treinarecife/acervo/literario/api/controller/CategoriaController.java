
package br.com.treinarecife.acervo.literario.api.controller;

import br.com.treinarecife.acervo.literario.api.dto.request.CategoriaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.CategoriaResponseDTO;
import br.com.treinarecife.acervo.literario.api.service.CategoriaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Cadastra uma nova categoria
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> salvar(
            @RequestBody CategoriaRequestDTO dto) {

        CategoriaResponseDTO categoriaSalva =
                categoriaService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaSalva);
    }

    // Lista todas as categorias
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodos() {

        return ResponseEntity.ok(categoriaService.listarTodos());
    }

    // Busca uma categoria pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    // Atualiza uma categoria
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody CategoriaRequestDTO dto) {

        CategoriaResponseDTO categoriaAtualizada =
                categoriaService.atualizar(id, dto);

        return ResponseEntity.ok(categoriaAtualizada);
    }

    // Exclui uma categoria
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        categoriaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
