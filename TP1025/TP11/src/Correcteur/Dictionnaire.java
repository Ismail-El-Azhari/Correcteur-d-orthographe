package Correcteur;

import java.io.File;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Dictionnaire {
	public String texte;
	// creation d'un tableau avec tous les mots du dictionnaire
	public List<String> dictionnaireList = new ArrayList<>();
	
	Dictionnaire() {}
	
	
	public void cree(File pathFichier) throws IOException {
		
		String fileDict = Files.readString(pathFichier.toPath(), StandardCharsets.UTF_8);
		// creation d'une liste avec les mots du dictionnaire
		this.dictionnaireList = Arrays.asList(fileDict.split("\\s+"));
		

		
		
	}
	 //url: https://gist.github.com/gabhi/11243437
    //C'est la methode de la distance de Levenshtein
    public static int distance(String s1, String s2){
        int edits[][]=new int[s1.length()+1][s2.length()+1];
        for(int i=0;i<=s1.length();i++)
            edits[i][0]=i;
        for(int j=1;j<=s2.length();j++)
            edits[0][j]=j;
        for(int i=1;i<=s1.length();i++){
            for(int j=1;j<=s2.length();j++){
                int u=(s1.charAt(i-1)==s2.charAt(j-1)?0:1);
                edits[i][j]=Math.min(
                                edits[i-1][j]+1,
                                Math.min(
                                   edits[i][j-1]+1,
                                   edits[i-1][j-1]+u
                                )
                            );
            }
        }
        return edits[s1.length()][s2.length()];
    }
    public String[] motsProches(String mot){
        return dictionnaireList.stream().sorted((p1, p2) -> distance(p1,mot)-distance(p2,mot)).limit(5).collect(Collectors.toList()).toArray(new String[5]);

    }
       
}



