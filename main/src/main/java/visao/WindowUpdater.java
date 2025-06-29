package visao;

import other.StaticObjects;

public class WindowUpdater extends Thread{
	public static boolean acabou = false;
	@Override
	public void run() {
		while(!acabou) {
			StaticObjects.getVTP().update();
		}
	}

}
