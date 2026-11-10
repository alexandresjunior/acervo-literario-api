
package br.com.treinarecife.acervo.literario.api.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "biografias")
public class Biografia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;

    private LocalDate dataNascimento;

    // Relacionamento um-para-um com Autor
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "autor_id",
        nullable = false,
        unique = true
    )
    private Autor autor;

    // Construtor padrão exigido pelo JPA
    public Biografia() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
