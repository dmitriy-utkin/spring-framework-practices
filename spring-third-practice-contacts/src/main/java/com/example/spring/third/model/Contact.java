package com.example.spring.third.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class Contact implements Comparable<Contact> {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @Override
    public int compareTo(Contact otherContact) {
        return Integer.compare(this.id, otherContact.id);
    }
}
