package com.steven.pescheteau.control;

import com.monitorjbl.xlsx.StreamingReader;
import com.steven.pescheteau.domain.*;
import com.steven.pescheteau.model.Settings;
import com.steven.pescheteau.view.Display;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 28/09/2016.
 */
public class ImportControl implements ActionListener{

    private Logger LOG = LoggerFactory.getLogger(ImportControl.class);
    private Display display;
    private JTabbedPane tab;

    public ImportControl(Display display, JTabbedPane tab) {
        super();
        this.display = display;
        this.tab = tab;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Classeur Excel (*.xlsx)", "xlsx"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){

            final File selectedFile = fileChooser.getSelectedFile();

            /*
            The application have to freeze for this step : in fact,
            the importation could false some search (incomplete results)
            */

            // Display loading
            JSVGCanvas svgCanvas = new JSVGCanvas();
            svgCanvas.setURI(getClass().getResource("/com/steven/pescheteau/images/balls.svg").toString());
            svgCanvas.setDisableInteractions(true);
            svgCanvas.setPreferredSize(new Dimension(200, 200));
            svgCanvas.setBackground(new Color(0,0,0,0));

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = GridBagConstraints.RELATIVE;
            final JLabel labelImport = new JLabel("Please wait, the import is underway...");
            panel.add(labelImport, gbc);
            panel.add(svgCanvas, gbc);

            display.getContentPane().removeAll();
            display.getContentPane().add(panel);
            display.getImportSupplier().setEnabled(false);
            display.getClear().setEnabled(false);
            display.validate();
            display.repaint();

            SwingWorker sw = new SwingWorker(){
                protected Object doInBackground() {
                    //Import file with Excel Streaming Reader
                    try (
                            Workbook workbook = StreamingReader.builder()
                                    .rowCacheSize(100)
                                    .bufferSize(4096)
                                    .open(selectedFile)) {
                        Sheet sheet = workbook.getSheet("Request for Quotation");

                        if (sheet == null){
                            JOptionPane.showMessageDialog(display,
                                    "Import have failed : \"Request for Quotation\" sheet "
                                            + "\ndoes not exist.",
                                    "Import error",
                                    JOptionPane.ERROR_MESSAGE);
                            return null;
                        }

                        int response =  JOptionPane.showConfirmDialog(null,
                                "Do you want to clear the database before importing ?",
                                "Clear Database", JOptionPane.YES_NO_OPTION);

                        if(response == JOptionPane.YES_OPTION){
                            City.clearTable();
                            Country.clearTable();
                            Road.clearTable();
                            Supplier.clearTable();
                            Truck.clearTable();
                            Zone.clearTable();
                        }

                        LOG.info("Opening Excel file has succeed.");

                        Map<Integer, String> trucksTable = new HashMap<>();
                        for (Row r : sheet) {
                            if (r.getRowNum() == (Settings.FIRST_LINE() - 2)){
                                // First iteration : we insert all new data from type trucks
                                for (int i = 35; i <= 40; i++) {
                                    if (!Truck.getTrucks().containsKey(r.getCell(i).getStringCellValue())) {
                                        Truck truck = new Truck(r.getCell(i).getStringCellValue());
                                        truck.insert();
                                    }
                                    trucksTable.put(i, r.getCell(i).getStringCellValue());
                                }
                            }
                            else if (r.getRowNum() >= (Settings.FIRST_LINE() - 1)) {
                                // Now we can start with the biggest iteration
                                if (r.getRowNum() > 1)
                                    labelImport.setText("Please wait, the import is underway... (" + r.getRowNum() + " lines processed)");
                                else
                                    labelImport.setText("Please wait, the import is underway... (" + r.getRowNum() + " line processed)");

                                boolean importError = false;

                                // Supplier
                                if (r.getCell(2) == null || r.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK
                                        || r.getCell(2).getCellType() == Cell.CELL_TYPE_ERROR) {
                                    LOG.info("Supplier is missing for row " + (r.getRowNum() + 1) + " : ignored.");
                                    importError = true;
                                } else if (!Supplier.getSuppliers().containsKey(r.getCell(2).getStringCellValue())) {
                                    Supplier supplier = new Supplier(r.getCell(2).getStringCellValue());
                                    supplier.insert();
                                }

                                // City
                                if (r.getCell(3) == null || r.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK
                                        || r.getCell(3).getCellType() == Cell.CELL_TYPE_ERROR) {
                                    LOG.info("City is missing for row " + (r.getRowNum() + 1) + " : ignored.");
                                    importError = true;
                                } else if (!City.getCities().containsKey(r.getCell(3).getStringCellValue())) {
                                    City city = new City(r.getCell(3).getStringCellValue());
                                    city.insert();
                                }

                                // Country
                                if (r.getCell(5) == null || r.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK
                                        || r.getCell(5).getCellType() == Cell.CELL_TYPE_ERROR) {
                                    LOG.info("Country is missing for row " + (r.getRowNum() + 1) + " : ignored.");
                                    importError = true;
                                } else if (!Country.getCountries().containsKey(r.getCell(5).getStringCellValue())) {
                                    Country country = new Country(r.getCell(5).getStringCellValue());
                                    country.insert();
                                }

                                // Zone
                                if (r.getCell(6) == null || r.getCell(6).getCellType() == Cell.CELL_TYPE_BLANK
                                        || r.getCell(6).getCellType() == Cell.CELL_TYPE_ERROR) {
                                    LOG.info("Zone is missing for row " + (r.getRowNum() + 1) + " : ignored.");
                                    importError = true;
                                } else if (!Zone.getZones().containsKey(r.getCell(6).getStringCellValue())) {
                                    Zone zone = new Zone(r.getCell(6).getStringCellValue());
                                    zone.insert();
                                }

                                if (!importError) {
                                    for (int i = 35; i <= 40; i++) {
                                        if (r.getCell(i) != null && r.getCell(i).getCellType() != Cell.CELL_TYPE_BLANK) {
                                            if (r.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC
                                                    && r.getCell(i + 7) != null
                                                    && r.getCell(i + 7).getCellType() == Cell.CELL_TYPE_NUMERIC
                                                    && r.getCell(i + 7).getNumericCellValue() != 0) {

                                                // We have found correct prices and trucks are precised !
                                                String startDate = r.getCell(0).getStringCellValue();
                                                String expiryDate = r.getCell(1).getStringCellValue();
                                                Supplier supplier = Supplier.getSuppliers().get(r.getCell(2).getStringCellValue());
                                                City city = City.getCities().get(r.getCell(3).getStringCellValue());
                                                String shipperName = r.getCell(4).getStringCellValue();
                                                Country country = Country.getCountries().get(r.getCell(5).getStringCellValue());
                                                Zone zone = Zone.getZones().get(r.getCell(6).getStringCellValue());
                                                String currency = r.getCell(7).getStringCellValue();
                                                double price = r.getCell(i).getNumericCellValue();
                                                Truck truck = Truck.getTrucks().get(trucksTable.get(i));
                                                int numberTruck = (int) r.getCell(i + 7).getNumericCellValue();

                                                // Now we can insert it !
                                                new Road(startDate, expiryDate, shipperName, city, supplier, currency,
                                                        country, zone, truck, price, numberTruck).insert();
                                            }
                                        } // if we have prices
                                    } // for
                                } // !importError
                            } // >= FIRST LINE
                        }
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                    }

                    display.majPanelSearch();
                    return null;
                }

                public void done(){
                    //Remove loading when the worker finished
                    display.getContentPane().removeAll();
                    display.getContentPane().add(tab);
                    display.getImportSupplier().setEnabled(true);
                    display.getClear().setEnabled(true);
                    display.validate();
                    display.repaint();
                }
            };

            sw.execute();
        }
    }
}
