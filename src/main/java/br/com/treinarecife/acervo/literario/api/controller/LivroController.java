
package br.com.treinarecife.acervo.literario.api.controller;

import br.com.treinarecife.acervo.literario.api.dto.request.LivroRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.LivroResponseDTO;
import br.com.treinarecife.acervo.literario.api.service.LivroService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    // Cadastra um novo livro
    @PostMapping
    public ResponseEntity<LivroResponseDTO> salvar(
            @RequestBody LivroRequestDTO dto) {

        LivroResponseDTO livroSalvo = livroService.salvar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(livroSalvo);
    }

    // Lista todos os livros
    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarTodos() {

        return ResponseEntity.ok(livroService.listarTodos());
    }

    // Busca um livro pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    // Busca livros pelo título
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<LivroResponseDTO>> buscarPorTitulo(
            @RequestParam String termo) {

        return ResponseEntity.ok(
                livroService.buscarPorTitulo(termo));
    }

    // Busca livros pelo nome da categoria
    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<LivroResponseDTO>> buscarPorCategoria(
            @RequestParam String nome) {

        return ResponseEntity.ok(
                livroService.buscarPorCategoria(nome));
    }

    // Atualiza um livro
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody LivroRequestDTO dto) {

        LivroResponseDTO livroAtualizado =
                livroService.atualizar(id, dto);

        return ResponseEntity.ok(livroAtualizado);
    }

    // Exclui um livro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        livroService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}
