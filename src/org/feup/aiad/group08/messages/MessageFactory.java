package org.feup.aiad.group08.messages;

import java.io.IOException;
import java.io.Serializable;

import org.feup.aiad.group08.data.AgentStatus;
import org.feup.aiad.group08.definitions.ItemPurchaseReceipt;
import org.feup.aiad.group08.definitions.MessageType;
import org.feup.aiad.group08.definitions.SalesInfo;
import org.feup.aiad.group08.definitions.StockPurchaseConditions;
import org.feup.aiad.group08.definitions.StockPurchaseReceipt;

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

    public static ACLMessage purchaseStock(AID receiver, int quantity) {
        return createMessage(MessageType.PURCHASE_STOCK, ACLMessage.REQUEST, quantity, receiver);
    }

    public static ACLMessage purchaseStockReply(ACLMessage from, StockPurchaseReceipt spr) {
        return createReply(from, ACLMessage.INFORM, spr);
    }

    public static ACLMessage confirmStockPurchase(AID receiver) {
        return createMessage(MessageType.CONFIRM_STOCK_PURCHASE, ACLMessage.REQUEST, receiver);
    }

    public static ACLMessage confirmStockPurchaseReply(ACLMessage from) {
        return createReply(from, ACLMessage.INFORM);
    }

    public static ACLMessage authorizeItemPurchase(AID... receivers) {
        return createMessage(MessageType.AUTHORIZE_ITEM_PURCHASE, ACLMessage.REQUEST, receivers);
    }

    public static ACLMessage authorizeItemPurchaseReply(ACLMessage from) {
        return createReply(from, ACLMessage.INFORM);
    }

    public static ACLMessage storeSalesInfo(AID... receivers) {
        return createMessage(MessageType.STORE_SALES_INFO, ACLMessage.REQUEST, receivers);
    }

    public static ACLMessage storeSalesInfoReply(ACLMessage from, Serializable content) {
        return createReply(from, ACLMessage.INFORM, content);
    }

    public static ACLMessage requestStockPurchaseConditions(AID warehouse) {
        return createMessage(MessageType.REQUEST_STOCK_PURCHASE_CONDITIONS, ACLMessage.REQUEST, warehouse);
    }

    public static ACLMessage requestStockPurchaseConditionsReply(ACLMessage from, StockPurchaseConditions spc) {
        return createReply(from, ACLMessage.INFORM, spc);
    }

    public static ACLMessage purchaseItem(SalesInfo bestItem) {
        return createMessage(MessageType.PURCHASE_ITEM, ACLMessage.REQUEST, bestItem, bestItem.getStoreName());
    }

    public static ACLMessage purchaseItemReply(ACLMessage from, ItemPurchaseReceipt ipr) {
        return createReply(from, ACLMessage.INFORM, ipr);
    }

    public static ACLMessage agentStatus(AID... receivers) {
        return createMessage(MessageType.AGENT_STATUS, ACLMessage.REQUEST, receivers);
    }

    public static ACLMessage agentStatusReply(ACLMessage from, AgentStatus status) {
        return createReply(from, ACLMessage.INFORM, status);
    }
}
