package server.handlers;

public class UserHandler {

    private final int Id;
    private final String Name;

    public UserHandler(int Id, String Name) {
        this.Id = Id;
        this.Name = Name;
    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }
}
