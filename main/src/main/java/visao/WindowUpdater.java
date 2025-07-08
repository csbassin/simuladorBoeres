package visao;

import config.ConfigData;

import javax.swing.*;

public class WindowUpdater extends Thread {
	@Override
	public void run() {
		while (!WindowData.acabou) {
			try {
				SwingUtilities.invokeLater(() -> {
					if (WindowData.vtp != null) {
						WindowData.vtp.update();
					}
					if (WindowData.vtlb != null) {
						WindowData.vtlb.update();
					}
					if (WindowData.vfe != null) {
						WindowData.vfe.update();
					}
					if (WindowData.lblCurrentInstruction != null) {
						WindowData.lblCurrentInstruction.setText("Instrução Atual: " + WindowData.currentInstructionText);
					}
					if (WindowData.vmp != null) {
						WindowData.vmp.update();
					}

				});
				if (WindowData.stepByStepMode) {
					Thread.sleep(100);
				} else {
					Thread.sleep(ConfigData.cicloCPU);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}

		// mostrar estado final
		SwingUtilities.invokeLater(() -> {
			if (WindowData.vtp != null) {
				WindowData.vtp.update();
			}
			if (WindowData.vtlb != null) {
				WindowData.vtlb.update();
			}
			if (WindowData.lblCurrentInstruction != null) {
				WindowData.lblCurrentInstruction.setText("Instrução Atual: " + WindowData.currentInstructionText);
			}
			if (WindowData.vfe != null) {
				WindowData.vfe.update();
			}
		});
		System.out.println("Thread de atualização da UI encerrada.");
	}
}