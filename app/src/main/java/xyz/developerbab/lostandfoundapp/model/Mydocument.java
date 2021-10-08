package xyz.developerbab.lostandfoundapp.model;

public class Mydocument {

    String names,categorynames,description,status,id,reference,date;

    public Mydocument() {
    }

    public Mydocument(String names, String categorynames, String description, String status, String id, String reference, String date) {
        this.names = names;
        this.categorynames = categorynames;
        this.description = description;
        this.status = status;
        this.id = id;
        this.reference = reference;
        this.date = date;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getCategorynames() {
        return categorynames;
    }

    public void setCategorynames(String categorynames) {
        this.categorynames = categorynames;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
