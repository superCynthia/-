package whr.socketThread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import whr.s_ui.MainFrame;

public class Server {

	private ServerSocket serverSocket = null;
	private boolean isClosed = false;
	private Socket socket = null;

	public void startServer() {
		try {
			serverSocket = new ServerSocket(8866);
			isClosed = false;
			Socket socket = null;
			System.out.println("Server start!");

			//���̴߳���ͻ��˵�����
			while (!isClosed) {
				socket = serverSocket.accept();
				MainFrame.text.setText(MainFrame.text.getText() + "\n������" + socket.getInetAddress());

				Thread thread = new Thread(new ServerThread(socket));
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void stopServer() {
		try {
			isClosed = true;
			//�������Լ�����һ����Ч�ı�־������Ϣ����ʹ��isClosed��Ϊtrue��������serverSocket.accept��ȡ���Ӳ��˳�ѭ��
			socket = new Socket();
			socket.connect(new InetSocketAddress("172.26.147.17", 8866));
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("close");
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "����������ʧ�ܣ�", "��ʾ", JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
