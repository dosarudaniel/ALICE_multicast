import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Blob class
 *
 * @author dosarudaniel@gmail.com
 * @since 2019-03-07
 *
 */
public class Blob implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static final int CHECKSUM_SIZE = 16;
	private String payload;
	private byte[] checksum;

	/**
	 * Parameterized constructor - creates a Blob object and it Calculates its
	 * checksum
	 *
	 * @param payload
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public Blob(String payload) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		this.payload = payload;
		this.checksum = calculateChecksum(payload);
	}

	/**
	 * Returns the payload and checks if the checksum is correct. Prints an error if
	 * the object is corrupt
	 *
	 * @return String - The payload of a Blob object
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	public String getPayload() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {
		byte[] checksum1 = calculateChecksum(this.payload);
		if (!Arrays.equals(this.checksum, checksum1)) {
			throw new Exception("Checksum failed!");
		}
		return this.payload;
	}

	/**
	 * Calculates the checksum for a certain string data
	 *
	 * @param data
	 * @return byte[] -  the checksum
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] calculateChecksum(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		mDigest.update(data.getBytes("utf8"));
		return mDigest.digest();
	}
}
