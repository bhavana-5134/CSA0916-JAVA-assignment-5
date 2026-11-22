import java.io.*;
import java.nio.file.*;

public class LineCounter {
    static long unbuffered(Path p) throws IOException {
        long count = 0;
        try (InputStream in = new FileInputStream(p.toFile())) {
            int b; boolean hasData = false;
            while ((b = in.read()) != -1) {
                hasData = true;
                if (b == '\n') count++;
            }
            if (hasData && Files.size(p) > 0) {
                try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "r")) {
                    raf.seek(raf.length() - 1);
                    if (raf.read() != '\n') count++;
                }
            }
        }
        return count;
    }
    static long buffered(Path p) throws IOException {
        long count = 0;
        try (BufferedReader r = Files.newBufferedReader(p)) {
            while (r.readLine() != null) count++;
        }
        return count;
    }
    public static void main(String[] args) throws Exception {
        Path p = Path.of(args.length == 0 ? "data/attendance_sample.log" : args[0]);
        long t1 = System.nanoTime(), a = unbuffered(p), t2 = System.nanoTime();
        long b = buffered(p), t3 = System.nanoTime();
        System.out.printf("Unbuffered lines=%d time=%.3f ms%n", a, (t2-t1)/1e6);
        System.out.printf("Buffered   lines=%d time=%.3f ms%n", b, (t3-t2)/1e6);
    }
}
