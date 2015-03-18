/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.gui;

import bank.BankQuoteReply;
import bank.BankQuoteRequest;
import common.BankReplyCellRenderer;
import common.BankRequestCellRenderer;
import common.SendReceiveTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Maja Pesic
 */
public class BankTable extends SendReceiveTable {

    private BankReplyCellRenderer replyRenderer = new BankReplyCellRenderer();
    private BankRequestCellRenderer requestRenderer = new BankRequestCellRenderer();

    public BankTable(TableModel model) {
        super(model);
        
        setReceiveColumns(new int[]{0,1,2,3,4});
        setSendColumns(new int[]{5,6,7});
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column < 5) {
            return requestRenderer;
        } else {
            return replyRenderer;
        }
    }

}
