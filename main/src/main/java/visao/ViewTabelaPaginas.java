package visao;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import modelo.processo.ImagemProcesso;
import modelo.tabelaPaginas.EntradaTP;
import other.StaticObjects;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class ViewTabelaPaginas {
	
	private static final String TITLEARRAY[] = {"Número da Página","Presente", "Modificada", "Utilizada", "Tempo do Último Uso", "Número do Quadro"};
	private JFrame frame;
	private final JLabel lblNewLabel = new JLabel("Tabela de Páginas");
	private JTable table;
	private JScrollPane scrollPane;
	private JComboBox<String> cmbProcesso;
	private JButton btnAplicar;
	DefaultTableModel dtm;
	public ViewTabelaPaginas() {
		initialize();

	}


	private void initialize() {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

		frame = new JFrame();
		frame.setVisible(true);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 11, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblNewLabel, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblNewLabel, 42, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().setLayout(springLayout);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 25));
		lblNewLabel.setForeground(Color.WHITE);
		frame.getContentPane().add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		springLayout.putConstraint(SpringLayout.NORTH, separator, 53, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, separator, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, separator, -10, SpringLayout.EAST, frame.getContentPane());
		separator.setMinimumSize(new Dimension(300, 5));
		separator.setPreferredSize(new Dimension(414, 5));
		separator.setMaximumSize(new Dimension(1920, 5));
		
		frame.getContentPane().add(separator);
		
		cmbProcesso = new JComboBox<>();
		springLayout.putConstraint(SpringLayout.NORTH, cmbProcesso, 65, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, cmbProcesso, 83, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, cmbProcesso, 213, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(cmbProcesso);
		
		JLabel lblProcesso = new JLabel("Processo:");
		springLayout.putConstraint(SpringLayout.NORTH, lblProcesso, 64, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblProcesso, 20, SpringLayout.WEST, frame.getContentPane());
		lblProcesso.setForeground(Color.WHITE);
		lblProcesso.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		frame.getContentPane().add(lblProcesso);
		
		scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 95, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 20, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -20, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -20, SpringLayout.EAST, frame.getContentPane());
		scrollPane.setBackground(Color.DARK_GRAY);
		scrollPane.setForeground(Color.DARK_GRAY);
		frame.getContentPane().add(scrollPane);
		
		dtm = new DefaultTableModel(new String[0][6], TITLEARRAY);
		table = new JTable();
		table.setModel(dtm);
		scrollPane.setViewportView(table);
		table.setBackground(Color.DARK_GRAY);
		table.setForeground(Color.WHITE);
		
		btnAplicar = new JButton("Aplicar");
		btnAplicar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String idProcesso = cmbProcesso.getSelectedItem().toString();
				boolean achou = false;
				int i = 0;
				ArrayList<ImagemProcesso> allProcessos = StaticObjects.getAllProcessos();
				
				while(!achou && i<allProcessos.size()) { // atualiza qual tabela de páginas será exibida
					if(idProcesso.equals(allProcessos.get(i).getIdProcesso())) {
						WindowData.tabelaDePaginasCurrentProcesso = allProcessos.get(i).getTabelaDePaginas();
						achou = true;
					}
					i++;
				}
				
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnAplicar, 6, SpringLayout.SOUTH, separator);
		springLayout.putConstraint(SpringLayout.WEST, btnAplicar, 6, SpringLayout.EAST, cmbProcesso);
		springLayout.putConstraint(SpringLayout.EAST, btnAplicar, 86, SpringLayout.EAST, cmbProcesso);
		btnAplicar.setForeground(Color.WHITE);
		btnAplicar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnAplicar.setContentAreaFilled(false);
		btnAplicar.setBorder(new LineBorder(Color.WHITE, 2));
		btnAplicar.setBackground(Color.DARK_GRAY);
		frame.getContentPane().add(btnAplicar);
		frame.setBounds(100, 100, 450, 680);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void addToComboProcessos(String processId) {
		cmbProcesso.addItem(processId);
	}
	public void update() {
		dtm.setRowCount(0);
		for(EntradaTP entrada:WindowData.tabelaDePaginasCurrentProcesso.getEntradas()) {
			String[] elementos = new String[6];
			//{"Número da Página","Presente", "Modificada", "Utilizada", "Tempo do Último Uso", "Número do Quadro"};
			elementos[0] = String.valueOf(entrada.getNumPagina());
			elementos[1] = String.valueOf(entrada.getPresenca());
			elementos[2] = String.valueOf(entrada.getModificacao());
			elementos[3] = String.valueOf(entrada.getUso());
			elementos[4] = String.valueOf(entrada.getTempoUltimoUso());
			elementos[5] = String.valueOf(entrada.getNumQuadro());
			dtm.addRow(elementos);
		}
		
	}
}
