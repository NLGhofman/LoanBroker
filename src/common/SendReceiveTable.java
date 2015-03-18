/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author Maja Pesic
 */
public abstract class SendReceiveTable extends JTable {

    private static final String SEPARATOR = File.separator;
    private static final String WORKING_DIRECTORY = System.getProperty("user.dir");
    private static final String RESOURCES = SEPARATOR + "images" + SEPARATOR;

    private static final String RECEIVE_ICON = "import1.png";
    private static final String SEND_ICON = "export1.png";

    private TableCellRenderer receiveHeaderRenderer;
    private TableCellRenderer sendHeaderRenderer;

    private JPopupMenu popup;

    public SendReceiveTable(TableModel model) {
        super(model);
        receiveHeaderRenderer = new HeaderRenderer(createIcon(RECEIVE_ICON));
        sendHeaderRenderer = new HeaderRenderer(createIcon(SEND_ICON));

        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        createPopUpMenu();
    }

    private void createPopUpMenu() {
        popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("remove all rows");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) getModel();
                while (getRowCount() > 0) {
                    model.removeRow(0);
                }
            }
        });
        popup.add(menuItem);

        addMouseListener(new PopupListener());
    }

//    private RequestReplyTableModel<REQUEST, REPLY> getRequestReplyModel() {
//        return (RequestReplyTableModel<REQUEST, REPLY>) getModel();
//    }
//
//    public void addRequest(REQUEST request) {
//        getRequestReplyModel().addRequest(request);
//    }
//
//    public void addReply(REQUEST request, REPLY reply) {
//        getRequestReplyModel().addReply(request, reply);
//    }
//
//    public void setCellData(Object obj, int row_index, int col_index) {
//        getRequestReplyModel().setValueAt(obj, row_index, col_index);
//    }
//
//    public REQUEST getSelectedRequest() {
//        int index = getSelectedRow();
//        return getRequestReplyModel().getRequest(index);
//    }
//
//    public REPLY getSelectedReply() {
//        int index = getSelectedRow();
//        return getRequestReplyModel().getReply(index);
//    }

    private static ImageIcon createIcon(String name) {
        String path = WORKING_DIRECTORY + RESOURCES + name;
        File iconFile = new File(path);
        URL iconURL = null;
        ImageIcon icon = null;
        try {
            iconURL = iconFile.toURI().toURL();
            icon = new ImageIcon(iconURL);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SendReceiveTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return icon;
    }

    public void setReceiveColumns(int[] columns) {
        for (int col_index : columns) {
            TableColumn column = getTableHeader().getColumnModel().getColumn(col_index);
            column.setHeaderRenderer(receiveHeaderRenderer);
        }
    }

    public void setSendColumns(int[] columns) {
        for (int col_index : columns) {
            TableColumn column = getTableHeader().getColumnModel().getColumn(col_index);
            column.setHeaderRenderer(sendHeaderRenderer);
        }
    }

    private class HeaderRenderer extends JLabel implements TableCellRenderer {

        protected HeaderRenderer(Icon icon) {
            super("", JLabel.CENTER);
            setIcon(icon);
            setVerticalTextPosition(JLabel.BOTTOM);
            setHorizontalTextPosition(JLabel.CENTER);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    private class PopupListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                        e.getX(), e.getY());
            }
        }
    }
}
