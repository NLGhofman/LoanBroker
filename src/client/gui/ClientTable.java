/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.ClientReply;
import client.ClientRequest;
import common.ClientReplyCellRenderer;
import common.ClientRequestCellRenderer;
import common.SendReceiveTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Maja Pesic
 */
public class ClientTable extends SendReceiveTable {

    private ClientRequestCellRenderer requestRenderer = new ClientRequestCellRenderer();
    private ClientReplyCellRenderer replyRenderer = new ClientReplyCellRenderer();

    public ClientTable(TableModel model) {
        super(model);
        setReceiveColumns(new int[]{3,4});
        setSendColumns(new int[]{0,1,2});
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column < 3) {
            return requestRenderer;
        } else {
            return replyRenderer;
        }
    }
}
