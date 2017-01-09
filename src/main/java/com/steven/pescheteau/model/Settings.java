package com.steven.pescheteau.model;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by steve on 27/09/2016.
 */
public class Settings {

    public static final String DATABASE = "bdd.sqlite";
    public static final String VERSION = "1.1";
    public static final String AUTHOR = "Steven PESCHETEAU";

    private static Settings instance = null;

    private int FIRST_LINE = 6;
    private int NUMBER_LINES_RESULT = 5;

    private Settings(){
        super();
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT firstLine, numberLinesResult FROM Settings;");
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            FIRST_LINE = resultSet.getInt("firstLine");
            NUMBER_LINES_RESULT = resultSet.getInt("numberLinesResult");

        } catch (SQLException e) {
            LoggerFactory.getLogger(Settings.class).error(e.getMessage());
        }
    }

    public static Settings getInstance(){
        if (instance == null)
            instance = new Settings();
        return instance;
    }

    public static int FIRST_LINE(){
        return getInstance().FIRST_LINE;
    }

    public static int NUMBER_LINES_RESULT(){
        return getInstance().NUMBER_LINES_RESULT;
    }


    public static void setFIRST_LINE(int FIRST_LINE) {
        getInstance().FIRST_LINE = FIRST_LINE;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Settings SET firstLine = ?;");
            preparedStatement.setInt(1, getInstance().FIRST_LINE);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LoggerFactory.getLogger(Settings.class).error(e.getMessage());
        }
    }

    public static void setNUMBER_LINES_RESULT(int NUMBER_LINES_RESULT) {
        getInstance().NUMBER_LINES_RESULT = NUMBER_LINES_RESULT;

        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Settings SET numberLinesResult = ?;");
            preparedStatement.setInt(1, getInstance().NUMBER_LINES_RESULT);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LoggerFactory.getLogger(Settings.class).error(e.getMessage());
        }
    }
}
