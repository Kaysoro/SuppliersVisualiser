package com.steven.pescheteau.control;

import com.steven.pescheteau.view.Display;
import com.steven.pescheteau.model.Connexion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by steve on 27/09/2016.
 */
public class CloseControl extends WindowAdapter implements ActionListener {

    private Display display;

    public CloseControl(Display display){
        super();
        this.display = display;
    }
    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        quit();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        quit();
    }

    public void quit(){
        Connexion.getInstance().close();
        display.dispose();
        System.exit(0);
    }
}
