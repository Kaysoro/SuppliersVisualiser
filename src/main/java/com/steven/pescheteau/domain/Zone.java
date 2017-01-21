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
public class Zone extends SearchableImp{

    public static Map<String, Zone> zones;
    private Logger LOG = LoggerFactory.getLogger(Zone.class);
    private String name;

    public Zone(String name){
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
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Zone(name) VALUES (?);");

            preparedStatement.setString(1, getName());
            preparedStatement.executeUpdate();

            getZones().put(getName(), this);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void delete() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Zone WHERE name = ?;");

            preparedStatement.setString(1, getName());
            preparedStatement.executeUpdate();

            getZones().remove(getName());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public static Map<String, Zone> getZones(){
        if (zones == null){
            LoggerFactory.getLogger(Zone.class).info("Searching for Zone data...");
            zones = new HashMap<String, Zone>();
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM Zone;");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String nom = resultSet.getString("name");
                    zones.put(nom, new Zone(nom));
                }

            } catch (SQLException e) {
                LoggerFactory.getLogger(Zone.class).error(e.getMessage());
            }
        }
        return zones;
    }

    @Override
    public String toString(){
        return name;
    }

    public static void clearTable(){
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Zone;");
            preparedStatement.executeUpdate();
            getZones().clear();
        } catch (SQLException e) {
            LoggerFactory.getLogger(Zone.class).error(e.getMessage());
        }
    }
}
