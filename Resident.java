import java.io.Serializable;
import java.sql.Connection;
public class Resident implements Serializable {
    private static final long serialVersionUID = 1L;
    String name, registerNo;
    transient String password;
    transient Connection connection;
    public Resident(String n,String r,String p){name=n;registerNo=r;password=p;}
    public String toString(){return name+" ("+registerNo+"), password restored="+(password!=null);}
}
