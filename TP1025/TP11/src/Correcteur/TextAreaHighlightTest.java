package Correcteur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.util.List;

class TextAreaHighlight extends JFrame
{
    JTextArea textComp;

    TextAreaHighlight()
    {
	textComp = new JTextArea();
	add(textComp);

    }	

    // Creates highlights around all occurrences of pattern in textComp
    public void highlight(String pattern) {
        // First remove all old highlights
        // removeHighlights(textComp);
    
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
    
            // Search for pattern
            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos+pattern.length(), myHighlightPainter);
                pos += pattern.length();
            }
        } catch (BadLocationException e) {
        }
    }
    
    public void highlight(List<String> patterns) {
        // First remove all old highlights
        removeHighlights(textComp);
    
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
    
            // Search for pattern
            for(String pattern : patterns) {
            	while ((pos = text.indexOf(pattern, pos)) >= 0) {
                    // Create highlighter using private painter and apply around pattern
                    hilite.addHighlight(pos, pos+pattern.length(), myHighlightPainter);
                    pos += pattern.length();
                }
            }
        } catch (BadLocationException e) {
        }
    }
    
    // Removes only our private highlights
    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
    
        for (int i=0; i<hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }
    
    // An instance of the private subclass of the default highlight painter
    Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red);
    
    // A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }
}






