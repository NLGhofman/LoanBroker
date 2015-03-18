/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker;

import client.ClientReply;
import client.ClientRequest;
import client.ClientSerializer;
import messaging.requestreply.AsynchronousReplier;
import messaging.requestreply.IRequestListener;

/**
 *
 * @author Zef
 */
public abstract class ClientGateway extends AsynchronousReplier<ClientRequest, ClientReply>
{
    
    public ClientGateway(String requestQueue)
            throws Exception
    {
        super(requestQueue, new ClientSerializer());
        super.setRequestListener(new IRequestListener<ClientRequest>()
        {
            
            @Override
            public void receivedRequest(ClientRequest request)
            {
                onClientRequestReceived(request);
            }
        });
    }
    
    public abstract void onClientRequestReceived(ClientRequest request);
}
