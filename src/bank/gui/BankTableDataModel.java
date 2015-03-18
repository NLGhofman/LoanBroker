/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.gui;

import bank.BankQuoteReply;
import bank.BankQuoteRequest;
import common.RequestReplyRow;
import common.RequestReplyTableModel;
import java.util.Vector;

/**
 *
 * @author Maja Pesic
 */
class BankTableDataModel extends RequestReplyTableModel<BankQuoteRequest, BankQuoteReply> {

    public BankTableDataModel() {
        super();
        Vector<String> header = new Vector<String>();
        header.add("ssn");
        header.add("credit");
        header.add("history");
        header.add("amount");
        header.add("time");
        header.add("interest");
        header.add("id");
        header.add("error");
        setColumns(header);
    }

    @Override
    protected RequestReplyRow<BankQuoteRequest, BankQuoteReply> createRow(BankQuoteRequest request) {
        return new Row(request);
    }

    private class Row extends RequestReplyRow<BankQuoteRequest, BankQuoteReply> {

        public Row(BankQuoteRequest request) {
            super(request, getRowCount());
        }

        @Override
        protected void fillRequestCells(BankQuoteRequest request) {
            if (request != null) {
                add(request.getSSN());
                add(request.getCredit());
                add(request.getHistory());
                add(request.getAmount());
                add(request.getTime());
            } else {
                add(request.getSSN());
                add("");
                add("");
                add("");
                add("");
            }
        }

        @Override
        protected void fillReplyCells(BankQuoteReply reply) {
            setValueAt(reply.getInterest(), getIndex(), 5);
            setValueAt(reply.getQuoteId(), getIndex(), 6);
            setValueAt(reply.getError(), getIndex(), 7);
        }
    }
}
