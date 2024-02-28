import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvParser {
    private final String path;

    public CsvParser(String path) {
        this.path = path;
    }

    private ArrayList<String> readLines() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return new ArrayList<>(reader.lines().toList());
        }
    }

    public Movie[] parse() throws IOException {
        ArrayList<String> lines = readLines();
        lines.remove(0);

        Movie[] movies = new Movie[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            String[] line = lines.get(i).split(",");
            String title = line[0];
            String[] cast =  line[1].contains("|") ? line[1].split("\\|") : new String[]{line[1]};
            String[] directors = line[2].contains("|") ? line[2].split("\\|") : new String[]{line[2]};
            String overview = line[3];
            int runtime = Integer.parseInt(line[4]);
            double rating = Double.parseDouble(line[5]);

            movies[i] = new Movie(title, cast, directors, overview, runtime, rating);
        }

        return movies;
    }
}
