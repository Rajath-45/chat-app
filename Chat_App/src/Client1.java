import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Client1 implements ActionListener {
    JTextField text;
    static JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static PrintWriter pw;

    Client1() {
        f.setLayout(null);

        JPanel jp = new JPanel();
        jp.setBackground(new Color(7, 94, 84));
        jp.setBounds(0, 0, 450, 70);
        jp.setLayout(null);
        f.add(jp);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/back.png"));
        Image i2 = i1.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 45, 45);
        jp.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/vk.jpg"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(50, 3, 70, 70);
        jp.add(profile);

        JLabel name = new JLabel("Virat");
        name.setBounds(120, 15, 99, 18);
        name.setForeground(Color.orange);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        jp.add(name);

        JLabel status = new JLabel("online");
        status.setBounds(120, 35, 101, 20);
        status.setForeground(Color.white);
        status.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        jp.add(status);

        a1 = new JPanel();
        a1.setBounds(5, 75, 440, 570);
        f.add(a1);

        text = new JTextField();
        text.setBounds(135, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        JButton send = new JButton("Send");
        send.setBounds(5, 655, 123, 40);
        send.setBackground(Color.RED);
        send.setForeground(Color.white);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.BOLD, 16));
        f.add(send);

        f.setSize(450, 700);
        f.setLocation(200, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.DARK_GRAY);

        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String out = text.getText();
            JPanel p2 = formatLabel(out, true); // Assuming the message is being sent

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));
            a1.add(vertical, BorderLayout.PAGE_START);

            pw.println(out);
            pw.flush();

            text.setText("");

            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static JPanel formatLabel(String message, boolean isSent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 110px\">" + message + "</p></html>");
        panel.add(output);
        output.setFont(new Font("Tahoma", Font.BOLD, 16)); // Set font style to bold
        output.setForeground(Color.white); // Set text color to white

        if (isSent) {
            output.setBackground(new Color(18, 140, 126)); // Color for sent messages
        } else {
            output.setBackground(new Color(67, 69, 67)); // Color for received messages
        }

        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);

        return panel;
    }


    public static void main(String args[]) throws UnknownHostException, IOException {
        new Client1();
        try (Socket link = new Socket("localhost", 3020)) {
            pw = new PrintWriter(link.getOutputStream());
            InputStreamReader ip = new InputStreamReader(link.getInputStream());
            BufferedReader b = new BufferedReader(ip);

            while (true) {
                a1.setLayout(new BorderLayout());
                String msg = b.readLine();
                JPanel panel = formatLabel(msg, false); // Assuming the message is received

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel, BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                a1.add(vertical, BorderLayout.PAGE_START);
                f.validate();
            }
        }
    }
}
