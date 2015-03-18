/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import messaging.GatewayException;
import messaging.MessageGateway;

/**
 *
 * @author Zef
 */
public abstract class LoanBrokerGateway
{
    private MessageGateway messageGateway;
    private ClientSerializer serializer;
    private String requestQueue;
    
    public LoanBrokerGateway(String requestQueue, String replyQueue) 
            throws GatewayException
    {
        try
        {
            this.requestQueue = requestQueue;
            this.serializer = new ClientSerializer();
            this.messageGateway = new MessageGateway(replyQueue);
            this.messageGateway.setListener(new MessageListener() {

                public void onMessage(Message msg)
                {
                    try
                    {
                        String body = ((TextMessage)msg).getText();
                        ClientReply reply = serializer.replyFromString(body);
                        onClientReplyReceived(reply);
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
            messageGateway.start();
        }
        catch (JMSException ex)
        {
            throw new GatewayException("An error has occured in starting the gateway.", ex);
        }
    }
    
    public void sendClientRequest(ClientRequest request) 
            throws GatewayException
    {
        try
        {
            String body = serializer.requestToString(request);
            Message message = messageGateway.createMessage(body);
            messageGateway.sendMessage(requestQueue, message);
        }
        catch (JMSException ex)
        {
            throw new GatewayException("An error has occured in sending the message.", ex);
        }
        catch (NamingException ex)
        {
            throw new GatewayException("An error has occured in sending the message.", ex);
        }
        
    }
    
    public abstract void onClientReplyReceived(ClientReply reply);
}
