package org.feup.aiad.group08;

import org.feup.aiad.group08.simulation.MASShoppingLauncher;

import jade.wrapper.StaleProxyException;
import uchicago.src.sim.engine.SimInit;

public class Main {

    public static void main(String[] args) throws StaleProxyException {
        SimInit init = new SimInit();
		init.setNumRuns(1);   // works only in batch mode
		init.loadModel(new MASShoppingLauncher(), null, true);
    }
}
