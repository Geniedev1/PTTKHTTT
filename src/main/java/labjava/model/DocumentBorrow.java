package labjava.model;

import java.util.Date;

/**
 * Đây là một lớp DTO/ViewModel, không phải là một Entity.
 * Nó đại diện cho 1 hàng trong bảng "Danh sách tài liệu mượn".
 */
public class DocumentBorrow extends Document {

    // Thông tin cơ bản
    private int borrowId;       // id của tbl_document_borrow (để xử lý trả)
    private String copyCode;      // Mã tài liệu (từ tbl_copy_of_document)
    private String title;         // Tên tài liệu (từ tbl_document)
    private Date dueDate;         // Hạn trả (từ tbl_document_borrow)

    // Thông tin tính toán (logic)
    private String overdueStatus;   // "Quá hạn" / "Đúng hạn"
    private double provisionalFine; // Phí tạm tính (vnd)
    // Constructors
    public DocumentBorrow() {
    }

    // Getters và Setters
    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String getCopyCode() {
        return copyCode;
    }

    public void setCopyCode(String copyCode) {
        this.copyCode = copyCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(String overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    public double getProvisionalFine() {
        return provisionalFine;
    }

    public void setProvisionalFine(double provisionalFine) {
        this.provisionalFine = provisionalFine;
    }
}