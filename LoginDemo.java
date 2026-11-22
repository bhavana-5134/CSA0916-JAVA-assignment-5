import java.sql.*;
public class LoginDemo {
    static boolean unsafe(String u,String p) throws SQLException {
        try(Connection c=DB.get(); Statement s=c.createStatement()) {
            String sql="SELECT * FROM app_user WHERE username='"+u+"' AND password_text='"+p+"'";
            try(ResultSet r=s.executeQuery(sql)){return r.next();}
        }
    }
    static boolean safe(String u,String p) throws SQLException {
        try(Connection c=DB.get(); PreparedStatement s=c.prepareStatement(
                "SELECT * FROM app_user WHERE username=? AND password_text=?")) {
            s.setString(1,u); s.setString(2,p);
            try(ResultSet r=s.executeQuery()){return r.next();}
        }
    }
    public static void main(String[] args) throws Exception {
        String attack="' OR '1'='1";
        System.out.println("Unsafe accepts crafted input: "+unsafe(attack,"x"));
        System.out.println("PreparedStatement accepts same input: "+safe(attack,"x"));
    }
}
