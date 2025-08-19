package br.com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonAlias("birth_year")
    private Integer birthYear;

    @JsonAlias("death_year")
    private Integer deathYear;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Livro livro;


    public Person() {}

    public Person(Long id, String name, Integer birthYear, Integer deathYear, Livro livro) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.livro = livro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public Livro getBook() {
        return livro;
    }

    public void setBook(Livro livro) {
        this.livro = livro;
    }
    @Override
    public String toString() {
        return String.format(
                "🧑 Autor:\n" +
                        "               ---------------------------\n" +
                        "                Nome: %s\n" +
                        "                Ano de Nascimento: %s\n" +
                        "                Ano de Falecimento: %s\n" +
                        "               ---------------------------\n           ",
                name,
                birthYear != null ? birthYear : "Desconhecido",
                deathYear != null ? deathYear : "Vivo!"
        );
    }
}
