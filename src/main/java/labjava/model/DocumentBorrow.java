package labjava.model;

import java.util.Date;

/**
 * Đây là một lớp DTO/ViewModel, không phải là một Entity.
 * Nó đại diện cho 1 hàng trong bảng "Danh sách tài liệu mượn".
 */
public class DocumentBorrow extends Document {

    private int borrowId;
    private String copyCode;
    private String title;
    private Date dueDate;
    private String overdueStatus;
    private double provisionalFine;
    public DocumentBorrow() {
    }

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