package mirrorFTP;


public class listPasta {
	
	private String nome;
	private String diretorio;
		
	public listPasta (String nome, String diretorio) {
		this.nome = nome;
		this.diretorio = diretorio;
	}
	
	public listPasta () {
		this.setNome(nome);
		this.setDiretorio(diretorio);
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
