package org.feup.aiad.group08.behaviours;

import org.feup.aiad.group08.definitions.MessageType;

import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public abstract class ReceiveInformBehaviour extends CyclicBehaviour {

    private static final long serialVersionUID = 1650194326833481390L;
    private Agent agent;
    private MessageType messageType;

    public ReceiveInformBehaviour(Agent agent, MessageType messageType) {
        this.agent = agent;
        this.messageType = messageType;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchConversationId(messageType.toString()));

        ACLMessage msg = agent.receive(mt);

        if (msg != null)
            processMessage(msg);
        else
            block();
    }

    public abstract void processMessage(ACLMessage msg);
}
