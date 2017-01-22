package com.steven.pescheteau.domain;

import com.steven.pescheteau.model.Connexion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 30/09/2016.
 */
public class Country extends SearchableImp {

    private static Map<String, Country> countries;
    private Logger LOG = LoggerFactory.getLogger(Country.class);
    private String name;

    public Country(String name){
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void insert() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Country(name) VALUES (?);");

            preparedStatement.setString(1, getName());
            preparedStatement.executeUpdate();

            getCountries().put(getName(), this);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void delete() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Country WHERE name = ?;");

            preparedStatement.setString(1, getName());
            preparedStatement.executeUpdate();

            getCountries().remove(getName());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public static Map<String, Country> getCountries(){
        if (countries == null){
            LoggerFactory.getLogger(Country.class).info("Searching for Country data...");
            countries = new HashMap<>();
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM Country;");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String nom = resultSet.getString("name");
                    countries.put(nom, new Country(nom));
                }

            } catch (SQLException e) {
                LoggerFactory.getLogger(Country.class).error(e.getMessage());
            }
        }
        return countries;
    }

    @Override
    public String toString(){
        return name;
    }

    public static void clearTable(){
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Country;");
            preparedStatement.executeUpdate();
            getCountries().clear();
        } catch (SQLException e) {
            LoggerFactory.getLogger(Country.class).error(e.getMessage());
        }
    }
}
