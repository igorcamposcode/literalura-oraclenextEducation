package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.ListLivro;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.model.Person;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.repository.PersonRepository;
import br.com.alura.literalura.service.Consumo;
import br.com.alura.literalura.service.ConverteDados;
import org.antlr.v4.runtime.InputMismatchException;

import java.util.*;

public class Principal {

    private LivroRepository livroRepository;
    private PersonRepository personRepository;
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private final String ENDERECOALL = "https://gutendex.com/books/?page=";
    private Consumo consumoApi = new Consumo();
    private ConverteDados converteDados = new ConverteDados();
    private Scanner leitura = new Scanner(System.in);

    String json = "";
    String confirmacao = "N";

    public Principal(LivroRepository livroRepository, PersonRepository personRepository) {
        this.livroRepository = livroRepository;
        this.personRepository = personRepository;
    }

    public void exibirMenu() {
        int opcao = -1;
        do {
            menu();
            desenhaSeta();
            try {
                opcao = leitura.nextInt();
                leitura.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite apenas números.");
                leitura.nextLine();
                pausa();
                continue;
            }

            switch (opcao) {
                case 1:
                    buscarLivroNaAPIEGravarBD();
                    pausa();
                    break;
                case 2:
                    listarLivrosCadastrados();
                    pausa();
                    break;
                case 3:
                    listarAutoresCadastrados();
                    pausa();
                    break;
                case 4:
                    buscarAutoresPorAno();
                    pausa();
                    break;
                case 5:
                    menuIdioma();
                    desenhaSeta();
                    buscarAutoresPorIdioma();
                    pausa();
                    break;
                case 6:
                    buscarEstatisticasLivrosAPI();
                    pausa();
                    break;
                case 7:
                    buscarTop10LivrosBD();
                    pausa();
                    break;
                case 8:
                    buscarAutorPorNomeBD();
                    pausa();
                    break;
                case 9:
                    buscarEstatisticasAutoresAno();
                    pausa();
                    break;
                case 0:
                    System.out.println("Saindo do Sistema...");
                    break;
                default:
                    System.out.println("Opção Inválida! Tente novamente.");
                    pausa();
            }
        } while (opcao != 0);
    }

    private void buscarEstatisticasAutoresAno() {
        System.out.println("Estatísticas dos Anos de Nasc. e Morte:");
        List<Person> lista = personRepository.findAllByOrderByNameAsc();

        IntSummaryStatistics statistics1 = lista.stream()
                .mapToInt(Person::getBirthYear)
                .summaryStatistics();

        IntSummaryStatistics statistics2 = lista.stream()
                .mapToInt(Person::getDeathYear)
                .summaryStatistics();

        desenhaLinha();
        System.out.println("Estatísticas dos Autores:");
        desenhaLinha();
        System.out.println("Total de " + statistics1.getCount() + " Autores analisados.");
        desenhaLinha();

        System.out.println("ANO DE NASCIMENTO:");
        System.out.printf("Média de Nasc: %.2f%n", statistics1.getAverage());
        System.out.printf("Mínimo: %d%n", statistics1.getMin());
        System.out.printf("Máximo: %d%n", statistics1.getMax());
        desenhaLinha();

        System.out.println("ANO DE MORTE:");
        System.out.printf("Média de Morte: %.2f%n", statistics2.getAverage());
        System.out.printf("Mínimo: %d%n", statistics2.getMin());
        System.out.printf("Máximo: %d%n", statistics2.getMax());
        desenhaLinha();
        desenhaLinha();
    }

    private void buscarEstatisticasLivrosAPI() {
        int totalPaginas = -1;
        while (totalPaginas <= 0) {
            try {
                System.out.println("**Digite a quantidade de páginas: ");
                System.out.println("** Atualmente a API possui um total de % livros %");
                System.out.println("** cadastrados, gerando aprox. % páginas %");
                System.out.println("** Esse processo pode levar um bom tempo");
                desenhaSeta();
                totalPaginas = Integer.parseInt(leitura.nextLine().trim());
                if (totalPaginas <= 0) {
                    System.out.println("Número inválido, Digite um valor maior que zero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, Digite apenas números inteiros.");
            }
        }

        List<Livro> livros = buscarTodosOsLivros(totalPaginas);

        IntSummaryStatistics statistics = livros.stream()
                .mapToInt(Livro::getDownloadCount)
                .summaryStatistics();

        desenhaLinha();
        System.out.println("Estatísticas de Downloads:");
        desenhaLinha();

        System.out.printf("Quantidade de livros analisados : %d%n", statistics.getCount());
        System.out.printf("Total de downloads: %d%n", statistics.getSum());
        System.out.printf("Média de downloads: %.2f%n", statistics.getAverage());
        System.out.printf("Menor < número de downloads: %d%n", statistics.getMin());
        System.out.printf("Maior > número de downloads: %d%n", statistics.getMax());
        desenhaLinha();
    }

    private void buscarAutoresPorIdioma() {
        String idioma = leitura.nextLine().trim().replaceAll("[^a-zA-Z\\-]", "");
        List<Livro> lista = livroRepository.buscarPorIdioma(idioma);

        if (lista.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma: " + idioma);
            return;
        }
        System.out.println(lista);
        System.out.println("Quantidade de Livros 📚: " + lista.size());
        desenhaLinha();
    }

    private void buscarAutoresPorAno() {
        int ano = -1;
        while (ano < 0) {
            try {
                System.out.println("Digite uma data para pesquisa: ");
                desenhaSeta();
                ano = Integer.parseInt(leitura.nextLine().trim());
                if (ano < 0) {
                    System.out.println("Ano inválido! Digite um valor maior ou igual a zero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite apenas números inteiros.");
            }
        }
        desenhaLinha();

        List<Person> lista = personRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(ano, ano);

        if (lista.size() == 0) {
            System.out.println("Nenhum Autor encontrado!");
        } else {
            System.out.println(lista);
        }
    }

    private void listarAutoresCadastrados() {
        List<Person> lista = personRepository.findAll();
        System.out.println(lista);
    }

    private void listarLivrosCadastrados() {
        List<Livro> lista = livroRepository.findAll();
        System.out.println(lista);
    }

    private void buscarLivroNaAPIEGravarBD() {
        System.out.print("Digite o nome do livro: ");
        desenhaSeta();
        String tituloLivro = leitura.nextLine().trim().replace(" ", "%20");
        desenhaLinha();

        String json = consumoApi.obterDados(ENDERECO + tituloLivro);
        ListLivro listLivro = converteDados.obterDados(json, ListLivro.class);

        if (listLivro.getResults() == null || listLivro.getResults().isEmpty()) {
            System.out.println("Nenhum livro encontrado com esse nome.");
            return;
        }

        Livro livroEncontrado = listLivro.getResults().get(0);
        System.out.println(livroEncontrado);
        pausa();

        System.out.println("Deseja gravar livro no Banco de Dados? (S/N)");
        desenhaSeta();
        String confirmacao = leitura.nextLine();

        if (!confirmacao.equalsIgnoreCase("S")) {
            return;
        }

        Optional<Livro> livroExistente = livroRepository.findByTitle(livroEncontrado.getTitle());

        if (livroExistente.isPresent()) {
            atualizarLivro(livroExistente.get(), livroEncontrado);
            System.out.println("Dados atualizados com sucesso!");
        } else {
            livroRepository.save(livroEncontrado);
            System.out.println("Livro gravado com sucesso!");
        }
    }

    private void atualizarLivro(Livro existente, Livro novo) {
        existente.setDownloadCount(novo.getDownloadCount());
        livroRepository.save(existente);
    }

    private void menu() {
        String menu = """
                ------------------------------------------------
                Menu de Opções:
                ------------------------------------------------
                1 - Buscar Livro pelo Título
                2 - Listar Livros cadastrados
                3 - Listar Autores cadastrados
                4 - Listar Autores vivos em determinado ano
                5 - Listar Livros em um determinado idioma
                6 - Estatísticas de Livros
                7 - Top 10 Livros (Mais baixados)
                8 - Buscar Autor por nome
                9 - Estatísticas Autor
                0 - Sair
                ------------------------------------------------
                Escolha uma opção:
                ------------------------------------------------
                """;
        System.out.println(menu);
    }

    private void menuIdioma() {
        String msg = """
                ------------------------------------------------
                Digite a abreviação do idioma:
                ------------------------------------------------
                🇧🇷 pt - Português
                🇪🇸 es - Espanhol
                🇫🇷 fr - Francês
                🇺🇸 en - Inglês
                ------------------------------------------------
                Escolha uma opção:
                ------------------------------------------------
                """;
        System.out.println(msg);
    }

    private void desenhaSeta() {
        System.out.print("->");
    }

    private void desenhaLinha() {
        System.out.println("------------------------------------------------");
    }

    private void pausa() {
        System.out.print("Pressione Enter para continuar...");
        leitura.nextLine();
    }

    private List<Livro> buscarTodosOsLivros(int totalPaginas) {
        List<Livro> allBooks = new ArrayList<>();
        for (int i = 1; i <= totalPaginas; i++) {
            json = consumoApi.obterDados(ENDERECOALL + i);
            ListLivro listBook = converteDados.obterDados(json, ListLivro.class);

            desenhaLinha();
            System.out.println("Página atual: " + i + "\n");
            System.out.println(listBook);

            allBooks.addAll(listBook.getResults());
        }
        return allBooks;
    }

    private void buscarAutorPorNomeBD() {
        System.out.println("Digite o nome do Autor:");
        desenhaSeta();
        String nomeAutor = leitura.nextLine();
        List<Person> lista = personRepository.findByNameContainingIgnoreCaseOrderByNameAsc(nomeAutor);
        System.out.println(lista);
    }

    private void buscarTop10LivrosBD() {
        List<Livro> lista = livroRepository.findTop10ByOrderByDownloadCountDesc();
        System.out.println(lista);
    }
}