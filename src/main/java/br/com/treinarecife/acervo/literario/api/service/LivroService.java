
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.entity.Livro;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.CategoriaRepository;
import br.com.treinarecife.acervo.literario.api.repository.LivroRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public LivroService(
            LivroRepository livroRepository,
            AutorRepository autorRepository,
            CategoriaRepository categoriaRepository) {

        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Cadastra um novo livro
    public Livro salvar(Livro livro) {
        validarRelacionamentos(livro);

        return livroRepository.save(livro);
    }

    // Lista todos os livros
    @Transactional(readOnly = true)
    public List<Livro> listarTodos() {

        return livroRepository.findAll();
    }

    // Busca um livro pelo ID
    @Transactional(readOnly = true)
    public Livro buscarPorId(Long id) {

        return livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado."));
    }

    // Busca livros pelo título
    @Transactional(readOnly = true)
    public List<Livro> buscarPorTitulo(String termo) {

        return livroRepository
                .findByTituloContainingIgnoreCase(termo);
    }

    // Busca livros pelo nome da categoria
    @Transactional(readOnly = true)
    public List<Livro> buscarPorCategoria(String nome) {

        return livroRepository.findByCategoriasNome(nome);
    }

    // Atualiza os dados de um livro
    public Livro atualizar(Long id, Livro dados) {

        Livro livro = buscarPorId(id);

        validarRelacionamentos(dados);

        livro.setTitulo(dados.getTitulo());
        livro.setIsbn(dados.getIsbn());
        livro.setAnoPublicacao(dados.getAnoPublicacao());
        livro.setAutor(dados.getAutor());
        livro.setCategorias(dados.getCategorias());

        return livroRepository.save(livro);
    }

    // Exclui um livro
    public void excluir(Long id) {

        Livro livro = buscarPorId(id);

        livroRepository.delete(livro);
    }

    // Valida os relacionamentos obrigatórios do livro
    private void validarRelacionamentos(Livro livro) {

        // Validação do autor
        if (livro.getAutor() == null ||
                livro.getAutor().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O cadastro do livro exige um autor.");
        }

        Autor autor = autorRepository
                .findById(livro.getAutor().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "O autor informado não está cadastrado."));

        // Validação das categorias
        if (livro.getCategorias() == null ||
                livro.getCategorias().isEmpty()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O cadastro do livro exige pelo menos uma categoria.");
        }

        // Obtém os IDs das categorias informadas
        Set<Long> categoriasIds = new HashSet<>();

        for (Categoria categoria : livro.getCategorias()) {

            if (categoria == null || categoria.getId() == null) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Todas as categorias informadas devem existir.");
            }

            categoriasIds.add(categoria.getId());
        }

        // Busca as categorias existentes no banco
        List<Categoria> categoriasEncontradas =
                categoriaRepository.findAllById(categoriasIds);

        // Verifica se todas as categorias foram encontradas
        if (categoriasEncontradas.size() != categoriasIds.size()) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Uma ou mais categorias informadas não estão cadastradas.");
        }

        // Substitui as referências recebidas pelas entidades persistidas
        livro.setAutor(autor);
        livro.setCategorias(categoriasEncontradas);
    }
}
