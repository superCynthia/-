package whr.threadOperation;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import whr.s_ui.MainFrame;

//ģʽ��ɫ��Producer-Consumer.Producer
public class AttachmentProcessor {

	private final String ATTACHMENT_STORE_BASE_DIR = "src/savefiledir";

	// ģʽ��ɫ��Producer-Consumer.Channel
	private static Channel<File> channal = new BlockingQueueChannel<File>(new ArrayBlockingQueue<File>(200));

	// ģʽ��ɫ��Producer-Consumer.Consumer
	private final AbstractTerminatableThread pretreatmentThread = new AbstractTerminatableThread() {

		@Override
		protected void doRun() throws Exception {
			File file = null;
			file = channal.take();

			if (file != null && file.exists()) {
				System.out.println("��channel��ȡ����һ����Ч�ļ�:" + file.getName());
				pretreatmentFile(file);
				terminationToken.reservations.decrementAndGet();
				System.out.println("������һ���ļ���");
				MainFrame.text.setText(MainFrame.text.getText() + "\n�Ѵ����ļ�" + file.getName());
			}
		}

		private void pretreatmentFile(File file) {
			// ģ��Ԥ����ʱ��
			Random rd = new Random();
			try {
				Thread.sleep(rd.nextInt(1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	public void init() {
		pretreatmentThread.terminationToken.setToShutdown(false);
		pretreatmentThread.start();
		MainFrame.text.setText(MainFrame.text.getText() + "\n��ʼĬ�ϴ������");
	}

	public void shutdown() {
		pretreatmentThread.terminate();
		MainFrame.text.setText(MainFrame.text.getText() + "\n���¸���������Ϻ󽫹ر�Ĭ�ϴ������");
	}

	// ���ļ���������channal
	public void saveAttachment(DataInputStream in, String documentId) throws IOException {
		File file = savaAsFile(in, documentId);
		try {
			channal.put(file);
			MainFrame.text.setText(MainFrame.text.getText() + "\n�ѱ����ļ�" + file.getName());
		} catch (InterruptedException e) {
			System.out.println("����channel����ʧ�ܣ�");
		}
		pretreatmentThread.terminationToken.reservations.incrementAndGet();
	}

	// �����ļ�
	private File savaAsFile(DataInputStream in, String documentId) throws IOException {
		String dirName = ATTACHMENT_STORE_BASE_DIR;
		File file = new File(dirName + "/" + documentId);

		FileOutputStream fos = new FileOutputStream(file);
		byte[] inputByte = new byte[1024];
		int length = 0;

		while (true) {
			if (in != null)
				length = in.read(inputByte, 0, inputByte.length);
			if (length == -1)
				break;
			fos.write(inputByte, 0, length);
			fos.flush();
		}

		fos.close();
		in.close();
		return file;
	}

}
