package xyz.developerbab.lostandfoundapp.model;

public class Document
 {
     String user,reference,category,name,description;

     public Document() {
     }

     public Document(String user, String reference, String category, String name, String description) {
         this.user = user;
         this.reference = reference;
         this.category = category;
         this.name = name;
         this.description = description;
     }

     public String getUser() {
         return user;
     }

     public void setUser(String user) {
         this.user = user;
     }

     public String getReference() {
         return reference;
     }

     public void setReference(String reference) {
         this.reference = reference;
     }

     public String getCategory() {
         return category;
     }

     public void setCategory(String category) {
         this.category = category;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getDescription() {
         return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }
 }
