/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import messaging.requestreply.AsynchronousRequestor;

/**
 *
 * @author Zef
 */
public class LoanBrokerGateway extends AsynchronousRequestor<ClientRequest, ClientReply>
{

    public LoanBrokerGateway(String requestQueue, String replyQueue)
            throws Exception
    {
        super(requestQueue, replyQueue, new ClientSerializer());
    }
}
