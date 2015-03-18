package creditbureau;

import creditbureau.gui.CreditFrame;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * This class represents one Credit Agency Application.
 * It should:
 *  1. Receive CreditRequest-s for a loan from the LoanBroker Messaging-Oriented Middleware (MOM).
 *  2. Randomly create CreditReply for each request (use method "getReply").
 *  3. Send the CreditReply from the LoanBroker MOM.
 */
public class CreditBureau {

    private final Random random = new Random(); // for random generation of replies
    private CreditFrame frame; // GUI
    private final CreditSerializer serializer; // serializer CreditRequest CreditReply to/from XML:
    /**
     * attributes for connection to JMS
     */
    private final Connection connection; // connection to the JMS server
    protected Session session; // JMS session fro creating producers, consumers and messages
    private final MessageProducer producer; // producer for sending messages
    private final MessageConsumer consumer; // consumer for receiving messages

    public CreditBureau(String creditRequestQueue, String creditReplyQueue) throws Exception {
        super();
        // connect to JMS
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
        props.put( ( "queue." + creditRequestQueue ) ,creditRequestQueue);
        props.put( ( "queue." + creditReplyQueue ) ,creditReplyQueue);        
        Context jndiContext = new InitialContext(props);
       
        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // connect to the sender channel
        Destination senderDestination = (Destination) jndiContext.lookup(creditReplyQueue);
        producer = session.createProducer(senderDestination);

        // connect to the receiver channel and register as a listener on it
        Destination receiverDestination = (Destination) jndiContext.lookup(creditRequestQueue);
        consumer = session.createConsumer(receiverDestination);
        consumer.setMessageListener(new MessageListener() {

            public void onMessage(Message msg) {
                onCreditRequest((TextMessage) msg);
            }
        });
        // create the serializer
        serializer = new CreditSerializer();

        // create GUI
        frame = new CreditFrame();
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                frame.setVisible(true);
            }
        });
    }
/**
     * Processes a new request message by randomly generating a reply and sending it back.
     * @param message the credit request message
     */
    private void onCreditRequest(TextMessage message) {
        try {
            CreditRequest request = serializer.requestFromString(message.getText());
            frame.addRequest(request);
            CreditReply reply = computeReply(request);
            Message replyMessage = session.createTextMessage(serializer.replyToString(reply));
            producer.send(replyMessage);
            frame.addReply(request, reply);
        } catch (Exception ex) {
            Logger.getLogger(CreditBureau.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   /**
    * Randomly generates a CreditReply given the request.
    * @param request is the CreditRequest for which the reply must be generated
    * @return a credit reply
    */
    private CreditReply computeReply(CreditRequest request) {
        int ssn = request.getSSN();

        int score = (int) (random.nextInt(600) + 300);
        int history = (int) (random.nextInt(19) + 1);

        return new CreditReply(ssn, score, history);
    }
    /**
     * Opens connection to JMS,so that messages can be send and received.
     */
    public void start() {
        try {
            connection.start();
        } catch (JMSException ex) {
            Logger.getLogger(CreditBureau.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
