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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * A gateway that handles JMS messaging.
 *
 * @author Zef
 */
public class MessagingGateway
{

    private final String ACTIVEMQ_CONTEXTFACTORY = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";
    private final String PROVIDER_URL = "tcp://localhost:61616";

    private Session session;
    private Connection connection;

    private Destination consumerDestination;
    private Destination producerDestination;

    private MessageConsumer consumer;
    private MessageProducer producer;

    public MessagingGateway(String producerQueue, String consumerQueue)
            throws NamingException, JMSException
    {
        Properties props = createProperties();
        if (consumerQueue != null && !consumerQueue.equals(""))
        {
            props.put("queue." + consumerQueue, consumerQueue);
        }

        if (producerQueue != null && !producerQueue.equals(""))
        {
            props.put("queue." + producerQueue, producerQueue);
        }

        Context jdniContext = new InitialContext(props);
        ConnectionFactory connectionFactory = (ConnectionFactory) jdniContext.lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        consumerDestination = (Destination) jdniContext.lookup(consumerQueue);
        consumer = session.createConsumer(consumerDestination);

        if (producerQueue != null && !producerQueue.equals(""))
        {
            producerDestination = (Destination) jdniContext.lookup(producerQueue);
            producer = session.createProducer(producerDestination);
        }
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

    public void sendMessage(Message message)
            throws JMSException
    {
        producer.send(message);
    }

    public void sendMessage(Destination destination, Message message)
            throws JMSException
    {
        MessageProducer tempProducer = session.createProducer(destination);
        tempProducer.send(message);
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
