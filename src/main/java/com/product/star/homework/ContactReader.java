package com.product.star.homework;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ContactReader {
    public List<Contact> readFromFile(Path filePath) {
        List<Contact> contacts = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split(",");
                if (parts.length != 3) continue;

                String[] nameParts = parts[0].trim().split(" ", 2);
                if (nameParts.length < 2) continue;

                String name = nameParts[0];
                String surname = nameParts[1];
                String phone = parts[1].trim();
                String email = parts[2].trim();

                contacts.add(new Contact(name, surname, phone, email));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read contacts from CSV", e);
        }
        return contacts;
    }
}
