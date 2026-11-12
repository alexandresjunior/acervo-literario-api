
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.dto.request.BiografiaRequestDTO;
import br.com.treinarecife.acervo.literario.api.dto.response.BiografiaResponseDTO;
import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Biografia;
import br.com.treinarecife.acervo.literario.api.mapper.BiografiaMapper;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.BiografiaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class BiografiaService {

    private final BiografiaRepository biografiaRepository;
    private final AutorRepository autorRepository;
    private final BiografiaMapper biografiaMapper;

    public BiografiaService(
            BiografiaRepository biografiaRepository,
            AutorRepository autorRepository,
            BiografiaMapper biografiaMapper) {

        this.biografiaRepository = biografiaRepository;
        this.autorRepository = autorRepository;
        this.biografiaMapper = biografiaMapper;
    }

    // Cadastra uma nova biografia
    public BiografiaResponseDTO salvar(BiografiaRequestDTO dto) {

        Autor autor = validarAutor(dto.autorId(), null);

        Biografia biografia = biografiaMapper.toEntity(dto);
        biografia.setAutor(autor);

        Biografia biografiaSalva = biografiaRepository.save(biografia);

        return biografiaMapper.toResponseDTO(biografiaSalva);
    }

    // Lista todas as biografias
    @Transactional(readOnly = true)
    public List<BiografiaResponseDTO> listarTodos() {

        return biografiaRepository.findAll().stream()
                .map(biografiaMapper::toResponseDTO)
                .toList();
    }

    // Busca uma biografia pelo ID
    @Transactional(readOnly = true)
    public BiografiaResponseDTO buscarPorId(Long id) {

        return biografiaRepository.findById(id)
                .map(biografiaMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada."));
    }

    // Busca a biografia de um autor
    @Transactional(readOnly = true)
    public BiografiaResponseDTO buscarPorAutor(Long autorId) {

        return biografiaRepository.findByAutor_Id(autorId)
                .map(biografiaMapper::toResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada para o autor informado."));
    }

    // Atualiza uma biografia
    public BiografiaResponseDTO atualizar(
            Long id,
            BiografiaRequestDTO dados) {

        Biografia biografia = biografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada."));

        Autor autor = validarAutor(dados.autorId(), id);

        biografia.setTexto(dados.texto());
        biografia.setDataNascimento(dados.dataNascimento());
        biografia.setAutor(autor);

        Biografia biografiaAtualizada =
                biografiaRepository.save(biografia);

        return biografiaMapper.toResponseDTO(biografiaAtualizada);
    }

    // Exclui uma biografia
    public void excluir(Long id) {

        Biografia biografia = biografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada."));

        biografiaRepository.delete(biografia);
    }

    // Valida o relacionamento entre biografia e autor
    private Autor validarAutor(Long autorId, Long biografiaId) {

        // Verifica se o ID do autor foi informado
        if (autorId == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O cadastro da biografia exige um autor.");
        }

        // Verifica se o autor existe
        Autor autor = autorRepository.findById(autorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "O autor informado não está cadastrado."));

        // Verifica se o autor já possui uma biografia
        biografiaRepository.findByAutor_Id(autorId)
                .ifPresent(biografiaExistente -> {

                    // Permite manter o mesmo autor durante uma atualização
                    if (biografiaId == null ||
                            !biografiaExistente.getId().equals(biografiaId)) {

                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Este autor já possui uma biografia cadastrada.");
                    }
                });

        return autor;
    }
}
