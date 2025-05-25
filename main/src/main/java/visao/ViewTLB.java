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
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ViewTLB {
	
	private static final String TITLEARRAY[] = {"Válido"," Número da Página","Presente", "Modificada", "Utilizada", "Tempo do Último Uso", "Número do Quadro"};
	private JFrame frame;
	private final JLabel lblNewLabel = new JLabel("Translation Lookaside Buffer");
	private JTable table;
	private JScrollPane scrollPane;

	public ViewTLB() {
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
		
		scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 20, SpringLayout.NORTH, separator);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 20, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -20, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -20, SpringLayout.EAST, frame.getContentPane());
		scrollPane.setBackground(Color.DARK_GRAY);
		scrollPane.setForeground(Color.DARK_GRAY);
		frame.getContentPane().add(scrollPane);
		
		DefaultTableModel dtm = new DefaultTableModel(new String[0][7], TITLEARRAY);
		table = new JTable();
		table.setModel(dtm);
		scrollPane.setViewportView(table);
		table.setBackground(Color.DARK_GRAY);
		table.setForeground(Color.WHITE);
		frame.setBounds(100, 100, 450, 680);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
