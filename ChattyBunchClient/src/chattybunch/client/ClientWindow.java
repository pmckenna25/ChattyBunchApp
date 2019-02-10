package chattybunch.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ClientWindow extends javax.swing.JFrame {

    private JTextField messageField;
    //private static JTextArea textArea;
    private static JTextPane tpane;
    private JButton btnSend;
    private JPanel southPane;
    private JPanel northPane;
    private JPanel eastPane;
    private JScrollPane contacts;
    private static JTextArea contactText;
    private JScrollPane jScrollPane2;
    private Client client;

    public ClientWindow() {
        initComponents();
        String name = JOptionPane.showInputDialog("Enter name");
        client = new Client(name, "localhost", 52864);
    }

    private void initComponents() {

        southPane = new javax.swing.JPanel();
        messageField = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        northPane = new javax.swing.JPanel();
        eastPane = new JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        //textArea = new javax.swing.JTextArea();
        tpane = new JTextPane();
        contacts = new JScrollPane();
        contactText = new JTextArea();
        Font front = new Font("Lucida Console", Font.PLAIN, 12);
        contactText.setFont(front);
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(messageField.getText().length()> 0){
                    client.send(client.getName() + " says: " + messageField.getText().trim());
                    messageField.setText("");
                }else{
                    JOptionPane.showMessageDialog(null, "You cannot send a blank field");
                }
            }
        };
        messageField.addActionListener(action);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chatty Bunch");
        setLocation(new java.awt.Point(600, 300));
        setPreferredSize(new java.awt.Dimension(550, 400));
        setResizable(false);
        EmptyBorder eb = new EmptyBorder(new Insets(10,10,10,10));
        tpane.setBorder(eb);
        tpane.setMargin(new Insets(5, 5, 5, 5));
        southPane.setLayout(new java.awt.BorderLayout());

        messageField.setColumns(60);
        southPane.add(messageField, java.awt.BorderLayout.WEST);

        btnSend.setText("Send");
        btnSend.addActionListener(e -> {
            client.send(client.getName() + " says: " + messageField.getText());
            messageField.setText("");
        });
        southPane.add(btnSend, java.awt.BorderLayout.LINE_END);

        getContentPane().add(southPane, java.awt.BorderLayout.SOUTH);
        eastPane.setLayout(new BorderLayout());
        northPane.setLayout(new java.awt.BorderLayout());
        contactText.setEditable(false);
        contactText.setColumns(12);
        contactText.setRows(5);
        contacts.setViewportView(contactText);
//        textArea.setEditable(false);
//        textArea.setColumns(20);
//        textArea.setRows(5);
        jScrollPane2.setViewportView(tpane);
        northPane.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        northPane.add(contacts, BorderLayout.EAST);
        getContentPane().add(northPane, java.awt.BorderLayout.CENTER);
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                client.disconnect();
                e.getWindow().dispose();
            }
        });

        pack();
    }

    public static void printToConsole(String message) {
        appendToPane(tpane, message+"\n", Color.BLACK);
        //textArea.setText(textArea.getText() + message + "\n");
    }
    
    public static void printToContacts(List<String> contacts) {
        
        String message = contacts.stream().collect(Collectors.joining("\n"));
        contactText.setText("Contacts:"+message);
    }
    
    public static void printAsServer(String msg) {
        
        appendToPane(tpane, msg+"\n", Color.RED);
        //textArea.setForeground(Color.red);
        
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    ClientWindow window = new ClientWindow();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
    
    private static void appendToPane(JTextPane tp, String msg, Color c){
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
}
