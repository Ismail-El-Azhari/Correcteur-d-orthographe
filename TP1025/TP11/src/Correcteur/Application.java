package Correcteur;
/* 
 * Inspir� de Filechooser de oracle
 * Cree par Ismail El Azhari et Jean-Yves Grenier,
 */


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class Application extends JPanel implements ActionListener, MouseListener {
    
	private static final long serialVersionUID = -5168171880488759069L;
	
	private static final String NEW_LINE = "\n";
	
	private List<String> uniqueToken;
	private Tokenizer tokenizer;
	private Dictionnaire dico;
	private Verificateur motUnique;
	private String fileContent;
	private JButton verifyButton,dictButton;
	private JMenuItem menuOpen,menuSave;
	private JTextArea log;
	private TextAreaHighlight textArea;
	private JFileChooser fc;
    

    public Application() {
        super(new BorderLayout());
        uniqueToken = new ArrayList<>();
        tokenizer = new Tokenizer();
        dico = new Dictionnaire();
        motUnique = new Verificateur();
    }
    
    private void initLayout() {
    	

        //Create the log first, because the action listeners need to refer to it.
        log = new JTextArea(5,15);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();

        
        //Create the verify button.  
        dictButton = new JButton("Ouvrir dictionnaire");
        dictButton.addActionListener(this);
        
        //Create the verify button.  
        verifyButton = new JButton("Verifier");
        // desactive le bouton de verification initialement
        verifyButton.setEnabled(false);
        verifyButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(verifyButton);
        buttonPanel.add(dictButton);

        
        // JmenuBar Creation
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("FILE");
        menuBar.add(menuFile);
        menuOpen = new JMenuItem("Open");
        menuOpen.addActionListener(this);
        menuSave = new JMenuItem("Save as");
        menuSave.addActionListener(this);
        menuFile.add(menuOpen);
        menuFile.add(menuSave);
        
        // TextAreaHighlight creation
        textArea = new TextAreaHighlight();      
        JScrollPane textScrollPane = new JScrollPane(textArea.textComp);
        
        //add items,menu,pane to the layout
        add(menuBar, BorderLayout.PAGE_START);
        add(buttonPanel, BorderLayout.SOUTH);
        add(textScrollPane,BorderLayout.CENTER);
        add(logScrollPane, BorderLayout.EAST);

    }

    public void actionPerformed(ActionEvent e) {
    	

        //Handle open button action.
        if (e.getSource() == menuOpen) {
            int returnVal = fc.showOpenDialog(Application.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	try {
            		// load le fichier
					fileContent = Files.readString(fc.getSelectedFile().toPath(), StandardCharsets.UTF_8);
            		// cree liste de mot unique
					tokenizer.lireFichier(fc.getSelectedFile().getPath());
            		
					// clear le textArea
					textArea.textComp.setText("");

					tokenizer.creerTokens();
					uniqueToken = tokenizer.listeTokUniques;
					// affiche le tableau de texte
					textArea.textComp.setText(fileContent);
					//System.out.println(tokenizer.listeTokUniques);
			
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                log.append(String.format("Opening: %s.%s", fc.getSelectedFile().getName(), NEW_LINE));
                // "Opening: " + fc.getSelectedFile().getName() + "." + NEW_LINE);
            } else {
                log.append(String.format("Open command cancelled by user.%s", NEW_LINE));
            }
            log.setCaretPosition(log.getDocument().getLength());
            
        

        //Handle save button action.
        } else if (e.getSource() == menuSave) {
            int returnVal = fc.showSaveDialog(Application.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                
            	File file = fc.getSelectedFile();
                //This is where a real application would save the file.
                String textContent = textArea.textComp.getText(); // take the content of the jtextarea
                // save the file in proper directory and into a txt file
               try(FileWriter fw = new FileWriter(file+".txt")) {
                    fw.write(textContent);
                }
             catch (Exception ex) {
                ex.printStackTrace();
            } 
        
                
                log.append(String.format("Saving: %s.%s", file.getName(), NEW_LINE));
            } else {
                log.append(String.format("Save command cancelled by user.%s", NEW_LINE));
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
        
        else if (e.getSource() == dictButton) {
        	// rend le bouton de verification actif 
            verifyButton.setEnabled(true);

        	int returnVal = fc.showOpenDialog(Application.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File fileDict = fc.getSelectedFile();
            	
                //This is where a real application would open the file.
            	try {
					dico.cree(fileDict);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	
            }
            
        }
        else if (e.getSource() == verifyButton) {
        	// transmettre liste des mots uniques au verificateur
        	if (uniqueToken.isEmpty()) {
        		if(textArea.textComp.getText().isBlank()) {
        			JOptionPane.showMessageDialog(textArea.textComp, "Aucun texte a corriger");
            		return;		
        		}
        		// Creation de la liste de mot unique
        		tokenizer.creerTokens(textArea.textComp.getText());
        		uniqueToken = tokenizer.listeTokUniques;
        	}
        	motUnique.verificateurMotInvalid(uniqueToken, dico.dictionnaireList);

        	
        	//System.out.println(motUnique.motInvalid);
        	textArea.highlight(motUnique.motInvalid);
        	
        	
            // Liste de mot suggéré
        	String[] motProches= dico.motsProches(uniqueToken.get(0));
        	for(String s:motProches) {
            System.out.println(s);
            log.append(String.format("Mot suggéré: %s.%s", s, NEW_LINE));
            
                    }
        }
        	 
            	
       }
    
   
    // creation du gui
    public void createAndShowGUI() {
    	initLayout();
        //Create and set up the window.
        JFrame frame = new JFrame("Correcteur de Texte");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Add content to the window.
        frame.add(this);

        //Display the window.
        frame.setSize(800, 500);
        frame.setVisible(true);
    }
// mouselistener
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouse clicked");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}