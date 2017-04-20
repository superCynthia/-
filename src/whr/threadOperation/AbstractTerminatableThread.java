package whr.threadOperation;

/**
 * ��ֹͣ�ĳ����̡߳�
 * 
 * ģʽ��ɫ��Two_phaseTermination.AbstractTerminatableThread
 *
 */
public abstract class AbstractTerminatableThread extends Thread implements Terminatable {

	// ģʽ��ɫ��Two_phaseTermination.TerminationToken
	public final TerminationToken terminationToken;

	public AbstractTerminatableThread() {
		this(new TerminationToken());
	}

	/**
	 * 
	 * @param terminationToken
	 *            �̼߳乲����̱߳�־ʵ��
	 */
	public AbstractTerminatableThread(TerminationToken terminationToken) {
		super();
		this.terminationToken = terminationToken;
		terminationToken.register(this);
	}

	protected abstract void doRun() throws Exception;

	protected void doCleanup(Exception cause) {
	}

	protected void doTerminate() {
	}

	@Override
	public void run() {
		Exception ex = null;
		try {
			for (;;) {
				// ��ִ���̵߳Ĵ����߼�ǰ���ж��߳�ֹͣ�ı�־
				if (terminationToken.isToShutdown() && terminationToken.reservations.get() <= 0)
					break;
				doRun();
			}
		} catch (Exception e) {
			// ʹ���߳��ܹ���Ӧinterrupt���ö��˳�
			ex = e;
		} finally {
			try {
				doCleanup(ex);
			} finally {
				terminationToken.notifyTreadTermination(this);
			}
		}
	}

	@Override
	public void interrupt() {
		terminate();
	}

	// ����ֹͣ�߳�
	@Override
	public void terminate() {
		terminationToken.setToShutdown(true);
		try {
			doTerminate();
		} finally {
			// ���޴��������������ͼǿ����ֹ�߳�
			if (terminationToken.reservations.get() <= 0)
				super.interrupt();
		}
	}

}
