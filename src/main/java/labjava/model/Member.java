// Member.java
package labjava.model;

public abstract class Member {
    private String username;
    private String email;
    private String birthDate;
    private String phone;
    private Name name;       // có thể null
    private Address address; // có thể null

    // getters/setters chung
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Name getName() { return name; }
    public void setName(Name name) { this.name = name; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
}
