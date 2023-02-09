package Correcteur;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Tokenizer {
    public String texte;
    public String[] textTokens;
    public ArrayList<String> listeTokUniques;

    public Tokenizer() {
    }

    public void lireFichier(String pathFichier) throws IOException {
    	
		BufferedReader reader = new BufferedReader(new FileReader(pathFichier));// creation du buffer de lecture
		String ligne = "";// stockage de le ligne en cours de lecture
		String texteLu = "";// stockage de tout le texte lu 
		
		while((ligne = reader.readLine())!= null) {
			texteLu += ligne + "\n";
		}
		
		texte = texteLu;		
	}
	

    public void creerTokens() {
        texte = texte.replaceAll("[^A-z0-9À-ÿ'-ç]", " ");
        textTokens = texte.split(" +");
        this.listeTokUniques = new ArrayList();
        String[] var4;
        int var3 = (var4 = this.textTokens).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            String tokenDisponible = var4[var2];
            if (!this.listeTokUniques.equals(tokenDisponible)) {
                this.listeTokUniques.add(tokenDisponible);
                //System.out.println(listeTokUniques);
            }
        }

    }
    // creation d'un token a partir d'un texte fourni
    public void creerTokens(String texteInput) {
    	texte = texteInput;
    	creerTokens();
    }
    // ecriture d'un fichier
    public void writeList(ArrayList<String> listeMots) throws IOException {
        File fichierSortie = new File("C:\\Users\\silve\\Documents\\tokUniques.txt");
        FileWriter writ = new FileWriter(fichierSortie);
        Iterator var5 = listeMots.iterator();

        while(var5.hasNext()) {
            String motDispo = (String)var5.next();
            writ.write(motDispo);
            writ.write("\n");
        }

        writ.close();
    }
}