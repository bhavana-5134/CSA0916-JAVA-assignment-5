public class StudentDaoDemo {
    public static void main(String[] args) throws Exception {
        StudentDao dao=new StudentDao();
        dao.insert(new Student(501,"Test Student","test501@example.com"));
        System.out.println("Found: "+dao.findById(501));
        System.out.println("All: "+dao.findAll());
    }
}
