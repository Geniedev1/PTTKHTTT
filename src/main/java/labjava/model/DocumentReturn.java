package labjava.model;

import java.util.Date;

public class DocumentReturn {
    private int id;
    private Date returnedAt; // "ngayTra"
    private String documentBorrowId; // Khóa ngoại đến tbl_document_borrow

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getReturnedAt() { return returnedAt; }
    public void setReturnedAt(Date returnedAt) { this.returnedAt = returnedAt; }

    public String getDocumentBorrowId() { return documentBorrowId; }
    public void setDocumentBorrowId(String documentBorrowId) {
        this.documentBorrowId = documentBorrowId;
    }
}