package com.steven.pescheteau.control;

import com.steven.pescheteau.domain.*;
import com.steven.pescheteau.model.Connexion;
import com.steven.pescheteau.model.Settings;
import com.steven.pescheteau.model.RoadTable;
import com.steven.pescheteau.model.SQL;
import com.steven.pescheteau.view.ButtonTabComponent;
import com.steven.pescheteau.view.PanelResults;
import com.steven.pescheteau.view.PanelSearch;
import org.apache.batik.swing.JSVGCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 28/09/2016.
 */
public class SearchControl implements ActionListener {

    Logger LOG = LoggerFactory.getLogger(SearchControl.class);
    private PanelSearch panel;

    public SearchControl(PanelSearch panel){
        super();
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Create name for this tab and build the SQL request
        StringBuilder title = new StringBuilder();
        final SQL sql = new SQL();
        Road road = new Road();
        sql.select()
                .param(road.getpId())
                .param(road.getpStartDate()).param(road.getpExpiryDate())
                .param(road.getpCurrency()).param(road.getpShipperName())
                .param(road.getpPrice()).param(road.getpNumberTruck())
                .param(road.getpSupplier()).param(road.getpShipperCity())
                .param(road.getpShipToZone()).param(road.getpShipToCountry())
                .param(road.getpTruckType()).param(road.getpYear())
        .from()
                .table(road);
        final boolean citySelected = ! panel.getShipperCity().getSelectedItem().equals("All");
        if (citySelected){
            title.append(panel.getShipperCity().getSelectedItem()).append(" to ");
            sql.where().param(road.getpShipperCity()).equals()
                    .value((String) panel.getShipperCity().getSelectedItem(), SQL.STRING);
        }
        else
            title.append("Cities to ");

        final boolean countrySelected = ! panel.getShipToCountry().getSelectedItem().equals("All");
        if (countrySelected){
            title.append(panel.getShipToCountry().getSelectedItem());
            sql.where().param(road.getpShipToCountry()).equals()
                    .value((String) panel.getShipToCountry().getSelectedItem(), SQL.STRING);
        }
        else
            title.append("Countries");

        final boolean zoneSelected = ! panel.getShipToZone().getSelectedItem().equals("All");
        if (zoneSelected){
            title.append("(").append(panel.getShipToZone().getSelectedItem()).append(")");
            sql.where().param(road.getpShipToZone()).equals()
                    .value((String) panel.getShipToZone().getSelectedItem(), SQL.STRING);
        }

        final boolean truckSelected = ! panel.getTruckType().getSelectedItem().equals("All");
        if (truckSelected){
            title.append(" by ").append(panel.getTruckType().getSelectedItem());
            sql.where().param(road.getpTruckType()).equals()
                    .value((String) panel.getTruckType().getSelectedItem(), SQL.STRING);
        }

        final boolean supplierSelected = ! panel.getSupplier().getSelectedItem().equals("All");
        if (supplierSelected){
            title.append(" for ").append(panel.getSupplier().getSelectedItem());
            sql.where().param(road.getpSupplier()).equals()
                    .value((String) panel.getSupplier().getSelectedItem(), SQL.STRING);
        }

        final boolean yearSelected = ! panel.getYear().getSelectedItem().equals("All");
        if (yearSelected){
            title.append(" in ").append(panel.getYear().getSelectedItem());
            sql.where().param(road.getpYear()).equals()
                    .value((String) panel.getYear().getSelectedItem(), SQL.INT);
        }

        sql.orderBy().param(road.getpPrice()).asc()
                .limit(Settings.NUMBER_LINES_RESULT());

        LOG.debug(sql.toString());

        final int index = panel.getTab().getTabCount();
        // Display loading
        JSVGCanvas svgCanvas = new JSVGCanvas();
        svgCanvas.setURI(getClass().getResource("/com/steven/pescheteau/images/balls.svg").toString());
        svgCanvas.setDisableInteractions(true);
        svgCanvas.setPreferredSize(new Dimension(200, 200));
        svgCanvas.setBackground(new Color(0,0,0,0));

        final JPanel animation = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        animation.add(new JLabel("Please wait, the search is underway..."), gbc);
        animation.add(svgCanvas, gbc);

        panel.getTab().addTab(title.toString(), animation);
        panel.getTab().setTabComponentAt(index, new ButtonTabComponent(panel.getTab(), true));
        panel.getTab().setSelectedIndex(index);

        // New Panel for response
        final PanelResults panelResults = new PanelResults();

        SwingWorker sw = new SwingWorker(){
            protected Object doInBackground() {
                List<Road> results = new ArrayList<Road>();

                Connexion connexion = Connexion.getInstance();
                Connection connection = connexion.getConnection();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        results.add(new Road(
                                resultSet.getString(Road.getpStartDate())
                                , resultSet.getString(Road.getpExpiryDate())
                                , resultSet.getString(Road.getpShipperName())
                                , City.getCities().get(resultSet.getString(Road.getpShipperCity()))
                                , Supplier.getSuppliers().get(resultSet.getString(Road.getpSupplier()))
                                , resultSet.getString(Road.getpCurrency())
                                , Country.getCountries().get(resultSet.getString(Road.getpShipToCountry()))
                                , Zone.getZones().get(resultSet.getString(Road.getpShipToZone()))
                                , Truck.getTrucks().get(resultSet.getString(Road.getpTruckType()))
                                , resultSet.getDouble(Road.getpPrice())
                                , resultSet.getInt(Road.getpNumberTruck())
                                , resultSet.getInt(Road.getpYear())
                        ));
                    }

                } catch (SQLException e) {
                    LOG.error(e.getMessage());
                }

                RoadTable table = new RoadTable(results);
                panelResults.getTable().setModel(table);
                panelResults.getTable().setRowSorter(new TableRowSorter<TableModel>(table));

                // We remove all the columns not useful
                if (supplierSelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.SUPPLIER()));
                if (citySelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.CITY()));
                if (countrySelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.COUNTRY()));
                if (zoneSelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.ZONE()));
                if (truckSelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.TRUCK()));
                if (yearSelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.YEAR()));

                return null;
            }

            public void done(){
                //Remove loading when the worker finished
                panel.getTab().setComponentAt(index, panelResults);
                panel.revalidate();
                panel.repaint();
            }
        };

        sw.execute();

        // Parameter choosed reinitialized.
        panel.getShipperCity().setSelectedIndex(0);
        panel.getShipToCountry().setSelectedIndex(0);
        panel.getShipToZone().setSelectedIndex(0);
        panel.getTruckType().setSelectedIndex(0);
        panel.getSupplier().setSelectedIndex(0);
        panel.getYear().setSelectedIndex(2);
    }
}