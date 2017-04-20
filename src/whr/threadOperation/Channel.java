package whr.threadOperation;

/**
 * ��ͨ�������߽��г���
 * 
 * @param <P>
 *            "��Ʒ"����
 */
public interface Channel<P> {
	/**
	 * ��ͨ����ȡ��һ��"��Ʒ"
	 * 
	 * @return "��Ʒ"
	 * @throws InterruptedException
	 */
	P take() throws InterruptedException;

	/**
	 * ��ͨ���д洢һ��"��Ʒ"
	 * 
	 * @param product
	 *            "��Ʒ"
	 * @throws InterruptedException
	 */
	void put(P product) throws InterruptedException;
}
