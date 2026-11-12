
package br.com.treinarecife.acervo.literario.api.controller;

import br.com.treinarecife.acervo.literario.api.dto.request.BiografiaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.BiografiaResponseDTO;
import br.com.treinarecife.acervo.literario.api.service.BiografiaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biografias")
public class BiografiaController {

    private final BiografiaService biografiaService;

    public BiografiaController(BiografiaService biografiaService) {
        this.biografiaService = biografiaService;
    }

    // Cadastra uma nova biografia
    @PostMapping
    public ResponseEntity<BiografiaResponseDTO> salvar(
            @RequestBody BiografiaRequestDTO dto) {

        BiografiaResponseDTO biografiaSalva =
                biografiaService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(biografiaSalva);
    }

    // Lista todas as biografias
    @GetMapping
    public ResponseEntity<List<BiografiaResponseDTO>> listarTodos() {

        return ResponseEntity.ok(biografiaService.listarTodos());
    }

    // Busca uma biografia pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<BiografiaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(biografiaService.buscarPorId(id));
    }

    // Busca a biografia de um autor
    @GetMapping("/autor/{autorId}")
    public ResponseEntity<BiografiaResponseDTO> buscarPorAutor(
            @PathVariable Long autorId) {

        return ResponseEntity.ok(
                biografiaService.buscarPorAutor(autorId));
    }

    // Atualiza uma biografia
    @PutMapping("/{id}")
    public ResponseEntity<BiografiaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody BiografiaRequestDTO dto) {

        BiografiaResponseDTO biografiaAtualizada =
                biografiaService.atualizar(id, dto);

        return ResponseEntity.ok(biografiaAtualizada);
    }

    // Exclui uma biografia
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        biografiaService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
