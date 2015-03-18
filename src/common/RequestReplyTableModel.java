/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Maja Pesic
 */
public abstract class RequestReplyTableModel<REQUEST, REPLY> extends DefaultTableModel {

    protected Vector<RequestReplyRow<REQUEST, REPLY>> row;

    public RequestReplyTableModel() {
        super();
    }

    protected void setColumns(Vector<String> columns) {
        row = new Vector<RequestReplyRow<REQUEST, REPLY>>();
        setDataVector(row, columns);
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

    public void addRequest(REQUEST request) {
        addRow(createRow(request));
    }

    protected abstract RequestReplyRow<REQUEST, REPLY> createRow(REQUEST request);

    public void addReply(REQUEST request, REPLY reply) {
        boolean found = false;
        Iterator<RequestReplyRow<REQUEST, REPLY>> it = row.iterator();
        RequestReplyRow<REQUEST, REPLY> rowFound = null;
        if (request != null) {

            while ((!found) && it.hasNext()) {
                rowFound = it.next();
                found = (rowFound.getRequest() == request);
            }
        }

        if (!found) { // if the request cannot be found, make a new 'empty' row for the reply
            rowFound = createRow(null);
            addRow(rowFound);
        }

        rowFound.setReply(reply);
    }

    public REQUEST getRequest(int index) {
        try {
            return row.get(index).getRequest();
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public REPLY getReply(int index) {
        return row.get(index).getReply();
    }
}
