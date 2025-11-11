package labjava.model;

import java.util.List;

// (Bạn cần import các model Reader, Librarian, DocumentReturn, FineDetail)

public class ReturnSlip {
    private int id;
    private double totalFine; // "tongPhat"
    private Reader reader;
    private Librarian librarian;
    private List<DocumentReturn> documentReturns;
    private List<FineDetail> fineDetails;

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getTotalFine() { return totalFine; }
    public void setTotalFine(double totalFine) { this.totalFine = totalFine; }

    public Reader getReader() { return reader; }
    public void setReader(Reader reader) { this.reader = reader; }

    public Librarian getLibrarian() { return librarian; }
    public void setLibrarian(Librarian librarian) { this.librarian = librarian; }

    public List<DocumentReturn> getDocumentReturns() { return documentReturns; }
    public void setDocumentReturns(List<DocumentReturn> documentReturns) {
        this.documentReturns = documentReturns;
    }

    public List<FineDetail> getFineDetails() { return fineDetails; }
    public void setFineDetails(List<FineDetail> fineDetails) {
        this.fineDetails = fineDetails;
    }
}