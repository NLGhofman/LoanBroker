/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker;

import creditbureau.CreditReply;
import creditbureau.CreditRequest;
import creditbureau.CreditSerializer;
import messaging.requestreply.AsynchronousRequestor;

/**
 *
 * @author Zef
 */
public class CreditGateway extends AsynchronousRequestor<CreditRequest, CreditReply>
{
    
    public CreditGateway(String requestQueue, String replyQueue)
            throws Exception
    {
        super(requestQueue, replyQueue, new CreditSerializer());
    }
}
