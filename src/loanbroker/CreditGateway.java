/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker;

import creditbureau.CreditReply;
import creditbureau.CreditRequest;
import creditbureau.CreditSerializer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import messaging.GatewayException;
import messaging.MessagingGateway;

/**
 *
 * @author Zef
 */
public abstract class CreditGateway
{

    private MessagingGateway gateway;
    private CreditSerializer serializer;
    
    public CreditGateway(String requestQueue, String replyQueue)
    {
        try
        {
            this.serializer = new CreditSerializer();
            this.gateway = new MessagingGateway(requestQueue, replyQueue);
            this.gateway.setListener(new MessageListener()
            {
                
                public void onMessage(Message msg)
                {
                    try
                    {
                        String body = ((TextMessage)msg).getText();
                        CreditReply reply = serializer.replyFromString(body);
                        onCreditReplyReceived(reply);
                    }
                    catch (JMSException ex)
                    {
                        Logger.getLogger(CreditGateway.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        catch (NamingException ex)
        {
            Logger.getLogger(CreditGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (JMSException ex)
        {
            Logger.getLogger(CreditGateway.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void start() 
            throws GatewayException
    {
        try
        {
            gateway.start();
        }
        catch (JMSException ex)
        {
            throw new GatewayException("An error has occured in starting the gateway.", ex);
        }
    }
    
    public void sendCreditRequest(CreditRequest request) 
            throws GatewayException
    {
        try
        {
            String body = serializer.requestToString(request);
            Message message = gateway.createMessage(body);
            gateway.sendMessage(message);
        }
        catch (JMSException ex)
        {
            throw new GatewayException("An error has occured in sending the message.", ex);
        }
    }
    
    public abstract void onCreditReplyReceived(CreditReply reply);
}
