package com.steven.pescheteau.control;

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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 28/09/2016.
 */
public class ImportControl implements ActionListener{

    Logger LOG = LoggerFactory.getLogger(ImportControl.class);
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

            List<String> years = new ArrayList<String>();
            Matcher m = Pattern.compile("(\\d{4})")
                    .matcher(selectedFile.getName());

            Object year;

            while (m.find())
                years.add(m.group());

            if (! years.isEmpty()){
                years.add("Other");

                year = JOptionPane.showInputDialog(null,
                        "Some years have been found in the document. Please select the year which\n" +
                                "correspond to the emission of these suppliers prices.",
                        "Year of the emission of suppliers prices",
                        JOptionPane.QUESTION_MESSAGE,
                        null, years.toArray(), years.get(0));

                if (year == null) return;

                if(year.equals("Other"))
                    do {
                        year = JOptionPane.showInputDialog(null,
                                "Please indicate the correct year which correspond to the emission\n"
                                       + " of these suppliers prices.",
                                "Year of the emission of suppliers prices",
                                JOptionPane.QUESTION_MESSAGE);
                        if (year == null) return;
                    }while(! Pattern.compile("^\\d{4}$").matcher((String) year).find());
            }
            else
                do {
                    year = JOptionPane.showInputDialog(null,
                            "Please indicate the correct year which correspond to the emission\n"
                                    + " of these suppliers prices.",
                            "Year of the emission of suppliers prices",
                            JOptionPane.QUESTION_MESSAGE);
                    if (year == null) return;
                }while(! Pattern.compile("^\\d{4}$").matcher((String) year).find());

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
            panel.add(new JLabel("Please wait, the import is underway..."), gbc);
            panel.add(svgCanvas, gbc);

            display.getContentPane().removeAll();
            display.getContentPane().add(panel);
            display.getImportSupplier().setEnabled(false);
            display.validate();
            display.repaint();

            final Object finalYear = year;
            SwingWorker sw = new SwingWorker(){
                protected Object doInBackground() {
                    //Import file with POI
                    try {

                        final Workbook workbook = WorkbookFactory.create(selectedFile);
                        final Sheet sheet = workbook.getSheet("Request for Quotation");
                        if (sheet == null){
                            JOptionPane.showMessageDialog(display,
                                    "Import have failed : \"Request for Quotation\" sheet "
                                            + "\ndoes not exist.",
                                    "Import error",
                                    JOptionPane.ERROR_MESSAGE);
                            return null;
                        }

                        int lastRowNum = sheet.getLastRowNum();
                        LOG.info("Import of " + lastRowNum + " rows succeded");

                        if ((lastRowNum - Settings.FIRST_LINE()) % Settings.ROWS_PER_SUPPLIER() != 0) {
                            JOptionPane.showMessageDialog(display,
                                    "Import have failed : the number of rows per supplier ("
                                            + Settings.ROWS_PER_SUPPLIER()
                                            + ")\ndoes not correspond to the document.",
                                    "Import error",
                                    JOptionPane.ERROR_MESSAGE);
                            return null;
                        }

                        // First iteration : we insert all new data from type trucks
                        Row row = sheet.getRow(4);
                        for (int i = 35; i <= 40; i++)
                            if (! Truck.getTrucks().containsKey(row.getCell(i).getStringCellValue())){
                                Truck truck = new Truck(row.getCell(i).getStringCellValue());
                                truck.insert();
                            }

                        // Now we can start with the biggest iteration
                        row = sheet.getRow(5);
                        Supplier currentSupplier = null;

                        do {
                            final Row finalRow = row;
                            row = sheet.getRow(finalRow.getRowNum() + 1);

                            // Supplier
                            if (finalRow.getCell(2) == null || finalRow.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK){
                                LOG.info("Supplier is missing for row " + (finalRow.getRowNum() + 1) + " : ignored.");
                                continue;
                            }
                            if (! Supplier.getSuppliers().containsKey(finalRow.getCell(2).getStringCellValue())){
                                Supplier supplier = new Supplier(finalRow.getCell(2).getStringCellValue());
                                supplier.insert();
                            }

                            // City
                            if (finalRow.getCell(3) == null || finalRow.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK){
                                LOG.info("City is missing for row " + (finalRow.getRowNum() + 1) + " : ignored.");
                                continue;
                            }
                            if (! City.getCities().containsKey(finalRow.getCell(3).getStringCellValue())){
                                City city = new City(finalRow.getCell(3).getStringCellValue());
                                city.insert();
                            }

                            // Country
                            if (finalRow.getCell(5) == null || finalRow.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK){
                                LOG.info("Country is missing for row " + (finalRow.getRowNum() + 1) + " : ignored.");
                                continue;
                            }
                            if (!Country.getCountries().containsKey(finalRow.getCell(5).getStringCellValue())){
                                Country country = new Country(finalRow.getCell(5).getStringCellValue());
                                country.insert();
                            }

                            // Zone
                            if (finalRow.getCell(6) == null || finalRow.getCell(6).getCellType() == Cell.CELL_TYPE_BLANK){
                                LOG.info("Zone is missing for row " + (finalRow.getRowNum() + 1) + " : ignored.");
                                continue;
                            }
                            final String zoneName;
                            if (finalRow.getCell(6).getCellType() == Cell.CELL_TYPE_NUMERIC)
                                zoneName = String.valueOf((int) finalRow.getCell(6).getNumericCellValue());
                            else
                                zoneName = finalRow.getCell(6).getStringCellValue();
                            if (! Zone.getZones().containsKey(zoneName)){
                                Zone zone = new Zone(zoneName);
                                zone.insert();
                            }

                            // Call DELETE Function if it's an update
                            if (currentSupplier == null || currentSupplier !=
                                   Supplier.getSuppliers().get(finalRow.getCell(2).getStringCellValue())){
                                currentSupplier = Supplier.getSuppliers().get(finalRow.getCell(2).getStringCellValue());
                                Road.deleteRoads(currentSupplier, Integer.parseInt((String) finalYear));
                            }


                            new Thread(){
                                @Override
                                public void run(){
                                    for (int i = 35; i <= 40; i++){
                                        if (finalRow.getCell(i) != null &&
                                                finalRow.getCell(i).getCellType() != Cell.CELL_TYPE_BLANK){

                                            if (finalRow.getCell(i).getCellType() != Cell.CELL_TYPE_NUMERIC){
                                                LOG.error("Price " + finalRow.getCell(i).getStringCellValue() + " at cell "
                                                        + column(i + 1) + (finalRow.getRowNum() + 1)
                                                        + " has been ignored.");
                                                continue;
                                            }
                                            // We have found correct prices !
                                            String startDate = finalRow.getCell(0).getStringCellValue();
                                            String expiryDate = finalRow.getCell(1).getStringCellValue();
                                            Supplier supplier = Supplier.getSuppliers().get(finalRow.getCell(2).getStringCellValue());
                                            City city = City.getCities().get(finalRow.getCell(3).getStringCellValue());
                                            String shipperName = finalRow.getCell(4).getStringCellValue();
                                            Country country = Country.getCountries().get(finalRow.getCell(5).getStringCellValue());
                                            Zone zone = Zone.getZones().get(zoneName);
                                            String currency = finalRow.getCell(7).getStringCellValue();
                                            double price = finalRow.getCell(i).getNumericCellValue();
                                            Truck truck = Truck.getTrucks().get(sheet.getRow(4).getCell(i).getStringCellValue());

                                            int numberTruck = -1;
                                            if (finalRow.getCell(i + 7) != null &&
                                                    finalRow.getCell(i + 7).getCellType() != Cell.CELL_TYPE_BLANK)
                                                numberTruck = (int) finalRow.getCell(i + 7).getNumericCellValue();

                                            // Now we can insert it !
                                            Road road = new Road(startDate, expiryDate, shipperName, city, supplier, currency,
                                                    country, zone, truck, price, numberTruck, Integer.parseInt((String) finalYear));
                                            road.insert();
                                        }
                                    }
                                }
                            }.start();

                        } while(row != null && row.getRowNum() <= lastRowNum);

                    } catch (Exception e) {
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
                    display.validate();
                    display.repaint();
                }
            };

            sw.execute();
        }
    }

    private String column(int n){
        String name = "";
        while (n > 0) {
            n--;
            name = (char)('A' + n % 26) + name;
            n /= 26;
        }
        return name;
    }
}
