package Correcteur;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collection;

public class Verificateur {
	
	public List<String> motInvalid;
	
	public void verificateurMotInvalid(List<String> motsUniques, List<String> dictionnaire){
		

		// valide la liste de mot unique avec celle du dictionnaire et conserve les mots qui n'y sont pas
		motInvalid = motsUniques.stream().filter(m -> !dictionnaire.contains(m.toLowerCase())).collect(Collectors.toList());
		

		
		
		
	}
	
	

	

}
