package io.github.medioqrity;

public class Student {

    private String name, phone, id, qq, mail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Student(String _name, String _phone, String _id, String _qq, String _mail) {
        name = _name;
        phone = _phone;
        id = _id;
        qq = _qq;
        mail = _mail;
    }

    public String toString() {
        return "(" + id + ", '" + name + "', '" + phone + "', '" + (qq == null ? "null" : qq) + "', '" + 
                (mail == null ? "null" : mail) + "')";
        // return "name: " + name + ", phone: " + phone + ", id: " + id + ", qq: " + qq + ", mail: " + mail;
    }
}

