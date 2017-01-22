package com.steven.pescheteau.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by steve on 28/09/2016.
 */
public class ExportControl implements ActionListener{

    private Logger LOG = LoggerFactory.getLogger(ExportControl.class);

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO
        JOptionPane.showMessageDialog(null, "Excel exporting is under development.",
                "Excel Exporting", JOptionPane.INFORMATION_MESSAGE);
    }
}
