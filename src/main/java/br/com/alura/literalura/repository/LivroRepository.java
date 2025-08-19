package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

// A anotação @EnableJpaRepositories não é necessária aqui
public interface LivroRepository extends JpaRepository<Livro, Long> {


    Optional<Livro> findByTitle(String title);

    @Query(value = "SELECT * FROM book WHERE :languages = ANY(languages)", nativeQuery = true)
    List<Livro> buscarPorIdioma(String languages);

    List<Livro> findTop10ByOrderByDownloadCountDesc();
}