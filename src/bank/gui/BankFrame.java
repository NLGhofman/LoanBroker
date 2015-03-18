/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.gui;

import bank.Bank;
import bank.BankQuoteReply;
import bank.BankQuoteRequest;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import main.JMSSettings;

/**
 * This class is used as GUI for the Bank application. The Frame contains a
 * table with received BankQuoteRequests and sent BankQuoteReplies. The Frame
 * positions itself in the top left corner of the screen. If multiple instances
 * of this frame are made, they will be positioned to the right of the previous
 * frame instance.
 *
 * @author Maja Pesic
 */
public class BankFrame extends JFrame
{

    private static final int BORDER = 20;
    private static int X_POSITION = BORDER;
    private JTextField interestComp = new JTextField();
    private JTextField errorComp = new JTextField("0");
    private JButton sendBtn = new JButton("send");
    private BankTableDataModel model = new BankTableDataModel();
    private BankTable table = new BankTable(model);
    private Bank bankModel;

    public BankFrame(Bank bankModel, String bankName)
    {
        super();
        this.bankModel = bankModel;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(bankName);
        initComponents();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) ((screenSize.getWidth() - 4 * BORDER) / 3);
        int h = (int) ((screenSize.getHeight() - 3 * BORDER) / 2);
        setSize(new Dimension(w, h));

        int x = X_POSITION;
        X_POSITION = X_POSITION + (int) getSize().getWidth() + BORDER;
        int y = BORDER;
        setLocation(x, y);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {

            public void valueChanged(ListSelectionEvent e)
            {
                selectedRequestChanged();
            }
        });
    }

    private void selectedRequestChanged()
    {
        boolean replyCanBeSent = false;
        BankQuoteRequest request = getSelectedRequest();
        if (request != null)
        { // start sending reply only if a valid request is selected
            BankQuoteReply reply = getSelectedReply();
            replyCanBeSent = (reply == null);
        }
        interestComp.setEnabled(replyCanBeSent);
        errorComp.setEnabled(replyCanBeSent);
        sendBtn.setEnabled(replyCanBeSent);
    }

    private void initComponents()
    {
        JPanel panel = new JPanel();
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("received requests"));
        panel.setLayout(new BorderLayout());
        panel.setOpaque(true);

        panel.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        table.setFillsViewportHeight(true);

        JPanel input = new JPanel(new GridLayout(3, 0));

        JPanel line = new JPanel(new BorderLayout());
        JLabel interestLabel = new JLabel("interest ");
        line.add(interestLabel, BorderLayout.WEST);
        line.add(interestComp, BorderLayout.CENTER);
        input.add(line);

        line = new JPanel(new BorderLayout());
        JLabel errorLabel = new JLabel("error      ");
        line.add(errorLabel, BorderLayout.WEST);
        line.add(errorComp, BorderLayout.CENTER);
        input.add(line);

        sendBtn.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                BankQuoteRequest request = getSelectedRequest();
                if (request != null)
                { // start sending reply only if a valid request is selected
                    BankQuoteReply reply = getSelectedReply();
                    if (reply == null)
                    { // send new reply only if no reply has been sent yet for this request
                        double interest = Double.parseDouble(interestComp.getText());
                        int error = Integer.parseInt(errorComp.getText());
                        if (onSendBankReplyClicked(request, interest, error))
                        {
                            selectedRequestChanged();
                        }
                    }
                }
            }
        });

        JPanel replyCompnent = new JPanel(new BorderLayout());
        String title = "";
        if (JMSSettings.getRunMode() == JMSSettings.RunMode.AUTOMATICALLY)
        {

            title = "Automatic runmode is on: replies are randomly generated and automatically sent back";
            interestComp.setVisible(false);
            errorComp.setVisible(false);
            sendBtn.setVisible(false);
            interestLabel.setVisible(false);
            errorLabel.setVisible(false);
        }
        else
        {
            title = "Manual runmode is on: send reply manually after selecting a row in the table above";
        }
        replyCompnent.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
        replyCompnent.add(input, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(sendBtn);
        replyCompnent.add(btnPanel, BorderLayout.SOUTH);

        panel.add(replyCompnent, BorderLayout.SOUTH);

//        sendBtn.setEnabled(!debug_mode);
//        interestComp.setEnabled(!debug_mode);
//        errorComp.setEnabled(!debug_mode);
        setContentPane(panel);
    }

    private BankQuoteRequest getSelectedRequest()
    {
        int selected_row_index = table.getSelectedRow();
        return model.getRequest(selected_row_index);
    }

    private BankQuoteReply getSelectedReply()
    {
        int selected_row_index = table.getSelectedRow();
        return model.getReply(selected_row_index);
    }

    public boolean onSendBankReplyClicked(BankQuoteRequest request, double interest, int error)
    {
        return bankModel.onSendBankReplyClicked(request, interest, error);
    }

    public void addRequest(BankQuoteRequest request)
    {
        model.addRequest(request);
    }

    public void addReply(BankQuoteRequest request, BankQuoteReply reply)
    {
        model.addReply(request, reply);
    }
}
