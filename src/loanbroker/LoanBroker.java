/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker;

import bank.BankQuoteReply;
import bank.BankQuoteRequest;
import bank.BankSerializer;
import client.*;
import creditbureau.CreditReply;
import creditbureau.CreditRequest;
import creditbureau.CreditSerializer;
import java.util.Properties;
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
import loanbroker.gui.LoanBrokerFrame;
import messaging.GatewayException;

/**
 *
 * @author Maja Pesic
 */
public class LoanBroker
{

    private LoanBrokerFrame frame;
    private ClientGateway clientGateway;
    private CreditGateway creditGateway;
    private BankGateway bankGateway;

    /**
     * Initializes attributes, and registers itself (method onClinetRequest) as
     * the listener for new client requests
     *
     * @param clientRequestQueue
     * @param clientReplyQueue
     * @param creditRequestQueue
     * @param creditReplyQueue
     * @param bankRequestQueue
     * @param bankReplyQueue
     * @throws java.lang.Exception
     */
    public LoanBroker(String clientRequestQueue, String clientReplyQueue, String creditRequestQueue, String creditReplyQueue, String bankRequestQueue, String bankReplyQueue) throws Exception
    {
        this.clientGateway = new ClientGateway(clientRequestQueue, clientReplyQueue)
        {

            @Override
            public void onClientRequestReceived(ClientRequest request)
            {
                try
                {
                    frame.addObject(null, request);
                    CreditRequest creditRequest = createCreditRequest(request);
                    creditGateway.sendCreditRequest(creditRequest);
                }
                catch (GatewayException ex)
                {
                    Logger.getLogger(LoanBroker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        this.creditGateway = new CreditGateway(creditRequestQueue, creditReplyQueue)
        {

            @Override
            public void onCreditReplyReceived(CreditReply reply)
            {
                try
                {
                    frame.addObject(null, reply);
                    BankQuoteRequest bankRequest = createBankRequest(null, reply);
                    bankGateway.sendBankQuoteRequest(bankRequest);
                }
                catch (GatewayException ex)
                {
                    Logger.getLogger(LoanBroker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        this.bankGateway = new BankGateway(bankRequestQueue, bankReplyQueue)
        {

            @Override
            public void onBankQuoteReplyReceived(BankQuoteReply reply)
            {
                try
                {
                    frame.addObject(null, reply);
                    ClientReply clientReply = createClientReply(reply);
                    clientGateway.sendClientReply(clientReply);
                }
                catch (GatewayException ex)
                {
                    Logger.getLogger(LoanBroker.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        };
        /*
         * Make the GUI
         */
        frame = new LoanBrokerFrame();
        java.awt.EventQueue.invokeLater(new Runnable()
        {

            public void run()
            {

                frame.setVisible(true);
            }
        });
    }

    /**
     * Generates a credit request based on the given client request.
     *
     * @param clientRequest
     * @return
     */
    private CreditRequest createCreditRequest(ClientRequest clientRequest)
    {
        return new CreditRequest(clientRequest.getSSN());
    }

    /**
     * Generates a bank quote request based on the given client request and
     * credit reply.
     *
     * @param creditReply
     * @return
     */
    private BankQuoteRequest createBankRequest(ClientRequest clientRequest, CreditReply creditReply)
    {
        int ssn = creditReply.getSSN();
        int score = creditReply.getCreditScore();
        int history = creditReply.getHistory();
        int amount = 100; // this must be hard coded because we don't know to which clientRequest this creditReply belongs to!!! 
        int time = 24;   // this must be hard coded because we don't know to which clientRequest this creditReply belongs to!!! 
        if (clientRequest != null)
        {
            amount = clientRequest.getAmount();
            time = clientRequest.getTime();
        }
        return new BankQuoteRequest(ssn, score, history, amount, time);
    }

    /**
     * Generates a client reply based on the given bank quote reply.
     *
     * @param creditReply
     * @return
     */
    private ClientReply createClientReply(BankQuoteReply reply)
    {
        return new ClientReply(reply.getInterest(), reply.getQuoteId());
    }

    /**
     * Opens connection to JMS,so that messages can be send and received.
     * @throws java.lang.Exception
     */
    public void start()
            throws Exception
    {
        clientGateway.start();
        creditGateway.start();
        bankGateway.start();
    }
}
