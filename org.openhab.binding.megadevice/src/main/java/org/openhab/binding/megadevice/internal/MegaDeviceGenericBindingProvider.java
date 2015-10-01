/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.megadevice.internal;

import org.openhab.binding.megadevice.MegaDeviceBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Petr
 * @since 0.0.1
 */
public class MegaDeviceGenericBindingProvider extends
		AbstractGenericBindingProvider implements MegaDeviceBindingProvider {
	private static final Logger logger = LoggerFactory
			.getLogger(MegaDeviceGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "megadevice";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			MegaDeviceBindingConfig config = new MegaDeviceBindingConfig();
			config.itemType = item.getClass();
			String[] configParts = bindingConfig.trim().split(":");
			config.password = configParts.length > 0 ? configParts[0]
					: "NO_Pass";
			config.ip = configParts.length > 0 ? configParts[1] : "NO_IP";
			config.port = configParts.length > 0 ? configParts[2] : "NO_PORT";
			addBindingConfig(item, config);
		} else {
			logger.warn("bindingConfig is NULL (item=" + item
					+ ") -> process bindingConfig aborted!");
		}
	}

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Petr
	 * @since 0.0.1
	 */
	static class MegaDeviceBindingConfig implements BindingConfig {
		Class<? extends Item> itemType;
		public String ip;
		public String port;
		public String password;
	}

	@Override
	public String getIP(String itemName) {
		// logger.debug("Megadevice ->>> getItemConfig: "+ itemName);
		MegaDeviceBindingConfig config = (MegaDeviceBindingConfig) bindingConfigs
				.get(itemName);
		// logger.debug("Megadevice ->>> Item: "+ itemName +" config -> " +
		// config.ip);
		return config.ip;
	}

	@Override
	public String getPORT(String itemName) {
		// logger.debug("Megadevice ->>> getItemConfig: "+ itemName);
		MegaDeviceBindingConfig config = (MegaDeviceBindingConfig) bindingConfigs
				.get(itemName);
		// logger.debug("Megadevice ->>> Item: "+ itemName +" config -> " +
		// config.port);
		return config.port;
	}

	@Override
	public String password(String itemName) {
		// logger.debug("Megadevice ->>> getItemConfig: "+ itemName);
		MegaDeviceBindingConfig config = (MegaDeviceBindingConfig) bindingConfigs
				.get(itemName);
		// logger.debug("Megadevice ->>> Item: "+ itemName +" config -> " +
		// config.password);
		return config.password;
	}

	@Override
	public Class<? extends Item> getItemType(String itemName) {
		MegaDeviceBindingConfig config = (MegaDeviceBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.itemType : null;
	}
}