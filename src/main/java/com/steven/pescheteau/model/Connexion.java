package com.steven.pescheteau.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteConfig;

/**
 * Created by steve on 27/09/2016.
 */

public class Connexion {
    private final static Logger LOG = LoggerFactory.getLogger(Connexion.class);
    private static Connexion instance = null;
    private Connection connection = null;
    private Statement statement = null;

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + Settings.DATABASE);

            statement = connection.createStatement();
            LOG.info("Connexion to " + Settings.DATABASE + " : success");

            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection("jdbc:sqlite:" + Settings.DATABASE,config.toProperties());

        } catch (ClassNotFoundException e) {
            LOG.error("SQLite library not found.");
        } catch (SQLException e) {
            LOG.error("Error while connecting to database");
        }
    }

    public void close() {
        try {
            connection.close();
            statement.close();
            LOG.info("Connexion close");
        } catch (SQLException e) {
            LOG.error("Error while closing connexion");
        }
    }

    public static Connexion getInstance(){
        if (instance == null) {
            instance = new Connexion();
        }
        return instance;
    }

    public ResultSet query(String requet) {
        ResultSet resultat = null;
        try {
            resultat = statement.executeQuery(requet);
        } catch (SQLException e) {
            LOG.error("Error in the request  : " + requet);
        }
        return resultat;
    }

    public Connection getConnection(){
        return connection;
    }
}