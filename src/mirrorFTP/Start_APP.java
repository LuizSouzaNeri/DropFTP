package mirrorFTP;

public class Start_APP {
	
	public static void main(String[] args) throws Exception {
		// INSTÂNCIA O OBJETO
		Drop_FTP A = new Drop_FTP();
		// EXECUTA O APP DE TEMPOS EM TEMPOS
//		boolean ok = true;
//		while (ok = true) {
			A.getDados();
			A.iniciaAplicativo();
//			Thread.sleep(A.getIntervalo());
		}
	}
//}
