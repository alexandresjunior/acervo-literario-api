
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.dto.request.CategoriaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.CategoriaResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Categoria;
import br.com.treinarecife.acervo.literario.api.mapper.CategoriaMapper;
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
    private final CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    // Cadastra uma nova categoria
    public CategoriaResponseDTO salvar(CategoriaRequestDTO dto) {
        Categoria categoria = categoriaMapper.toEntity(dto);

        validarNome(categoria.getNome(), null);

        Categoria categoriaSalva = categoriaRepository.save(categoria);

        return categoriaMapper.toResponseDTO(categoriaSalva);
    }

    // Lista todas as categorias
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodos() {

        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toResponseDTO)
                .toList();
    }

    // Busca uma categoria pelo ID
    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {

        return categoriaRepository.findById(id)
                .map(categoriaMapper::toResponseDTO)
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
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dados) {
        CategoriaResponseDTO categoriaDTO = buscarPorId(id);

        validarNome(dados.nome(), categoriaDTO.id());

        Categoria categoria = categoriaMapper.toEntity(dados);

        categoria.setNome(dados.nome());

        Categoria categoriaAtualizada = categoriaRepository.save(categoria);

        return categoriaMapper.toResponseDTO(categoriaAtualizada);
    }

    // Exclui uma categoria
    public void excluir(Long id) {
        CategoriaResponseDTO categoriaDTO = buscarPorId(id);

        categoriaRepository.deleteById(categoriaDTO.id());
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
