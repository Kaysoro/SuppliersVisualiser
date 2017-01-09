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
    private JList<Object> shipperCity;
    private JList<Object> shipToCountry;
    private JList<Object> shipToZone;
    private JList<Object> truckType;
    private JList<Object> supplier;
    private JList<Object> year;

    public PanelSearch(JTabbedPane tab) {
        super(new BorderLayout());
        this.tab = tab;

        // Recherches possibles
        shipperCity = new JList<Object>();
        shipToCountry = new JList<Object>();
        shipToZone = new JList<Object>();
        truckType = new JList<Object>();
        supplier = new JList<Object>();
        year = new JList<Object>();

        shipperCity.setLayoutOrientation(JList.VERTICAL);
        shipToCountry.setLayoutOrientation(JList.VERTICAL);
        shipToZone.setLayoutOrientation(JList.VERTICAL);
        truckType.setLayoutOrientation(JList.VERTICAL);
        supplier.setLayoutOrientation(JList.VERTICAL);
        year.setLayoutOrientation(JList.VERTICAL);

        JScrollPane shipperCityScroll = new JScrollPane(shipperCity);
        JScrollPane shipToCountryScroll = new JScrollPane(shipToCountry);
        JScrollPane shipToZoneScroll = new JScrollPane(shipToZone);
        JScrollPane truckTypeScroll = new JScrollPane(truckType);
        JScrollPane supplierScroll = new JScrollPane(supplier);
        JScrollPane yearScroll = new JScrollPane(year);

        shipperCityScroll.setPreferredSize(new Dimension(200, 100));
        shipToCountryScroll.setPreferredSize(new Dimension(200, 100));
        shipToZoneScroll.setPreferredSize(new Dimension(200, 100));
        truckTypeScroll.setPreferredSize(new Dimension(200, 100));
        supplierScroll.setPreferredSize(new Dimension(200, 100));
        yearScroll.setPreferredSize(new Dimension(200, 100));

        JPanel panelSearch = new JPanel();
        JLabel label = new JLabel("Shipper City");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        JPanel panel = new JPanel();
        panelSearch.add(panel);
        panel.add(label);
        panel.add(shipperCityScroll);
        label = new JLabel("Ship to Country");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        panelSearch.add(panel);
        panel.add(label);
        panel.add(shipToCountryScroll);
        label = new JLabel("Ship to Zone");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        panelSearch.add(panel);
        panel.add(label);
        panel.add(shipToZoneScroll);
        label = new JLabel("Truck's Type");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        panelSearch.add(panel);
        panel.add(label);
        panel.add(truckTypeScroll);
        label = new JLabel("Supplier");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        panelSearch.add(panel);
        panel.add(label);
        panel.add(supplierScroll);
        label = new JLabel("Year");
        label.setPreferredSize(new Dimension(100, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel = new JPanel();
        panelSearch.add(panel);
        panel.add(label);
        panel.add(yearScroll);

        JButton button = new JButton("Search");
        button.setPreferredSize(new Dimension(250, 30));
        button.addActionListener(new SearchControl(this));

        panelSearch.add(button);

        add(panelSearch, BorderLayout.CENTER);
        JLabel tips = new JLabel("<html>Tips : <i>Hold down the CTRL key to select multiple lines per list</i></html>");
        tips.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        tips.setHorizontalAlignment(JLabel.CENTER);
        add(tips, BorderLayout.SOUTH);

        majData();
    }

    public JTabbedPane getTab() {
        return tab;
    }

    public JList<Object> getShipperCity() {
        return shipperCity;
    }

    public JList<Object> getShipToZone() {
        return shipToZone;
    }

    public JList<Object> getShipToCountry() {
        return shipToCountry;
    }

    public JList<Object> getTruckType() {
        return truckType;
    }

    public JList<Object> getSupplier() {
        return supplier;
    }

    public JList<Object> getYear() {
        return year;
    }

    public void majData() {
        shipperCity.setListData(DataCharger.getCities());
        shipToCountry.setListData(DataCharger.getCountries());
        shipToZone.setListData(DataCharger.getZones());
        truckType.setListData(DataCharger.getTrucks());
        supplier.setListData(DataCharger.getSuppliers());
        year.setListData(DataCharger.getYears());

        shipperCity.setSelectedIndices(new int[] { });
        shipToCountry.setSelectedIndices(new int[] { });
        shipToZone.setSelectedIndices(new int[] { });
        truckType.setSelectedIndices(new int[] { });
        supplier.setSelectedIndices(new int[] { });
        year.setSelectedIndices(new int[] { 1 });
    }
}
