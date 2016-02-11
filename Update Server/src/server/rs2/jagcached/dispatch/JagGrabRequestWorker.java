package server.rs2.jagcached.dispatch;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;

import server.rs2.jagcached.fs.IndexedFileSystem;
import server.rs2.jagcached.net.jaggrab.JagGrabRequest;
import server.rs2.jagcached.net.jaggrab.JagGrabResponse;

/**
 * A worker which services JAGGRAB requests.
 * @author Graham
 */
public final class JagGrabRequestWorker extends RequestWorker<JagGrabRequest> {

	/**
	 * Creates the JAGGRAB request worker.
	 * @param fs The file system.
	 */
	public JagGrabRequestWorker(IndexedFileSystem fs) {
		super(fs);
	}

	@Override
	protected ChannelRequest<JagGrabRequest> nextRequest() throws InterruptedException {
		return RequestDispatcher.nextJagGrabRequest();
	}

	@Override
	protected void service(IndexedFileSystem fs, Channel channel, JagGrabRequest request) throws IOException {
		String path = request.getFilePath();
		ByteBuffer buf = VirtualResourceMapper.getVirtualResource(fs, path);
		if (buf == null) {
			channel.close();
		} else {
			ChannelBuffer wrapped = ChannelBuffers.wrappedBuffer(buf);
			channel.write(new JagGrabResponse(wrapped)).addListener(ChannelFutureListener.CLOSE);
		}
	}

}
