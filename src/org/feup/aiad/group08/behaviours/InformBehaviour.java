package org.feup.aiad.group08.behaviours;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.feup.aiad.group08.definitions.MessageType;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class InformBehaviour extends OneShotBehaviour {

    private static final long serialVersionUID = 6931650648741910411L;
    private Agent agent;
    private MessageType messageType;
    private Serializable content;
    protected List<AID> receivers;

    public InformBehaviour(Agent agent, MessageType messageType, AID... receivers) {
        this.agent = agent;
        this.messageType = messageType;
        this.receivers = Arrays.asList(receivers);
    }

    public InformBehaviour(Agent agent, MessageType messageType, Serializable content, AID... receivers) {
        this(agent, messageType, receivers);
        this.content = content;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setConversationId(messageType.toString());

        if (content != null) {
            try {
                msg.setContentObject(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (AID r : receivers)
            msg.addReceiver(r);

        agent.send(msg);
    }
    
}
