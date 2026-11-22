import java.nio.file.*;
public class PassFileAdversarialDemo {
    public static void main(String[] args) throws Exception {
        Path p=Path.of("data/bus_passes_damaged.csv");
        PassStore store=new PassStore(p);
        System.out.println("Valid record P1001: "+store.find("P1001"));
        System.out.println("Valid record after damaged lines P1003: "+store.find("P1003"));
        System.out.println("Missing ID: "+store.find("P9999"));
        System.out.println("Route R12: "+store.listByRoute("R12"));
    }
}
