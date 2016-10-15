package com.steven.pescheteau.control;

import com.steven.pescheteau.model.Settings;
import com.steven.pescheteau.view.SettingsDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by steve on 11/10/2016.
 */
public class SettingsControl implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
        final SettingsDialog view = new SettingsDialog();
        view.getFirstLineExcel().setValue(Settings.FIRST_LINE());
        view.getNumberRowSupplier().setValue(Settings.ROWS_PER_SUPPLIER());
        view.getNumberRowResult().setValue(Settings.NUMBER_LINES_RESULT());

        view.getOk().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setFIRST_LINE((int) view.getFirstLineExcel().getValue());
                Settings.setROWS_PER_SUPPLIER((int) view.getNumberRowSupplier().getValue());
                Settings.setNUMBER_LINES_RESULT((int) view.getNumberRowResult().getValue());
                view.dispose();
            }
        });

        view.getCancel().addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });

        view.setVisible(true);
    }
}
