import java.sql.*;
public class WalletTransferDemo {
    public static void main(String[] args) throws Exception {
        try(Connection c=DB.get()) {
            c.setAutoCommit(false);
            try {
                try(PreparedStatement debit=c.prepareStatement(
                    "UPDATE wallet SET balance=balance-500 WHERE wallet_id=1 AND balance>=500");
                    PreparedStatement credit=c.prepareStatement(
                    "UPDATE wallet SET balance=balance+500 WHERE wallet_id=2")) {
                    if(debit.executeUpdate()!=1) throw new SQLException("Insufficient balance");
                    if(credit.executeUpdate()!=1) throw new SQLException("Destination wallet missing");
                }
                c.commit(); System.out.println("Transfer committed");
            } catch(Exception ex) {
                c.rollback(); System.out.println("Transfer rolled back: "+ex.getMessage());
            }
        }
    }
}
