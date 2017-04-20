package whr.socketThread;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import whr.threadOperation.AttachmentProcessor;

public class ServerThread implements Runnable {

	private Socket socket = null;

	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		InputStream is = null;
		DataInputStream dis = null;

		try {
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			String title = dis.readUTF();
			//����������Է������Ĺر���ʾ���ӣ������̴߳���Task
			if (!"close".equals(title))
				new AttachmentProcessor().saveAttachment(dis, title);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (dis != null)
					dis.close();
				if (is != null)
					is.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
