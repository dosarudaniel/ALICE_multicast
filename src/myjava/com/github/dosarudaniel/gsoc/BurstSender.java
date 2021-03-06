package myjava.com.github.dosarudaniel.gsoc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BurstSender {
    public static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private String ip_address;
    private int portNumber;
    private int payloadLength;
    private int rate;
    private int timeToRun = 0;
    public static int nrPacketsSent = 0;
    public static boolean counterRunning = false;

    private Thread thread = new Thread(new Runnable() {
	SingletonLogger singletonLogger2 = new SingletonLogger();
	Logger logger2 = this.singletonLogger2.getLogger();

	@Override
	public void run() {
	    int oldNrPacketsSent = 0;
	    while (BurstSender.counterRunning) {
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		this.logger2.log(Level.INFO, "Sent " + (BurstSender.nrPacketsSent - oldNrPacketsSent)
			+ " packets per second. \n" + "Total " + BurstSender.nrPacketsSent);
		oldNrPacketsSent = BurstSender.nrPacketsSent;
	    }
	}
    });

    public BurstSender(String ip_address, int portnumber, int payloadLength, int rate, int timeToRun)
	    throws SecurityException {
	this.ip_address = ip_address;
	this.portNumber = portnumber;
	this.payloadLength = payloadLength;
	this.rate = rate;
	this.timeToRun = timeToRun;
    }

    public void work() {
	String payload = Utils.randomString(this.payloadLength);

	byte[] packet = payload.getBytes(Charset.forName(Utils.CHARSET));

	long milis = 1_000 / this.rate;
	int nanos = (1_000_000_000 / this.rate) % 1_000_000;

	try (DatagramSocket socket = new DatagramSocket()) {
	    InetAddress group = InetAddress.getByName(this.ip_address);
	    DatagramPacket datagramPacket = new DatagramPacket(packet, packet.length, group, this.portNumber);
	    this.thread.start();

	    long t = System.currentTimeMillis();
	    long end = t + 1000 * this.timeToRun;
	    counterRunning = true;
	    while (System.currentTimeMillis() < end) {
		Thread.sleep(milis, nanos);
		socket.send(datagramPacket);
		nrPacketsSent++;
	    }
	    counterRunning = false;
	    this.thread.join();
	} catch (SocketException e1) {
	    e1.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
}
