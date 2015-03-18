package client;

import client.gui.ClientFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import messaging.GatewayException;
import messaging.requestreply.IReplyListener;

/**
 * This class represents one Client Application. It: 1. Creates a ClientRequest
 * for a loan. 2. Sends it to the LoanBroker Messaging-Oriented Middleware
 * (MOM). 3. Receives the reply from the LoanBroker MOM.
 *
 */
public class LoanTestClient
{

    private LoanBrokerGateway gateway;
    private ClientFrame frame;

    public LoanTestClient(String name, String requestQueue, String replyQueue) throws Exception
    {
        gateway = new LoanBrokerGateway(requestQueue, replyQueue);
        // create the GUI
        frame = new ClientFrame(name)
        {

            @Override
            public void send(ClientRequest request)
            {
                sendRequest(request);
            }
        };

        java.awt.EventQueue.invokeLater(new Runnable()
        {

            public void run()
            {

                frame.setVisible(true);
            }
        });
    }

    /**
     * Sends new loan request to the LoanBroker.
     *
     * @param request
     */
    public void sendRequest(ClientRequest request)
    {
        try
        {
            gateway.sendRequest(request, new IReplyListener<ClientRequest, ClientReply>()
            {

                @Override
                public void onReply(ClientRequest request, ClientReply reply)
                {
                    frame.addReply(request, reply);
                }
            });
            frame.addRequest(request);
        }
        catch (GatewayException ex)
        {
            Logger.getLogger(LoanTestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Opens connection to JMS,so that messages can be send and received.
     *
     * @throws java.lang.Exception
     */
    public void start()
            throws Exception
    {
        gateway.start();
    }
}
