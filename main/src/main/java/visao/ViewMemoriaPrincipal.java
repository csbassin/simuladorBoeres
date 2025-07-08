package visao;

import config.ConfigData;
import modelo.processo.ImagemProcesso;
import modelo.tabelaPaginas.EntradaTP;
import other.StaticObjects;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ViewMemoriaPrincipal {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel dtm;

    public ViewMemoriaPrincipal() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Visualização da Memória Principal");
        frame.setBounds(100, 100, 450, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("Estado dos Quadros da Memória");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        frame.getContentPane().add(lblTitulo, BorderLayout.NORTH);

        String[] columnNames = {"Quadro", "ID Processo", "Nº Página"};
        dtm = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(dtm);



        table.setDefaultRenderer(Object.class, new ClockPointerRenderer());
        configureTableStyle();


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(Color.BLACK));
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    }


    private void configureTableStyle() {
        table.setForeground(Color.WHITE);
        table.setBackground(Color.DARK_GRAY);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setRowHeight(20);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.DARK_GRAY);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }


    public void update() {
        dtm.setRowCount(0);

        String[][] quadros = new String[ConfigData.quantidadeQuadros][2];

        for (ImagemProcesso processo : StaticObjects.getAllProcessos()) {
            for (EntradaTP entrada : processo.getTabelaDePaginas().getEntradas()) {
                if (entrada.getPresenca()) {
                    int numQuadro = entrada.getNumQuadro();
                    if (numQuadro >= 0 && numQuadro < ConfigData.quantidadeQuadros) {
                        quadros[numQuadro][0] = processo.getIdProcesso();
                        quadros[numQuadro][1] = String.valueOf(entrada.getNumPagina());
                    }
                }
            }
        }

        for (int i = 0; i < ConfigData.quantidadeQuadros; i++) {
            String processoId = quadros[i][0] != null ? quadros[i][0] : "--- Vazio ---";
            String paginaNum = quadros[i][1] != null ? quadros[i][1] : "---";
            dtm.addRow(new Object[]{i, processoId, paginaNum});
        }
    }



    private static class ClockPointerRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Reseta para o estilo padrão
            c.setBackground(Color.DARK_GRAY);
            c.setForeground(Color.WHITE);

            // Verifica se o algoritmo CLOCK está ativo e se a linha atual é a do ponteiro
            if ("CLOCK".equals(ConfigData.tipoSubstituicaoPaginas) && row == WindowData.ponteiroClock) {
                if (column == 0) {

                    setText("-> " + value);
                }
                c.setBackground(new Color(0, 80, 120));
            } else {
                if (column == 0) {
                    setText("   " + value); // Adiciona espaço para alinhar
                }
            }

            return c;
        }
    }

}