
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.repository.CategoriaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Cadastra uma nova categoria
    public Categoria salvar(Categoria categoria) {

        validarNome(categoria.getNome(), null);

        return categoriaRepository.save(categoria);
    }

    // Lista todas as categorias
    @Transactional(readOnly = true)
    public List<Categoria> listarTodos() {

        return categoriaRepository.findAll();
    }

    // Busca uma categoria pelo ID
    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {

        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada."));
    }

    // Busca uma categoria pelo nome
    @Transactional(readOnly = true)
    public Categoria buscarPorNome(String nome) {

        return categoriaRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Categoria não encontrada."));
    }

    // Atualiza uma categoria
    public Categoria atualizar(Long id, Categoria dados) {

        Categoria categoria = buscarPorId(id);

        validarNome(dados.getNome(), id);

        categoria.setNome(dados.getNome());

        return categoriaRepository.save(categoria);
    }

    // Exclui uma categoria
    public void excluir(Long id) {

        Categoria categoria = buscarPorId(id);

        categoriaRepository.delete(categoria);
    }

    // Valida o nome da categoria
    private void validarNome(String nome, Long categoriaId) {

        if (nome == null || nome.isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O nome da categoria é obrigatório.");
        }

        categoriaRepository.findByNomeIgnoreCase(nome)
                .ifPresent(categoriaExistente -> {

                    // Permite manter o próprio nome durante uma atualização
                    if (categoriaId == null ||
                            !categoriaExistente.getId().equals(categoriaId)) {

                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Já existe uma categoria cadastrada com esse nome.");
                    }
                });
    }
}
