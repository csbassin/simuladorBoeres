package visao;

import modelo.Execucao.CPU;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.concurrent.Semaphore;

public class WindowData {
	public static volatile boolean acabou = false;
	public static CPU cpu;
	public static ViewTabelaPaginas vtp;
	public static ViewTLB vtlb;
	public static ViewMemoriaPrincipal vmp;
	public static ViewFilasDeEstado vfe;

	private static int pageFaults = 0;

	public static volatile boolean stepByStepMode = false;
	public static final Semaphore stepSemaphore = new Semaphore(0);
	public static JButton btnNextStep;
	public static JLabel lblCurrentInstruction;
	public static volatile String currentInstructionText = "";


	public static long tempoLogico = 0; // Contador para o tempo de uso
	public static volatile int ponteiroClock = -1; // onde o ponteiro do clock aponta


	public synchronized static void ocorreuPageFault() {
		pageFaults++;
	}

	public synchronized static int getPageFaults() {
		return pageFaults;
	}
}