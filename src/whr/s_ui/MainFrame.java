package whr.s_ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import whr.socketThread.Server;
import whr.threadOperation.AttachmentProcessor;

public class MainFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static Server server = null;

	//static:�Ա����������ж�JTextArea�е����ݽ��и��£�volatile����֤������ʽ��������¸ñ������ڴ�ɼ���
	public static volatile JTextArea text = new JTextArea();
	private JButton jb3 = new JButton("��ʼִ��Ĭ�ϲ���");
	private JButton jb4 = new JButton("�ر�Ĭ�ϲ���");
	private JScrollPane scroll = new JScrollPane(text);

	public MainFrame(boolean isShow) {
		setTitle("�ļ����������");
		init();
		setSize(500, 350);
		// ��ȡ��Ļ�ߴ�
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// ��ȡ������Ĵ���ߴ�
		Dimension frameSize = this.getSize();
		// �������洰�����
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("ִ��windowclosed");
				super.windowClosing(e);
				if (server != null)
					server.stopServer();
				else
					System.out.println("serversocket=null");
				System.exit(-1);
			}
		});
		this.setVisible(isShow);
		//server�Ĳ�������ѭ����������Ҫ�����������������ڹر��¼��򴰿���ʾ�ȣ�����
		server = new Server();
		server.startServer();
	}

	public void init() {
		JPanel jp = new JPanel();
		BoxLayout boxLayoutY = new BoxLayout(jp, BoxLayout.Y_AXIS);
		BoxLayout boxLayoutX;
		jp.setLayout(boxLayoutY);

		// ��岼��
		// ��һ��
		JPanel jpr1 = new JPanel();
		boxLayoutX = new BoxLayout(jpr1, BoxLayout.X_AXIS);
		jpr1.setLayout(boxLayoutX);
		jpr1.add(Box.createHorizontalStrut(30));
		jpr1.add(this.jb3);
		jpr1.add(Box.createHorizontalStrut(30));
		jpr1.add(this.jb4);
		jpr1.add(Box.createHorizontalStrut(30));
		jb3.addActionListener(this);
		jb4.addActionListener(this);

		jp.add(Box.createVerticalStrut(30));
		jp.add(jpr1);
		jp.add(Box.createVerticalStrut(20));
		// ������
		scroll.setPreferredSize(new Dimension(320, 150));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jp.add(scroll);
		jp.add(Box.createVerticalStrut(30));

		this.add(jp);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("��ʼִ��Ĭ�ϲ���")) {
			AttachmentProcessor ap = new AttachmentProcessor();
			ap.init();
		} else if (e.getActionCommand().equals("�ر�Ĭ�ϲ���")) {
			new AttachmentProcessor().shutdown();
		}
	}

}
