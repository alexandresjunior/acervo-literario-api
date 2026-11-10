
package br.com.treinarecife.acervo.literario.api.repository;

import br.com.treinarecife.acervo.literario.api.entity.Livro;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    // Buscar livros pelo título (ignorando maiúsculas e minúsculas)
    List<Livro> findByTituloContainingIgnoreCase(String termo);

    // Buscar livros pelo nome da categoria
    List<Livro> findByCategoriasNome(String nome);

    // Verificar se um autor possui livros cadastrados
    boolean existsByAutor_Id(Long autorId);

}
