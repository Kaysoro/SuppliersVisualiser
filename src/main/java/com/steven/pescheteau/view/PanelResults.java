package com.steven.pescheteau.view;

import com.steven.pescheteau.control.ExportControl;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Created by steve on 03/10/2016.
 */
public class PanelResults extends JPanel {

    private JTable table;
    private JLabel totalTruck;
    private JLabel totalSpend;

    public PanelResults(){
        super(new BorderLayout());
        table = new JTable(){
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        table.setBackground(new Color(240, 240, 240));
        table.getTableHeader().setReorderingAllowed(true);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel total = new JPanel();
        total.setPreferredSize(new Dimension(1, 25));
        total.add(new JLabel("TOTAL Truck : "));
        totalTruck = new JLabel("0");
        totalTruck.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        total.add(totalTruck);
        total.add(new JLabel("TOTAL Spend : "));
        totalSpend = new JLabel("0");
        totalSpend.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        total.add(totalSpend);
        JButton exportExcel = new JButton(new ImageIcon(getClass().getResource("/com/steven/pescheteau/images/excel.png")));
        exportExcel.setToolTipText("Excel Exporting");
        exportExcel.setPreferredSize(new Dimension(20, 20));
        exportExcel.setFocusPainted(false);
        exportExcel.addActionListener(new ExportControl());
        total.add(exportExcel);
        add(total, BorderLayout.SOUTH);
    }

    public JTable getTable(){
        return table;
    }

    public JLabel getTotalTruck() {
        return totalTruck;
    }

    public JLabel getTotalSpend() {
        return totalSpend;
    }
}
