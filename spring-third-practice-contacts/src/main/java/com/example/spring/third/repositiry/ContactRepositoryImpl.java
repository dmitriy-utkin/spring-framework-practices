package com.example.spring.third.repositiry;

import com.example.spring.third.error.ContactNotFoundError;
import com.example.spring.third.model.Contact;
import com.example.spring.third.repositiry.mapper.ContactMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ContactRepositoryImpl implements ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Contact> findAll() {
        log.debug("ContactRepositoryImpl -> findAll");

        String sql = "SELECT * FROM contact ORDER BY id";
        return jdbcTemplate.query(sql, new ContactMapper());
    }

    @Override
    public Optional<Contact> findById(int id) {
        log.debug("ContactRepositoryImpl -> findById");
        String sql = "SELECT * FROM contact WHERE id = ?";
        Contact contact = DataAccessUtils.singleResult(
                jdbcTemplate.query(sql,
                        new ArgumentPreparedStatementSetter(new Object[]{id}),
                        new RowMapperResultSetExtractor<>(new ContactMapper(), 1))
        );
        return Optional.ofNullable(contact);
    }

    @Override
    public void save(Contact contact) {
        log.debug("ContactRepositoryImpl -> save");
        contact.setId(maxId() + 1);
        String sql = "INSERT INTO contact (id, firstName, lastName, email, phone) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                contact.getId(),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getEmail(),
                contact.getPhone());
    }

    @Override
    public void update(Contact contact) {
        log.debug("ContactRepositoryImpl -> update");
        Contact oldContactVersion = findById(contact.getId()).orElse(null);
        if (oldContactVersion != null) {
            String sql = "UPDATE contact SET firstName = ?, lastName = ?, email = ?, phone = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    contact.getFirstName(),
                    contact.getLastName(),
                    contact.getEmail(),
                    contact.getPhone(),
                    contact.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        log.debug("ContactRepositoryImpl -> deleteById");
        String sql = "DELETE FROM contact WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void batchInsert(List<Contact> contacts) {
        log.debug("ContactRepositoryImpl -> batchInsert");
        String sql = "INSERT INTO contact (id, firstName, lastName, email, phone) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Contact contact = contacts.get(i);
                ps.setInt(1, contact.getId());
                ps.setString(2, contact.getFirstName());
                ps.setString(3, contact.getLastName());
                ps.setString(4, contact.getEmail());
                ps.setString(5, contact.getPhone());
            }

            @Override
            public int getBatchSize() {
                return contacts.size();
            }
        });

    }

    @Override
    public int count() {
        log.debug("ContactRepositoryImpl -> count");
        String sql = "SELECT COUNT(*) FROM contact";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count == null ? 0 : count;
    }

    @Override
    public int maxId() {
        log.debug("ContactRepositoryImpl -> maxId");
        String sql = "SELECT MAX(id) FROM contact";
        Integer maxId = jdbcTemplate.queryForObject(sql, Integer.class);
        return maxId == null ? 0 : maxId;
    }

    @Override
    public void deleteAll() {
        log.debug("ContactRepositoryImpl -> deleteAll");
        String sql = "DELETE FROM contact";
        jdbcTemplate.update(sql);
    }

    @Override
    public boolean existsById(int id) {
        log.debug("ContactRepositoryImpl -> existsById");
        return findById(id).isPresent();

    }


}
