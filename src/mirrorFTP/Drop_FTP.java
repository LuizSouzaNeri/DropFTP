package mirrorFTP;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Drop_FTP {
	
	// VARIAVÉIS DO APP
	private int intervalo;		
	private Function_Disco pasta;
	private Function_FTP ftp;
	private String dirLocal;
	private String dirRemoto;
	
	public int getIntervalo() {
		return intervalo;
	}
	
	// ARRAYS
	private ArrayList<String> aFilesAmbosDir = new ArrayList<>();
	private ArrayList<String> aFilesEnvFtp = new ArrayList<>();
	private ArrayList<String> aFilesRecFtp = new ArrayList<>();
	private ArrayList<String> aPastasAmbosDir = new ArrayList<>();
	private ArrayList<String> aPastaRecFtp = new ArrayList<>();
	private ArrayList<String> aPastaEnvFtp = new ArrayList<>();
	
	// CONSTRUTOR DA CLASSE
	public Drop_FTP () throws IOException {
		pasta = new Function_Disco();
		ftp = new Function_FTP();
	}
	
	// INICIA A CAPTURA DOS DADOS
	public void getDadosInicial () throws IOException {
		File f = new File("entradas.txt");
		InputStream isFile = new FileInputStream(f);
		BufferedReader brFile = new BufferedReader(
				new InputStreamReader(isFile));
		ftp.setHost(brFile.readLine());
		ftp.setPort(brFile.readLine());
		this.intervalo = new Integer(brFile.readLine());
		ftp.setUser(brFile.readLine());
		ftp.setPass(brFile.readLine());
		this.dirLocal = brFile.readLine();
		this.dirRemoto = brFile.readLine();
	}
	
	// AQUI OS COMANDO PARA A APLICAÇÃO
	// INICIA APP
	public void iniciaAplicativo () throws IOException {
		this.getDadosInicial();
		ftp.conect();
		ftp.login();
		ftp.criaListaArqFTP();
		pasta.criaListaArqLocal(dirLocal);
		verificaFileExcluido();
		verificaPastaExcluida();
		this.processaOsDados();
		feedBack();
		sinc();
	}
	
	// PROCESSA OS DADOS OBTIDOS E FORMA A LISTA DE AQUIVOS DO DISCO, DO FTP E POR ULTIMO GERA ARRAYS
	// PARA A SINCRONIA DE DADOS
	public void processaOsDados () throws IOException {
		this.verificaFileExcluido();
		this.verificaPastaExcluida();
		this.comparaFiles();
		this.comparaPastas();
		this.comparaArquivoPorData();
	}
	
	// MÉTODOS PARA O APPBOX
	// VERIFICA SE HÁ ENTRE OS DIRETÓRIOS FILES EXCLUÍDOS
	private void verificaFileExcluido () throws IOException {
		ArrayList<String> auxPasta = new ArrayList<>();
		ArrayList<String> auxFtp = new ArrayList<>();
		auxPasta.addAll(pasta.aFilesRemPasta);
		auxFtp.addAll(ftp.aFilesRemFtp);
		pasta.aFilesRemPasta.clear();
		ftp.aFilesRemFtp.clear();
		if (auxPasta.size() != 0) {
			for (int i = 0; i < auxPasta.size(); i++) {
				if ((pasta.aFilesNaPasta.contains(auxPasta.get(i)) == false) && (ftp.aFilesNoFtp.contains(auxPasta.get(i)) == true)) {
					ftp.aFilesRemFtp.add(auxPasta.get(i));
					ftp.aFilesNoFtp.remove(auxPasta.get(i));
				}
				else {
					auxPasta.remove(i);
					i--;
				}
			}
		}
		if (auxFtp.size() != 0) {
			for (int i = 0; i < auxFtp.size(); i++) {
				if ((ftp.aFilesNoFtp.contains(auxFtp.get(i)) == false) && (pasta.aFilesNaPasta.contains(auxFtp.get(i)) == true)) {
					pasta.aFilesRemPasta.add(auxFtp.get(i));
					pasta.aFilesNaPasta.remove(auxFtp.get(i));
				}
				else {
					auxFtp.remove(i);
					i--;
				}
			}
		}
	}
	// VERIFICA SE HÁ ENTRE OS DIRETÓRIOS PASTAS EXCLUÍDAS
	private void verificaPastaExcluida () throws IOException {
		ArrayList<String> auxPasta = new ArrayList<>();
		ArrayList<String> auxFtp = new ArrayList<>();
		auxPasta.addAll(pasta.aPastasRemovidas);
		auxFtp.addAll(ftp.aPastaNoFtpRemovidas);
		pasta.aPastasRemovidas.clear();
		ftp.aPastaNoFtpRemovidas.clear();
		if (auxPasta.size() != 0) {
			for (int i = 0; i < auxPasta.size(); i++) {
				if ((pasta.aPastas.contains(auxPasta.get(i)) == false) && (ftp.aPastaNoFtp.contains(auxPasta.get(i)) == true)) {
					ftp.aPastaNoFtpRemovidas.add(auxPasta.get(i));
					ftp.aPastaNoFtp.remove(auxPasta.get(i));
				}
				else {
					auxPasta.remove(i);
					i--;
				}
			}
		}
		if (auxFtp.size() != 0) {
			for (int i = 0; i < auxFtp.size(); i++) {
				if ((ftp.aPastaNoFtp.contains(auxFtp.get(i)) == false) && (pasta.aPastas.contains(auxFtp.get(i)) == true)) {
					pasta.aPastasRemovidas.add(auxFtp.get(i));
					pasta.aPastas.remove(auxFtp.get(i));
				}
				else {
					auxFtp.remove(i);
					i--;
				}
			}
		}
	}	
	// COMPARA ARQUIVOS DE AMBOS OS DIRETÓRIOS COM BASE NA DATA
	private void comparaArquivoPorData () throws IOException {		
		for (int i = 0; i < aFilesAmbosDir.size(); i++) {
			String nome = aFilesAmbosDir.get(i);
			long dataNoFtp = Long.parseLong(ftp.dataModArqFTP(nome, dirRemoto)) - 300;
			long dataNaPst = Long.parseLong(pasta.dataModArqLocal(nome, dirLocal));
			if (dataNoFtp > dataNaPst) {
				aFilesRecFtp.add(aFilesAmbosDir.get(i));
			}
			if (dataNoFtp < dataNaPst) {
				aFilesEnvFtp.add(aFilesAmbosDir.get(i));
			}
		}
	}
	
	// COMPARA ARQUIVOS COM BASE NO NOME
	private void comparaFiles () throws IOException {
		this.aFilesEnvFtp.clear();
		this.aFilesRecFtp.clear();
		this.aFilesAmbosDir.clear();
		ArrayList<String> aux = new ArrayList<>();
		aux.addAll(ftp.aFilesNoFtp);
		aux.addAll(pasta.aFilesNaPasta);
		for (int i = 0; i < aux.size(); i++) {
			if ((ftp.aFilesNoFtp.contains(aux.get(i))) && (pasta.aFilesNaPasta.contains(aux.get(i)))) {
				if (this.aFilesAmbosDir.contains(aux.get(i)) == false) {
					this.aFilesAmbosDir.add(aux.get(i));
					aux.remove(aux.get(i));
					i--;
				}
				else {
					aux.remove(aux.get(i));
					i--;
				}
			}
		}
		for (int i = 0; i < aux.size(); i++) {
			if (ftp.aFilesNoFtp.contains(aux.get(i)) && (ftp.aFilesRemFtp.contains(aux.get(i)) == false)) {
				this.aFilesRecFtp.add(aux.get(i));
			}
			if (pasta.aFilesNaPasta.contains(aux.get(i)) && (pasta.aFilesRemPasta.contains(aux.get(i)) == false)) {
				this.aFilesEnvFtp.add(aux.get(i));
			}
		}
	}
	// COMPARA ARQUIVOS COM BASE NO NOME
	private void comparaPastas() throws IOException {
		this.aPastaEnvFtp.clear();
		this.aPastaRecFtp.clear();
		this.aPastasAmbosDir.clear();
		ArrayList<String> aux = new ArrayList<>();
		aux.addAll(ftp.aPastaNoFtp);
		aux.addAll(pasta.aPastas);
		for (int i = 0; i < aux.size(); i++) {
			if ((ftp.aPastaNoFtp.contains(aux.get(i))) && (pasta.aPastas.contains(aux.get(i)))) {
				if (this.aPastasAmbosDir.contains(aux.get(i)) == false) {
					this.aPastasAmbosDir.add(aux.get(i));
					aux.remove(aux.get(i));
					i--;
				}
				else {
					aux.remove(aux.get(i));
					i--;
				}
			}
		}
		for (int i = 0; i < aux.size(); i++) {
			if (ftp.aPastaNoFtp.contains(aux.get(i)) && (ftp.aPastaNoFtpRemovidas.contains(aux.get(i)) == false)) {
				this.aPastaRecFtp.add(aux.get(i));
			}
			if (pasta.aPastas.contains(aux.get(i)) && (pasta.aPastasRemovidas.contains(aux.get(i)) == false)) {
				this.aPastaEnvFtp.add(aux.get(i));
			}
		}
	}
	
	// METODO PARA A SINCRONIZAÇÃO DOS DIRETÓRIOS
	public void sinc() throws IOException {
		System.out.println("Iniciando a Sincronização dos dados... Aguarde...");
		if (ftp.aFilesRemFtp.size() != 0) {
			System.out.println("Apagando do FTP arquivos apagados da Pasta... Aguarde...");
			for (int i = 0; i < ftp.aFilesRemFtp.size(); i++) {
				String nome = ftp.aFilesRemFtp.get(i);
				ftp.excluiArq(nome, dirRemoto);
			}
			ftp.aFilesRemFtp.clear();
			System.out.println("Arquivos removidos com sucesso do FTP...");
		}
		if (pasta.aFilesRemPasta.size() != 0) {
			System.out.println("Apagando da Pasta arquivos apagados do FTP... Aguarde...");
			for (int i = 0; i < pasta.aFilesRemPasta.size(); i++) {
				String nome = pasta.aFilesRemPasta.get(i);
				pasta.excluiArq(nome, dirLocal);
			}
			pasta.aFilesRemPasta.clear();
			System.out.println("Arquivos removidos com sucesso da Pasta...");
		}
		if (this.aFilesEnvFtp.size() != 0){
			System.out.println("Upload dos arquivos... Aguarde...");
			for (int i = 0; i < this.aFilesEnvFtp.size(); i++) {
				String nome = this.aFilesEnvFtp.get(i);
				ftp.uploadFile(nome, this.dirLocal);
				long data = Long.parseLong(ftp.dataModArqFTP(nome, dirRemoto)) - 300;
				pasta.setDataFile(data, nome, dirLocal, aFilesEnvFtp);
			}
			this.aFilesEnvFtp.clear();
			System.out.println("Upload dos arquivos concluído...");
		}
		if (this.aFilesRecFtp.size() != 0){
			System.out.println("Download dos arquivos... Aguarde...");
			for (int i = 0; i < this.aFilesRecFtp.size(); i++) {
				String nome = this.aFilesRecFtp.get(i);
				ftp.downloadFile(nome, this.dirLocal);
			}
			this.aFilesRecFtp.clear();
			System.out.println("Download dos arquivos concluído...");
		}
		aFilesAmbosDir.clear();
		ftp.quit();
		System.out.println("Sincronização Concluída...");
	}
	
	// MÉTODO PARA IMPRIMIR O CONTEÚDO DOS ARRAYSLIST
	public void imprimirStatus(ArrayList array) {
		for (int i = 0; i < array.size(); i++) {
			String nome = (String) array.get(i);
			System.out.println(nome);
		}
	}
	public void feedBack () {
		System.out.println("<<<<<<<<<<<<<< AÇÕES A SEREM TOMADAS >>>>>>>>>>>>>");
		System.out.println("<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("<<<<<<<<<<<<<<<<<<< Arq's FTP >>>>>>>>>>>>>>>>>>>>");
		System.out.println("Files:");
		imprimirStatus(ftp.aFilesNoFtp);
		System.out.println("Pastas:");
		imprimirStatus(ftp.aPastaNoFtp);
		System.out.println("<<<<<<<<<<<<<<<<<<< Arq's DISC >>>>>>>>>>>>>>>>>>>");
		System.out.println("Files:");
		imprimirStatus(pasta.aFilesNaPasta);
		System.out.println("Pastas:");
		imprimirStatus(pasta.aPastas);
		System.out.println("<<<<<<<<<<<<<<<<<<< Arq's em AMBOS >>>>>>>>>>>>>>>");
		System.out.println("Files:");
		imprimirStatus(aFilesAmbosDir);
		System.out.println("Pastas:");
		imprimirStatus(aPastasAmbosDir);
		System.out.println("<<<<<<<<<<<<<<<<<<< ENV p/ FTP >>>>>>>>>>>>>>>>>>>");
		System.out.println("Files:");
		imprimirStatus(aFilesEnvFtp);
		System.out.println("Pastas:");
		imprimirStatus(aPastaEnvFtp);
		System.out.println("<<<<<<<<<<<<<<<<<<< REC do FTP >>>>>>>>>>>>>>>>>>>");
		System.out.println("Files:");
		imprimirStatus(aFilesRecFtp);
		System.out.println("Pastas:");
		imprimirStatus(aPastaRecFtp);
		System.out.println("<<<<<<<<<<<<<<<<<<< EXCLUIR FTP >>>>>>>>>>>>>>>>>>");
		System.out.println("Files:");
		imprimirStatus(ftp.aFilesRemFtp);
		System.out.println("Pastas:");
		imprimirStatus(ftp.aPastaNoFtpRemovidas);
		System.out.println("<<<<<<<<<<<<<<<<<<< EXCLUIR LOCAL >>>>>>>>>>>>>>>>");
		System.out.println("Files:");
		imprimirStatus(pasta.aFilesRemPasta);
		System.out.println("Pastas:");
		imprimirStatus(pasta.aPastasRemovidas);
	}
	
}
