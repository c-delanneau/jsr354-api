/*
 * CREDIT SUISSE IS WILLING TO LICENSE THIS SPECIFICATION TO YOU ONLY UPON THE CONDITION THAT YOU ACCEPT ALL OF THE TERMS CONTAINED IN THIS AGREEMENT. PLEASE READ THE TERMS AND CONDITIONS OF THIS AGREEMENT CAREFULLY. BY DOWNLOADING THIS SPECIFICATION, YOU ACCEPT THE TERMS AND CONDITIONS OF THE AGREEMENT. IF YOU ARE NOT WILLING TO BE BOUND BY IT, SELECT THE "DECLINE" BUTTON AT THE BOTTOM OF THIS PAGE.
 *
 * Specification:  JSR-354  Money and Currency API ("Specification")
 *
 * Copyright (c) 2012-2013, Credit Suisse
 * All rights reserved.
 */
package javax.money.format;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This interface defines access to the exchange and currency conversion logic
 * of JavaMoney.
 * 
 * @author Anatole Tresch
 */
public final class MonetaryFormats {

	private static MonetaryFormatsSpi monetaryFormatSpi = loadMonetaryFormatSpi();

	private MonetaryFormats() {
	}

	private static MonetaryFormatsSpi loadMonetaryFormatSpi() {
		MonetaryFormatsSpi spi = null;
		try {
			// try loading directly from ServiceLoader
			Iterator<MonetaryFormatsSpi> instances = ServiceLoader.load(
					MonetaryFormatsSpi.class).iterator();
			if (instances.hasNext()) {
				spi = instances.next();
				return spi;
			}
		} catch (Exception e) {
			Logger.getLogger(MonetaryFormats.class.getName()).log(Level.INFO,
					"No MonetaryFormatSpi found, using  default.", e);
		}
		return new DefaultMonetaryFormatsSpi();
	}

	/**
	 * Return the style id's supported by this {@link ItemFormatterFactorySpi}
	 * instance.
	 * 
	 * @see LocalizationStyle#getId()
	 * @param targetType
	 *            the target type, never {@code null}.
	 * @return the supported style ids, never {@code null}.
	 */
	public static Collection<String> getSupportedStyleIds(Class<?> targetType) {
		Collection<String> styleIDs = monetaryFormatSpi
				.getSupportedStyleIds(targetType);
		if (styleIDs == null) {
			Logger.getLogger(MonetaryFormats.class.getName()).log(
					Level.WARNING,
					"MonetaryFormatSpi.getSupportedStyleIds returned null for "
							+ targetType);
			return Collections.emptySet();
		}
		return styleIDs;
	}

	/**
	 * Method allows to check if a named style is supported.
	 * 
	 * @param targetType
	 *            the target type, never {@code null}.
	 * @param styleId
	 *            The style id.
	 * @return true, if a spi implementation is able to provide an
	 *         {@link ItemFormat} for the given style.
	 */
	public static boolean isSupportedStyle(Class<?> targetType, String styleId) {
		return monetaryFormatSpi.isSupportedStyle(targetType, styleId);
	}

	/**
	 * This method returns an instance of an {@link ItemFormat} .
	 * 
	 * @param targetType
	 *            the target type, never {@code null}.
	 * @param style
	 *            the {@link LocalizationStyle} to be attached to this
	 *            {@link ItemFormat}, which also contains the target
	 *            {@link Locale} instances to be used, as well as other
	 *            attributes configuring this instance.
	 * @return the formatter required, if available.
	 * @throws ItemFormatException
	 *             if the {@link LocalizationStyle} passed can not be used for
	 *             configuring the {@link ItemFormat} and no matching
	 *             {@link ItemFormat} could be provided.
	 */
	public static <T> ItemFormat<T> getItemFormat(Class<T> targetType,
			LocalizationStyle style) throws ItemFormatException {
		if (style == null) {
			throw new IllegalArgumentException("style required.");
		}
		if (targetType == null) {
			throw new IllegalArgumentException("targetType required.");
		}
		try {
			ItemFormat<T> f = monetaryFormatSpi.getItemFormat(targetType,
					style);
			if (f != null) {
				return f;
			}
			throw new ItemFormatException("No formatter available for "
					+ targetType + " and " + style);
		} catch (Exception e) {
			throw new ItemFormatException("Error accessing formatter for "
					+ targetType + " and " + style, e);
		}
	}

	/**
	 * This method returns an instance of an {@link ItemFormat}. This method
	 * is a convenience method for
	 * {@code getItemFormatter(LocalizationStyle.valueOf(locale)) }.
	 * 
	 * @param targetType
	 *            the target type, never {@code null}.
	 * @param locale
	 *            The target locale.
	 * @return the formatter required, if available.
	 * @throws ItemFormatException
	 *             if the {@link LocalizationStyle} passed can not be used for
	 *             configuring the {@link ItemFormat} and no matching
	 *             {@link ItemFormat} could be provided.
	 */
	public static <T> ItemFormat<T> getItemFormat(Class<T> targetType,
			Locale locale) throws ItemFormatException {
		return getItemFormat(targetType, LocalizationStyle.of(locale));
	}

	public static interface MonetaryFormatsSpi {
		/**
		 * Return the style id's supported by this
		 * {@link ItemFormatterFactorySpi} instance.
		 * 
		 * @see LocalizationStyle#getId()
		 * @param targetType
		 *            the target type, never {@code null}.
		 * @return the supported style ids, never {@code null}.
		 */
		public Collection<String> getSupportedStyleIds(Class<?> targetType);

		/**
		 * Method allows to check if a named style is supported.
		 * 
		 * @param targetType
		 *            the target type, never {@code null}.
		 * @param styleId
		 *            The style id.
		 * @return true, if a spi implementation is able to provide an
		 *         {@link ItemFormat} for the given style.
		 */
		public boolean isSupportedStyle(Class<?> targetType, String styleId);

		/**
		 * This method returns an instance of an {@link ItemFormat} .
		 * 
		 * @param targetType
		 *            the target type, never {@code null}.
		 * @param style
		 *            the {@link LocalizationStyle} to be attached to this
		 *            {@link ItemFormat}, which also contains the target
		 *            {@link Locale} instances to be used, as well as other
		 *            attributes configuring this instance.
		 * @return the formatter required, if available.
		 * @throws ItemFormatException
		 *             if the {@link LocalizationStyle} passed can not be used
		 *             for configuring the {@link ItemFormat} and no matching
		 *             {@link ItemFormat} could be provided.
		 */
		public <T> ItemFormat<T> getItemFormat(Class<T> targetType,
				LocalizationStyle style) throws ItemFormatException;

	}

	private static final class DefaultMonetaryFormatsSpi implements
			MonetaryFormatsSpi {

		@Override
		public Collection<String> getSupportedStyleIds(Class<?> targetType) {
			return Collections.emptySet();
		}

		@Override
		public boolean isSupportedStyle(Class<?> targetType, String styleId) {
			return false;
		}

		@Override
		public <T> ItemFormat<T> getItemFormat(Class<T> targetType,
				LocalizationStyle style) throws ItemFormatException {
			throw new ItemFormatException("No MonetaryFormatSpi registered.");
		}

	}
}
