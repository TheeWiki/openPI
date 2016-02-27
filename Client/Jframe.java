import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;


public class Jframe extends Client implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8173720640262523513L;
	private JFrame frame;

	public Jframe(String args[]) throws Exception {
		super();
		try {
			sign.signlink.startpriv(InetAddress.getByName(server));
			initUI();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void initUI() {
		try {
			JFrame.setDefaultLookAndFeelDecorated(true);
			frame = new JFrame(Constants.CLIENT_NAME);
			frame.setLayout(new BorderLayout());
			setFocusTraversalKeysEnabled(false);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JPanel gamePanel = new JPanel();

			gamePanel.setLayout(new BorderLayout());
			gamePanel.add(this);
			gamePanel.setPreferredSize(new Dimension(765, 503));
			
			JMenuBar jmenubar = new JMenuBar();
			frame.add(jmenubar);
			frame.getContentPane().add(gamePanel, BorderLayout.CENTER);
			frame.pack();

			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setResizable(false);

			init();
		} catch (Exception e) {
				e.printStackTrace();
		}
	}

	public URL getCodeBase() {
		try {
			return new URL("http://" + server + "/cache");
		} catch (Exception e) {
			return super.getCodeBase();
		}
	}

	public URL getDocumentBase() {
		return getCodeBase();
	}

	public void loadError(String s) {
		System.out.println("loadError: " + s);
	}

	public String getParameter(String key) {
			return "";
	}

	private static void openUpWebSite(String url) {
		Desktop d = Desktop.getDesktop();
		try {
			d.browse(new URI(url)); 	
		} catch (Exception e) {
		}
	}

	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		try {
			if (cmd != null) {
				if (cmd.equalsIgnoreCase("exit")) {
					System.exit(0);
				}
				if (cmd.equalsIgnoreCase("Project-Insanity.net")) {
					openUpWebSite("http://www.project-insanity.net/");
				}	
			}
		} catch (Exception e) {
		}
	}
}