/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package creditbureau.gui;

import common.CreditReplyCellRenderer;
import common.CreditRequestCellRenderer;
import common.SendReceiveTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Maja Pesic
 */
public class CreditTable extends SendReceiveTable
{

    private CreditRequestCellRenderer requestRenderer = new CreditRequestCellRenderer();
    private CreditReplyCellRenderer replyRenderer = new CreditReplyCellRenderer();

    public CreditTable(TableModel model)
    {
        super(model);
        setReceiveColumns(new int[]
        {
            0
        });
        setSendColumns(new int[]
        {
            1, 2
        });
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        if (column == 0)
        {
            return requestRenderer;
        }
        else
        {
            return replyRenderer;
        }
    }
}
