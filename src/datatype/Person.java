package datatype;

public class Person {
    private Coordinates coordinatePair;
    private String label;
    private int id;
    private long lastSeen;
    public int index;

    public Person() {
    }


    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Coordinates getCoordinatePair() {
        return coordinatePair;
    }

    public void setCoordinatePair(Coordinates coordinatePair) {
        this.coordinatePair = coordinatePair;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
    
    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
}
