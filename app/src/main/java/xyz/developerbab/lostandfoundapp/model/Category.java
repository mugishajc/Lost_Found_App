package xyz.developerbab.lostandfoundapp.model;

public class Category
{ String categoryname,xyz;

    public Category() {
    }

    public Category(String categoryname, String xyz) {
        this.categoryname = categoryname;
        this.xyz = xyz;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getXyz() {
        return xyz;
    }

    public void setXyz(String xyz) {
        this.xyz = xyz;
    }
}
