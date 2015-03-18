/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.gui;

import client.ClientReply;
import java.awt.event.ActionEvent;
import client.ClientRequest;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class is used as GUI for the TestClient application.
 * The Frame contains a table with sent ClietnRequests and received ClientReplies.
 * The Frame positions itself in the down left corner of the screen.
 * If multiple instances of this frame are made,
 * they will be positioned to the right of the previous frame instance.
 * @author Maja Pesic
 */
public abstract class ClientFrame extends JFrame {

    private static final int BORDER = 20;
    private static int X_POSITION = BORDER;
    
    private JTextArea text = new JTextArea();
    private JTextField ssnComp = new JTextField("1");
    private JTextField amountComp = new JTextField("100000");
    private JTextField timeComp = new JTextField("24");
    private ClientTableDataModel model = new ClientTableDataModel();
    private ClientTable table = new ClientTable(model);

    public ClientFrame(String name) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth() / 5);
        int h = (int) ( (screenSize.getHeight() - (4*BORDER))/ 2);
        setSize(new Dimension(w, h));

        int x = X_POSITION;
        X_POSITION = X_POSITION + (int) getSize().getWidth() + BORDER;
        int y = (int) (screenSize.getHeight() - h) - BORDER * 2;
        setLocation(x, y);
        setTitle(name);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        text.setEditable(false);
        panel.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        JPanel input = new JPanel(new GridLayout(3, 0));

        JPanel line = new JPanel(new BorderLayout());
        line.add(new JLabel("ssn        "), BorderLayout.WEST);
        line.add(ssnComp, BorderLayout.CENTER);
        input.add(line);
        line = new JPanel(new BorderLayout());
        line.add(new JLabel("amount "), BorderLayout.WEST);
        line.add(amountComp, BorderLayout.CENTER);
        input.add(line);
        line = new JPanel(new BorderLayout());
        line.add(new JLabel("time       "), BorderLayout.WEST);
        line.add(timeComp, BorderLayout.CENTER);
        input.add(line);

        JButton sendBtn = new JButton("send");
        sendBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sendRequest();
            }
        });

        JPanel manual = new JPanel(new BorderLayout());
        manual.setBorder(javax.swing.BorderFactory.createTitledBorder("send client requests"));
        manual.add(input, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(sendBtn);
        manual.add(btnPanel, BorderLayout.SOUTH);
        panel.add(manual, BorderLayout.SOUTH);
        setContentPane(panel);
    }

    private void sendRequest() {
        int ssn = Integer.parseInt(ssnComp.getText().replaceAll(" ", ""));
        int amount = Integer.parseInt(amountComp.getText().replaceAll(" ", ""));
        int time = Integer.parseInt(timeComp.getText().replaceAll(" ", ""));
        send(new ClientRequest(ssn, amount, time));
    }

    public abstract void send(ClientRequest request);

    public void addRequest(ClientRequest request) {
        model.addRequest(request);
    }

    public void addReply(ClientRequest request, ClientReply reply) {
        model.addReply(request, reply);
    }
}
