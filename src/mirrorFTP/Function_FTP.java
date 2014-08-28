package mirrorFTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class Function_FTP {
	
	private Socket controle;
	private InputStream iscontr;
	private OutputStream oscontr;
	private Socket dados;
	private OutputStream osDados;
	private InputStream isDados;
	
	public Function_FTP() throws IOException {
	}
	
	// ARRAYS
	protected ArrayList<String> aFilesNoFtp = new ArrayList<>();
	protected ArrayList<String> aFilesRemFtp = new ArrayList<>();
	protected ArrayList<String> aPastaNoFtp = new ArrayList<>();
	protected ArrayList<String> aPastaNoFtpRemovidas = new ArrayList<>();
	
	
	// VARIAVEIS LOCAIS
	String host;
	int port;
	String user;
	String pass;
	
	// GET E SET
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = Integer.parseInt(port);
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

	// CAPTURA DE RESPOSTA
	public String getControlResp() throws IOException{
		BufferedReader br = new BufferedReader(
				new InputStreamReader(this.iscontr));
		String resp = br.readLine();
		System.out.println(resp);
		return resp;
	}	
	
	// ABRE CONEXÃO
	public void conect() throws IOException{
		this.controle = new Socket(host, port);
		this.iscontr = controle.getInputStream();
		this.oscontr = controle.getOutputStream();		
		this.getControlResp();
	}	
	
	// LOGIN E SENHA
	public void login() throws IOException{
		String comand = "USER " + user + "\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
		comand = "PASS " + pass + "s\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
	}
	
	// CRIA DIRETÖRIO
	public void newFolder (String nome) throws IOException{
		String comand = "MKD " + nome + "\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
	}
	
	// ENTRA NO DIRETORIO XXXX
	public void chargeWorkingDir (String path) throws IOException{
		String comand = "CWD " + path + "\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
	}
	
	// QUIT DO FTP
	public void quit () throws IOException{
		String comand = "QUIT\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
	}
	
	// POE O SERVIDOR EM ESCUTA
	public void pasvMOD () throws IOException {
		String comand = "PASV\r\n";
		this.oscontr.write(comand.getBytes());
		String resp = getControlResp();
		StringTokenizer st = new StringTokenizer(resp);
		st.nextToken("(");
		String ip = st.nextToken(",").substring(1) + "." +
			 st.nextToken(",") + "." +
			 st.nextToken(",") + "." +
			 st.nextToken(",");
		int value1 = Integer.parseInt(st.nextToken(","));
		int value2 = Integer.parseInt(st.nextToken(")").substring(1));
		int port = value1*256 + value2;

		this.dados = new Socket(ip, port);
		this.isDados = dados.getInputStream();
		this.osDados = dados.getOutputStream();
	}
	
	// COMANDO PARA TIPAR CANAL DE ARQUIVOS
	public void chargeType (String type) throws IOException {
		String comand = "TYPE " + type + "\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
	}
	
	// LISTA DIRETORIO
	public String list (String path) throws IOException{
		this.pasvMOD();
		String comand = "LIST\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
		BufferedReader br = new BufferedReader(new InputStreamReader(isDados));
		String resp = "";
		String line;
		while ((line = br.readLine())!= null){
			resp = resp + "\n" + line;
		}
		System.out.println(resp);
		this.getControlResp();
		return resp;
	}
	
	// CRIA LISTA DIRETORIO
	public void criaListaArqFTP () throws IOException {
		if ((aFilesNoFtp.size() == 0) && (aPastaNoFtp.size() == 0)) {
		this.pasvMOD();
		String comand = "LIST\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
		BufferedReader br = new BufferedReader(new InputStreamReader(isDados));
		String resp = "";
		String line;
		String array[] = new String[2];
			while ((line = br.readLine())!= null){
				resp = resp + "\n" + line;
	 			array = resp.split(":");
				if (array[0].contains("-rw-r--r--") == true) {
					String aux = array[1];
					resp = aux.substring(3,aux.length()).trim();
					this.aFilesNoFtp.add(resp);
				}
				else {
					String aux = array[1];
					resp = aux.substring(3,aux.length()).trim();
					this.aPastaNoFtp.add(resp);
				}
			}
		}
		else {
			aFilesRemFtp.addAll(aFilesNoFtp);
			aFilesNoFtp.clear();
			aPastaNoFtpRemovidas.addAll(aPastaNoFtp);
			aPastaNoFtp.clear();
			this.pasvMOD();
			String comand = "LIST\r\n";
			this.oscontr.write(comand.getBytes());
			this.getControlResp();
			BufferedReader br = new BufferedReader(new InputStreamReader(isDados));
			String resp = "";
			String line;
			String array[] = new String[2];
				while ((line = br.readLine())!= null){
					resp = resp + "\n" + line;
		 			array = resp.split(":");
					if (array[0].contains("-rw-r--r--") == true) {
						String aux = array[1];
						resp = aux.substring(3,aux.length()).trim();
						this.aFilesNoFtp.add(resp);
					}
					else {
						String aux = array[1];
						resp = aux.substring(3,aux.length()).trim();
						this.aPastaNoFtp.add(resp);
					}
				}
		}
		this.getControlResp();
	}
	
	// BAIXAR ARQUIVOS DO FTP
	public void downloadFile (String nome, String diretorio) throws IOException {
		this.pasvMOD();
		this.chargeType("I");
		String comand = "RETR " + nome + "\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
		File file = new File (diretorio + nome);
		System.out.println(diretorio+nome);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] buf = new byte[1000];
		int len;
		while ((len = this.isDados.read(buf)) != -1) {
			fos.write(buf, 0 , len);
		}
		fos.flush();
		fos.close();
		dados.close();
		this.getControlResp();
	}
	
	// UPAR ARQUIVOS DO FTP
	public void uploadFile (String nome, String diretorio) throws IOException {
		this.pasvMOD();
		this.chargeType("I");
		String comand = "STOR " + nome + "\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
		File file = new File (diretorio + nome);
		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[1000];
		int len;
		while ((len = fis.read(buf)) != -1) {
			this.osDados.write(buf, 0, len);
		}
		fis.close();
		dados.close();
		this.getControlResp();
	}
	
	// DATA MODIFICAÇÃO ARQUIVO FTP XXXX
	public String dataModArqFTP(String nome, String diretorio) throws IOException {
		chargeWorkingDir(diretorio);
		String comand = "MDTM " + nome + "\r\n";
		this.oscontr.write(comand.getBytes());
		String resp = getControlResp().replaceAll(" ","").trim();
		char[] aux = resp.toCharArray();
		char[] aux2 = new char[12];
		for (int i = 3; i <= 14; i++) {
			aux2[i-3] = aux[i];
		}
		resp = new String(aux2);
		return resp;
	}
	
	// EXCLUIR ARQUIVO FTP
	public void excluiArq(String nome, String diretorio) throws IOException {
		chargeWorkingDir(diretorio);
		String comand = "DELE " + nome + "\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
	}
	
	// CRIAR DIRETÓRIO
	public void criaDir (String nome, String diretorio) throws IOException {
		chargeWorkingDir(diretorio);
		String comand = "MKD " + nome + "\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
	}
	
	// APAGA DIRETÓRIO
	public void removeDir (String nome, String diretorio) throws IOException {
		chargeWorkingDir(diretorio);
		String comand = "RMD " + nome + "\r\n";
		this.oscontr.write(comand.getBytes());
		this.getControlResp();
	}
}
