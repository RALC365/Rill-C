import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
public class SwungDemo {
   public static void main(String args[]) throws BadLocationException {
      JFrame frame = new JFrame("Demo");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Container container = frame.getContentPane();
      JTextPane textPane = new JTextPane();
      textPane.setBackground(Color.orange);
      SimpleAttributeSet attributeSet = new SimpleAttributeSet();
      StyleConstants.setItalic(attributeSet, true);
      textPane.setCharacterAttributes(attributeSet, true);
      textPane.setText("We waited long for ");
      Font font = new Font("Serif", Font.ITALIC, 18);
      textPane.setFont(font);
      StyledDocument doc = textPane.getStyledDocument();
      Style style = textPane.addStyle("", null);
      StyleConstants.setForeground(style, Color.red);
      StyleConstants.setBackground(style, Color.white);
      doc.insertString(doc.getLength(), "Game of Thrones ", style);
      StyleConstants.setForeground(style, Color.yellow);
      StyleConstants.setBackground(style, Color.gray);
      doc.insertString(doc.getLength(), "Season 8", style);
      JScrollPane scrollPane = new JScrollPane(textPane);
      container.add(scrollPane, BorderLayout.CENTER);
      frame.setSize(550, 300);
      frame.setVisible(true);
   }
}