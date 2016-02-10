package server.util;

public class Buffer {

	public int getBigSmart() {
		if (data[currentOffset] >= 0)
			return readUShort();
		return 0x7fffffff & getInt();
	}

	public final int getInt() {
		currentOffset += 4;
		return ((0xff & data[-3 + currentOffset]) << 16)
				+ ((((0xff & data[-4 + currentOffset]) << 24) + ((data[-2
						+ currentOffset] & 0xff) << 8)) + (data[-1
						+ currentOffset] & 0xff));
	}

	public int readUnsignedSmart3() {
		try {
			int i = 0xff & data[currentOffset];
			if ((i ^ 0xffffffff) <= -129)
				return -49152 + readUnsignedWord();
			return -64 + readUnsignedByte();
		} catch (Exception e) {
			e.printStackTrace();
			return 45;
		}
	}

	public int read3Bytes() {
		currentOffset += 3;
		return ((data[currentOffset - 3] & 0xff) << 16)
				+ ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
	}

	public int g3() {
		currentOffset += 3;
		return ((data[currentOffset - 3] & 0xff) << 16)
				+ ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
	}

	public byte g1b() {
		return data[currentOffset++];
	}

	public int gsmart() {
		int i = data[currentOffset] & 0xff;
		if (i < 128)
			return g1() - 64;
		else
			return g2() - 49152;
	}

	public int g1() {
		return data[currentOffset++] & 0xff;
	}

	final int readUnsignedSmart() {
		int i_76_ = data[currentOffset] & 0xff;
		if ((i_76_ ^ 0xffffffff) > -129)
			return readUnsignedByte();
		return -32768 + readUShort();
	}

	final int read24BitInt() {// read24bitint
		currentOffset += 3;
		return ((0xff & data[-1 + currentOffset]) + ((data[-2 + currentOffset] << 8 & 0xff00) + ((data[-3
				+ currentOffset] & 0xff) << 16)));
	}

	final int readInt() {// readInt?
		currentOffset += 4;
		return ((0xff & data[-1 + currentOffset])
				+ (data[currentOffset - 2] << 8 & 0xff00)
				+ (data[-4 + currentOffset] << 24 & ~0xffffff) - -(0xff0000 & data[-3
				+ currentOffset] << 16));
	}

	public final byte readByte() {// readByte
		return data[currentOffset++];
	}
	
	public byte[] readBytes() {
		int i = currentOffset;
		while (data[currentOffset++] != 10)
			;
		byte abyte0[] = new byte[currentOffset - i - 1];
		System.arraycopy(data, i, abyte0, i - i, currentOffset - 1 - i);
		return abyte0;
	}


	final byte readShort() {// readShort
		return (byte) (-data[currentOffset++] + 128);
	}

	final int v(int i) {
		currentOffset += 3;
		return (0xff & data[currentOffset - 3] << 16)
				+ (0xff & data[currentOffset - 2] << 8)
				+ (0xff & data[currentOffset - 1]);
	}

	public int g2() {
		currentOffset += 2;
		return ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
	}

	public int g4() {
		currentOffset += 4;
		return ((data[currentOffset - 4] & 0xff) << 24)
				+ ((data[currentOffset - 3] & 0xff) << 16)
				+ ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
	}

	public int readUSmart2() {
		int baseVal = 0;
		int lastVal = 0;
		while ((lastVal = method422()) == 32767) {
			baseVal += 32767;
		}
		return baseVal + lastVal;
	}

	public String readNewString() {
		int i = currentOffset;
		while (data[currentOffset++] != 0)
			;
		return new String(data, i, currentOffset - i - 1);
	}

	public byte[] getData(byte[] buffer2) {
		for (int i = 0; i < buffer2.length; i++)
			buffer2[i] = data[currentOffset++];
		return buffer2;
	}

	public Buffer(byte abyte0[]) {
		data = abyte0;
		currentOffset = 0;

	}

	public int readShort2() {
		currentOffset += 2;
		int i = ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
		if (i > 60000)
			i = -65535 + i;
		return i;
	}

	public Buffer(byte abyte0[], int i) {
		aByte1399 = 108;
		aBoolean1403 = false;
		aBoolean1404 = true;
		if (i <= 0) {
			throw new NullPointerException();
		} else {
			data = abyte0;
			currentOffset = 0;
			return;
		}
	}

	public void writeByte(int i) {
		if (currentOffset + 2 >= data.length)
			return;
		data[currentOffset++] = (byte) i;
	}

	public void createUnecryptedFrame(int i) {
		data[currentOffset++] = (byte) i;
	}

	public void writeSignedWord(int i) {
		data[currentOffset++] = (byte) (i >> 8);
		data[currentOffset++] = (byte) i;
	}

	public void writeShort(int i) {
		data[currentOffset++] = (byte) i;
		data[currentOffset++] = (byte) (i >> 8);
	}

	public void writeMedium(int i) {
		data[currentOffset++] = (byte) (i >> 16);
		data[currentOffset++] = (byte) (i >> 8);
		data[currentOffset++] = (byte) i;
	}

	public void writeReversedInt(int i) {
		data[currentOffset++] = (byte) (i >> 24);
		data[currentOffset++] = (byte) (i >> 16);
		data[currentOffset++] = (byte) (i >> 8);
		data[currentOffset++] = (byte) i;
	}

	public void writeInt(int j) {
		data[currentOffset++] = (byte) j;
		data[currentOffset++] = (byte) (j >> 8);
		data[currentOffset++] = (byte) (j >> 16);
		data[currentOffset++] = (byte) (j >> 24);
		return;
	}

	public void writeQWord(int i, long l) {
		try {
			data[currentOffset++] = (byte) (int) (l >> 56);
			data[currentOffset++] = (byte) (int) (l >> 48);
			data[currentOffset++] = (byte) (int) (l >> 40);
			data[currentOffset++] = (byte) (int) (l >> 32);
			data[currentOffset++] = (byte) (int) (l >> 24);
			data[currentOffset++] = (byte) (int) (l >> 16);
			data[currentOffset++] = (byte) (int) (l >> 8);
			data[currentOffset++] = (byte) (int) l;
		} catch (RuntimeException runtimeexception) {
		}
	}

	@SuppressWarnings("deprecation")
	public void writeString(String s) {
		s.getBytes(0, s.length(), data, currentOffset);
		currentOffset += s.length();
		data[currentOffset++] = 10;
	}

	public void writeBytes(byte abyte0[], int i, int j) {
		for (int k = j; k < j + i; k++)
			data[currentOffset++] = abyte0[k];

	}

	public void writeFrameSize(int i) {
		data[currentOffset - i - 1] = (byte) i;
	}

	public int readUnsignedByte() {
		return data[currentOffset++] & 0xff;
	}

	public byte readSByte() {
		return data[currentOffset++];
	}

	public byte readSignedByte() {
		return data[currentOffset++];
	}

	public int readUShort() {
		currentOffset += 2;
		return ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
	}

	public int readUnsignedWord() {
		currentOffset += 2;
		return ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
	}

	public int readUnsignedShort() {
		currentOffset += 2;
		return ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
	}

	public int method411() {
		currentOffset += 2;
		int i = ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int readSignedWord() {
		currentOffset += 2;
		int i = ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int read3Bytes1() {
		currentOffset += 3;
		return ((data[currentOffset - 3] & 0xff) << 16)
				+ ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
	}

	public int readDWord() {
		currentOffset += 4;
		return ((data[currentOffset - 4] & 0xff) << 24)
				+ ((data[currentOffset - 3] & 0xff) << 16)
				+ ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] & 0xff);
	}

	public int read5() {
		currentOffset += 5;
		return ((data[currentOffset - 5] & 0xff) << 32)
				+ ((data[currentOffset - 4] & 0xff) << 24)
				+ ((data[currentOffset - 3] & 0xff) << 16)
				+ (data[currentOffset - 2] & 0xff);
	}

	public long readQWord() {
		long l = (long) readDWord() & 0xffffffffL;
		long l1 = (long) readDWord() & 0xffffffffL;
		return (l << 32) + l1;
	}

	public String readString() {
		int i = currentOffset;
		while (data[currentOffset++] != 10)
			;
		return new String(data, i, currentOffset - i - 1);
	}

	public byte[] method416(byte byte0) {
		int i = currentOffset;
		while (data[currentOffset++] != 10)
			;
		byte abyte0[] = new byte[currentOffset - i - 1];
		if (byte0 != 30)
			aBoolean1404 = !aBoolean1404;
		for (int j = i; j < currentOffset - 1; j++)
			abyte0[j - i] = data[j];

		return abyte0;
	}

	public void method417(int i, int j, byte abyte0[]) {
		for (int l = j; l < j + i; l++)
			abyte0[l] = data[currentOffset++];

	}

	public void method418() {
		bit_pos = currentOffset * 8;
	}

	public int gbits(int i, int j) {
		int k = bit_pos >> 3;
		int l = 8 - (bit_pos & 7);
		int i1 = 0;
		if (j != 0)
			aBoolean1403 = !aBoolean1403;
		bit_pos += i;
		for (; i > l; l = 8) {
			i1 += (data[k++] & anIntArray1409[l]) << i - l;
			i -= l;
		}

		if (i == l)
			i1 += data[k] & anIntArray1409[l];
		else
			i1 += data[k] >> l - i & anIntArray1409[i];
		return i1;
	}

	public void end_bit_block() {
		currentOffset = (bit_pos + 7) / 8;
	}

	public int readByteOrShort() {
		int i = data[currentOffset] & 0xff;
		if (i < 128)
			return readUnsignedByte() - 64;
		else
			return readUShort() - 49152;
	}

	public int method422() {
		int i = data[currentOffset] & 0xff;
		if (i < 128)
			return readUnsignedByte();
		else
			return readUShort() - 32768;
	}

	public void method424(int i) {
		data[currentOffset++] = (byte) (-i);
	}

	public void method425(int i, int j) {
		data[currentOffset++] = (byte) (128 - j);
		i = 90 / i;
	}

	public int method426() {
		return data[currentOffset++] - 128 & 0xff;
	}

	public int method427() {
		return -data[currentOffset++] & 0xff;
	}

	public int method428() {
		return 128 - data[currentOffset++] & 0xff;
	}

	public byte method429() {
		return (byte) (-data[currentOffset++]);
	}

	public byte method430() {
		return (byte) (128 - data[currentOffset++]);
	}

	public void writeUnsignedWordBigEndian(int i) {
		data[currentOffset++] = (byte) i;
		data[currentOffset++] = (byte) (i >> 8);
	}

	public void writeUnsignedWordA(int j) {
		data[currentOffset++] = (byte) (j >> 8);
		data[currentOffset++] = (byte) (j + 128);
	}

	public void writeSignedWordBigEndian(int j) {
		data[currentOffset++] = (byte) (j + 128);
		data[currentOffset++] = (byte) (j >> 8);
	}

	public int method434(byte byte0) {
		currentOffset += 2;
		if (byte0 != aByte1399)
			return 3;
		else
			return ((data[currentOffset - 1] & 0xff) << 8)
					+ (data[currentOffset - 2] & 0xff);
	}

	public int method435() {
		currentOffset += 2;
		return ((data[currentOffset - 2] & 0xff) << 8)
				+ (data[currentOffset - 1] - 128 & 0xff);
	}

	public int readUnsignedEndian() {
		currentOffset += 2;
		return ((data[currentOffset - 1] & 0xff) << 8)
				+ (data[currentOffset - 2] - 128 & 0xff);
	}

	public int readSignedEndian() {
		currentOffset += 2;
		int j = ((data[currentOffset - 1] & 0xff) << 8)
				+ (data[currentOffset - 2] & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int readSignedEndian_dup() {
		currentOffset += 2;
		int j = ((data[currentOffset - 1] & 0xff) << 8)
				+ (data[currentOffset - 2] - 128 & 0xff);
		if (j > 32767)
			j -= 0x10000;
		return j;
	}

	public int method439() {
		currentOffset += 4;
		return ((data[currentOffset - 2] & 0xff) << 24)
				+ ((data[currentOffset - 1] & 0xff) << 16)
				+ ((data[currentOffset - 4] & 0xff) << 8)
				+ (data[currentOffset - 3] & 0xff);
	}

	public int method440() {
		currentOffset += 4;
		return ((data[currentOffset - 3] & 0xff) << 24)
				+ ((data[currentOffset - 4] & 0xff) << 16)
				+ ((data[currentOffset - 1] & 0xff) << 8)
				+ (data[currentOffset - 2] & 0xff);
	}

	public void method441(int i, byte abyte0[], int j) {
		for (int k = (i + j) - 1; k >= i; k--)
			data[currentOffset++] = (byte) (abyte0[k] + 128);

	}

	public void method442(int i, int j, byte abyte0[]) {
		for (int k = (j + i) - 1; k >= j; k--)
			abyte0[k] = data[currentOffset++];

	}

	private byte aByte1399;
	private boolean aBoolean1403;
	private boolean aBoolean1404;
	public byte data[];
	public int currentOffset;
	public int bit_pos;
	private static int anIntArray1408[];
	private static final int anIntArray1409[] = { 0, 1, 3, 7, 15, 31, 63, 127,
			255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 0x1ffff,
			0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
			0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff,
			0x7fffffff, -1 };
	public static boolean aBoolean1418;

	static {
		anIntArray1408 = new int[256];
		for (int j = 0; j < 256; j++) {
			int i = j;
			for (int k = 0; k < 8; k++)
				if ((i & 1) == 1)
					i = i >>> 1 ^ 0xedb88320;
				else
					i >>>= 1;

			anIntArray1408[j] = i;
		}

	}
	
}
