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
public class Truck extends SearchableImp{

    private static Map<String, Truck> trucks;
    private Logger LOG = LoggerFactory.getLogger(Truck.class);
    private String type;

    public Truck(String type){
        super();
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public void insert() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Truck(type) VALUES (?);");

            preparedStatement.setString(1, getType());
            preparedStatement.executeUpdate();

            getTrucks().put(getType(), this);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void delete() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Truck WHERE type = ?;");

            preparedStatement.setString(1, getType());
            preparedStatement.executeUpdate();

            getTrucks().remove(getType());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public static Map<String, Truck> getTrucks(){
        if (trucks == null){
            LoggerFactory.getLogger(Truck.class).info("Searching for Truck data...");
            trucks = new HashMap<>();
            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT type FROM Truck;");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String type = resultSet.getString("type");
                    trucks.put(type, new Truck(type));
                }

            } catch (SQLException e) {
                LoggerFactory.getLogger(Truck.class).error(e.getMessage());
            }
        }
        return trucks;
    }

    @Override
    public String toString(){
        return type;
    }

    public static void clearTable(){
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Truck;");
            preparedStatement.executeUpdate();
            getTrucks().clear();
        } catch (SQLException e) {
            LoggerFactory.getLogger(Truck.class).error(e.getMessage());
        }
    }
}
