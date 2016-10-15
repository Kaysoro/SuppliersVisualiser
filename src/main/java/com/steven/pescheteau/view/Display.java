package com.steven.pescheteau.view;

import com.steven.pescheteau.control.AboutControl;
import com.steven.pescheteau.control.CloseControl;
import com.steven.pescheteau.control.ImportControl;
import com.steven.pescheteau.control.SettingsControl;

import javax.swing.*;
import java.awt.*;

/**
 * Created by steve on 27/09/2016.
 */
public class Display extends JFrame {

    private JMenuItem importSupplier;
    private JMenuItem quit;
    private JMenuItem settings;
    private JMenuItem about;
    private PanelSearch panelSearch;

    public Display(){
        super("Suppliers Owens Corning");

        // basic parameter window
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(new Dimension(800, 500));
        setResizable(true);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(this.getClass().getResource("/com/steven/pescheteau/images/rankings.png")).getImage());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException e) {}


        // Menu
        JMenu menuFile = new JMenu("   File   ");
        importSupplier =  new JMenuItem("Import Prices Supplier");
        importSupplier.setAccelerator(KeyStroke.getKeyStroke("control I"));
        importSupplier.setToolTipText("Import an XLSX file provided by a supplier");

        quit =  new JMenuItem("Quit");
        quit.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        quit.setToolTipText("Close the application");

        JMenu menuOther = new JMenu("   ?   ");
        settings =  new JMenuItem("Settings");
        settings.setAccelerator(KeyStroke.getKeyStroke("control P"));
        settings.setToolTipText("Configure this application");

        about =  new JMenuItem("About");
        about.setAccelerator(KeyStroke.getKeyStroke("control A"));
        about.setToolTipText("Some informations about this application");

        menuFile.add(importSupplier);
        menuFile.add(quit);

        menuOther.add(settings);
        menuOther.add(about);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuOther);

        setJMenuBar(menuBar);

        JTabbedPane tab = new JTabbedPane();
        panelSearch = new PanelSearch(tab);
        tab.addTab("New Search", panelSearch);
        tab.setTabComponentAt(0, new ButtonTabComponent(tab, false));
        getContentPane().add(tab);

        // Adding some controls
        CloseControl closeCtrl = new CloseControl(this);
        importSupplier.addActionListener(new ImportControl(this, tab));
        quit.addActionListener(closeCtrl);

        settings.addActionListener(new SettingsControl());
        about.addActionListener(new AboutControl());
        this.addWindowListener(closeCtrl);

        setVisible(true);
    }

    public JMenuItem getImportSupplier() {
        return importSupplier;
    }

    public JMenuItem getQuit() {
        return quit;
    }

    public JMenuItem getSettings() {
        return settings;
    }

    public JMenuItem getAbout() {
        return about;
    }

    public void majPanelSearch(){
        panelSearch.majData();
    }
}
