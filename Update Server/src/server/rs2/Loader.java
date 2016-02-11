package server.rs2;

import java.io.IOException;

import server.rs2.jagcached.FileServer;

public class Loader {

	/**
	 * The main method of the server, starts it up and gets it running
	 * 
	 * @param args
	 * @throws NullPointerException
	 * @throws IOException
	 */
	public static void main(String[] args) {
		System.out.println("Starting up the file server.");
		try {
			new FileServer().start();
			new server.rs2.fileserver.FileServer().bind();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("The file server is now ready to accept connections.");
		System.gc();
	}
}
