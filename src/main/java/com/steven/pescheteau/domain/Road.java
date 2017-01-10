package com.steven.pescheteau.domain;

import com.steven.pescheteau.model.Connexion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by steve on 01/10/2016.
 */
public class Road extends SearchableImp{

    Logger LOG = LoggerFactory.getLogger(Road.class);
    private String id;
    private String startDate;
    private String expiryDate;
    private String shipperName;
    private City shipperCity;
    private Supplier supplier;
    private String currency;
    private Country shipToCountry;
    private Zone shipToZone;
    private Truck truckType;
    private double price;
    private int numberTruck;

    public Road(String id, String startDate, String expiryDate, String shipperName, City shipperCity, Supplier supplier,
                String currency, Country shipToCountry, Zone shipToZone, Truck truckType, double price, int numberTruck) {
        super();
        this.id = id;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.shipperName = shipperName;
        this.shipperCity = shipperCity;
        this.supplier = supplier;
        this.currency = currency;
        this.shipToCountry = shipToCountry;
        this.shipToZone = shipToZone;
        this.truckType = truckType;
        this.price = price;
        this.numberTruck = numberTruck;
    }

    public Road(String startDate, String expiryDate, String carrier, City shipperCity, Supplier supplier, String currency,
                Country shipToCountry, Zone shipToZone, Truck truckType, double price, int numberTruck) {
       this(null, startDate, expiryDate, carrier, shipperCity, supplier, currency, shipToCountry, shipToZone, truckType,
               price, numberTruck);
    }

    public Road(){}

    @Override
    public void insert() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Road (id, startDate, "
                    + "expiryDate, currency, shipperName, price, numberTruck, supplier, "
                    + "shipperCity, shipToZone, shipToCountry, truckType) "
                    + "VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

            preparedStatement.setString(1, getStartDate());
            preparedStatement.setString(2, getExpiryDate());
            preparedStatement.setString(3, getCurrency());
            preparedStatement.setString(4, getShipperName());
            preparedStatement.setDouble(5, getPrice());
            preparedStatement.setInt(6, getNumberTruck());
            preparedStatement.setString(7, getSupplier().getName());
            preparedStatement.setString(8, getShipperCity().getName());
            preparedStatement.setString(9, getShipToZone().getName());
            preparedStatement.setString(10, getShipToCountry().getName());
            preparedStatement.setString(11, getTruckType().getType());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }

    }

    @Override
    public void delete() {
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement;

            if (id != null){
                preparedStatement = connection.prepareStatement("DELETE FROM Road WHERE id = ?;");
                preparedStatement.setString(1, getId());
            }
            else {
                preparedStatement = connection.prepareStatement("DELETE FROM Road "
                        + "WHERE shipperCity = ?"
                        + " AND shipToCountry = ?"
                        + " AND shipToZone = ?"
                        + " AND truckType = ?"
                        + " AND supplier = ?;");
                preparedStatement.setString(1, getShipperCity().getName());
                preparedStatement.setString(2, getShipToCountry().getName());
                preparedStatement.setString(3, getShipToZone().getName());
                preparedStatement.setString(4, getTruckType().getType());
                preparedStatement.setString(5, getSupplier().getName());
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
    }

    public static void deleteRoads(Supplier supplier){
        Connexion connexion = Connexion.getInstance();
        Connection connection = connexion.getConnection();

        try{
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("DELETE FROM Road "
                        + "WHERE supplier = ?;");
                preparedStatement.setString(1, supplier.getName());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LoggerFactory.getLogger(Road.class).error(e.getMessage());
        }
    }

    /** Parameters **/

    public static String getpSupplier(){
        return "supplier";
    }

    public static String getpStartDate() {
        return "startDate";
    }

    public static String getpExpiryDate() {
        return "expiryDate";
    }

    public static String getpShipperName() {
        return "shipperName";
    }

    public static String getpCurrency() {
        return "currency";
    }

    public static String getpShipperCity() {
        return "shipperCity";
    }

    public static String getpShipToCountry() {
        return "shipToCountry";
    }

    public static String getpShipToZone() {
        return "shipToZone";
    }

    public static String getpTruckType() {
        return "truckType";
    }

    public static String getpPrice() {
        return "price";
    }

    public static String getpNumberTruck() {
        return "numberTruck";
    }

    public static String getpId() {
        return "id";
    }

    /** Values **/

    public Supplier getSupplier(){
        return supplier;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getShipperName() {
        return shipperName;
    }

    public String getCurrency() {
        return currency;
    }

    public City getShipperCity() {
        return shipperCity;
    }

    public Country getShipToCountry() {
        return shipToCountry;
    }

    public Zone getShipToZone() {
        return shipToZone;
    }

    public Truck getTruckType() {
        return truckType;
    }

    public double getPrice() {
        return price;
    }

    public int getNumberTruck() {
        return numberTruck;
    }

    public String getId() {
        return id;
    }
}
