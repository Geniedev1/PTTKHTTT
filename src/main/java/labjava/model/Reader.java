// Reader.java
package labjava.model;

public class Reader extends Member {
    private int id;
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }


    public String getFullName() {
        if (this.getName() == null) {
            return "[Chưa có tên]";
        }
        Name n = this.getName();
        String s = (n.getSurname() != null) ? n.getSurname() : "";
        String m = (n.getMiddleName() != null) ? n.getMiddleName() : "";
        String g = (n.getGivenName() != null) ? n.getGivenName() : "";
        return (s + " " + m + " " + g).trim().replaceAll("\\s+", " ");
    }
}
