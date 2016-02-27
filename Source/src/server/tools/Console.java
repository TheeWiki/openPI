package server.tools;

import server.panel.ControlPanel;

/**
 * This tool is simply designed for testing out the console itself, rather than
 * starting up the whole server to access console. Can be utilized to debug,
 * change, fix, etc.. component.
 * 
 * @author Dennis
 *
 */
public class Console {
	
	/**
	 * Creates a main void which creates a runnable source
	 * in which the Control Panel can appear from.
	 * @param args
	 */
	public static void main(String[] args) {
		
		/**
		 * Creates a new form know as Control Panel, manage players easily
		 * via Control Panel, etc..
		 */
		new ControlPanel(true);
	}
}