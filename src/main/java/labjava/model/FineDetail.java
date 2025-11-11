package labjava.model;

// (Bạn cần import model Fine)

public class FineDetail {
    private int id;
    private int quantity; // "soLuong"
    private Fine fine; // "loaiPhat"

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Fine getFine() { return fine; }
    public void setFine(Fine fine) { this.fine = fine; }
}