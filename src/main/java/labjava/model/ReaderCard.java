package labjava.model;

import java.sql.Date;

public class ReaderCard {
    private int id;
    private Date createdAt;
    private Date expiredAt;
    private String status;

    public ReaderCard() {}

    public ReaderCard(int id, Date createdAt, Date expiredAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getExpiredAt() { return expiredAt; }
    public void setExpiredAt(Date expiredAt) { this.expiredAt = expiredAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
