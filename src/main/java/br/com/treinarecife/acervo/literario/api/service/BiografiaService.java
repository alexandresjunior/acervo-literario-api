
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.entity.Biografia;
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

    public BiografiaService(
            BiografiaRepository biografiaRepository,
            AutorRepository autorRepository) {

        this.biografiaRepository = biografiaRepository;
        this.autorRepository = autorRepository;
    }

    // Cadastra uma nova biografia
    public Biografia salvar(Biografia biografia) {

        validarAutor(biografia, null);

        return biografiaRepository.save(biografia);
    }

    // Lista todas as biografias
    @Transactional(readOnly = true)
    public List<Biografia> listarTodos() {

        return biografiaRepository.findAll();
    }

    // Busca uma biografia pelo ID
    @Transactional(readOnly = true)
    public Biografia buscarPorId(Long id) {

        return biografiaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada."));
    }

    // Busca a biografia de um autor
    @Transactional(readOnly = true)
    public Biografia buscarPorAutor(Long autorId) {

        return biografiaRepository.findByAutor_Id(autorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Biografia não encontrada para o autor informado."));
    }

    // Atualiza uma biografia
    public Biografia atualizar(Long id, Biografia dados) {

        Biografia biografia = buscarPorId(id);

        validarAutor(dados, id);

        biografia.setTexto(dados.getTexto());
        biografia.setDataNascimento(dados.getDataNascimento());
        biografia.setAutor(dados.getAutor());

        return biografiaRepository.save(biografia);
    }

    // Exclui uma biografia
    public void excluir(Long id) {

        Biografia biografia = buscarPorId(id);

        biografiaRepository.delete(biografia);
    }

    // Valida o relacionamento entre biografia e autor
    private void validarAutor(Biografia biografia, Long biografiaId) {

        if (biografia.getAutor() == null ||
                biografia.getAutor().getId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O cadastro da biografia exige um autor.");
        }

        Autor autor = autorRepository
                .findById(biografia.getAutor().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "O autor informado não está cadastrado."));

        // Verifica se o autor já possui uma biografia
        biografiaRepository.findByAutor_Id(autor.getId())
                .ifPresent(biografiaExistente -> {

                    // Permite manter o próprio autor durante uma atualização
                    if (biografiaId == null ||
                            !biografiaExistente.getId().equals(biografiaId)) {

                        throw new ResponseStatusException(
                                HttpStatus.CONFLICT,
                                "Este autor já possui uma biografia cadastrada.");
                    }
                });

        biografia.setAutor(autor);
    }
}
