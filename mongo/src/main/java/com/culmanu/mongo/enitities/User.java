package com.culmanu.mongo.enitities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private int age;
    private String email;
    private Address address;
    private Date createdAt;
    private List<String> tags;
    private double balance;
    private String managerId; // For graphLookup example

    @Data
    public static class Address {
        private String city;
        private String country;
    }
}
