package bank;

import bank.gui.BankFrame;
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
import main.JMSSettings;
import main.JMSSettings.RunMode;

/**
 * This class represents one Bank Application.
 * It should eventually:
 *  1. Receive BankQuoteRequest-s for a loan from the LoanBroker Messaging-Oriented Middleware (MOM).
 *  2. Randomly create BankQuoteReply for each request (use method "computeBankReply").
 *  3. Send the BankQuoteReply from the LoanBroker MOM.
 */
public class Bank {

    private static final int ERROR_CODE = 1;
    private static final int NO_ERROR_CODE = 0;
    private final double primeRate = 3.5;
    private final String name;
    private final double ratePremium = 0.5;
    private final int maxLoanTerm = 10000;
    private int quoteCounter = 0;
    protected Random random = new Random();
    private BankFrame frame; // GUI
    private final BankSerializer serializer; // serializer BankQuoteRequest BankQuoteReply to/from XML:
    /**
     * attributes for connection to JMS
     */
    private final Connection connection; // connection to the JMS server
    protected Session session; // JMS session for creating producers, consumers and messages
    private final MessageProducer producer; // producer for sending messages
    private final MessageConsumer consumer; // consumer for receiving messages

    public Bank(String bankName, String bankRequestQueue, String bankReplyQueue) throws Exception {
        super();
        this.name = bankName;
        // connect to JMS
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL,"tcp://localhost:61616");
        props.put( ( "queue." + bankRequestQueue ) ,bankRequestQueue);
        props.put( ( "queue." + bankReplyQueue ) ,bankReplyQueue);        
        Context jndiContext = new InitialContext(props);
       
        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // connect to the sender channel
        Destination senderDestination = (Destination) jndiContext.lookup(bankReplyQueue);
        producer = session.createProducer(senderDestination);

        // connect to the receiver channel and register as a listener on it
        Destination receiverDestination = (Destination) jndiContext.lookup(bankRequestQueue);
        consumer = session.createConsumer(receiverDestination);
        consumer.setMessageListener(new MessageListener() {

            public void onMessage(Message msg) {
                onBankQuoteRequest((TextMessage) msg);
            }
        });

        // create the serializer
        serializer = new BankSerializer();

        // create GUI
        frame = new BankFrame(this, name);
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {

                frame.setVisible(true);
            }
        });
    }

    public boolean onSendBankReplyClicked(BankQuoteRequest request, double interest, int error)
    {
         BankQuoteReply reply = createReply(interest,error);
         return sendReply(request, reply);
    }
    
    public BankQuoteReply createReply(double interest, int error){
       String quoteID = name + "-" + String.valueOf(++quoteCounter);
       return new BankQuoteReply(interest, quoteID, error);
    }

    /**
     * Processes a new request message. Only if the debug_mode is true, this method
     * randomly generates a reply and sends it back.
     * @param message
     */
    private void onBankQuoteRequest(TextMessage message) {
        try {
            BankQuoteRequest request = serializer.requestFromString(message.getText());
            frame.addRequest(request);
            if (JMSSettings.getRunMode() == RunMode.AUTOMATICALLY) { // only in automatic mode send immediately random reply

                BankQuoteReply reply = computeReplyRandomly(request);
                Bank.this.sendReply(request, reply);

            }
        } catch (Exception ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sends the reply for one request.
     * @param request for which the reply is sent
     * @param reply
     * @return true if the reply is successfully sent, false if sending fails
     */
    private boolean sendReply(BankQuoteRequest request, BankQuoteReply reply) {
        try {
            Message replyMessage = session.createTextMessage(serializer.replyToString(reply));
            producer.send(replyMessage);
            frame.addReply(request, reply);
            return true;
        } catch (JMSException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Randomly generates a reply for the given request.
     * @param request for which the reply should be generated.
     * @return randomly generated reply
     */
    private BankQuoteReply computeReplyRandomly(BankQuoteRequest request) {

        double interest = 0.0;
        int error = ERROR_CODE;
        if (request.getTime() <= maxLoanTerm) {

            double temp = primeRate + ratePremium + (double) (request.getTime() / 12) / 10 + (double) random.nextInt(10) / 10;
            interest = round(temp, 1); // round to one decimal

            error = NO_ERROR_CODE;
        }
        return createReply(interest,error);

    }

    /**
     * Rounds a decimal number.
     * @param value is the decimal value to be rounded.
     * @param decimals the number of decimal places after rounding.
     * @return
     */
    private double round(double value, int decimals) {
        final int temp = (int) Math.pow(10, decimals);// temp will be 1 or 10 or 100 or 1000 or , ...
        double result = Math.round(value * temp);
        result = result / temp;
        return result;
    }

    /**
     * Opens connection to JMS,so that messages can be send and received.
     */
    public void start() {
        try {
            connection.start();
        } catch (JMSException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
