package com.steven.pescheteau.view;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

/**
 * Created by steve on 11/10/2016.
 */
public class SettingsDialog  extends JDialog {

    private static final long serialVersionUID = 1L;
    private JFormattedTextField firstLineExcel;
    private JFormattedTextField numberRowSupplier;
    private JFormattedTextField numberRowResult;
    private JButton ok;
    private JButton cancel;

    public SettingsDialog() {
        super();
        setSize(new Dimension(400, 170));
        setLocationRelativeTo(null);
        setAlwaysOnTop(false);
        setResizable(false);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Suppliers Owens Corning - Settings");
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(1);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        firstLineExcel = new JFormattedTextField(formatter);
        numberRowSupplier = new JFormattedTextField(formatter);
        numberRowResult = new JFormattedTextField(formatter);

        JPanel datas = new JPanel(new GridLayout(3, 2, 5, 5));
        datas.add(new JLabel("First Row Data in XLSX file"));
        datas.add(firstLineExcel);
        datas.add(new JLabel("Row Number per Supplier in XLSX"));
        datas.add(numberRowSupplier);
        datas.add(new JLabel("Row Number for Results of Search"));
        datas.add(numberRowResult);

        ok = new JButton("       OK        ");
        cancel = new JButton("       Cancel        ");

        JPanel actions = new JPanel();
        actions.add(ok);
        actions.add(cancel);

        panel.add(datas, BorderLayout.CENTER);
        panel.add(actions, BorderLayout.SOUTH);
        getContentPane().add(panel);
    }

    public JFormattedTextField getFirstLineExcel() {
        return firstLineExcel;
    }

    public JFormattedTextField getNumberRowSupplier() {
        return numberRowSupplier;
    }

    public JFormattedTextField getNumberRowResult() {
        return numberRowResult;
    }

    public JButton getOk() {
        return ok;
    }

    public JButton getCancel() {
        return cancel;
    }
}
