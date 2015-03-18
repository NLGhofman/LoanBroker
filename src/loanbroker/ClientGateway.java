/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker;

import client.ClientReply;
import client.ClientRequest;
import client.ClientSerializer;
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
public abstract class ClientGateway
{

    private MessagingGateway gateway;
    private ClientSerializer serializer;

    public ClientGateway(String requestQueue, String replyQueue)
            throws GatewayException
    {
        try
        {
            this.serializer = new ClientSerializer();
            this.gateway = new MessagingGateway(replyQueue, requestQueue);
            this.gateway.setListener(new MessageListener()
            {

                public void onMessage(Message msg)
                {
                    try
                    {
                        String body = ((TextMessage) msg).getText();
                        ClientRequest request = serializer.requestFromString(body);
                        onClientRequestReceived(request);
                    }
                    catch (JMSException ex)
                    {
                        Logger.getLogger(ClientGateway.class.getName()).log(Level.SEVERE, null, ex);
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

    public void sendClientReply(ClientReply reply)
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

    public abstract void onClientRequestReceived(ClientRequest request);
}
