package mirrorFTP;

import java.util.ArrayList;
import java.util.List;

public class ListaEnc {

	List<No> lista;
	
	public ListaEnc () {
		lista = new ArrayList<No>();
	}
	
	public void addNo (No no) {
		lista.add(no);
	}
	
	public void remNo (No no, String name) {
		if (lista.contains(no.getNome())) {
			lista.remove(no);
		}
	}
	
	public String getNo (String nome) {
		if (lista.contains(nome)) {
			return nome;
		}
		return null;
	}
	
		
}
