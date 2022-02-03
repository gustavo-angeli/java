package br.com.gusta.cm.visao;

import java.awt.GridLayout;

import javax.swing.JPanel;

import br.com.gusta.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {

	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		setLayout(new GridLayout(
				tabuleiro.getLinhas(), tabuleiro.getColunas()));

		int total = tabuleiro.getLinhas() * tabuleiro.getColunas();
		
		tabuleiro.paraCadaCampo(c -> add(new BotaoCampo(c)));
		tabuleiro.registrarObservador(e -> {
			//TODO mostrar resultado para usuario
		});
	}
	
}