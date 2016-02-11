package server.rs2.fileserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.support.BaseByteBuffer;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

import server.rs2.fileserver.UpdateSession.Type;

/**
 * Handles connection events.
 * 
 * @author Graham Edgecombe
 * 
 */
public class ConnectionHandler implements IoHandler {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ConnectionHandler.class.getName());

	/**
	 * The type of handler we are.
	 */
	private Type type;

	/**
	 * Creates the handler.
	 * 
	 * @param type
	 *            The type of handler.
	 */
	public ConnectionHandler(Type type) {
		this.type = type;
	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		arg0.close();
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception {
		logger.log(Level.SEVERE, "Error while handling request.", throwable);
	}

	@Override
	public void messageReceived(IoSession session, Object in) throws Exception {
		if(in instanceof BaseByteBuffer)
			((UpdateSession) session.getAttribute("session")).readLine(getRS2String(in));
		else if(in instanceof String)
			((UpdateSession) session.getAttribute("session")).readLine((String) in);
	}
	
	public static String getRS2String(Object in) {
		BaseByteBuffer buf = (BaseByteBuffer) in;
		StringBuilder bldr = new StringBuilder();
		byte b;
		while (buf.hasRemaining() && (b = buf.get()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	@Override
	public void sessionCreated(IoSession arg0) throws Exception {
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("Accepted a connection from: " + session.getRemoteAddress().toString());
		session.getFilterChain().addFirst("textFilter", new ProtocolCodecFilter(new TextLineCodecFactory()));
		session.setAttribute("session", new UpdateSession(type, session));
	}

	@Override
	public void sessionClosed(IoSession arg0) throws Exception {
	}

}
