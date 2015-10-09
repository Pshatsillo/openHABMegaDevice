package org.openhab.binding.megadevice.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MegaDeviceActivator implements BundleActivator {

	private static Logger logger = LoggerFactory
			.getLogger(MegaDeviceActivator.class);

	@Override
	public void start(BundleContext context) throws Exception {
		logger.info("MegaDevice binding has been started.");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		logger.debug("MegaDevice binding has been stopped.");
	}

}
