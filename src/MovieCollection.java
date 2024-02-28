import java.util.*;

public class MovieCollection {
    private final Movie[] movies;
    private final Scanner scanner = new Scanner(System.in);

    public MovieCollection() {
        CsvParser parser = new CsvParser("src/movies_data.csv");
        try {
            this.movies = parser.parse();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing movies_data.csv: ", e);
        }
    }

    public void mainMenu() {
        System.out.println("Welcome to the movie collection!");
        String menuOption = "";

        while (!menuOption.equals("q")) {
            Util.clearScreen();
            System.out.println("------------ Main Menu ----------");
            System.out.println("- search (t)itles");
            System.out.println("- search (c)ast");
            System.out.println("- (q)uit");
            System.out.print("Enter choice: ");
            menuOption = scanner.nextLine();

            switch (menuOption) {
                case "t":
                    searchTitles();
                    break;
                case "c":
                    searchCast();
                    break;
                case "q":
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void searchTitles() {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine();

        displayTitleResults(Util.searchMovies(movies, searchTerm));
    }

    public void searchCast() {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().trim();
        LinkedHashMap<String, List<Movie>> castMap = Util.searchCast(movies, searchTerm);

        if (castMap.isEmpty()) {
            System.out.println("No cast members found!");
            return;
        }

        List<String> actors = new ArrayList<>(castMap.keySet());
        for (int i = 0; i < actors.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, actors.get(i));
        }

        System.out.print("Enter actor number for more info: ");
        int actorNumber = Integer.parseInt(scanner.nextLine());

        if (actorNumber < 1 || actorNumber > actors.size()) {
            System.out.println("Invalid actor number!");
            return;
        }

        Util.clearScreen();
        System.out.printf("Movies with %s:%n", actors.get(actorNumber - 1));

        List<Movie> selectedMovies = castMap.get(actors.get(actorNumber - 1));
        displayTitleResults(selectedMovies);
    }

    private void displayTitleResults(List<Movie> results) {
        if (results.isEmpty()) {
            System.out.println("No movies found!");
            System.out.print("\nPress enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        Map<Integer, Movie> movieMap = new HashMap<>();

        System.out.println("Search results:");
        for (int i = 1; i < results.size() + 1; i++) {
            movieMap.put(i, results.get(i-1));
            System.out.printf("%d. %s%n", i, results.get(i-1).title());
        }

        System.out.print("Enter movie number for more info: ");
        int movieNumber = Integer.parseInt(scanner.nextLine());

        Movie selectedMovie = movieMap.get(movieNumber);

        System.out.println("Title: " + selectedMovie.title());
        System.out.println("Cast: " + String.join(", ", selectedMovie.cast()));
        System.out.println("Directors: " + String.join(", ", selectedMovie.directors()));
        System.out.println("Overview: " + selectedMovie.overview());
        System.out.println("Runtime: " + selectedMovie.runtime() + " minutes");
        System.out.println("Rating: " + selectedMovie.rating());

        System.out.print("\nPress enter to return to the main menu...");
        scanner.nextLine();
    }
}
