
package br.com.treinarecife.acervo.literario.api.repository;

import br.com.treinarecife.acervo.literario.api.entity.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNomeIgnoreCase(String nome);

}
