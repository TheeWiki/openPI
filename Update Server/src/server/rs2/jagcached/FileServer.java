package server.rs2.jagcached;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

import server.rs2.Loader;
import server.rs2.jagcached.dispatch.RequestWorkerPool;
import server.rs2.jagcached.net.FileServerHandler;
import server.rs2.jagcached.net.NetworkConstants;
import server.rs2.jagcached.net.OnDemandPipelineFactory;

/**
 * The core class of the file server.
 * @author Graham
 */
public final class FileServer {
	
	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(FileServer.class.getName());
	
	/**
	 * The executor service.
	 */
	private final ExecutorService service = Executors.newCachedThreadPool();
	
	/**
	 * The request worker pool.
	 */
	private final RequestWorkerPool pool = new RequestWorkerPool();
	
	/**
	 * The file server event handler.
	 */
	private final FileServerHandler handler = new FileServerHandler();
	
	/**
	 * The timer used for idle checking.
	 */
	private final Timer timer = new HashedWheelTimer();
	
	/**
	 * Starts the file server.
	 * @throws Exception if an error occurs.
	 */
	public void start() throws Exception {
		pool.start();
		start("ondemand", new OnDemandPipelineFactory(handler, timer), NetworkConstants.SERVICE_PORT);
	}

	/**
	 * Starts the specified service.
	 * @param name The name of the service.
	 * @param pipelineFactory The pipeline factory.
	 * @param port The port.
	 */
	private void start(String name, ChannelPipelineFactory pipelineFactory, int port) {
		SocketAddress address = new InetSocketAddress(port);
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.setFactory(new NioServerSocketChannelFactory(service, service));
		bootstrap.setPipelineFactory(pipelineFactory);
		bootstrap.bind(address);
		System.out.println("Successfully binded jaggrab on port: " + port);
	}

}
