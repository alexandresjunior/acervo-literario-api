
package br.com.treinarecife.acervo.literario.api.repository;

import br.com.treinarecife.acervo.literario.api.entity.Biografia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BiografiaRepository extends JpaRepository<Biografia, Long> {

    Optional<Biografia> findByAutor_Id(Long autorId);

}
