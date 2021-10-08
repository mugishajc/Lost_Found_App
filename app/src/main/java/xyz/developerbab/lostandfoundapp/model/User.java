package xyz.developerbab.lostandfoundapp.model;

public class User {

    private String name;

    private String email;

    private String telephone;

    private String level;

    private String password;

    public User() {
    }

    public User(String name, String email, String telephone, String level, String password) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.level = level;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}