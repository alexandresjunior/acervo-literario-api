
package br.com.treinarecife.acervo.literario.api.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String nacionalidade;

    @OneToOne(mappedBy = "autor",
              cascade = CascadeType.ALL,
              orphanRemoval = true)
    private Biografia biografia;

    @OneToMany(mappedBy = "autor")
    private List<Livro> livros = new ArrayList<>();

    // Construtor padrão exigido pelo JPA
    public Autor() {
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Biografia getBiografia() {
        return biografia;
    }

    public void setBiografia(Biografia biografia) {
        this.biografia = biografia;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
}
