package creditbureau;

import creditbureau.gui.CreditFrame;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import messaging.GatewayException;

/**
 * This class represents one Credit Agency Application. It should: 1. Receive
 * CreditRequest-s for a loan from the LoanBroker Messaging-Oriented Middleware
 * (MOM). 2. Randomly create CreditReply for each request (use method
 * "getReply"). 3. Send the CreditReply from the LoanBroker MOM.
 */
public class CreditBureau
{

    private final Random random = new Random(); // for random generation of replies
    private CreditFrame frame;
    private LoanBrokerGateway gateway;

    public CreditBureau(String creditRequestQueue) 
            throws Exception
    {
        gateway = new LoanBrokerGateway(creditRequestQueue)
        {

            @Override
            public void onCreditRequestReceived(CreditRequest request)
            {
                try
                {
                    frame.addRequest(request);
                    CreditReply reply = computeReply(request);
                    frame.addReply(request, reply);
                    gateway.sendReply(request, reply);
                }
                catch (GatewayException ex)
                {
                    Logger.getLogger(CreditBureau.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        // create GUI
        frame = new CreditFrame();
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            @Override
            public void run()
            {
                frame.setVisible(true);
            }
        });
    }

    /**
     * Randomly generates a CreditReply given the request.
     *
     * @param request is the CreditRequest for which the reply must be generated
     * @return a credit reply
     */
    private CreditReply computeReply(CreditRequest request)
    {
        int ssn = request.getSSN();

        int score = (int) (random.nextInt(600) + 300);
        int history = (int) (random.nextInt(19) + 1);

        return new CreditReply(ssn, score, history);
    }

    /**
     * Opens connection to JMS,so that messages can be send and received.
     * @throws java.lang.Exception
     */
    public void start()
            throws Exception
    {
        gateway.start();
    }
}
