/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.ClientReply;
import client.ClientRequest;
import common.RequestReplyRow;
import common.RequestReplyTableModel;
import java.util.Vector;

/**
 *
 * @author Maja Pesic
 */
public class ClientTableDataModel extends RequestReplyTableModel<ClientRequest, ClientReply> {

    public ClientTableDataModel() {
        super();
        Vector<String> header = new Vector<String>();
        header.add("ssn");
        header.add("amount");
        header.add("time");
        header.add("interest");
        header.add("id");
        setColumns(header);
    }

    @Override
    protected RequestReplyRow<ClientRequest, ClientReply> createRow(ClientRequest request) {
        return new Row(request);
    }

    private class Row extends RequestReplyRow<ClientRequest, ClientReply> {

        public Row(ClientRequest request) {
            super(request, getRowCount());
        }

        @Override
        protected void fillRequestCells(ClientRequest request) {
            if (request != null) {
                add(request.getSSN());
                add(request.getAmount());
                add(request.getTime());
            } else {
                add("");
                add("");
                add("");
            }
        }

        @Override
        protected void fillReplyCells(ClientReply reply) {
            setValueAt(reply.getInterest(), getIndex(), 3);
            setValueAt(reply.getQuoteID(), getIndex(), 4);
        }
    }
}
