package myjava.com.github.dosarudaniel.gsoc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

import myjava.com.github.dosarudaniel.gsoc.Blob.PACHET_TYPE;

public class FragmentedBlob {
	private static final int CHECKSUM_SIZE = 16;
	private int fragmentOffset;
	private String key;
	private UUID objectUUID;
	private PACHET_TYPE pachetType;
	private byte[] payloadChecksum;
	private byte[] payload;

	public FragmentedBlob(String payload) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	}

	public FragmentedBlob(byte[] payload, int fragmentOffset, PACHET_TYPE pachetType, String key)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		this.fragmentOffset = fragmentOffset;
	}

	public FragmentedBlob(byte[] serialisedFragmentedBlob) {
		// deserializare manuala
		// TODO
	}

	public int getFragmentOffset() {
		return this.fragmentOffset;
	}

	public void setFragmentOffset(int fragmentOffset) {
		this.fragmentOffset = fragmentOffset;
	}

	// serializare manuala
	public byte[] toBytes() throws IOException {
		byte[] fragmentOffset_byte = ByteBuffer.allocate(4).putInt(fragmentOffset).array();

		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			// 1. fragment Offset
			out.write(fragmentOffset_byte);
			// 2. key
			out.write(key.getBytes(Charset.forName("UTF-8")));

			byte pachetType_byte = (byte) (this.pachetType == PACHET_TYPE.METADATA ? 0 : 1);
			byte[] pachetType_byte_array = new byte[1];
			pachetType_byte_array[0] = pachetType_byte;
			out.write(pachetType_byte_array);

			out.write(UUIDUtils.getBytes(this.objectUUID));

			/*
			 * 
			 * 
			 * private String key; private UUID objectUUID; private PACHET_TYPE pachetType;
			 * private byte[] payload; private byte[] payloadChecksum;
			 */

			byte[] pachet = out.toByteArray();

			return pachet;
		}
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public UUID getObjectUUID() {
		return objectUUID;
	}

	public void setObjectUUID(UUID objectUUID) {
		this.objectUUID = objectUUID;
	}

	public PACHET_TYPE getPachetType() {
		return pachetType;
	}

	public void setPachetType(PACHET_TYPE pachetType) {
		this.pachetType = pachetType;
	}

	public byte[] getPayloadChecksum() {
		return payloadChecksum;
	}

	public void setPayloadChecksum(byte[] payloadChecksum) {
		this.payloadChecksum = payloadChecksum;
	}

	/**
	 * Returns the payload and checks if the checksum is correct.
	 *
	 * @return String - The payload of a Blob object
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws IOException                  - if checksum failed
	 */
	public byte[] getPayload() throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
		if (!Arrays.equals(this.payloadChecksum, calculateChecksum(this.payload))) {
			throw new IOException("Checksum failed!");
		}
		return this.payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	/**
	 * Calculates the sha1 checksum for a certain string data
	 *
	 * @param data used to generate checksum
	 * @return payloadChecksum
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] calculateChecksum(byte[] data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		mDigest.update(data);
		return mDigest.digest();
	}
}