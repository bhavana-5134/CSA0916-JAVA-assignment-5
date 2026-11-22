import java.io.*;
public class SerializationDemo {
    public static void main(String[] args) throws Exception {
        Hostel h=new Hostel("SIMATS Hostel"); Room r=new Room(101);
        r.add(new Resident("Bhavana","192521153","secret")); h.add(r);
        try(ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream("hostel.ser"))){o.writeObject(h);}
        try(ObjectInputStream i=new ObjectInputStream(new FileInputStream("hostel.ser"))){
            Hostel copy=(Hostel)i.readObject(); System.out.println(copy);
        }
    }
}
