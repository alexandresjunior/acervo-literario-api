
package br.com.treinarecife.acervo.literario.api.controller;

import br.com.treinarecife.acervo.literario.api.dto.request.AutorRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.AutorResponseDTO;
import br.com.treinarecife.acervo.literario.api.service.AutorService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    // Cadastra um novo autor
    @PostMapping
    public ResponseEntity<AutorResponseDTO> salvar(
            @RequestBody AutorRequestDTO dto) {

        AutorResponseDTO autorSalvo = autorService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(autorSalvo);
    }

    // Lista todos os autores
    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listarTodos() {

        return ResponseEntity.ok(autorService.listarTodos());
    }

    // Busca um autor pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    // Atualiza um autor
    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody AutorRequestDTO dto) {

        AutorResponseDTO autorAtualizado =
                autorService.atualizar(id, dto);

        return ResponseEntity.ok(autorAtualizado);
    }

    // Exclui um autor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        autorService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
