/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * A gateway that handles JMS messaging.
 * @author Zef
 */
public class MessageGateway
{
    private final String ACTIVEMQ_CONTEXTFACTORY = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
    private final String PROVIDER_URL = "tcp://localhost:61616";

    private Session session;
    private Connection connection;
    
    private Destination consumerDestination;
    private MessageConsumer consumer;
    
    public MessageGateway(String consumerQueue) 
            throws NamingException, JMSException
    {
        Properties props = createProperties();
        props.put("queue." + consumerQueue, consumerQueue);
        
        Context jdniContext = new InitialContext(props);
        ConnectionFactory connectionFactory = (ConnectionFactory)jdniContext.lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        consumerDestination = (Destination)jdniContext.lookup(consumerQueue);
        consumer = session.createConsumer(consumerDestination);
    }
    
    public Destination getConsumerDestination()
    {
        return consumerDestination;
    }
    
    public void setListener(MessageListener listener) 
            throws JMSException
    {
        consumer.setMessageListener(listener);
    }
    
    public void start() 
            throws JMSException
    {
        connection.start();
    }
    
    public void sendMessage(String queueName, Message message) 
            throws NamingException, JMSException
    {
        Properties props = createProperties();
        props.put("queue." + queueName, queueName);
        Context jdniContext = new InitialContext(props);
        Destination destination = (Destination)jdniContext.lookup(queueName);
        MessageProducer producer = session.createProducer(destination);
        producer.send(message);
    }
    
    public Message createMessage(String body) 
            throws JMSException
    {
        return session.createTextMessage(body);
    }
    
    private Properties createProperties()
    {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, ACTIVEMQ_CONTEXTFACTORY);
        props.setProperty(Context.PROVIDER_URL, PROVIDER_URL);
        return props;
    }
}
