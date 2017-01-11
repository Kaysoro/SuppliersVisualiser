package com.steven.pescheteau;

import com.steven.pescheteau.view.Display;
import com.steven.pescheteau.model.Connexion;

/**
 * Created by steve on 27/09/2016.
 */
public class Main {

    public static void main(String[] args) {
        // TODO que faire si database doesn't exist ?
        Connexion.getInstance().connect();
        new Display();
    }
}
