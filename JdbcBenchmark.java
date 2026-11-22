import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public class JdbcBenchmark {
    static final String SQL="INSERT INTO bus_pass(pass_id,register_no,student_name,route_no,boarding_stop,validity_date,amount) VALUES(?,?,?,?,?,?,?)";
    static void bind(PreparedStatement p,int i)throws SQLException{
        p.setString(1,"BENCH"+i);p.setString(2,"REG"+i);p.setString(3,"Bench "+i);
        p.setString(4,"R1");p.setString(5,"Stop");p.setDate(6,Date.valueOf(LocalDate.of(2027,3,31)));
        p.setBigDecimal(7,new BigDecimal("500.00"));
    }
    static long prepared(int n,boolean batch)throws Exception{
        try(Connection c=DB.get()){
            c.setAutoCommit(false);
            try(PreparedStatement p=c.prepareStatement(SQL)){
                long start=System.nanoTime();
                for(int i=1;i<=n;i++){bind(p,i);if(batch)p.addBatch();else p.executeUpdate();
                    if(batch && i%1000==0)p.executeBatch();}
                if(batch)p.executeBatch(); c.commit(); return System.nanoTime()-start;
            }catch(Exception e){c.rollback();throw e;}
        }
    }
    static long statement(int n)throws Exception{
        try(Connection c=DB.get();Statement s=c.createStatement()){
            c.setAutoCommit(false);long start=System.nanoTime();
            for(int i=1;i<=n;i++){
                String q="INSERT INTO bus_pass VALUES('STMT"+i+"','REG"+i+"','Bench "+i+
                    "','R1','Stop','2027-03-31',500.00)";
                s.executeUpdate(q);
            }c.commit();return System.nanoTime()-start;
        }
    }
    public static void main(String[] args)throws Exception{
        for(int n:new int[]{1000,10000,100000}){
            System.out.printf("%d rows | Statement %.2f ms | Prepared %.2f ms | Batch %.2f ms%n",
                n,statement(n)/1e6,prepared(n,false)/1e6,prepared(n,true)/1e6);
        }
    }
}
