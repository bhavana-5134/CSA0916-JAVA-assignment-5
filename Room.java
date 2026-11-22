import java.io.Serializable;
import java.util.*;
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    int roomNo; List<Resident> residents=new ArrayList<>();
    Room(int n){roomNo=n;}
    void add(Resident r){residents.add(r);}
    public String toString(){return "Room "+roomNo+" "+residents;}
}
