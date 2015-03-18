/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbroker.gui;

import bank.BankQuoteReply;
import client.ClientRequest;
import creditbureau.CreditReply;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
/**
 * This class is used as GUI for the LoanBroker application.
 * The Frame contains a table with received ClientRequests, CreditRequests and BankQuoteRequests.
 * The Frame positions itself in the down center of the screen.
 * @author Maja Pesic
 */

public class LoanBrokerFrame extends JFrame {

    private static final int BORDER = 20;
    private BrokerTableModel model = new BrokerTableModel();
    private BrokerTable table = new BrokerTable(model);

    public LoanBrokerFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() / 4);
        int h = (int) ((screenSize.getHeight() - (4 * BORDER)) / 2);
        setSize(new Dimension(w, h));

        int x = (int) ((screenSize.getWidth() - w) / 2);
        int y = (int) (screenSize.getHeight() - h) - BORDER * 2;
        setLocation(x, y);
        setTitle("Loan Broker");
        setContentPane(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
    }

    public synchronized void addObject(ClientRequest owner, Object newObject) {
        if (newObject instanceof ClientRequest) {
            model.setClientRequest((ClientRequest) newObject);
        } else if (newObject instanceof CreditReply) {
            model.setCreditReply(owner, (CreditReply) newObject);
        } else if (newObject instanceof BankQuoteReply) {
            model.setBankReply(owner, (BankQuoteReply) newObject);
        }
    }
}
