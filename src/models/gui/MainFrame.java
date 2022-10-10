package models.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    JTextArea txtChat;

    public MainFrame(int width, int height){
        super("PRO2 ChatClient");
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initGui();
        setVisible(true);
    }

    private void initGui(){
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.add(initLoginPanel(), BorderLayout.NORTH);
        panelMain.add(initChatPanel(), BorderLayout.CENTER);
        panelMain.add(initMessagePanel(), BorderLayout.SOUTH);
        add(panelMain);
    }

    private JPanel initLoginPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Username"));
        JTextField txtInputUsername = new JTextField("",30);
        panel.add(txtInputUsername);
        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("button login clicked");
            }
        });
        panel.add(btnLogin);
        return panel;
    }

    private JPanel initChatPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        txtChat = new JTextArea();
        txtChat.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(txtChat);

        panel.add(scrollPane);

        /*
        for (int i = 0; i < 50; i++) {
            txtChat.append("Message" + i + "\n");
        }
         */

        return panel;
    }

    private JPanel initMessagePanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtInputMessage = new JTextField("",50);
        panel.add(txtInputMessage);
        JButton btnSendMessage = new JButton("Send");
        btnSendMessage.addActionListener(e -> {
            System.out.println("btn send clicked - " + txtInputMessage.getText());

            txtChat.append(txtInputMessage.getText() + "\n");
            txtInputMessage.setText("");
        });
        panel.add(btnSendMessage);

        return panel;
    }
}
