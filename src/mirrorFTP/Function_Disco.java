package mirrorFTP;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Function_Disco {
	
	// ARRAYS
	protected ArrayList<String> aFilesNaPasta = new ArrayList<>();
	protected ArrayList<String> aFilesRemPasta = new ArrayList<>();
	protected ArrayList<String> aPastas = new ArrayList<>();
	protected ArrayList<String> aPastasRemovidas = new ArrayList<>();
	
	//EXCLUIR ARQUIVO
	public void excluiArq(String nome, String diretorio) {
		File file = new File (diretorio+nome);
		if (file.exists()) {
			file.delete();
		}
	}
	
	// DATA DE MODIFICA��O ARQUIVO LOCAL
	public String dataModArqLocal (String nome, String diretorio) {
		File arquivo = new File (diretorio + nome);
		Date myDate = null;
		if(arquivo.isFile()){
			 myDate = new Date(arquivo.lastModified());
		}
		String resp = new SimpleDateFormat("yyyyMMddHHmm").format(myDate);
		return resp;
	}
	
	// CRIA LISTA ARQUIVOS LOCAL
	public void criaListaArqLocal (String diretorio) {
		File arquivo = new File (diretorio);
		File[] aux = null;
		aux = arquivo.listFiles();
		if ((this.aFilesNaPasta.size() == 0) && (this.aPastas.size() == 0)) {
			for (int i = 0; i < aux.length; i++) {
				if (aux[i].isFile()) {
					this.aFilesNaPasta.add(aux[i].getName());
				}
				else {
					this.aPastas.add(aux[i].getName());
				}
			}
			this.aFilesRemPasta.clear();
			this.aPastasRemovidas.clear();
		}
		else {
			aFilesRemPasta.addAll(aFilesNaPasta);
			aFilesNaPasta.clear();
			aPastasRemovidas.addAll(aPastas);
			aPastas.clear();
			for (int i = 0; i < aux.length; i++) {
				if (aux[i].isFile()) {
					this.aFilesNaPasta.add(aux[i].getName());
				}
				else {
					this.aPastas.add(aux[i].getName());
				}
			}
		}
	}
}