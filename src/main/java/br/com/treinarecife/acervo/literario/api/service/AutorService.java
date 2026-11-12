package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.dto.request.AutorRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.AutorResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.mapper.AutorMapper;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.LivroRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final AutorMapper autorMapper;

    public AutorService(
            AutorRepository autorRepository,
            LivroRepository livroRepository,
            AutorMapper autorMapper) {

        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
        this.autorMapper = autorMapper;
    }

    // Cadastrar autor
    public AutorResponseDTO salvar(AutorRequestDTO dto) {
        Autor autor = autorMapper.toEntity(dto);
        Autor autorSalvo = autorRepository.save(autor);
        
        return autorMapper.toResponseDTO(autorSalvo);
    }

    // Listar todos os autores
    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listarTodos() {
        return autorRepository.findAll().stream()
                .map(autorMapper::toResponseDTO)
                .toList();
    }

    // Buscar autor pelo ID
    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(Long id) {
        return autorRepository.findById(id)
                .map(autorMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado."
                ));
    }

    // Atualizar autor
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dados) {
        AutorResponseDTO autorDTO = buscarPorId(id);

        Autor autor = autorMapper.toEntity(dados);
        autor.setId(id); // Garante que a atualização ocorrerá no registro correto
        autor.setNome(dados.nome());
        autor.setNacionalidade(dados.nacionalidade());

        Autor autorAtualizado = autorRepository.save(autor);

        return autorMapper.toResponseDTO(autorAtualizado);
    }

    // Excluir autor
    public void excluir(Long id) {
        // Verifica se o autor existe utilizando o próprio método da classe
        AutorResponseDTO autorDTO = buscarPorId(id);

        // Verifica se existem livros associados ao autor
        boolean possuiLivros = livroRepository.existsByAutor_Id(id);

        if (possuiLivros) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir este autor, "
                    + "pois existem livros cadastrados em seu nome."
            );
        }

        // Exclui o autor
        autorRepository.deleteById(autorDTO.id());
    }
}