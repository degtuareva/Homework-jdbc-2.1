package com.product.star.homework;

public class Contact {
    private Long id;
    private final String name;
    private final String surname;
    private final String phone;
    private final String email;

    public Contact(Long id, String name, String surname, String phone, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }

    public Contact(String name, String surname, String phone, String email) {
        this(null, name, surname, phone, email);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}
