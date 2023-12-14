package com.contacts_practiceOne.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"email"})
@Builder
public class Contact {

    private String fullName;
    private String phoneNumber;
    private String email;

    @Override
    public String toString() {
        return fullName + " | " + phoneNumber + " | " + email;
    }

}
