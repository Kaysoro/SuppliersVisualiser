package com.steven.pescheteau.view;

import com.steven.pescheteau.control.DataCharger;
import com.steven.pescheteau.control.SearchControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Created by steve on 28/09/2016.
 */

public class PanelSearch extends JPanel {

    Logger LOG = LoggerFactory.getLogger(PanelSearch.class);
    private JTabbedPane tab;
    private JComboBox<Object> shipperCity;
    private JComboBox<Object> shipToCountry;
    private JComboBox<Object> shipToZone;
    private JComboBox<Object> truckType;
    private JComboBox<Object> supplier;
    private JComboBox<Object> year;

    public PanelSearch(JTabbedPane tab) {
        super();
        this.tab = tab;

        // Recherches possibles
        shipperCity = new JComboBox<Object>();
        shipToCountry = new JComboBox<Object>();
        shipToZone = new JComboBox<Object>();
        truckType = new JComboBox<Object>();
        supplier = new JComboBox<Object>();
        year = new JComboBox<Object>();
        majData();

        shipperCity.setPreferredSize(new Dimension(200, 25));
        shipToCountry.setPreferredSize(new Dimension(200, 25));
        shipToZone.setPreferredSize(new Dimension(200, 25));
        truckType.setPreferredSize(new Dimension(200, 25));
        supplier.setPreferredSize(new Dimension(200, 25));
        year.setPreferredSize(new Dimension(200, 25));

        JLabel label = new JLabel("Shipper City");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        JPanel panel = new JPanel();
        add(panel);
        panel.add(label);
        panel.add(shipperCity);
        label = new JLabel("Ship to Country");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        add(panel);
        panel.add(label);
        panel.add(shipToCountry);
        label = new JLabel("Ship to Zone");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        add(panel);
        panel.add(label);
        panel.add(shipToZone);
        label = new JLabel("Truck's Type");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        add(panel);
        panel.add(label);
        panel.add(truckType);
        label = new JLabel("Supplier");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        add(panel);
        panel.add(label);
        panel.add(supplier);
        label = new JLabel("Year");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        add(panel);
        panel.add(label);
        panel.add(year);

        JButton button = new JButton("Search");
        button.setPreferredSize(new Dimension(250, 30));
        button.addActionListener(new SearchControl(this));

        add(button);
    }

    public JTabbedPane getTab() {
        return tab;
    }

    public JComboBox<Object> getShipperCity() {
        return shipperCity;
    }

    public JComboBox<Object> getShipToZone() {
        return shipToZone;
    }

    public JComboBox<Object> getShipToCountry() {
        return shipToCountry;
    }

    public JComboBox<Object> getTruckType() {
        return truckType;
    }

    public JComboBox<Object> getSupplier() {
        return supplier;
    }

    public JComboBox<Object> getYear() {
        return year;
    }

    public void majData() {
        shipperCity.setModel(new DefaultComboBoxModel(DataCharger.getCities()));
        shipToCountry.setModel(new DefaultComboBoxModel(DataCharger.getCountries()));
        shipToZone.setModel(new DefaultComboBoxModel(DataCharger.getZones()));
        truckType.setModel(new DefaultComboBoxModel(DataCharger.getTrucks()));
        supplier.setModel(new DefaultComboBoxModel(DataCharger.getSuppliers()));
        year.setModel(new DefaultComboBoxModel(DataCharger.getYears()));
        year.setSelectedIndex(2);
    }
}
