import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author jack
 *
 */

public class ItemListSearch implements ActionListener, KeyListener {

	private JTextArea namePanel;
	private JTextArea idPanel;
	private JTextArea searchPanel;

	public ItemListSearch() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame localJFrame = new JFrame(
				"openPI - Item Search");
		localJFrame.setDefaultCloseOperation(2);
		localJFrame.getContentPane().setLayout(new BorderLayout());
		this.namePanel = new JTextArea();
		this.namePanel.setEditable(false);
		localJFrame.setResizable(false);
		this.idPanel = new JTextArea();
		this.idPanel.setEditable(false);
		JPanel localJPanel1 = new JPanel(new FlowLayout());
		localJPanel1.add(this.namePanel);
		localJPanel1.add(this.idPanel);
		JScrollPane localJScrollPane = new JScrollPane(localJPanel1, 22, 31);
		localJScrollPane.setPreferredSize(new Dimension(280, 515));
		String str = "\n";
		for (int i = 0; i < ItemDef.totalItems; i++) {
			if (i != 0) {
				this.namePanel.append(str + ItemDef.forID(i).name);
				this.idPanel.append(str + ItemDef.forID(i).id);
			} else {
				i = 1;
				this.namePanel.append(ItemDef.forID(i).name);
				this.idPanel.append(String.valueOf(ItemDef.forID(i).id));
			}
		}
		JButton localJButton = new JButton("Search");
		localJButton.addActionListener(this);
		this.searchPanel = new JTextArea();
		this.searchPanel.addKeyListener(this);
		this.searchPanel.setLineWrap(false);
		this.searchPanel.setRows(1);
		this.searchPanel.setColumns(9);
		JPanel localJPanel2 = new JPanel(new FlowLayout());
		localJPanel2.add(this.searchPanel);
		localJPanel2.add(localJButton);
		localJFrame.getContentPane().add(localJScrollPane, "Center");
		localJFrame.getContentPane().add(localJPanel2, "South");
		localJFrame.pack();
		
		localJFrame.setLocationRelativeTo(null);
		localJFrame.setVisible(true);
		this.searchPanel.requestFocus();
	}

	private void executeSearch() {
		String str1 = this.searchPanel.getText();
		this.namePanel.setText("");
		this.idPanel.setText("");
		String str2 = "\n";
		for (int i = 0; i < ItemDef.totalItems; i++) {
			if (ItemDef.forID(i).name == null)
				continue;
			if (!ItemDef.forID(i).name.toLowerCase().contains(
					str1.toLowerCase()))
				continue;
			if (i != 0) {
				this.namePanel.append(str2 + ItemDef.forID(i).name);
				this.idPanel.append(str2 + ItemDef.forID(i).id);
			} else {
				i = 1;
				this.namePanel.append(ItemDef.forID(i).name);
				this.idPanel.append(String.valueOf(ItemDef.forID(i).id));
			}
		}

		if (this.namePanel.getText().equals(""))
			this.namePanel.setText("No Results Found");
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			executeSearch();
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10) {
			this.searchPanel.setText(this.searchPanel.getText().replace("\n",
					""));
		}
	}

	public void actionPerformed(ActionEvent e) {
		executeSearch();
	}

}