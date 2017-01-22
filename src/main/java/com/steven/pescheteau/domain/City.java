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
public class City extends SearchableImp{

    private static Map<String, City> cities;
    private Logger LOG = LoggerFactory.getLogger(City.class);
    private String name;

    public City(String name){
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
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO City(name) VALUES (?);");

            preparedStatement.setString(1, getName());
            preparedStatement.executeUpdate();

            getCities().put(getName(), this);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void delete() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM City WHERE name = ?;");

            preparedStatement.setString(1, getName());
            preparedStatement.executeUpdate();

            getCities().remove(getName());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public static Map<String, City> getCities(){
        if (cities == null){
            LoggerFactory.getLogger(City.class).info("Searching for City data...");
            cities = new HashMap<>();
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM City;");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String nom = resultSet.getString("name");
                    cities.put(nom, new City(nom));
                }

            } catch (SQLException e) {
                LoggerFactory.getLogger(City.class).error(e.getMessage());
            }
        }
        return cities;
    }

    @Override
    public String toString(){
        return name;
    }

    public static void clearTable(){
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM City;");
            preparedStatement.executeUpdate();
            getCities().clear();
        } catch (SQLException e) {
            LoggerFactory.getLogger(City.class).error(e.getMessage());
        }
    }
}
