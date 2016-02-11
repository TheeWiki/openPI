package server.rs2.fileserver;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.zip.CRC32;

import server.rs2.jagcached.fs.IndexedFileSystem;

/**
 * Handles update requests and creates a response.
 * 
 * @author Graham Edgecombe
 * 
 */
public class RequestHandler {

	/**
	 * The absolute path of the files directory.
	 */
	public static final String FILES_DIRECTORY = new File("data/htdocs/").getAbsolutePath();

	/**
	 * The cached CRC table.
	 */
	private static ByteBuffer crcTable = null;

	/**
	 * Handles a single request.
	 * 
	 * @param request
	 *            The request.
	 * @return The response.
	 */
	public static synchronized Response handle(Request request) {
		IndexedFileSystem fs = null;
		try {
			fs = new IndexedFileSystem(new File("./data/fs/"), true);
		} catch(Exception e) {
		}
		String path = request.getPath();
		if (path.equals("/")) {
			path = "/index.html";
		}
		String mime = getMimeType(path);
		try {
			if (crcTable == null) {
				crcTable = calculateCrcTable(fs);
			}
			if (path.startsWith("/crc")) {
				return new Response(crcTable.asReadOnlyBuffer(), mime);
			} else if (path.startsWith("/title")) {
				return new Response(fs.getFile(0, 1).array(), mime);
			} else if (path.startsWith("/config")) {
				return new Response(fs.getFile(0, 2).array(), mime);
			} else if (path.startsWith("/interface")) {
				return new Response(fs.getFile(0, 3).array(), mime);
			} else if (path.startsWith("/media")) {
				return new Response(fs.getFile(0, 4).array(), mime);
			} else if (path.startsWith("/versionlist")) {
				return new Response(fs.getFile(0, 5).array(), mime);
			} else if (path.startsWith("/textures")) {
				return new Response(fs.getFile(0, 6).array(), mime);
			} else if (path.startsWith("/wordenc")) {
				return new Response(fs.getFile(0, 7).array(), mime);
			} else if (path.startsWith("/sounds")) {
				return new Response(fs.getFile(0, 8).array(), mime);
			}
			path = new File(FILES_DIRECTORY + path).getAbsolutePath();
			if (!path.startsWith(FILES_DIRECTORY)) {
				return null;
			}
			RandomAccessFile f = new RandomAccessFile(path, "r");
			try {
				MappedByteBuffer data = f.getChannel().map(MapMode.READ_ONLY, 0, f.length());
				return new Response(data, mime);
			} finally {
				f.close();
			}
		} catch (IOException ex) {
			return null;
		}
	}

	private static ByteBuffer calculateCrcTable(IndexedFileSystem fs) throws IOException {
		final CRC32 crc = new CRC32();
		int[] checksums = new int[9];

		/*
		 * Set the first checksum. As 0 is the CRC table itself (which we are
		 * calculating!), this is set to the client version instead.
		 */
		checksums[0] = 317;

		/*
		 * Calculate the checksums.
		 */
		for (int i = 1; i < checksums.length; i++) {
			byte[] file = fs.getFile(0, i).array(); // each of these maps
															// to the files
															// above
			crc.reset();
			crc.update(file, 0, file.length);
			checksums[i] = (int) crc.getValue();
		}

		/*
		 * This is some sort of overall hash of all the checksums themselves.
		 */
		int hash = 1234;

		/*
		 * Calculate the hash from every checksum.
		 */
		for (int i = 0; i < checksums.length; i++) {
			hash = (hash << 1) + checksums[i];
		}

		/*
		 * And write the table to a bytebuffer.
		 */
		ByteBuffer bb = ByteBuffer.allocate(4 * (checksums.length + 1));
		for (int i = 0; i < checksums.length; i++) {
			bb.putInt(checksums[i]);
		}
		bb.putInt(hash);
		bb.flip();
		return bb;
	}

	/**
	 * Gets the mime type of a file.
	 * 
	 * @param path
	 *            The path to the file.
	 * @return The mime type.
	 */
	private static String getMimeType(String path) {
		String mime = "application/octect-stream";
		if (path.endsWith(".htm") || path.endsWith(".html")) {
			mime = "text/html";
		} else if (path.endsWith(".jar")) {
			mime = "application/java-archive";
		}
		return mime;
	}

}
