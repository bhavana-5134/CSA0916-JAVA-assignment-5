import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class PassStore {
    private final Path path;
    public PassStore(Path path) { this.path = path; }

    public void add(BusPass pass) throws IOException {
        Path parent = path.toAbsolutePath().getParent();
        if (parent != null) Files.createDirectories(parent);
        try (BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(pass.toCsv());
            w.newLine();
        }
    }

    // TODO 1 completed: scan line-by-line, skip damaged lines and return the matching record.
    public BusPass find(String id) throws IOException {
        if (!Files.exists(path)) throw new FileNotFoundException(path.toString());
        try (BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                try {
                    BusPass p = BusPass.fromCsv(line);
                    if (p.passId().equals(id)) return p;
                } catch (RuntimeException ex) {
                    System.err.println("Skipping damaged line: " + line);
                }
            }
        }
        return null;
    }

    public List<BusPass> listByRoute(String route) throws IOException {
        if (!Files.exists(path)) throw new FileNotFoundException(path.toString());
        List<BusPass> result = new ArrayList<>();
        try (BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                try {
                    BusPass p = BusPass.fromCsv(line);
                    if (p.routeNo().equalsIgnoreCase(route)) result.add(p);
                } catch (RuntimeException ex) {
                    System.err.println("Skipping damaged line: " + line);
                }
            }
        }
        return result;
    }
}
