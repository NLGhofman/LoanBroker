/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package creditbureau.gui;

import creditbureau.CreditReply;
import creditbureau.CreditRequest;
import common.RequestReplyRow;
import common.RequestReplyTableModel;
import java.util.Vector;

/**
 *
 * @author Maja Pesic
 */
public class CreditTableDataModel extends RequestReplyTableModel<CreditRequest, CreditReply>
{

    public CreditTableDataModel()
    {
        Vector<String> header = new Vector<String>();
        header.add("ssn");
        header.add("score");
        header.add("history");
        setColumns(header);
    }

    @Override
    protected RequestReplyRow<CreditRequest, CreditReply> createRow(CreditRequest request)
    {
        return new Row(request);
    }

    private class Row extends RequestReplyRow<CreditRequest, CreditReply>
    {

        public Row(CreditRequest request)
        {
            super(request, getRowCount());
        }

        @Override
        protected void fillRequestCells(CreditRequest request)
        {
            add(request.getSSN());
        }

        @Override
        protected void fillReplyCells(CreditReply reply)
        {
            setValueAt(reply.getCreditScore(), getIndex(), 1);
            setValueAt(reply.getHistory(), getIndex(), 2);
        }
    }
}
