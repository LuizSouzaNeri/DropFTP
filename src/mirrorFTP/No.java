package mirrorFTP;

import java.util.ArrayList;


public class No {
	
	private char tipo;
	private String nome;
	private String diretorio;
		
	public No (char tipo, String nome, String diretorio, String data) {
		this.setTipo(tipo);
		this.setNome(nome);
		this.setDiretorio(diretorio);
	}
	
	public No () {
		this.setTipo(tipo);
		this.setNome(nome);
		this.setDiretorio(diretorio);
	}

	public char getTipo() {
		return tipo;
	}

	public void setTipo(char tipo) {
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDiretorio() {
		return diretorio;
	}

	public void setDiretorio(String diretorio) {
		this.diretorio = diretorio;
	}
	
}
