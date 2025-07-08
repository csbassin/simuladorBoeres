package visao;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Optional;

import modelo.processo.ImagemProcesso;
import modelo.tabelaPaginas.EntradaTP;
import other.StaticObjects;

public class ViewTabelaPaginas {

	private JFrame frame;
	private JTable table;
	private JComboBox<String> cmbProcesso;
	private DefaultTableModel dtm;
	private JLabel lblInfo;

	public ViewTabelaPaginas() {
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame("Tabela de Páginas");
		frame.setBounds(600, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setLayout(new BorderLayout(10, 10));

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		topPanel.setBackground(Color.DARK_GRAY);

		JLabel lblProcesso = new JLabel("Processo:");
		lblProcesso.setForeground(Color.WHITE);
		lblProcesso.setFont(new Font("Segoe UI", Font.BOLD, 14));
		topPanel.add(lblProcesso);

		cmbProcesso = new JComboBox<>();
		cmbProcesso.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cmbProcesso.setPreferredSize(new Dimension(150, 25));
		topPanel.add(cmbProcesso);

		JButton btnAplicar = new JButton("Visualizar");
		btnAplicar.addActionListener(e -> update());
		topPanel.add(btnAplicar);

		lblInfo = new JLabel("Page Faults: 0");
		lblInfo.setForeground(Color.ORANGE);
		lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
		topPanel.add(Box.createHorizontalStrut(20));
		topPanel.add(lblInfo);

		frame.getContentPane().add(topPanel, BorderLayout.NORTH);

		String[] columnNames = {"Página", "Quadro", "Presente", "Modificada", "Usada", "Tempo último Uso"};
		dtm = new DefaultTableModel(null, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(dtm);
		table.setForeground(Color.WHITE);
		table.setBackground(Color.DARK_GRAY);
		table.setFillsViewportHeight(true);

		table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new LineBorder(Color.BLACK));
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	public void addToComboProcessos(String processId) {
		cmbProcesso.addItem(processId);
		if (cmbProcesso.getItemCount() == 1) {
			cmbProcesso.setSelectedIndex(0);
		}
	}

	public void update() {
		String idProcesso = (String) cmbProcesso.getSelectedItem();
		if (idProcesso == null) {
			dtm.setRowCount(0);
			return;
		}

		Optional<ImagemProcesso> processoOpt = StaticObjects.findProcessById(idProcesso);

		if (processoOpt.isPresent()) {
			EntradaTP[] entradas = processoOpt.get().getTabelaDePaginas().getEntradas();
			dtm.setRowCount(0);

			for (EntradaTP entrada : entradas) {
				Object[] row = new Object[]{
						entrada.getNumPagina(),
						entrada.getNumQuadro() == -1 ? "N/A" : entrada.getNumQuadro(),
						entrada.getPresenca(),
						entrada.getModificacao(),
						entrada.getUso(),
						entrada.getTempoUltimoUso()
				};
				dtm.addRow(row);
			}
		} else {
			dtm.setRowCount(0);
		}

		lblInfo.setText("Page Faults: " + WindowData.getPageFaults());
	}


	private static class CustomTableCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			// Reseta a cor de fundo para a padrão
			c.setBackground(Color.DARK_GRAY);
			c.setForeground(Color.WHITE);

			// Pega os valores da linha para decidir a cor
			boolean presente = (boolean) table.getModel().getValueAt(row, 2);
			boolean modificada = (boolean) table.getModel().getValueAt(row, 3);

			// Coluna "Presente" (índice 2)
			if (column == 2 && !presente) {
				c.setBackground(new Color(139, 0, 0));
			}

			// Coluna "Modificada" (índice 3)
			if (column == 3 && modificada) {
				c.setBackground(new Color(255, 140, 0));
			}

			if (isSelected) {
				c.setBackground(table.getSelectionBackground());
				c.setForeground(table.getSelectionForeground());
			}

			return c;
		}
	}

}