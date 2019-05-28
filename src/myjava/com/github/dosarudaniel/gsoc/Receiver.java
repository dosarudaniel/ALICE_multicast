/**
 * myjava.com.github.dosarudaniel.gsoc provides the classes necessary to send/
 * receive multicast messages which contains Blob object with random length,
 * random content payload.
 */
package myjava.com.github.dosarudaniel.gsoc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Receiver class waits for DatagramPacket from the Sender and prints the
 * timestamp and the content of the packet is the checksum is correct
 *
 * @author dosarudaniel@gmail.com
 * @since 2019-03-07
 *
 */
public class Receiver {
    private final static int BUF_SIZE = 65536;
    private byte[] buf = new byte[BUF_SIZE];
    private String ip_address;
    private int portNumber;

    /**
     * Parameterized constructor
     *
     * @param ip_address
     * @param portNumber
     */
    public Receiver(String ip_address, int portNumber) {
	super();
	this.ip_address = ip_address;
	this.portNumber = portNumber;
    }

    /**
     * Receive multicast packets Opens a multicast socket and it sets that socket to
     * a multicast group. Receives continuously DatagramPackets through the
     * multicast DatagramSocket Every DatagramPacket is deserialized and by calling
     * the getPayload method every message's checksum is verified. The receiver
     * prints the current timestamp and the content of the message if the message
     * with the content "end" is received, the Receiver unit stops.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    public void work() throws IOException, ClassNotFoundException, NoSuchAlgorithmException {
	try (MulticastSocket socket = new MulticastSocket(this.portNumber)) {
	    InetAddress group = InetAddress.getByName(this.ip_address);
	    socket.joinGroup(group);
	    while (true) {
		// Receive object
		DatagramPacket packet = new DatagramPacket(this.buf, this.buf.length);
		socket.receive(packet);
		// deserialize
		// FragmentedBlob fragmentedBlob = ByteToFragmentedBlob(packet);
		Blob blob = (Blob) deserialize(this.buf);
		// Print timestamp and content
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
		String payload = "";// new String(blob.getPayload(), "UTF-8");
		System.out.println("[" + timeStamp + "]Data received:" + payload);
		if ("end".equals(payload)) {
		    break;
		}

	    }
	    socket.leaveGroup(group);
	}
    }

    /**
     * Deserializes objects received through DatagramPackets
     *
     * @param data - The serialized object
     * @return Object - The deserialized object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	try (ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in)) {
	    return is.readObject();
	}
    }
}
