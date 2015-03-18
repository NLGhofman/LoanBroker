/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker.gui;

import bank.gui.*;
import bank.BankQuoteReply;
import bank.BankQuoteRequest;
import common.BankReplyCellRenderer;
import common.BankRequestCellRenderer;
import common.ClientReplyCellRenderer;
import common.ClientRequestCellRenderer;
import common.CreditReplyCellRenderer;
import common.SendReceiveTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Maja Pesic
 */
public class BrokerTable extends SendReceiveTable {

    private ClientRequestCellRenderer clientRequestRenderer = new ClientRequestCellRenderer();
    private CreditReplyCellRenderer creditRenderer = new CreditReplyCellRenderer();
    private BankReplyCellRenderer bankRenderer = new BankReplyCellRenderer();
    private ClientReplyCellRenderer clientReplyRenderer = new ClientReplyCellRenderer();

    public BrokerTable(TableModel model) {
        super(model);

        setReceiveColumns(new int[]{0, 1, 2, 3, 4, 5, 6});
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {

        if (column < 3) {
            return clientRequestRenderer;
        } else {
            if (column < 5) {
                return creditRenderer;
            } else {
                if (column < 14) {
                    return bankRenderer;
                } else {
                    return clientReplyRenderer;
                }
            }
        }
    }
}
