/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import messaging.requestreply.AsynchronousReplier;
import messaging.requestreply.IRequestListener;

/**
 *
 * @author Zef
 */
public abstract class LoanBrokerGateway extends AsynchronousReplier<BankQuoteRequest, BankQuoteReply>
{
    
    public LoanBrokerGateway(String requestQueue)
            throws Exception
    {
        super(requestQueue, new BankSerializer());
        super.setRequestListener(new IRequestListener<BankQuoteRequest>()
        {
            
            @Override
            public void receivedRequest(BankQuoteRequest request)
            {
                onBankQuoteRequestReceived(request);
            }
        });
    }
    
    public abstract void onBankQuoteRequestReceived(BankQuoteRequest request);
}
