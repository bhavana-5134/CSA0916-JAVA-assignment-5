import java.sql.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class MigrationTool {
    static final String SQL="INSERT INTO bus_pass(pass_id,register_no,student_name,route_no,boarding_stop,validity_date,amount) VALUES(?,?,?,?,?,?,?)";
    public static void main(String[] args)throws Exception{
        Path file=Path.of(args.length>0?args[0]:"data/bus_passes.csv");
        List<BusPass> valid=new ArrayList<>(); int lineNo=0;
        for(String line:Files.readAllLines(file,StandardCharsets.UTF_8)){
            lineNo++; try{valid.add(BusPass.fromCsv(line));}
            catch(Exception e){System.out.println("Rejected line "+lineNo+": "+e.getMessage()+" | "+line);}
        }
        try(Connection c=DB.get()){
            c.setAutoCommit(false);
            try(PreparedStatement p=c.prepareStatement(SQL)){
                int count=0;
                for(BusPass b:valid){
                    p.setString(1,b.passId());p.setString(2,b.registerNo());p.setString(3,b.name());
                    p.setString(4,b.routeNo());p.setString(5,b.boardingStop());
                    p.setDate(6,Date.valueOf(b.validityDate()));p.setBigDecimal(7,b.amount());
                    p.addBatch(); if(++count%1000==0)p.executeBatch();
                }
                p.executeBatch();c.commit();System.out.println("Migrated valid records: "+valid.size());
            }catch(Exception e){c.rollback();throw e;}
            DatabaseMetaData m=c.getMetaData();
            System.out.println("Database: "+m.getDatabaseProductName()+" "+m.getDatabaseProductVersion());
            System.out.println("JDBC driver: "+m.getDriverName()+" "+m.getDriverVersion());
            try(ResultSet r=m.getTables(c.getCatalog(),null,"%",new String[]{"TABLE"})){
                System.out.println("Tables:");
                while(r.next())System.out.println(" - "+r.getString("TABLE_NAME"));
            }
        }
    }
}
