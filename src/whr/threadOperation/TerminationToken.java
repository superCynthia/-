package whr.threadOperation;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * �߳�ֹͣ��־
 *
 */

public class TerminationToken {

	// ʹ��volatile���Σ��Ա�֤������ʽ��������¸ñ������ڴ�ɼ���
	protected static volatile boolean toShutdown = false;
	// �ṩԭ�Ӳ�����Integer����,ʵ���̰߳�ȫ��������¼����TerminationTokenʵ���Ŀ�ֹͣ�̵߳�����
	public final AtomicInteger reservations = new AtomicInteger(0);

	/*
	 * �ڶ����ֹͣ�߳�ʵ������һ��TerminationTokenʵ��������£��ö������ڼ�¼��Щ����
	 * TerminationTokenʵ���Ŀ�ֹͣ�̣߳��Ա㾡���ܼ�����������£�ʵ����Щ�̵߳�ֹͣ
	 */
	private final Queue<WeakReference<Terminatable>> coordinatedThreads;

	public TerminationToken() {
		coordinatedThreads = new ConcurrentLinkedQueue<WeakReference<Terminatable>>();
	}

	public boolean isToShutdown() {
		return toShutdown;
	}

	protected void setToShutdown(boolean toShutdown) {
		TerminationToken.toShutdown = toShutdown;
	}

	protected void register(Terminatable thread) {
		coordinatedThreads.add(new WeakReference<Terminatable>(thread));
	}

	/**
	 * ֪ͨTerminationTokenʵ���������ʵ�������п�ֹͣ�߳��е�һ���߳�ֹͣ�ˣ� �Ա���ֹͣ����δ��ֹͣ���߳�.
	 * 
	 * @param thread
	 *            ��ֹͣ���߳�
	 */
	protected void notifyTreadTermination(Terminatable thread) {
		WeakReference<Terminatable> wrThread;
		Terminatable otherThread;
		while (null != (wrThread = coordinatedThreads.poll())) {
			otherThread = wrThread.get();
			if (null != otherThread && otherThread != thread)
				otherThread.terminate();
		}
	}

}
