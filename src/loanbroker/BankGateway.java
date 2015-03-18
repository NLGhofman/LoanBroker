/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker;

import bank.BankQuoteReply;
import bank.BankQuoteRequest;
import bank.BankSerializer;
import messaging.requestreply.AsynchronousRequestor;

/**
 *
 * @author Zef
 */
public class BankGateway extends AsynchronousRequestor<BankQuoteRequest, BankQuoteReply>
{

    public BankGateway(String requestQueue, String replyQueue)
            throws Exception
    {
        super(requestQueue, replyQueue, new BankSerializer());
    }
}
