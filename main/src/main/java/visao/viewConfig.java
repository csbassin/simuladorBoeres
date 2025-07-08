package visao;

import config.ConfigData;
import modelo.Execucao.CPU;
import other.StaticObjects;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class viewConfig {

	private JFrame frame;
	private JTextField txtQtdQMem;
	private JTextField txtQuadroSize;
	private JTextField txtQtdETLB;
	private JTextField txtPath;
	private JComboBox<String> cmbPolSubs;
	private JButton btnIniciar;
	private JTextField txtTempoAcessoMP;
	private JTextField txtTempoAcessoMS;
	private JTextField txtTempoAcessoTLB;
	private JTextField txtCicloCPU;
	private JCheckBox chkStepByStep;
	private JButton btnNextStep;
	private JLabel lblCurrentInstruction;

	public viewConfig() {
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame = new JFrame("Configuração do Simulador de Memória Virtual");
		frame.setBounds(100, 100, 520, 560);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		JLabel lblTitulo = new JLabel("Configuração do Simulador");
		lblTitulo.setBounds(10, 11, 484, 31);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitulo.setForeground(Color.WHITE);
		frame.getContentPane().add(lblTitulo);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 53, 484, 2);
		frame.getContentPane().add(separator);

		int yPos = 70;
		int lblWidth = 320;
		int txtWidth = 100;
		int height = 25;
		int padding = 10;

		addLabelAndTextField("Quantidade de Quadros na Memória:", String.valueOf(ConfigData.quantidadeQuadros), yPos, lblWidth, txtWidth, height);
		txtQtdQMem = (JTextField) frame.getContentPane().getComponent(frame.getContentPane().getComponentCount() - 1);

		yPos += height + padding;
		addLabelAndTextField("Tamanho do Quadro/Página (bytes):", String.valueOf(ConfigData.quadroSize), yPos, lblWidth, txtWidth, height);
		txtQuadroSize = (JTextField) frame.getContentPane().getComponent(frame.getContentPane().getComponentCount() - 1);

		yPos += height + padding;
		addLabelAndTextField("Tamanho da TLB (entradas):", String.valueOf(ConfigData.qntdPagTlB), yPos, lblWidth, txtWidth, height);
		txtQtdETLB = (JTextField) frame.getContentPane().getComponent(frame.getContentPane().getComponentCount() - 1);

		yPos += height + padding;
		addLabelAndTextField("Caminho do Arquivo de Entrada:", ConfigData.pathInput, yPos, lblWidth, txtWidth, height);
		txtPath = (JTextField) frame.getContentPane().getComponent(frame.getContentPane().getComponentCount() - 1);

		yPos += height + padding;
		addLabelAndTextField("Tempo de Acesso MP (ms):", String.valueOf(ConfigData.tempoAcessoMP), yPos, lblWidth, txtWidth, height);
		txtTempoAcessoMP = (JTextField) frame.getContentPane().getComponent(frame.getContentPane().getComponentCount() - 1);

		yPos += height + padding;
		addLabelAndTextField("Tempo de Acesso MS (ms):", String.valueOf(ConfigData.tempoAcessoMS), yPos, lblWidth, txtWidth, height);
		txtTempoAcessoMS = (JTextField) frame.getContentPane().getComponent(frame.getContentPane().getComponentCount() - 1);

		yPos += height + padding;
		addLabelAndTextField("Tempo de Acesso TLB (ms):", String.valueOf(ConfigData.tempoAcessoTLB), yPos, lblWidth, txtWidth, height);
		txtTempoAcessoTLB = (JTextField) frame.getContentPane().getComponent(frame.getContentPane().getComponentCount() - 1);

		yPos += height + padding;
		addLabelAndTextField("Ciclo da CPU (ms):", String.valueOf(ConfigData.cicloCPU), yPos, lblWidth, txtWidth, height);
		txtCicloCPU = (JTextField) frame.getContentPane().getComponent(frame.getContentPane().getComponentCount() - 1);

		yPos += height + padding;
		JLabel lblPolSubs = new JLabel("Política de Substituição de Página:");
		lblPolSubs.setForeground(Color.WHITE);
		lblPolSubs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblPolSubs.setBounds(20, yPos, 250, height);
		frame.getContentPane().add(lblPolSubs);

		cmbPolSubs = new JComboBox<>(new String[]{"LRU", "CLOCK"});
		cmbPolSubs.setBounds(280, yPos, 180, height);
		cmbPolSubs.setSelectedItem(ConfigData.tipoSubstituicaoPaginas);
		frame.getContentPane().add(cmbPolSubs);

		yPos += height + padding;
		chkStepByStep = new JCheckBox("Executar em modo Passo a Passo");
		chkStepByStep.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		chkStepByStep.setForeground(Color.WHITE);
		chkStepByStep.setBackground(Color.DARK_GRAY);
		chkStepByStep.setBounds(15, yPos, 300, height);
		frame.getContentPane().add(chkStepByStep);

		yPos += height + padding;
		lblCurrentInstruction = new JLabel("Instrução Atual: (simulação não iniciada)");
		lblCurrentInstruction.setFont(new Font("Monospaced", Font.BOLD, 14));
		lblCurrentInstruction.setForeground(Color.WHITE);
		lblCurrentInstruction.setBounds(15, yPos, 480, height);
		frame.getContentPane().add(lblCurrentInstruction);

		// Armazena a referência para a UI poder atualizar
		WindowData.lblCurrentInstruction = lblCurrentInstruction;

		yPos += height + 20;


		// Cria o botão "Iniciar"
		btnIniciar = new JButton("Iniciar Simulação");
		btnIniciar.addActionListener(e -> iniciarSimulacao());
		btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnIniciar.setForeground(Color.WHITE);
		btnIniciar.setBackground(new Color(0, 120, 0));
		btnIniciar.setBorder(new LineBorder(Color.WHITE));
		frame.getContentPane().add(btnIniciar);

		// Cria o botão "Próxima Instrução"
		btnNextStep = new JButton("Próxima Instrução");
		btnNextStep.addActionListener(e -> WindowData.stepSemaphore.release());
		btnNextStep.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnNextStep.setEnabled(false);
		frame.getContentPane().add(btnNextStep);

		// Define a posição dos botões
		btnIniciar.setBounds(40, yPos, 200, 35);
		btnNextStep.setBounds(260, yPos, 200, 35);

		// salva referência do botão Próxima Instrução
		WindowData.btnNextStep = btnNextStep;

	}

	private void addLabelAndTextField(String labelText, String textFieldText, int y, int lblWidth, int txtWidth, int height) {
		JLabel label = new JLabel(labelText);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		label.setBounds(20, y, lblWidth, height);
		frame.getContentPane().add(label);

		JTextField textField = new JTextField(textFieldText);
		textField.setBounds(lblWidth + 30, y, txtWidth + 50, height);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
	}

	private void iniciarSimulacao() {
		try {
			ConfigData.quantidadeQuadros = Integer.parseInt(txtQtdQMem.getText());
			ConfigData.quadroSize = Integer.parseInt(txtQuadroSize.getText());
			ConfigData.qntdPagTlB = Integer.parseInt(txtQtdETLB.getText());
			ConfigData.pathInput = txtPath.getText();
			ConfigData.tipoSubstituicaoPaginas = (String) cmbPolSubs.getSelectedItem();

			ConfigData.tempoAcessoMP = Integer.parseInt(txtTempoAcessoMP.getText());
			ConfigData.tempoAcessoMS = Integer.parseInt(txtTempoAcessoMS.getText());
			ConfigData.tempoAcessoTLB = Integer.parseInt(txtTempoAcessoTLB.getText());
			ConfigData.cicloCPU = Integer.parseInt(txtCicloCPU.getText());

			WindowData.lblCurrentInstruction.setText("Instrução Atual: (aguardando início)");
			WindowData.stepByStepMode = chkStepByStep.isSelected();

			btnIniciar.setEnabled(false);
			if (WindowData.stepByStepMode) {
				btnNextStep.setEnabled(true);
				btnIniciar.setText("Simulação Iniciada");
			} else {
				btnIniciar.setText("Simulando...");
			}
			btnIniciar.setBackground(Color.GRAY);

			WindowData.vtp = new ViewTabelaPaginas();
			WindowData.vtlb = new ViewTLB();
			WindowData.vmp = new ViewMemoriaPrincipal();
			WindowData.vfe = new ViewFilasDeEstado();

			CPU cpu = new CPU();
			WindowData.cpu = cpu;
			cpu.start();

			new WindowUpdater().start();

		} catch (NumberFormatException e1) {
			JOptionPane.showMessageDialog(frame, "Digite os números corretamente. Apenas inteiros são válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(frame, "Ocorreu um erro ao iniciar: " + e2.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
}