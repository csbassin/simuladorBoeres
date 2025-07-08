package visao;

import modelo.Execucao.CPU;
import other.Fila;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ViewFilasDeEstado {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public ViewFilasDeEstado() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Filas de Estados dos Processos");
        frame.setBounds(1050, 100, 350, 250); // Ajuste no tamanho da janela
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        // --- Tabela de Filas ---
        String[] columnNames = {"Estado", "Processos na Fila"};
        model = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células não editáveis
            }
        };

        table = new JTable(model);
        configureTableStyle();

        // Adiciona as linhas fixas para cada estado
        model.addRow(new Object[]{"Pronto", ""});
        model.addRow(new Object[]{"Pronto-Suspenso", ""});
        model.addRow(new Object[]{"Bloqueado", ""});
        model.addRow(new Object[]{"Finalizado", ""});


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(Color.BLACK));
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void configureTableStyle() {
        table.setBackground(new Color(60, 63, 65));
        table.setForeground(Color.WHITE);
        table.setFont(new Font("Monospaced", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(Color.GRAY);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.DARK_GRAY);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    public void update() {
        CPU cpu = WindowData.cpu;
        if (cpu == null) return;

        // Atualiza a célula de cada fila com a lista de processos formatada
        updateRow(0, cpu.prontos);
        updateRow(1, cpu.prontosSuspensos);
        updateRow(2, cpu.bloqueados);
        updateRow(3, cpu.finalizados);
    }

    private void updateRow(int rowIndex, Fila<String> fila) {

        String processos = String.join(", ", fila);
        model.setValueAt(processos, rowIndex, 1);
    }
}