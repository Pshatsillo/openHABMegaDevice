package org.openhab.binding.megadevice.internal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MegadeviceHttpServer extends Thread {
	private static Logger logger = LoggerFactory
			.getLogger(MegaDeviceActivator.class);
	private static int portnumber = 8989;

	public void run() {
		ServerSocket ss = null;

		logger.info("Starting MegaHttpServer at " + portnumber + " port");

		try {
			ss = new ServerSocket(portnumber);
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
			new Thread(new MegaDeviceHttpSocket(s)).start();
		}
	}
	
	public static void setPort(int port){
		portnumber = port;
	}
	

}
