package com.steven.pescheteau.model;

import com.steven.pescheteau.domain.Road;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by steve on 10/10/2016.
 */
public class RoadTable extends AbstractTableModel{

    private List<Road> roads;
    public static final String[] header = {"#", "Start Date", "Expiry Date", "Shipper Name", "Currency", "Price",
            "#Trucks", "Spend", "Saving / Best price",
            "Carrier", "Shipper City", "Ship to Country", "Ship to Zone", "Truck's Type" // Optional
    };

    public RoadTable(List<Road> roads){
        super();
        this.roads = roads;
    }

    @Override
    public int getRowCount() {
        return roads.size();
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    public String getColumnName(int columnIndex) {
        return header[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Road road = roads.get(rowIndex);

        switch(columnIndex){
            case 0: // #
                return rowIndex + 1;
            case 1: // Start Date
                return road.getStartDate();
            case 2: // Expiry Date
                return road.getExpiryDate();
            case 3: // ShipperName
                return road.getShipperName();
            case 4: // Currency
                return road.getCurrency();
            case 5: // Price (round X.XX)
                return Math.round(road.getPrice() * 100) / 100;
            case 6: // Number truck
                return road.getNumberTruck() != -1 ? road.getNumberTruck() : " - ";
            case 7: // Spend (round X.XX)
                return road.getNumberTruck() != -1 ? Math.round(road.getNumberTruck() * road.getPrice() * 100) / 100 : " - ";
            case 8: // Saving / Best price (round X.X)
                double value = (Math.round((100 * road.getPrice() / roads.get(0).getPrice() - 100) * 100)) / 100d;
                return Math.round(value * 10) / 10 + "%";

            // Optional
            case 9: // Supplier
                return road.getSupplier();
            case 10: // City
                return road.getShipperCity();
            case 11: // Country
                return road.getShipToCountry();
            case 12: // Zone
                return road.getShipToZone();
            case 13: // Truck's type
                return road.getTruckType();

            default: //Should never arrive
                return null;
        }
    }

    @Override
    public Class getColumnClass(int column) {
        switch (column) {
            case 0: // #
                return Integer.class;
            case 1: // Start Date
                return String.class;
            case 2: // Expiry Date
                return String.class;
            case 3: // ShipperName
                return String.class;
            case 4: // Currency
                return String.class;
            case 5: // Price (round X.XX)
                return Double.class;
            case 6: // Number truck
                return Integer.class;
            case 7: // Spend (round X.XX)
                return Double.class;
            case 8: // Saving / Best price (round X.X)
                return String.class;

            // Optional
            case 9: // Supplier
                return String.class;
            case 10: // City
                return String.class;
            case 11: // Country
                return String.class;
            case 12: // Zone
                return String.class;
            case 13: // Truck's type
                return String.class;
            default:
                return String.class;
        }
    }

    public static String SUPPLIER(){
        return header[9];
    }

    public static String CITY(){
        return header[10];
    }

    public static String COUNTRY(){
        return header[11];
    }

    public static String ZONE(){
        return header[12];
    }

    public static String TRUCK(){
        return header[13];
    }
}
