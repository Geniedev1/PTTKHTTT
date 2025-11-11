package labjava.model;

import java.util.Date;

public class Document {

    private int id; // ID của bản copy
    private String title; // "maBanSao"
    private String copyCondition; // "tinhTrang" (e.g., 'good', 'damaged', 'lost')
    private Date dateRelease;
    private String langegue;
    private String discription;
    // Mối quan hệ: Một bản copy thuộc về 1 Document

    // Constructors
    public Document() {
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateRelease(Date dateRelease) {
        this.dateRelease = dateRelease;
    }

    public void setLangegue(String langegue) {
        this.langegue = langegue;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateRelease() {
        return dateRelease;
    }

    public String getLangegue() {
        return langegue;
    }

    public String getDiscription() {
        return discription;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getCopyCondition() {
        return copyCondition;
    }

    public void setCopyCondition(String copyCondition) {
        this.copyCondition = copyCondition;
    }


}