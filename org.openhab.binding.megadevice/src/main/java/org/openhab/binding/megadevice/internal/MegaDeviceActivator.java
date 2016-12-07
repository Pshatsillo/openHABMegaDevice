/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.megadevice.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Petr Shatsillo
 * @since 1.9.0
 */

public class MegaDeviceActivator implements BundleActivator {

	private static Logger logger = LoggerFactory
			.getLogger(MegaDeviceActivator.class);
	
	private static BundleContext context;

	
	public void start(BundleContext context) throws Exception {
		logger.info("MegaDevice binding has been started.");
	}

	
	public void stop(BundleContext context) throws Exception {
		logger.debug("MegaDevice binding 0.1.2.4 has been stopped.");
	}
	
	/**
	 * Returns the bundle context of this bundle
	 * 
	 * @return the bundle context
	 */
	public static BundleContext getContext() {
		return context;
	}

}
