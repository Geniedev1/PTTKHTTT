package labjava.model;

public class  CopyOfDocument extends Document {
    private String status;
    private String codeCopy;
    public CopyOfDocument() {
        super();
    }
   public void setCodeCopy(String codeCopy) {
        this.codeCopy = codeCopy;
    }
    public String getCodeCopy(){
        return codeCopy;
    }
public void setStatus(String status) {

    this.status = status;}
    public String getStatus(){
        return status;
    }
}
