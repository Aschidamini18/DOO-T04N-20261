package fag.tela;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

// deixar a tela no estilo Netflix.

public final class EstiloVisualNetflix {
	public static final Color FUNDO_PRINCIPAL = new Color(20, 20, 20);
	public static final Color FUNDO_SECUNDARIO = new Color(32, 32, 32);
	public static final Color FUNDO_CAMPO = new Color(45, 45, 45);
	public static final Color VERMELHO = new Color(229, 9, 20);
	public static final Color VERMELHO_ESCURO = new Color(178, 7, 16);
	public static final Color TEXTO_CLARO = new Color(245, 245, 245);
	public static final Color TEXTO_APAGADO = new Color(180, 180, 180);
	public static final Color LINHA_TABELA = new Color(38, 38, 38);
	public static final Font FONTE_TITULO = new Font("Arial", Font.BOLD, 28);
	public static final Font FONTE_PADRAO = new Font("Arial", Font.PLAIN, 14);
	public static final Font FONTE_NEGRITO = new Font("Arial", Font.BOLD, 14);
	public static final Font FONTE_DETALHES = new Font("Consolas", Font.PLAIN, 14);

	private EstiloVisualNetflix() {
	}

	public static Border criarBordaPainel() {
		return BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(55, 55, 55)),
				BorderFactory.createEmptyBorder(14, 14, 14, 14));
	}

	public static void aplicarPainelEscuro(JPanel painel) {
		painel.setBackground(FUNDO_PRINCIPAL);
	}

	public static void aplicarPainelSecundario(JPanel painel) {
		painel.setBackground(FUNDO_SECUNDARIO);
		painel.setBorder(criarBordaPainel());
	}

	public static void aplicarRotulo(JLabel rotulo) {
		rotulo.setForeground(TEXTO_CLARO);
		rotulo.setFont(FONTE_NEGRITO);
	}

	public static void aplicarCampoTexto(JTextField campo) {
		campo.setBackground(FUNDO_CAMPO);
		campo.setForeground(TEXTO_CLARO);
		campo.setCaretColor(TEXTO_CLARO);
		campo.setFont(FONTE_PADRAO);
		campo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(70, 70, 70)),
				BorderFactory.createEmptyBorder(8, 10, 8, 10)));
	}

	public static void aplicarAreaTexto(JTextArea area) {
		area.setBackground(FUNDO_SECUNDARIO);
		area.setForeground(TEXTO_CLARO);
		area.setCaretColor(TEXTO_CLARO);
		area.setFont(FONTE_DETALHES);
		area.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
	}

	public static void aplicarBotaoVermelho(JButton botao) {
		botao.setBackground(VERMELHO);
		botao.setForeground(Color.BLACK);
		botao.setFocusPainted(false);
		botao.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
		botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botao.setFont(FONTE_NEGRITO);
	}

	public static void aplicarBotaoEscuro(JButton botao) {
		botao.setBackground(new Color(210, 210, 210));
		botao.setForeground(Color.BLACK);
		botao.setFocusPainted(false);
		botao.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
		botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		botao.setFont(FONTE_NEGRITO);
	}

	public static void aplicarCombo(JComboBox<?> combo) {
		combo.setBackground(new Color(235, 235, 235));
		combo.setForeground(Color.BLACK);
		combo.setFont(FONTE_PADRAO);
		combo.setRenderer(new BasicComboBoxRenderer() {
			
			@Override
			public java.awt.Component getListCellRendererComponent(javax.swing.JList lista, Object valor, int indice,
					boolean selecionado, boolean foco) {
				var componente = super.getListCellRendererComponent(lista, valor, indice, selecionado, foco);
				componente.setBackground(selecionado ? new Color(210, 210, 210) : new Color(235, 235, 235));
				componente.setForeground(Color.BLACK);
				componente.setFont(FONTE_PADRAO);
				return componente;
			}
		});
	}

	public static void aplicarTabela(JTable tabela) {
		tabela.setBackground(FUNDO_SECUNDARIO);
		tabela.setForeground(TEXTO_CLARO);
		tabela.setGridColor(new Color(55, 55, 55));
		tabela.setSelectionBackground(VERMELHO_ESCURO);
		tabela.setSelectionForeground(Color.WHITE);
		tabela.setFont(FONTE_PADRAO);
		tabela.setRowHeight(30);
		tabela.getTableHeader().setBackground(new Color(235, 235, 235));
		tabela.getTableHeader().setForeground(Color.BLACK);
		tabela.getTableHeader().setFont(FONTE_NEGRITO);
		tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			
			@Override
			public java.awt.Component getTableCellRendererComponent(JTable tabela, Object valor, boolean selecionado,
					boolean foco, int linha, int coluna) {
				var componente = super.getTableCellRendererComponent(tabela, valor, selecionado, foco, linha, coluna);
				if (!selecionado) {
					componente.setBackground(linha % 2 == 0 ? FUNDO_SECUNDARIO : LINHA_TABELA);
					componente.setForeground(TEXTO_CLARO);
				}
				setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
				return componente;
			}
		});
	}

	public static void aplicarRolagem(JScrollPane rolagem) {
		rolagem.setBorder(BorderFactory.createLineBorder(new Color(55, 55, 55)));
		rolagem.getViewport().setBackground(FUNDO_SECUNDARIO);
	}

	public static void aplicarFundo(JComponent componente) {
		componente.setBackground(FUNDO_PRINCIPAL);
	}
}
