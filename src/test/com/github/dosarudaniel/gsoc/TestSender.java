/**
 * test.com.github.dosarudaniel.gsoc provides the classes necessary to test the
 * Sender and Receiver from the myjava.com.github.dosarudaniel.gsoc package
 */
package test.com.github.dosarudaniel.gsoc;

import java.util.Timer;

import myjava.com.github.dosarudaniel.gsoc.Sender;

/**
 * Test the Sender unit by scheduling a send multicast action every
 * TIME_INTERVAL_SECONDS
 * 
 * @author dosarudaniel@gmail.com
 * @since 2019-03-07
 *
 */
public class TestSender {
    static final int TIME_INTERVAL_SECONDS = 60;

    /**
     * Schedules a sender to send a multicast message every TIME_INTERVAL_SECONDS
     *
     * @param args multicastIpAddress and portNumber
     */
    public static void main(String[] args) {
	Timer timer = new Timer();
	// java TestSender <IP> <PORT_NUMBER> <NR_OF_PACKETS_TO_BE_SENT>
	// <MAX_PAYLOAD_SIZE>
	// Ex: java TestSender 230.0.0.0 5000 200 1024
	timer.schedule(
		new Sender(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])), 0,
		TIME_INTERVAL_SECONDS * 1000L);
    }
}
