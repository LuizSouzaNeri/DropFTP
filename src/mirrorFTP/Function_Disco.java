package mirrorFTP;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Function_Disco {

	// ARRAYS
	protected ArrayList<String> aFilesNaPasta;
	protected ArrayList<String> aFilesRemPasta;
	protected ArrayList<String> aPastas;
	protected ArrayList<String> aPastasRemovidas;

	public Function_Disco() {
		aFilesNaPasta = new ArrayList<>();
		aFilesRemPasta = new ArrayList<>();
		aPastas = new ArrayList<>();
		aPastasRemovidas = new ArrayList<>();
	}

	// EXCLUIR ARQUIVO
	public boolean excluiArq(String nome, String diretorio) {
		File file = new File(diretorio + nome);
		if (file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}

	// DATA DE MODIFICAÇÃO ARQUIVO LOCAL
	public String dataModArqLocal(String nome, String diretorio) {
		File arquivo = new File(diretorio + nome);
		Date myDate = null;
		if (arquivo.isFile()) {
			myDate = new Date(arquivo.lastModified());
		}
		String resp = new SimpleDateFormat("yyyyMMddHHmm").format(myDate);
		return resp;
	}

	// CRIA LISTA ARQUIVOS LOCAL
	public void criaListaArqLocal(String diretorio) {
		File arquivo = new File(diretorio);
		File[] aux = null;
		aux = arquivo.listFiles();
		if ((this.aFilesNaPasta.size() == 0) && (this.aPastas.size() == 0)) {
			for (int i = 0; i < aux.length; i++) {
				if (aux[i].isFile()) {
					this.aFilesNaPasta.add(aux[i].getName());
				} else {
					this.aPastas.add(aux[i].getName());

				}
			}
			this.aFilesRemPasta.clear();
			this.aPastasRemovidas.clear();
		} else {
			aFilesRemPasta.addAll(aFilesNaPasta);
			aFilesNaPasta.clear();
			aPastasRemovidas.addAll(aPastas);
			aPastas.clear();
			for (int i = 0; i < aux.length; i++) {
				if (aux[i].isFile()) {
					this.aFilesNaPasta.add(aux[i].getName());
				} else {
					this.aPastas.add(aux[i].getName());
				}
			}
		}
	}

	// SETA DATA ATUALIZADA NO ARQUIVO LOCAL BASEADA NA DATA DO FTP
	public void setDataFile(String data, String nome, String diretorio)
			throws ParseException {
		// Long newdata = Long.parseLong(data) - 20005;
		// data = newdata.toString();
		File arquivo = new File(diretorio + nome);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC-3"));
		String newLastModifiedString = data;
		Date newLastModifiedDate = dateFormat.parse(newLastModifiedString);
		arquivo.setLastModified(newLastModifiedDate.getTime());
	}

	// CRIA DIRETÓRIO
	public void criaDir(String localAtual, String nome) {
		File dirLocal = new File(localAtual + nome);
		dirLocal.mkdir();
	}

}
