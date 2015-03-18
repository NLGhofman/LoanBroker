/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package creditbureau;

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
public abstract class LoanBrokerGateway
{    

    private MessagingGateway gateway;
    private CreditSerializer serializer;
    
    public LoanBrokerGateway(String requestQueue, String replyQueue)
            throws GatewayException
    {
        try
        {
            this.serializer = new CreditSerializer();
            this.gateway = new MessagingGateway(replyQueue, requestQueue);
            this.gateway.setListener(new MessageListener()
            {
                
                public void onMessage(Message msg)
                {
                    try
                    {
                        String body = ((TextMessage)msg).getText();
                        CreditRequest request = serializer.requestFromString(body);
                        onCreditRequestReceived(request);
                    }
                    catch (JMSException ex)
                    {
                        Logger.getLogger(LoanBrokerGateway.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        catch (NamingException ex)
        {
            throw new GatewayException("An error has occured in creating the gateway.", ex);
        }
        catch (JMSException ex)
        {
            throw new GatewayException("An error has occured in creating the gateway.", ex);
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
    
    public void sendCreditReply(CreditReply reply)
            throws GatewayException
    {
        try
        {
            String body = serializer.replyToString(reply);
            Message message = gateway.createMessage(body);
            gateway.sendMessage(message);
        }
        catch (JMSException ex)
        {
            throw new GatewayException("An error has occured in sending the message.", ex);
        }
    }
    
    public abstract void onCreditRequestReceived(CreditRequest request);
}
