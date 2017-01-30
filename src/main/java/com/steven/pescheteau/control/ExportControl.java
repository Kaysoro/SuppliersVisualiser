package com.steven.pescheteau.control;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by steve on 28/09/2016.
 */
public class ExportControl implements ActionListener{

    private Logger LOG = LoggerFactory.getLogger(ExportControl.class);
    private String title;
    private JTable table;

    public ExportControl(String title, JTable table){
        super();
        this.title = title;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export data results");
        fileChooser.setSelectedFile(new File("Results.xlsx"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Classeur Excel (*.xlsx)", "xlsx"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(title);

            // Header
            Row header = sheet.createRow(0);
            for(int column = 0; column < table.getColumnCount(); column++)
                if (table.convertColumnIndexToView(column) != -1)
                    header.createCell(column).setCellValue(table.getColumnName(column));

            // Data
            for (int rowCount = 1; rowCount <= table.getRowCount(); rowCount++) {
                Row row = sheet.createRow(rowCount);

                for (int columnCount = 0; columnCount < table.getColumnCount(); columnCount++) {
                    Object value = table.getValueAt(rowCount - 1, columnCount);
                    if (value instanceof Double)
                        row.createCell(columnCount).setCellValue((Double) value);
                    else if (value instanceof Integer)
                        row.createCell(columnCount).setCellValue((Integer) value);
                    else if (value instanceof String)
                        row.createCell(columnCount).setCellValue((String) value);
                    else
                        row.createCell(columnCount).setCellValue(value.toString());
                }
            }

            String path = fileToSave.getAbsolutePath();
            if (! path.endsWith(".xlsx")) path += ".xlsx";
            try (FileOutputStream outputStream = new FileOutputStream(path)) {
                workbook.write(outputStream);
                JOptionPane.showMessageDialog(null, "Excel exporting succeeded.",
                        "Excel Exporting", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "Excel exporting failed.",
                        "Excel Exporting", JOptionPane.INFORMATION_MESSAGE);
                LOG.error(e1.getMessage());
            }
        }
    }
}
