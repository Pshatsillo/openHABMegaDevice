package org.openhab.binding.megadevice.internal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MegadeviceHttpServer extends Thread {

	private static Logger logger = LoggerFactory
			.getLogger(MegaDeviceActivator.class);

	public void run() {
		ServerSocket ss = null;

		logger.info("Starting http...");

		try {
			ss = new ServerSocket(8989);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			Socket s = null;
			try {
				s = ss != null ? ss.accept() : null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.err.println("Client accepted");
			new Thread(new MegaDeviceHttpSocket(s)).start();
		}
	}

}
