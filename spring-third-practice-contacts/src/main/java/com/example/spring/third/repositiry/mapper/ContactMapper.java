package com.example.spring.third.repositiry.mapper;

import com.example.spring.third.model.Contact;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactMapper implements RowMapper<Contact> {

    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Contact(rs.getInt(Contact.Fields.id),
                rs.getString(Contact.Fields.firstName),
                rs.getString(Contact.Fields.lastName),
                rs.getString(Contact.Fields.email),
                rs.getString(Contact.Fields.phone));
    }
}
