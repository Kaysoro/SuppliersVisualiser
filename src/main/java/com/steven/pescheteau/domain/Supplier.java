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
public class Supplier extends SearchableImp{

    public static Map<String, Supplier> suppliers;
    private Logger LOG = LoggerFactory.getLogger(Supplier.class);
    private String name;

    public Supplier(String name){
        super();
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public void insert() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Supplier(name) VALUES (?);");

            preparedStatement.setString(1, getName());
            preparedStatement.executeUpdate();

            getSuppliers().put(getName(), this);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public void delete() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Supplier WHERE name = ?;");

            preparedStatement.setString(1, getName());
            preparedStatement.executeUpdate();

            getSuppliers().remove(getName());
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public static Map<String, Supplier> getSuppliers(){
        if (suppliers == null){
            suppliers = new HashMap<String, Supplier>();

            Connexion connexion = Connexion.getInstance();
            Connection connection = connexion.getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM Supplier;");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String nom = resultSet.getString("name");
                    suppliers.put(nom, new Supplier(nom));
                }

            } catch (SQLException e) {
                LoggerFactory.getLogger(Supplier.class).error(e.getMessage());
            }
        }
        return suppliers;
    }

    @Override
    public String toString(){
        return name;
    }
}
