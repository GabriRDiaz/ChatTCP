package server.side;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerSide extends JFrame implements ActionListener {
	private static final long servialVersionUID = 1L;
	static ServerSocket sv;
	static final int PORT = 44444;
	static int CONNECTIONS = 0;
	static int ONLINE = 0;
	static int MAX = 10;
	
	static JTextField msg = new JTextField("");
	static JTextField msg2 = new JTextField("");
	private JScrollPane scp;
	static JTextArea txtarea;
	JButton leave = new JButton("Leave");
	static Socket table[] = new Socket[10];

	public ServerSide() {
		super("Server side window");
		setLayout(null);
		msg.setBounds(10,10,400,30);
		add(msg);
		msg.setEditable(false);
		
		msg2.setBounds(10,348,400,30);
		add(msg2);
		msg2.setEditable(false);
		
		txtarea = new JTextArea();
		scp = new JScrollPane(txtarea);
		scp.setBounds(10,50,400,300);
		add(scp);
		
		leave.setBounds(420,10,100,30);
		add(leave);
		
		txtarea.setEditable(false);
		leave.addActionListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==leave) {
			try {
				sv.close();
			}catch(IOException ex) {
				ex.printStackTrace();
			}
			System.exit(0);
		}
	}
	public static void main(String[] args) throws IOException {
		sv = new ServerSocket(PORT);
		System.out.println("Server init");
		ServerSide window = new ServerSide();
		window.setBounds(0, 0, 540, 400);
		window.setVisible(true);
		msg.setText("Online clients: "+0);
		
		while(CONNECTIONS < MAX) {
			Socket s = new Socket();
			try {
				s = sv.accept();
			}catch(SocketException se) {
				break;
			}
			table[CONNECTIONS] = s;
			CONNECTIONS++;
			ONLINE++;
			ServerThread thread = new ServerThread(s);
			thread.start();
		}
		if(!sv.isClosed())
			try {
				msg2.setForeground(Color.red);
				msg2.setText("MAX CLIENTS REACHED: "+CONNECTIONS);
				sv.close();
			}catch(IOException exc) {
				exc.printStackTrace();
			}
		System.out.println("Server closed...");
	}
}
