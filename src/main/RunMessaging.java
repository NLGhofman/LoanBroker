package main;


import bank.Bank;
import client.ClientRequest;
import client.LoanTestClient;
import creditbureau.CreditBureau;
import loanbroker.LoanBroker;

/**
 * This application tests the LoanBroker system.
 * 
 */
public class RunMessaging {

    public static void main(String[] args) {
        try {
            // read the queue names from file "MESSAGING.ini"  
            JMSSettings.init("MESSAGING_CHANNELS.ini");
            JMSSettings.setRunMode(JMSSettings.RunMode.AUTOMATICALLY); // this means that the Bank will automatically generate an Interest rate and return it!
            final String clientRequestQueue = JMSSettings.get(JMSSettings.LOAN_REQUEST);
            final String clientReplyQueue = JMSSettings.get(JMSSettings.LOAN_REPLY);
            final String creditRequestQueue = JMSSettings.get(JMSSettings.CREDIT_REQUEST);
            final String creditReplyQueue = JMSSettings.get(JMSSettings.CREDIT_REPLY);
            final String ingRequestQueue = JMSSettings.get(JMSSettings.BANK_1);
            final String bankReplyQueue = JMSSettings.get(JMSSettings.BANK_REPLY);

            // create a LoanBroker middleware
            LoanBroker broker = new LoanBroker(clientRequestQueue, clientReplyQueue, creditRequestQueue, creditReplyQueue, ingRequestQueue, bankReplyQueue);

            // create a Client Application
            LoanTestClient client = new LoanTestClient("The Hypotheker", clientRequestQueue, clientReplyQueue);
            
            // create the CreditBureau Application
            CreditBureau creditBureau = new CreditBureau(creditRequestQueue,creditReplyQueue);

            // create one Bank application
            Bank ing = new Bank("ING", ingRequestQueue, bankReplyQueue);

            // open all connections in the broker, client and credit applications
            broker.start();
            creditBureau.start();
            ing.start();
            client.start();

            // send three requests
            client.sendRequest(new ClientRequest(1, 100000, 24));
            client.sendRequest(new ClientRequest(2, 88888, 5));
            client.sendRequest(new ClientRequest(3, 100, 5));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
