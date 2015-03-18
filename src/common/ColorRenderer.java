/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Maja Pesic
 */
public abstract class ColorRenderer extends DefaultTableCellRenderer {
    

    public ColorRenderer() {
        super();
    }

    protected abstract Color getColor();

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object color,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, color,
                isSelected, hasFocus, row, column);
        if (!isSelected) {
            comp.setBackground(getColor());
        } else { // If not shaded, match the table's background
            comp.setBackground(getBackground());
        }
        return comp;
    }
}
