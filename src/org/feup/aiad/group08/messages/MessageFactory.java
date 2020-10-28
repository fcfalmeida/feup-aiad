package org.feup.aiad.group08.messages;

import java.io.IOException;
import java.io.Serializable;

import org.feup.aiad.group08.definitions.MessageType;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class MessageFactory {

    private static ACLMessage createMessage(MessageType type, int performative, AID... receivers) {
        ACLMessage message = new ACLMessage(performative);
        message.setConversationId(type.toString());

        for (AID r : receivers)
            message.addReceiver(r);

        return message;
    }

    private static ACLMessage createMessage(MessageType type, int performative, Serializable content,
            AID... receivers) {
        ACLMessage message = createMessage(type, performative, receivers);

        try {
            message.setContentObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }

    private static ACLMessage createReply(ACLMessage from, int performative) {
        MessageType type = MessageType.fromString(from.getConversationId());

        return createMessage(type, performative);
    }

    private static ACLMessage createReply(ACLMessage from, int performative, Serializable content) {
        MessageType type = MessageType.fromString(from.getConversationId());

        return createMessage(type, performative, content);
    }
    
    public static ACLMessage authorizeStockPurchase(AID... receivers) {
        return createMessage(MessageType.AUTHORIZE_STOCK_PURCHASE, ACLMessage.REQUEST, receivers);
    }

    public static ACLMessage authorizeStockPurchaseReply(ACLMessage from) {
        return createReply(from, ACLMessage.INFORM);
    }

    public static ACLMessage purchaseStock(AID receiver) {
        return createMessage(MessageType.PURCHASE_STOCK, ACLMessage.REQUEST, receiver);
    }

    public static ACLMessage purchaseStockReply(ACLMessage from) {
        return createReply(from, ACLMessage.INFORM);
    }

    public static ACLMessage confirmStockPurchase(AID receiver) {
        return createMessage(MessageType.CONFIRM_STOCK_PURCHASE, ACLMessage.REQUEST, receiver);
    }

    public static ACLMessage confirmStockPurchaseReply(ACLMessage from) {
        return createReply(from, ACLMessage.INFORM);
    }
}
