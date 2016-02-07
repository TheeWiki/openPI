package server.fileserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import server.fileserver.UpdateSession.Type;

/**
 * <p>
 * An implementation of the JAGGRAB and HTTP server.
 * </p>
 * 
 * <p>
 * A lot of the work in the JAGGRAB server is based on some code Miss Silabsoft
 * re-released, originally authored by Winterlove.
 * </p>
 * 
 * @author Graham Edgecombe
 * 
 */
public class FileServer {

	/**
	 * The JAGGRAB port to listen on.
	 */
	public static final int JAGGRAB_PORT = 43595;
	//public static final int JAGGRAB_PORT = 43594;

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(FileServer.class.getName());

	/**
	 * The <code>IoAcceptor</code> instance.
	 */
	private final IoAcceptor jaggrabAcceptor = new SocketAcceptor();

	/**
	 * The <code>IoAcceptor</code> instance.
	 */
	private final IoAcceptor httpAcceptor = new SocketAcceptor();

	/**
	 * Creates the jaggrab server.
	 */
	public FileServer() {
	}

	/**
	 * Binds the server to the ports.
	 * 
	 * @return The server instance, for chaining.
	 * @throws IOException
	 */
	public FileServer bind() throws IOException {
		jaggrabAcceptor.bind(new InetSocketAddress(JAGGRAB_PORT), new ConnectionHandler(Type.JAGGRAB));
		return this;
	}

}
