package server.util;

import server.Constants;

public class Buffer {

	public Buffer(byte data[]) {
		payload = data;
		currentOffset = 0;
	}

	public byte readSignedByteA() {
		return (byte) (payload[currentOffset++] - 128);
	}

	public byte readSignedByteC() {
		return (byte) (-payload[currentOffset++]);
	}

	public byte readSignedByteS() {
		return (byte) (128 - payload[currentOffset++]);
	}

	public int readUnsignedByteA() {
		return payload[currentOffset++] - 128 & 0xff;
	}

	public int readUnsignedByteC() {
		return -payload[currentOffset++] & 0xff;
	}

	public int readUnsignedByteS() {
		return 128 - payload[currentOffset++] & 0xff;
	}

	public void writeByteA(int value) {
		ensureCapacity(1);
		payload[currentOffset++] = (byte) (value + 128);
	}

	public void writeByteS(int value) {
		ensureCapacity(1);
		payload[currentOffset++] = (byte) (128 - value);
	}

	public void writeByteC(int value) {
		ensureCapacity(1);
		payload[currentOffset++] = (byte) (-value);
	}

	public int readLEShort() {
		currentOffset += 2;
		int i = ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int readSignedShortA() {
		currentOffset += 2;
		int i = ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] - 128 & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int readLEShortA() {
		currentOffset += 2;
		int i = ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] - 128 & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int readLEUShort() {
		currentOffset += 2;
		return ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] & 0xff);
	}

	public int readUShortA() {
		currentOffset += 2;
		return ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] - 128 & 0xff);
	}

	public int readLEUShortA() {
		currentOffset += 2;
		return ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] - 128 & 0xff);
	}

	public void writeLEShortA(int value) {
		ensureCapacity(2);
		payload[currentOffset++] = (byte) (value + 128);
		payload[currentOffset++] = (byte) (value >> 8);
	}

	public void writeShortA(int value) {
		ensureCapacity(2);
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) (value + 128);
	}

	public int readInt_v1() {
		currentOffset += 4;
		return ((payload[currentOffset - 2] & 0xff) << 24)
				+ ((payload[currentOffset - 1] & 0xff) << 16)
				+ ((payload[currentOffset - 4] & 0xff) << 8)
				+ (payload[currentOffset - 3] & 0xff);
	}

	public int readInt_v2() {
		currentOffset += 4;
		return ((payload[currentOffset - 3] & 0xff) << 24)
				+ ((payload[currentOffset - 4] & 0xff) << 16)
				+ ((payload[currentOffset - 1] & 0xff) << 8)
				+ (payload[currentOffset - 2] & 0xff);
	}

	public void writeInt_v1(int value) {
		ensureCapacity(4);
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) value;
		payload[currentOffset++] = (byte) (value >> 24);
		payload[currentOffset++] = (byte) (value >> 16);
	}

	public void writeInt_v2(int value) {
		ensureCapacity(4);
		payload[currentOffset++] = (byte) (value >> 16);
		payload[currentOffset++] = (byte) (value >> 24);
		payload[currentOffset++] = (byte) value;
		payload[currentOffset++] = (byte) (value >> 8);
	}

	public void readReverseData(byte data[], int offset, int length) {
		for (int index = (length + offset) - 1; index >= length; index--)
			data[index] = payload[currentOffset++];

	}

	public void writeReverseData(byte data[], int offset, int length) {
		ensureCapacity(offset);
		for (int index = (length + offset) - 1; index >= length; index--)
			payload[currentOffset++] = data[index];

	}

	public void readReverseDataA(byte data[], int offset, int length) {
		ensureCapacity(offset);
		for (int index = (length + offset) - 1; index >= length; index--)
			data[index] = (byte) (payload[currentOffset++] - 128);

	}

	public void writeReverseDataA(byte data[], int offset, int length) {
		ensureCapacity(offset);
		for (int index = (length + offset) - 1; index >= length; index--)
			payload[currentOffset++] = (byte) (data[index] + 128);

	}

	public void createFrame(int id) {
		ensureCapacity(1);
		payload[currentOffset++] = (byte) (id + packetEncryption.getNextValue());
	}

	private static final int frameStackSize = 10;
	private int frameStackPtr = -1;
	private int frameStack[] = new int[frameStackSize];

	public void createFrameVarSize(int id) {
		ensureCapacity(3);
		payload[currentOffset++] = (byte) (id + packetEncryption.getNextValue());
		payload[currentOffset++] = 0;
		if (frameStackPtr >= frameStackSize - 1) {
			throw new RuntimeException("Stack overflow");
		} else
			frameStack[++frameStackPtr] = currentOffset;
	}

	public void createFrameVarSizeWord(int id) {
		ensureCapacity(2);
		payload[currentOffset++] = (byte) (id + packetEncryption.getNextValue());
		writeShort(0);
		if (frameStackPtr >= frameStackSize - 1) {
			throw new RuntimeException("Stack overflow");
		} else
			frameStack[++frameStackPtr] = currentOffset;
	}

	public void endFrameVarSize() {
		if (frameStackPtr < 0)
			throw new RuntimeException("Stack empty");
		else
			writeFrameSize(currentOffset - frameStack[frameStackPtr--]);
	}

	public void endFrameVarSizeWord() {
		if (frameStackPtr < 0)
			throw new RuntimeException("Stack empty");
		else
			writeFrameSizeWord(currentOffset - frameStack[frameStackPtr--]);
	}

	public void writeByte(int value) {
		ensureCapacity(1);
		payload[currentOffset++] = (byte) value;
	}

	public void writeShort(int value) {
		ensureCapacity(2);
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) value;
	}

	public void writeLEShort(int value) {
		ensureCapacity(2);
		payload[currentOffset++] = (byte) value;
		payload[currentOffset++] = (byte) (value >> 8);
	}

	public void writeTriByte(int value) {
		ensureCapacity(3);
		payload[currentOffset++] = (byte) (value >> 16);
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) value;
	}

	public void writeInt(int value) {
		ensureCapacity(4);
		payload[currentOffset++] = (byte) (value >> 24);
		payload[currentOffset++] = (byte) (value >> 16);
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) value;
	}

	public void writeLEInt(int value) {
		ensureCapacity(4);
		payload[currentOffset++] = (byte) value;
		payload[currentOffset++] = (byte) (value >> 8);
		payload[currentOffset++] = (byte) (value >> 16);
		payload[currentOffset++] = (byte) (value >> 24);
	}

	public void writeLong(long value) {
		ensureCapacity(8);
		payload[currentOffset++] = (byte) (int) (value >> 56);
		payload[currentOffset++] = (byte) (int) (value >> 48);
		payload[currentOffset++] = (byte) (int) (value >> 40);
		payload[currentOffset++] = (byte) (int) (value >> 32);
		payload[currentOffset++] = (byte) (int) (value >> 24);
		payload[currentOffset++] = (byte) (int) (value >> 16);
		payload[currentOffset++] = (byte) (int) (value >> 8);
		payload[currentOffset++] = (byte) (int) value;
	}

	public void writeString(java.lang.String text) {
		ensureCapacity(text.length());
		System.arraycopy(text.getBytes(), 0, payload, currentOffset, text.length());
		currentOffset += text.length();
		payload[currentOffset++] = 10;
	}

	public void writeBytes(byte data[], int offset, int length) {
		ensureCapacity(offset);
		for (int index = length; index < length + offset; index++)
			payload[currentOffset++] = data[index];
	}

	public void writeFrameSize(int value) {
		payload[currentOffset - value - 1] = (byte) value;
	}

	public void writeFrameSizeWord(int value) {
		payload[currentOffset - value - 2] = (byte) (value >> 8);
		payload[currentOffset - value - 1] = (byte) value;
	}

	public int readUnsignedByte() {
		return payload[currentOffset++] & 0xff;
	}

	public byte readSignedByte() {
		return payload[currentOffset++];
	}

	public int readUnsignedWord() {
		currentOffset += 2;
		return ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] & 0xff);
	}

	public int readSignedShort() {
		currentOffset += 2;
		int value = ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] & 0xff);
		if (value > 32767)
			value -= 0x10000;
		return value;
	}

	public int readInt() {
		currentOffset += 4;
		return ((payload[currentOffset - 4] & 0xff) << 24)
				+ ((payload[currentOffset - 3] & 0xff) << 16)
				+ ((payload[currentOffset - 2] & 0xff) << 8)
				+ (payload[currentOffset - 1] & 0xff);
	}

	public long readLong() {
		long msi = readInt() & 0xffffffffL;
		long lsi = readInt() & 0xffffffffL;
		return (msi << 32) + lsi;
	}

	public String readString() {
		int value = currentOffset;
		while (payload[currentOffset++] != 10);
		return new String(payload, value, currentOffset - value - 1);
	}

	public void readBytes(byte data[], int offset, int length) {
		for (int index = length; index < length + offset; index++)
			data[index] = payload[currentOffset++];

	}

	public void initBitAccess() {
		bitPosition = currentOffset * 8;
	}

	public void writeBits(int numBits, int value) {
		ensureCapacity(((int) Math.ceil(numBits * 8)) * 4);
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;

		for (; numBits > bitOffset; bitOffset = 8) {
			payload[bytePos] &= ~bitMaskOut[bitOffset];
			payload[bytePos++] |= (value >> (numBits - bitOffset))
					& bitMaskOut[bitOffset];

			numBits -= bitOffset;
		}
		if (numBits == bitOffset) {
			payload[bytePos] &= ~bitMaskOut[bitOffset];
			payload[bytePos] |= value & bitMaskOut[bitOffset];
		} else {
			payload[bytePos] &= ~(bitMaskOut[numBits] << (bitOffset - numBits));
			payload[bytePos] |= (value & bitMaskOut[numBits]) << (bitOffset - numBits);
		}
	}

	public void finishBitAccess() {
		currentOffset = (bitPosition + 7) / 8;
	}

	public byte payload[] = null;
	public int currentOffset = 0;
	public int bitPosition = 0;

	public static int bitMaskOut[] = new int[32];
	static {
		for (int index = 0; index < 32; index++)
			bitMaskOut[index] = (1 << index) - 1;
	}

	public void ensureCapacity(int value) {
		if ((currentOffset + value + 1) >= payload.length) {
			byte[] oldBuffer = payload;
			int newLength = (payload.length * 2);
			payload = new byte[newLength];
			System.arraycopy(oldBuffer, 0, payload, 0, oldBuffer.length);
			ensureCapacity(value);
		}
	}

	public void reset() {
		if (!(currentOffset > Constants.BUFFER_SIZE)) {
			byte[] oldBuffer = payload;
			payload = new byte[Constants.BUFFER_SIZE];
			for (int index = 0; index < currentOffset; index++) {
				payload[index] = oldBuffer[index];
			}
		}
	}

	public ISAACCipher packetEncryption = null;
}
