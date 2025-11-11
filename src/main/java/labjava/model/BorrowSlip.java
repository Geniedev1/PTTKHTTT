package labjava.model;

import java.util.Date;
import java.util.List;

public class BorrowSlip {

    private int id;
    private int readerId;
    private int librarianId;
    private String status; // Đổi từ "trangThai"
    private Date createdAt;

    // Danh sách các mục mượn con (theo sơ đồ UML)
    private List<DocumentBorrow> documentBorrows; // Đổi tên

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReaderId() { return readerId; }
    public void setReaderId(int readerId) { this.readerId = readerId; }

    public int getLibrarianId() { return librarianId; }
    public void setLibrarianId(int librarianId) { this.librarianId = librarianId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public List<DocumentBorrow> getDocumentBorrows() { return documentBorrows; }
    public void setDocumentBorrows(List<DocumentBorrow> documentBorrows) {
        this.documentBorrows = documentBorrows;
    }
}