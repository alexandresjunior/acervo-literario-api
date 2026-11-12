package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.dto.request.LivroRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.LivroResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.entity.Livro;
import br.com.treinarecife.acervo.literario.api.mapper.LivroMapper;
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
    private final LivroMapper livroMapper;

    public LivroService(
            LivroRepository livroRepository,
            AutorRepository autorRepository,
            CategoriaRepository categoriaRepository,
            LivroMapper livroMapper) {

        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.livroMapper = livroMapper;
    }

    // Cadastra um novo livro
    public LivroResponseDTO salvar(LivroRequestDTO dto) {
        Livro livro = livroMapper.toEntity(dto);
        validarRelacionamentos(livro);
        
        Livro livroSalvo = livroRepository.save(livro);
        
        return livroMapper.toResponseDTO(livroSalvo);
    }

    // Lista todos os livros
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> listarTodos() {
        return livroRepository.findAll().stream()
                .map(livroMapper::toResponseDTO)
                .toList();
    }

    // Busca um livro pelo ID
    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(livroMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Livro não encontrado."));
    }

    // Busca livros pelo título
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> buscarPorTitulo(String termo) {
        return livroRepository.findByTituloContainingIgnoreCase(termo).stream()
                .map(livroMapper::toResponseDTO)
                .toList();
    }

    // Busca livros pelo nome da categoria
    @Transactional(readOnly = true)
    public List<LivroResponseDTO> buscarPorCategoria(String nome) {
        return livroRepository.findByCategoriasNome(nome).stream()
                .map(livroMapper::toResponseDTO)
                .toList();
    }

    // Atualiza os dados de um livro
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dados) {
        LivroResponseDTO livroDTO = buscarPorId(id);

        Livro livro = livroMapper.toEntity(dados);
        livro.setId(id); // Garante que a atualização ocorrerá no registro correto
        
        validarRelacionamentos(livro);

        livro.setTitulo(dados.titulo());
        livro.setIsbn(dados.isbn());
        livro.setAnoPublicacao(dados.anoPublicacao());
        // Obs: O setAutor e setCategorias são processados pelo validarRelacionamentos().

        Livro livroAtualizado = livroRepository.save(livro);

        return livroMapper.toResponseDTO(livroAtualizado);
    }

    // Exclui um livro
    public void excluir(Long id) {
        LivroResponseDTO livroDTO = buscarPorId(id);

        livroRepository.deleteById(livroDTO.id());
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