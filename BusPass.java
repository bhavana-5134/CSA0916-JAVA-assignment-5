import java.math.BigDecimal;
import java.time.LocalDate;

public record BusPass(String passId, String registerNo, String name, String routeNo,
                      String boardingStop, LocalDate validityDate, BigDecimal amount) {
    public String toCsv() {
        return String.join(",", passId, registerNo, name, routeNo, boardingStop,
                validityDate.toString(), amount.toPlainString());
    }
    public static BusPass fromCsv(String line) {
        String[] f = line.split(",", -1);
        if (f.length != 7) throw new IllegalArgumentException("Expected 7 fields");
        BigDecimal amount = new BigDecimal(f[6]);
        if (amount.signum() < 0) throw new IllegalArgumentException("Negative amount");
        return new BusPass(f[0], f[1], f[2], f[3], f[4], LocalDate.parse(f[5]), amount);
    }
}
