/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package creditbureau.gui;

import creditbureau.CreditReply;
import creditbureau.CreditRequest;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * This class is used as GUI for the CreditBuerau application.
 * The Frame contains a table with received CreditRequests and sent CreditReplies.
 * The Frame positions itself in the down right corner of the screen.
 * @author Maja Pesic
 */
public class CreditFrame extends JFrame {

    private static final int BORDER = 20;
    private CreditTableDataModel model = new CreditTableDataModel();
    private CreditTable table = new CreditTable(model);

    public CreditFrame() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Credit Buerau");
        initComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() / 7);
        int h = (int) ( (screenSize.getHeight() - 4*BORDER )/ 2);
         setSize(new Dimension(w, h));

        int x = (int) (screenSize.getWidth() - w) - BORDER;
        int y = (int) (screenSize.getHeight() - h) - BORDER * 2;
        setLocation(x, y);

    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("credit requests"));
        panel.setLayout(new BorderLayout());
        panel.setOpaque(true);
        setContentPane(panel);
        panel.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        table.setFillsViewportHeight(true);

    }

    public void addRequest(CreditRequest request) {
        model.addRequest(request);
    }

    public void addReply(CreditRequest request, CreditReply reply) {
        model.addReply(request, reply);
    }
}
