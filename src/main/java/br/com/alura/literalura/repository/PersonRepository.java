package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(int ano, int ano1);

    List<Person> findByNameContainingIgnoreCaseOrderByNameAsc(String nomeAutor);

    List<Person> findAllByOrderByNameAsc();
}
