package mirrorFTP;

import java.util.ArrayList;
import java.util.List;

public class ListaEnc {

	List<No> noList;
	
	public ListaEnc () {
		noList = new ArrayList<No>();
	}
	
	public void addNo (No no) {
		noList.add(no);
	}
	
	public void remNo (No no, String name) {
		if (noList.contains(no.getNome())) {
			noList.remove(no);
		}
	}
	
	public String getNo (String nome) {
		if (noList.contains(nome)) {
			return nome;
		}
		return null;
	}
	
		
}
