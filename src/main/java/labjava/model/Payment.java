package labjava.model;

import java.util.Date;

public class Payment {
    private int id;
    private String transactionCode;
    private String method;
    private String status;
    private Date paidAt;
    private int returnSlipId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTransactionCode() { return transactionCode; }
    public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getPaidAt() { return paidAt; }
    public void setPaidAt(Date paidAt) { this.paidAt = paidAt; }

    public int getReturnSlipId() { return returnSlipId; }
    public void setReturnSlipId(int returnSlipId) { this.returnSlipId = returnSlipId; }
}