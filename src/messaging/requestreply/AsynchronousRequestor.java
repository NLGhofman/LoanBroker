package messaging.requestreply;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import messaging.GatewayException;
import messaging.MessagingGateway;

/**
 * This class is used for sending requests and receiving replies in asynchronous
 * communication.This class inherits ythe MessagingGateway, i.e., it has access
 * to a MessageSender and MessageReceiver.
 *
 * @param <REQUEST> is the domain class for requests
 * @param <REPLY> is the domain class for replies
 * @author Maja Pesic
 */
public class AsynchronousRequestor<REQUEST, REPLY>
{

    /**
     * Class Pair is just used to make it possible to store pairs of REQUEST,
     * ReplyListener in a hashtable!
     */
    private class Pair
    {

        private IReplyListener<REQUEST, REPLY> listener;
        private REQUEST request;

        private Pair(IReplyListener<REQUEST, REPLY> listener, REQUEST request)
        {
            this.listener = listener;
            this.request = request;
        }
    }

    /**
     * For sending and receiving messages
     */
    private MessagingGateway gateway;

    /**
     * contains registered reply listeners for each sent request
     */
    private Map<String, Pair> listeners;
    /**
     * used for serialization of requests and replies
     */
    private IRequestReplySerializer<REQUEST, REPLY> serializer;

    /**
     * The only constructor. This constructor does the following: 1. creates the
     * serializer and listener. 2. registeres itself as the listener on the
     * MessageReceiver (method onReply)
     *
     * @param requestSenderQueue
     * @param replyReceiverQueue
     * @param serializer
     * @throws java.lang.Exception
     */
    public AsynchronousRequestor(String requestSenderQueue, String replyReceiverQueue, IRequestReplySerializer<REQUEST, REPLY> serializer) 
            throws Exception
    {
        this.serializer = serializer;
        this.listeners = new HashMap<>();

        gateway = new MessagingGateway(requestSenderQueue, replyReceiverQueue);
        gateway.setListener(new MessageListener()
        {
            @Override
            public void onMessage(Message message)
            {
                onReply((TextMessage) message);
            }
        });
    }

    /**
     * Opens JMS connection in order to be able to send messages and to start
     * receiving messages.
     * @throws messaging.GatewayException
     */
    public void start() 
            throws GatewayException
    {
        try
        {
            gateway.start();
        }
        catch (JMSException ex)
        {
            throw new GatewayException("An error has occured in starting the asynchronous replier.", ex);
        }
    }

    /**
     * @throws messaging.GatewayException
     * @todo implement this method! Sends one request. Immediately, a listener
     * is registered for this request. This listener will be notified when
     * (later) a reply for this request arrives. This method should: 1. create a
     * Message for the request (use serializer). 2. set the JMSReplyTo of the
     * Message to be the Destination of the gateway's receiver. 3. send the
     * Message 4. register the listener to belong to the JMSMessageID of the
     * request Message
     *
     * @param request is the request object (a domain class) to be sent
     * @param listener is the listener that will be notified when the reply
     * arrives for this request
     */
    public synchronized void sendRequest(REQUEST request, IReplyListener<REQUEST, REPLY> listener) 
            throws GatewayException
    {
        try
        {
            String body = serializer.requestToString(request);
            Message requestMessage = gateway.createMessage(body);
            requestMessage.setJMSReplyTo(gateway.getConsumerDestination());
            gateway.sendMessage(requestMessage);
            Pair pair = new Pair(listener, request);
            listeners.put(requestMessage.getJMSMessageID(), pair);
        }
        catch (JMSException ex)
        {
            throw new GatewayException("An error has occured in sending a request.", ex);
        }
    }

    /**
     * @todo implement this method! This method is invoked for processing of a
     * single reply when it arrives. This method should be registered on the
     * MessageReceiver. This method should: 1. get the registered listener fo
     * the JMSCorrelationID of the Message 2. de-serialize the REPLY from the
     * Message 3. notify the listener about the arrival of the REPLY 4.
     * unregister the listener
     * @param message the reply message
     */
    private synchronized void onReply(TextMessage message)
    {
        try
        {
            Pair pair = listeners.get(message.getJMSCorrelationID());
            REPLY reply = serializer.replyFromString(message.getText());
            pair.listener.onReply(pair.request, reply);
            listeners.remove(message.getJMSCorrelationID());
        }
        catch (JMSException ex)
        {
            Logger.getLogger(AsynchronousRequestor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
