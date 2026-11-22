import java.nio.file.*;
import java.nio.charset.StandardCharsets;
public class LogGenerator {
    public static void main(String[] a) throws Exception {
        long bytes = Long.parseLong(a[0]); Path out = Path.of(a[1]);
        String line = "Attendance record: student checked in\n";
        try (var w = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            long written = 0;
            while (written < bytes) { w.write(line); written += line.length(); }
        }
        System.out.println("Created " + out + " (" + Files.size(out) + " bytes)");
    }
}
