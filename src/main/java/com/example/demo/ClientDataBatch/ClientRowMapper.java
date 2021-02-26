package com.example.demo.ClientDataBatch;

import com.example.demo.DomainModel.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ClientRowMapper object implementing RowMapper interface and use JdbcTemplate object methods to make database
 * operations while using ClientMapper object
 */
public class ClientRowMapper implements RowMapper<Person> {

    /**
     * Read Cursor for a Client from resultSet and create a Client object with that data
     *
     * @param resultSet set with data for a client from the database
     * @param i         currentRow
     * @return Client
     * @throws SQLException
     */
    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("id"));
        person.setFamilyName(resultSet.getString("family_Name"));
        person.setGivenName(resultSet.getString("given_Name"));
        person.setDateOfExpiryPassport(resultSet.getDate("date_Of_Expiry_Passport"));
        person.setSocialNumber(resultSet.getString("social_Number"));
        return person;
    }
}