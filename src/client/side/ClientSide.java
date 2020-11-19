package client.side;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientSide extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	Socket socket = null;
	DataInputStream is;
	DataOutputStream os;
	
	String name;
	static JTextField msg = new JTextField();
	private JScrollPane scp;
	static JTextArea txta1;
	JButton send = new JButton("Send");
	JButton logout = new JButton("Logout");
	boolean repeat = true;
	
	public ClientSide(Socket s, String name) {
		super("CLIENT SIDE WINDOW: "+name);
		setLayout(null);
		msg.setBounds(10,10,400,30);
		add(msg);
		txta1 = new JTextArea();
		scp = new JScrollPane(txta1);
		add(scp);
		send.setBounds(420,10,100,30);
		add(send);
		logout.setBounds(420,50,100,30);
		add(logout);
		txta1.setEditable(false);
		send.addActionListener(this);
		logout.addActionListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		socket = s;
		this.name=name;
		try {
			is = new DataInputStream(socket.getInputStream());
			os = new DataOutputStream(socket.getOutputStream());
			String txt =name + " joined the chat";
			os.writeUTF(txt);
		}catch(IOException e) {
			System.out.println("IO ERROR");
			e.printStackTrace();
			System.exit(0);
		}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==send) {
			String txt= name+ ":" + msg.getText();
		try {
			msg.setText("");
			os.writeUTF(txt);
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		}
		if(e.getSource() == logout) {
			String txt = name+ " left the chat";
			try {
				os.writeUTF(txt);
				os.writeUTF("*");
				repeat = false;
			}catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	public void execute() {
		String txt = "";
		while(repeat) {
			try {
				txt = is.readUTF();
				txta1.setText(txt);
			}catch(IOException e) {
				JOptionPane.showMessageDialog(null, "COULD NOT CONNECT THE SV\n"+e.getMessage(), "Connection error", JOptionPane.ERROR_MESSAGE);
				repeat = false;
			}
		}
		try {
			socket.close();
			System.exit(0);
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		int port = 44444;
		String name = JOptionPane.showInputDialog("Write your name: ");
		Socket s= null;
		try {
			s = new Socket("localhost",port);
		}catch(IOException e) {
			JOptionPane.showMessageDialog(null, "COULD NOT CONNECT THE SV\n"+e.getMessage(), "Connection error", JOptionPane.ERROR_MESSAGE);	
			System.exit(0);
		}
		if(!name.trim().equals("")) {
			ClientSide client = new ClientSide(s,name);
			client.setBounds(0,0,540,400);
			client.setVisible(true);
			client.execute();
		}else {
			System.out.println("Name is clear");
		}
	}
}
