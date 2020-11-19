package server.side;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread {
	DataInputStream is;
	Socket socket = null;
	
	public ServerThread(Socket s) {
		socket = s;
		try {
			is = new DataInputStream(socket.getInputStream());
		}catch(IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
	}
	public void run() {
		ServerSide.msg.setText("CONNECTED CLIENTS: "+ServerSide.ONLINE);
		String txt = ServerSide.txtarea.getText();
		sendMsg(txt);
		
		while(true) {
			String str = "";
			try {
				str = is.readUTF();
				if(str.trim().equals("*")) {
					ServerSide.ONLINE--;
					ServerSide.msg.setText("CONNECTED CLIENTS: "+ServerSide.ONLINE);
					break;
				}
				ServerSide.txtarea.append(str + "\n");
				txt = ServerSide.txtarea.getText();
				sendMsg(txt);
			}catch(Exception e) {
				e.printStackTrace();
				break;
			}}
		}
		private void sendMsg(String txt) {
		int i;
		for(i=0; i<ServerSide.CONNECTIONS; i++) {
			Socket s1 = ServerSide.table[i];
			try {
				DataOutputStream os = new DataOutputStream(s1.getOutputStream());
				os.writeUTF(txt);
			}catch (SocketException se) {}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
