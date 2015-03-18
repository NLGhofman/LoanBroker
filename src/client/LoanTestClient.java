package client;

import client.gui.ClientFrame;
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

/**
 * This class represents one Client Application.
 * It:
 *  1. Creates a ClientRequest for a loan.
 *  2. Sends it to the LoanBroker Messaging-Oriented Middleware (MOM).
 *  3. Receives the reply from the LoanBroker MOM.
 * 
 */
public class LoanTestClient {

    /*
     * Connection to JMS
     */
    private final Connection connection; // to connect to the JMS
    protected Session session; // session for making messages, producers and consumers

    private final MessageProducer producer; // for sending messages
    private final MessageConsumer consumer; // for receiving messages

    private final ClientSerializer serializer; // for serializing ClientRequest and ClientReply to/from XML
    
    private ClientFrame frame; // GUI

    public LoanTestClient(String name, String requestQueue, String replyQueue) throws Exception {
        super();
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
        props.put( ( "queue." + requestQueue ) ,requestQueue);
        props.put( ( "queue." + replyQueue ) ,replyQueue);        
        Context jndiContext = new InitialContext(props);
       
        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // connect to the sender channel
        Destination senderDestination = (Destination) jndiContext.lookup(requestQueue);
        producer = session.createProducer(senderDestination);
        // connect to the receiver channel
        Destination receiverDestination = (Destination) jndiContext.lookup(replyQueue);
        consumer = session.createConsumer(receiverDestination);
        consumer.setMessageListener(new MessageListener() {

            public void onMessage(Message message) {
                processLoanOffer((TextMessage) message);
            }
        });
         // create the serializer
        serializer = new ClientSerializer();
         // create the GUI
        frame = new ClientFrame(name) {

            @Override
            public void send(ClientRequest request) {
                sendRequest(request);
            }
        };

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                frame.setVisible(true);
            }
        });
    }
    /**
     * Sends new loan request to the LoanBroker.
     * @param request
     */
    public void sendRequest(ClientRequest request) {
        try {
            producer.send(session.createTextMessage(serializer.requestToString(request)));
            frame.addRequest(request);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * This message is called whenever a new client reply message arrives.
     *  The message is de-serialized into a ClientReply, and the reply is shown in the GUI.
     * @param message
     */
    private void processLoanOffer(TextMessage message) {
        try {
            ClientReply reply = serializer.replyFromString(((TextMessage) message).getText());
            frame.addReply(null, reply);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Opens connection to JMS,so that messages can be send and received.
     */
    public void start() {
        try {
            connection.start();
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}