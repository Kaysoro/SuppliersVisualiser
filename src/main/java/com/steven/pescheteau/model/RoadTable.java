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
            "Carrier", "Shipper City", "Ship to Country", "Ship to Zone", "Truck's Type", "Year" // Optional
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
            case 5: // Price
                return road.getPrice();
            case 6: // Number truck
                return road.getNumberTruck() != -1 ? road.getNumberTruck() : " - ";
            case 7: // Spend
                return road.getNumberTruck() != -1 ? road.getNumberTruck() * road.getPrice() : " - ";
            case 8: // Saving / Best price
                return (Math.round((100 * road.getPrice() / roads.get(0).getPrice() - 100) * 100)) / 100d + "%";

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
            case 14: // Year
                return road.getYear();

            default: //Should never arrive
                return null;
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

    public static String YEAR(){
        return header[14];
    }
}
