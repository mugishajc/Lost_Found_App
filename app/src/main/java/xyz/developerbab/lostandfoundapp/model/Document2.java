package xyz.developerbab.lostandfoundapp.model;

public class Document2 {
    String userphoto_url,username,status,userdate,docname,doccategory,docdesdcription,docdate,id;

    public Document2() {
    }

    public Document2(String userphoto_url, String username, String status, String userdate, String docname, String doccategory, String docdesdcription, String docdate, String id) {
        this.userphoto_url = userphoto_url;
        this.username = username;
        this.status = status;
        this.userdate = userdate;
        this.docname = docname;
        this.doccategory = doccategory;
        this.docdesdcription = docdesdcription;
        this.docdate = docdate;
        this.id = id;
    }

    public String getUserphoto_url() {
        return userphoto_url;
    }

    public void setUserphoto_url(String userphoto_url) {
        this.userphoto_url = userphoto_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserdate() {
        return userdate;
    }

    public void setUserdate(String userdate) {
        this.userdate = userdate;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDoccategory() {
        return doccategory;
    }

    public void setDoccategory(String doccategory) {
        this.doccategory = doccategory;
    }

    public String getDocdesdcription() {
        return docdesdcription;
    }

    public void setDocdesdcription(String docdesdcription) {
        this.docdesdcription = docdesdcription;
    }

    public String getDocdate() {
        return docdate;
    }

    public void setDocdate(String docdate) {
        this.docdate = docdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
