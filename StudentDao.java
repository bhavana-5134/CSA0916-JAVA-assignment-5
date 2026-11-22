import java.sql.*;
import java.util.*;
public class StudentDao implements Dao<Student, Integer> {
    public Student insert(Student s) throws SQLException {
        String q = "INSERT INTO student(student_id,student_name,email) VALUES(?,?,?)";
        try (Connection c=DB.get(); PreparedStatement p=c.prepareStatement(q)) {
            p.setInt(1,s.studentId()); p.setString(2,s.name()); p.setString(3,s.email());
            p.executeUpdate(); return s;
        }
    }
    private Student map(ResultSet r) throws SQLException {
        return new Student(r.getInt("student_id"), r.getString("student_name"), r.getString("email"));
    }
    public Optional<Student> findById(Integer id) throws SQLException {
        try(Connection c=DB.get(); PreparedStatement p=c.prepareStatement(
                "SELECT * FROM student WHERE student_id=?")) {
            p.setInt(1,id); try(ResultSet r=p.executeQuery()) {
                return r.next()?Optional.of(map(r)):Optional.empty();
            }
        }
    }
    public List<Student> findAll() throws SQLException {
        List<Student> list=new ArrayList<>();
        try(Connection c=DB.get(); Statement s=c.createStatement();
            ResultSet r=s.executeQuery("SELECT * FROM student")) {
            while(r.next()) list.add(map(r));
        } return list;
    }
    public boolean update(Student s) throws SQLException {
        try(Connection c=DB.get(); PreparedStatement p=c.prepareStatement(
                "UPDATE student SET student_name=?,email=? WHERE student_id=?")) {
            p.setString(1,s.name()); p.setString(2,s.email()); p.setInt(3,s.studentId());
            return p.executeUpdate()==1;
        }
    }
    public boolean delete(Integer id) throws SQLException {
        try(Connection c=DB.get(); PreparedStatement p=c.prepareStatement(
                "DELETE FROM student WHERE student_id=?")) {
            p.setInt(1,id); return p.executeUpdate()==1;
        }
    }
}
