import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
public class PassLoader {
    static String SQL="INSERT INTO bus_pass(pass_id,register_no,student_name,route_no,boarding_stop,validity_date,amount) VALUES(?,?,?,?,?,?,?)";
    static void bind(PreparedStatement p,int i) throws SQLException {
        p.setString(1,"LOAD"+i); p.setString(2,"REG"+i); p.setString(3,"Student "+i);
        p.setString(4,"R"+(i%5)); p.setString(5,"Main Gate");
        p.setDate(6,Date.valueOf(LocalDate.of(2027,3,31)));
        p.setBigDecimal(7,new BigDecimal("1000.00"));
    }
    public static void main(String[] args) throws Exception {
        try(Connection c=DB.get()) {
            c.setAutoCommit(false);
            try(PreparedStatement p=c.prepareStatement(SQL)) {
                for(int i=1;i<=10;i++){bind(p,i);p.addBatch();}
                int[] counts=p.executeBatch(); c.commit();
                System.out.println("Batch inserted rows: "+counts.length);
            } catch(Exception e){c.rollback();throw e;}
            try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM bus_pass LIMIT 10")) {
                ResultSetMetaData m=r.getMetaData();
                for(int i=1;i<=m.getColumnCount();i++)
                    System.out.println(m.getColumnName(i)+" : "+m.getColumnTypeName(i));
            }
        }
    }
}
