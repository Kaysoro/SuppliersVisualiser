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

    private Logger LOG = LoggerFactory.getLogger(SearchControl.class);
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
                .param(Road.getpId())
                .param(Road.getpStartDate()).param(Road.getpExpiryDate())
                .param(Road.getpCurrency()).param(Road.getpShipperName())
                .param(Road.getpPrice()).param(Road.getpNumberTruck())
                .param(Road.getpSupplier()).param(Road.getpShipperCity())
                .param(Road.getpShipToZone()).param(Road.getpShipToCountry())
                .param(Road.getpTruckType())
        .from()
                .table(road);


        final boolean citySelected = ! panel.getShipperCity().getSelectedValuesList().isEmpty();
        final boolean citySingleSelected = panel.getShipperCity().getSelectedValuesList().size() == 1;
        if (citySelected){
            List<Object> cities = panel.getShipperCity().getSelectedValuesList();

            if (citySingleSelected)
                title.append(cities.get(0));
            else
                title.append("Cities");
            title.append(" to ");

            sql.where().parenthese();
            sql.param(Road.getpShipperCity()).equals()
                    .value((String) cities.get(0), SQL.STRING);
            cities.remove(0);

            for(Object city : cities)
                sql.or().param(Road.getpShipperCity()).equals()
                        .value((String) city, SQL.STRING);
            sql.parenthese();
        }
        else
            title.append("Cities to ");

        final boolean countrySelected = ! panel.getShipToCountry().getSelectedValuesList().isEmpty();
        final boolean countrySingleSelected = panel.getShipToCountry().getSelectedValuesList().size() == 1;
        if (countrySelected){
            List<Object> countries = panel.getShipToCountry().getSelectedValuesList();

            if (countrySingleSelected)
                title.append(countries.get(0));
            else
                title.append("Countries");

            sql.where().parenthese();
            sql.param(Road.getpShipToCountry()).equals()
                    .value((String) countries.get(0), SQL.STRING);
            countries.remove(0);

            for(Object country : countries)
                sql.or().param(Road.getpShipToCountry()).equals()
                        .value((String) country, SQL.STRING);
            sql.parenthese();
        }
        else
            title.append("Countries");

        final boolean zoneSelected = ! panel.getShipToZone().getSelectedValuesList().isEmpty();
        final boolean zoneSingleSelected = panel.getShipToZone().getSelectedValuesList().size() == 1;
        if (zoneSelected){
            List<Object> zones = panel.getShipToZone().getSelectedValuesList();

            if (zoneSingleSelected)
                title.append("(").append(zones.get(0)).append(")");

            sql.where().parenthese();
            sql.param(Road.getpShipToZone()).equals()
                    .value((String) zones.get(0), SQL.STRING);
            zones.remove(0);

            for(Object zone : zones)
                sql.or().param(Road.getpShipToZone()).equals()
                        .value((String) zone, SQL.STRING);
            sql.parenthese();
        }

        final boolean truckSelected = ! panel.getTruckType().getSelectedValuesList().isEmpty();
        final boolean truckSingleSelected = panel.getTruckType().getSelectedValuesList().size() == 1;
        if (truckSelected){
            List<Object> trucks = panel.getTruckType().getSelectedValuesList();

            if (truckSingleSelected)
                title.append(" by ").append(trucks.get(0));

            sql.where().parenthese();
            sql.param(Road.getpTruckType()).equals()
                    .value((String) trucks.get(0), SQL.STRING);
            trucks.remove(0);

            for(Object truck : trucks)
                sql.or().param(Road.getpTruckType()).equals()
                        .value((String) truck, SQL.STRING);
            sql.parenthese();
        }

        final boolean supplierSelected = ! panel.getSupplier().getSelectedValuesList().isEmpty();
        final boolean supplierSingleSelected = panel.getSupplier().getSelectedValuesList().size() == 1;
        if (supplierSelected){
            List<Object> suppliers = panel.getSupplier().getSelectedValuesList();

            if (supplierSingleSelected)
                title.append(" for ").append(suppliers.get(0));

            sql.where().parenthese();
            sql.param(Road.getpSupplier()).equals()
                    .value((String) suppliers.get(0), SQL.STRING);
            suppliers.remove(0);

            for(Object supplier : suppliers)
                sql.or().param(Road.getpSupplier()).equals()
                        .value((String) supplier, SQL.STRING);
            sql.parenthese();
        }

        sql.orderBy().param(Road.getpPrice()).asc()
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
                List<Road> results = new ArrayList<>();

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
                        ));
                    }

                } catch (SQLException e) {
                    LOG.error(e.getMessage());
                }

                RoadTable table = new RoadTable(results);
                panelResults.getTable().setModel(table);
                panelResults.getTable().setRowSorter(new TableRowSorter<TableModel>(table));

                // We remove all the columns not useful
                if (supplierSelected && supplierSingleSelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.SUPPLIER()));
                if (citySelected && citySingleSelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.CITY()));
                if (countrySelected && countrySingleSelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.COUNTRY()));
                if (zoneSelected && zoneSingleSelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.ZONE()));
                if (truckSelected && truckSingleSelected)
                    panelResults.getTable().removeColumn(panelResults.getTable().getColumn(RoadTable.TRUCK()));

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
        panel.majData();
    }
}