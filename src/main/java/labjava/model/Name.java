package labjava.model;

public class Name {
    private int id;
    private String givenName;   // tên
    private String surname;     // họ
    private String middleName;  // tên đệm

    public Name() {}

    public Name(int id, String givenName, String surname, String middleName) {
        this.id = id;
        this.givenName = givenName;
        this.surname = surname;
        this.middleName = middleName;
    }

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getGivenName() { return givenName; }
    public void setGivenName(String givenName) { this.givenName = givenName; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }
}
