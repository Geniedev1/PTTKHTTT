package labjava.model;

public class Address {
    private int id;
    private String houseNumber;
    private String building;
    private String streetNumber;
    private String district;
    private String ward;
    private String province;
    private String description;

    public Address() {}

    public Address(int id, String houseNumber, String building, String streetNumber,
                   String district, String ward, String province, String description) {
        this.id = id;
        this.houseNumber = houseNumber;
        this.building = building;
        this.streetNumber = streetNumber;
        this.district = district;
        this.ward = ward;
        this.province = province;
        this.description = description;
    }

    // getters/setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    public String getStreetNumber() { return streetNumber; }
    public void setStreetNumber(String streetNumber) { this.streetNumber = streetNumber; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getWard() { return ward; }
    public void setWard(String ward) { this.ward = ward; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
