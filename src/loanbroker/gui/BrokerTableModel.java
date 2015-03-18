/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker.gui;

import bank.BankQuoteReply;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import client.ClientRequest;
import creditbureau.CreditReply;

/**
 *
 * @author Maja Pesic
 */
public class BrokerTableModel extends DefaultTableModel {

    protected Vector<BrokerRow> rows;

    public BrokerTableModel() {
        super();
        Vector<String> header = new Vector<String>();
        header.add("ssn");
        header.add("amount");
        header.add("time");
        header.add("credit");
        header.add("history");
        header.add("interest");
        header.add("id");
        setColumns(header);
    }

    protected void setColumns(Vector<String> columns) {
        rows = new Vector<BrokerRow>();
        setDataVector(rows, columns);
    }

    /**
     * Returns false regardless of parameter values.
     *
     * @param   row             the row whose value is to be queried
     * @param   column          the column whose value is to be queried
     * @return                  true
     * @see #setValueAt
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void setClientRequest(ClientRequest request) {
        createRow(request);
    }

    public void setCreditReply(ClientRequest request, CreditReply reply) {
        BrokerRow row = getRow(request);
        row.setCreditReply(reply);
    }

    public void setBankReply(ClientRequest request, BankQuoteReply reply) {
        BrokerRow row = getRow(request);
        row.setBankQuoteReply1(reply);
    }

    private BrokerRow createRow(ClientRequest request) {
        BrokerRow row = new BrokerRow(getRowCount());
        row.setClientRequest(request);
        addRow(row);
        return row;
    }

    private BrokerRow getRow(ClientRequest request) {
        boolean found = false;
        Iterator<BrokerRow> it = rows.iterator();
        BrokerRow rowFound = null;

        if (request != null) {
            while ((!found) && it.hasNext()) {
                rowFound = it.next();
                found = (rowFound.getClientRequest() == request);
            }
        }

        if (!found) {
            rowFound = createRow(request);
        }
        return rowFound;
    }

    public ClientRequest getClientRequest(int index) {
        try {
            return rows.get(index).getClientRequest();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class BrokerRow extends Vector {

        private static final int SSN = 0;
        private static final int AMOUNT = 1;
        private static final int TIME = 2;
        private static final int SCORE = 3;
        private static final int HISTORY = 4;
        private static final int INTEREST1 = 5;
        private static final int QUOTE1 = 6;
        private ClientRequest clientRequest = null;
        private CreditReply creditReply = null;
        private BankQuoteReply bankQuoteReply1 = null;
        private int nr;

        public BrokerRow(int index) {
            super();
            ensureCapacity(7);
            nr = index;
        }

        protected int getIndex() {
            return nr;
        }

        private void setValue(int column, Object aValue) {
            setValueAt(aValue, nr, column);
        }

        public BankQuoteReply getBankQuoteReply1() {
            return bankQuoteReply1;
        }

        public void setBankQuoteReply1(BankQuoteReply bankQuoteReply) {
            this.bankQuoteReply1 = bankQuoteReply;
            setValue(INTEREST1, bankQuoteReply.getInterest());
            setValue(QUOTE1, bankQuoteReply.getQuoteId());
        }

        public ClientRequest getClientRequest() {
            return clientRequest;
        }

        public void setClientRequest(ClientRequest clientRequest) {
            this.clientRequest = clientRequest;
            if (clientRequest != null) {
                add(clientRequest.getSSN());
                add(clientRequest.getAmount());
                add(clientRequest.getTime());
            } else {
                add("");
                add("");
                add("");
            }
        }

        public CreditReply getCreditReply() {
            return creditReply;
        }

        public void setCreditReply(CreditReply creditReply) {
            this.creditReply = creditReply;
            setValue(SCORE, creditReply.getCreditScore());
            setValue(HISTORY, creditReply.getHistory());
        }
    }
}
