import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;

public class PassStoreDemo {
    public static void main(String[] args) throws Exception {
        Path file = Path.of(args.length > 0 ? args[0] : "data/bus_passes.csv");
        PassStore store = new PassStore(file);
        System.out.println("Find P1003: " + store.find("P1003"));
        System.out.println("Find P9999: " + store.find("P9999"));
        System.out.println("Route R12: " + store.listByRoute("R12"));
        if (args.length > 1 && args[1].equalsIgnoreCase("add")) {
            store.add(new BusPass("P2000", "192521153", "Bhavana B", "R12",
                    "Main Gate", LocalDate.of(2027, 3, 31), new BigDecimal("1500.00")));
            System.out.println("Added P2000");
        }
    }
}
