package visao;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import modelo.tabelaPaginas.EntradaTP;
import java.util.Map;

public class ViewTLB {

	private JFrame frame;
	private JTable table;
	private DefaultTableModel dtm;
	private JLabel lblInfo;

	public ViewTLB() {
		initialize();
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame("Translation Lookaside Buffer (TLB)");
		frame.setBounds(600, 620, 700, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setLayout(new BorderLayout(10, 10));

		lblInfo = new JLabel("Conteúdo da TLB para o Processo: N/A");
		lblInfo.setForeground(Color.WHITE);
		lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblInfo, BorderLayout.NORTH);

		String[] columnNames = {"Nº Página", "Nº Quadro", "Modificada"};
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

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new LineBorder(Color.BLACK));
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	public void update() {
		if (WindowData.cpu == null || WindowData.cpu.tlb == null) return;

		Map<Integer, EntradaTP> entradas = WindowData.cpu.tlb.getEntradas();
		String processoId = WindowData.cpu.tlb.getProcessoIdAtual();

		lblInfo.setText("Conteúdo da TLB para o Processo: " + (processoId != null ? processoId : "N/A"));

		dtm.setRowCount(0); // limpa tabela

		for (Map.Entry<Integer, EntradaTP> entry : entradas.entrySet()) {
			EntradaTP entradaTP = entry.getValue();
			Object[] row = new Object[]{
					entradaTP.getNumPagina(),
					entradaTP.getNumQuadro(),
					entradaTP.getModificacao()
			};
			dtm.addRow(row);
		}
	}
}