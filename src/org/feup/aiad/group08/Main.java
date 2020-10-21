package org.feup.aiad.group08;

import org.feup.aiad.group08.agents.ManagerAgent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {

    public static void main(String[] args) throws StaleProxyException {
        Runtime rt = Runtime.instance();

        Profile p = new ProfileImpl();
        ContainerController container = rt.createAgentContainer(p);

        AgentController ac = container.acceptNewAgent("manager", new ManagerAgent(5));
        ac.start();
    }
}
