package main.java.visao;

import main.java.config.ConfigData;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class viewConfig {
	
	private static final String TITLEARRAY[] = {"Válido"," Número da Página","Presente", "Modificada", "Utilizada", "Tempo do Último Uso", "Número do Quadro"};
	private JFrame frame;
	private final JLabel lblNewLabel = new JLabel("Configuração do simulador");
	private JButton btnIniciar;
	private JLabel lblTamanhoDaMemria;
	private JLabel lblTamanhoDoQuadro;
	private JLabel lblQuantidadeDeEntradas;
	private JLabel lblLocalDoArquivo;
	private JTextField txtQtdQMem;
	private JTextField txtQuadroSize;
	private JTextField txtQtdETLB;
	private JTextField txtPath;

	public viewConfig() {
		initialize();

	}
	
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

		frame = new JFrame();
		frame.setVisible(true);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setLayout(null);
		lblNewLabel.setBounds(10, 11, 304, 31);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		lblNewLabel.setForeground(Color.WHITE);
		frame.getContentPane().add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 53, 450, 5);
		separator.setMinimumSize(new Dimension(300, 5));
		separator.setPreferredSize(new Dimension(414, 5));
		separator.setMaximumSize(new Dimension(1920, 5));
		
		frame.getContentPane().add(separator);
		
		btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int qtdqmp = Integer.parseInt(txtQtdQMem.getText());
					int tpsize = Integer.parseInt(txtQtdETLB.getText());
					int quadroSize = Integer.parseInt(txtQuadroSize.getText());
					ConfigData.quantidadeQuadros = qtdqmp;
					ConfigData.tpSize = tpsize;
					ConfigData.quadroSize = quadroSize;
					ConfigData.pathInput = txtPath.getText();
				}catch(NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Digite o número corretamente. Apenas Inteiros são válidos.");
				}
				
			}
						
		});
		btnIniciar.setBounds(369, 204, 91, 31);
		btnIniciar.setBackground(Color.DARK_GRAY);
		btnIniciar.setForeground(Color.WHITE);
		btnIniciar.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnIniciar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		frame.getContentPane().add(btnIniciar);
		
		lblTamanhoDaMemria = new JLabel("Quantidade de quadros da Memória Principal:");
		lblTamanhoDaMemria.setBounds(20, 64, 325, 22);
		lblTamanhoDaMemria.setForeground(Color.WHITE);
		lblTamanhoDaMemria.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		frame.getContentPane().add(lblTamanhoDaMemria);
		
		lblTamanhoDoQuadro = new JLabel("Tamanho do quadro da Memória Principal:");
		lblTamanhoDoQuadro.setBounds(21, 91, 296, 22);
		lblTamanhoDoQuadro.setForeground(Color.WHITE);
		lblTamanhoDoQuadro.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		frame.getContentPane().add(lblTamanhoDoQuadro);
		
		lblQuantidadeDeEntradas = new JLabel("Quantidade de Entradas da TLB:");
		lblQuantidadeDeEntradas.setBounds(20, 120, 219, 22);
		lblQuantidadeDeEntradas.setForeground(Color.WHITE);
		lblQuantidadeDeEntradas.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		frame.getContentPane().add(lblQuantidadeDeEntradas);
		
		lblLocalDoArquivo = new JLabel("Local do arquivo de entrada:");
		lblLocalDoArquivo.setBounds(20, 152, 196, 22);
		lblLocalDoArquivo.setForeground(Color.WHITE);
		lblLocalDoArquivo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		frame.getContentPane().add(lblLocalDoArquivo);
		
		txtQtdQMem = new JTextField();
		txtQtdQMem.setText("200");
		txtQtdQMem.setBounds(351, 64, 91, 20);
		frame.getContentPane().add(txtQtdQMem);
		txtQtdQMem.setColumns(10);
		
		txtQuadroSize = new JTextField();
		txtQuadroSize.setText("4192");
		txtQuadroSize.setBounds(323, 93, 119, 20);
		txtQuadroSize.setColumns(10);
		frame.getContentPane().add(txtQuadroSize);
		
		txtQtdETLB = new JTextField();
		txtQtdETLB.setText("30");
		txtQtdETLB.setBounds(245, 122, 197, 20);
		txtQtdETLB.setColumns(10);
		frame.getContentPane().add(txtQtdETLB);
		
		txtPath = new JTextField();
		txtPath.setText("C:/SimuladorFiles/input.txt");
		txtPath.setBounds(222, 152, 220, 20);
		txtPath.setColumns(10);
		frame.getContentPane().add(txtPath);
		
		DefaultTableModel dtm = new DefaultTableModel(new String[0][7], TITLEARRAY);
		frame.setBounds(100, 100, 486, 284);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
