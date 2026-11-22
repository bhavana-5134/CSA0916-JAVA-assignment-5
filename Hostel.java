import java.io.Serializable;
import java.util.*;
public class Hostel implements Serializable {
    private static final long serialVersionUID = 1L;
    String name; List<Room> rooms=new ArrayList<>();
    Hostel(String n){name=n;}
    void add(Room r){rooms.add(r);}
    public String toString(){return name+" "+rooms;}
}
