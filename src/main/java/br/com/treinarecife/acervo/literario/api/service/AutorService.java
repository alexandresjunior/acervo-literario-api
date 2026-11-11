
package br.com.treinarecife.acervo.literario.api.service;

import br.com.treinarecife.acervo.literario.api.entity.Autor;
import br.com.treinarecife.acervo.literario.api.repository.AutorRepository;
import br.com.treinarecife.acervo.literario.api.repository.LivroRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
@Transactional
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    public AutorService(
            AutorRepository autorRepository,
            LivroRepository livroRepository) {

        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
    }

    // Cadastrar autor
    public Autor salvar(Autor autor) {
        return autorRepository.save(autor);
    }

    // Listar todos os autores
    @Transactional(readOnly = true)
    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    // Buscar autor pelo ID
    @Transactional(readOnly = true)
    public Autor buscarPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor não encontrado."
                    )
                );
    }

    // Atualizar autor
    public Autor atualizar(Long id, Autor dados) {

        Autor autor = buscarPorId(id);

        autor.setNome(dados.getNome());
        autor.setNacionalidade(dados.getNacionalidade());

        return autorRepository.save(autor);
    }

    // Excluir autor
    public void excluir(Long id) {

        // Verifica se o autor existe
        Autor autor = buscarPorId(id);

        // Verifica se existem livros associados ao autor
        boolean possuiLivros =
                livroRepository.existsByAutor_Id(id);

        if (possuiLivros) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Não é possível excluir este autor, "
                + "pois existem livros cadastrados em seu nome."
            );
        }

        // Exclui o autor
        autorRepository.delete(autor);
    }
}
