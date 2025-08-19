package br.com.alura.literalura.model;

import java.util.Collection;
import java.util.List;

public class ListLivro { private Long count;
    private String next;
    private String previous;
    private List<Livro> results;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public  List<Livro> getResults() {
        return results;
    }

    public void setResults(List<Livro> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return String.format(
                        "Resultados da busca:\n" +
                        "------------------------------------------------\n" +
                        "Total de livros na API: %d\n" +
                        "Próxima página: %s\n" +
                        "Página anterior: %s\n" +

                        "------------------------------------------------\n",
                         count, next, previous);//, results);

    }
}
