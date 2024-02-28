import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Util {
    public static void clearScreen() {
        try {
            String operatingSystem = System.getProperty("os.name");

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred while trying to clear the screen: " + e.getMessage());
        }
    }
    private static List<Movie> sortMovies(List<Movie> movies) {
        return movies.stream()
                .sorted((movie1, movie2) -> movie1.title().compareToIgnoreCase(movie2.title()))
                .collect(Collectors.toList());
    }
    public static List<Movie> searchMovies(Movie[] movies, String searchTerm) {

        return sortMovies(Arrays.stream(movies)
                .filter(movie -> movie.title().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList()));
    }

    public static LinkedHashMap<String, List<Movie>> searchCast(Movie[] movies, String searchTerm) {
        HashMap<String, ArrayList<Movie>> map = new HashMap<>();

        Arrays.stream(movies).
                forEach(movie -> Arrays.stream(movie.cast()).
                        filter(actor -> actor.toLowerCase().contains(searchTerm.toLowerCase())).
                        forEach(actor -> map.computeIfAbsent(actor, k -> new ArrayList<>()).add(movie)));

        return map.entrySet().stream().
                        sorted(Map.Entry.comparingByKey()).
                collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> sortMovies(entry.getValue()),
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}