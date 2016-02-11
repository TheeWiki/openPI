package server.rs2.jagcached.dispatch;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import server.rs2.jagcached.fs.FileDescriptor;
import server.rs2.jagcached.fs.IndexedFileSystem;
import server.rs2.jagcached.net.ondemand.OnDemandRequest;
import server.rs2.jagcached.net.ondemand.OnDemandResponse;

/**
 * A worker which services 'on-demand' requests.
 * @author Graham
 */
public final class OnDemandRequestWorker extends RequestWorker<OnDemandRequest> {
	
	/**
	 * The maximum length of a chunk, in bytes.
	 */
	private static final int CHUNK_LENGTH = 500;

	/**
	 * Creates the 'on-demand' request worker.
	 * @param fs The file system.
	 */
	public OnDemandRequestWorker(IndexedFileSystem fs) {
		super(fs);
	}

	@Override
	protected ChannelRequest<OnDemandRequest> nextRequest() throws InterruptedException {
		return RequestDispatcher.nextOnDemandRequest();
	}

	@Override
	protected void service(IndexedFileSystem fs, Channel channel, OnDemandRequest request) throws IOException {
		FileDescriptor desc = request.getFileDescriptor();
				
		ByteBuffer buf = fs.getFile(desc);
		int length = buf.remaining();
		
		for (int chunk = 0; buf.remaining() > 0; chunk++) {
			int chunkSize = buf.remaining();
			if (chunkSize > CHUNK_LENGTH) {
				chunkSize = CHUNK_LENGTH;
			}
			
			byte[] tmp = new byte[chunkSize];
			buf.get(tmp, 0, tmp.length);
			ChannelBuffer chunkData = ChannelBuffers.wrappedBuffer(tmp, 0, chunkSize);
			
			OnDemandResponse response = new OnDemandResponse(desc, length, chunk, chunkData);
			channel.write(response);
		}
	}

}
