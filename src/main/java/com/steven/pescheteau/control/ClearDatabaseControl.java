package com.steven.pescheteau.control;

import com.steven.pescheteau.domain.*;
import com.steven.pescheteau.view.AboutDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by steve on 28/09/2016.
 */
public class ClearDatabaseControl implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        int response =  JOptionPane.showConfirmDialog(null,
                "Are your sure you want to clear the database ?",
                "Clear Database", JOptionPane.YES_NO_OPTION);

        if(response == JOptionPane.YES_OPTION){
            City.clearTable();
            Country.clearTable();
            Road.clearTable();
            Supplier.clearTable();
            Truck.clearTable();
            Zone.clearTable();
        }
    }
}
