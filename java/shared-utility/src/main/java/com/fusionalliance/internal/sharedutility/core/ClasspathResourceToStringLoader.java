/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 18, 2017
 */
package com.fusionalliance.internal.sharedutility.core;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This utility class loads the contents of a resource from the classpath and returns the contents as a String.
 */
public final class ClasspathResourceToStringLoader {

	/**
	 * Retrieve the contents of a resource on the classpath and return it in a String.
	 * 
	 * @param resourceNameParm
	 *            required: for a package relative to the class passed, pass an unqualified path, i.e. "resource.txt" or "foo/resource.txt";
	 *            otherwise, pass a fully qualified path, i.e. "/com/fusionalliance/spage/resource.txt" or
	 *            "/com/fusionalliance/spage/foo/resource.txt"
	 * @param referenceClassParm
	 *            required: the Class used to locate the resource; this is needed for relative paths and ensuring that the correct ClassLoader is used
	 * @param charsetParm
	 *            required: Avoid using {@link Charset#defaultCharset()}, as this may lead to problems if resources are moved across OS's; use UTF-8
	 *            to create resources and pass {@link StandardCharsets.UTF_8} to avoid issues)
	 * @return String representation of the contents of the file
	 * @throws ValidationException
	 *             if a required parameter is missing
	 * @throws IllegalArgumentException
	 *             if the resource is not found or cannot be loaded
	 */
	public static String retrieveContents(final String resourceNameParm, final Class<?> referenceClassParm, final Charset charsetParm) {
		ValidationUtility.checkStringNotBlank("The resource name is blank.", resourceNameParm);
		ValidationUtility.checkObjectNotNull("Class is null.", referenceClassParm);
		ValidationUtility.checkObjectNotNull("Charset is null.", charsetParm);

		final InputStream resourceStream = referenceClassParm.getResourceAsStream(resourceNameParm);

		if (null == resourceStream) {
			throw new IllegalArgumentException(
					"No resource found for name: " + resourceNameParm + ", relative package: " + referenceClassParm.getPackage().getName());
		}

		try {
			final Reader resourceReader = new InputStreamReader(resourceStream, charsetParm);

			return ReaderToStringLoader.readToString(resourceReader);
		}
		catch (final Exception e) {
			throw new IllegalArgumentException("Failed to read contents of resource: " + resourceNameParm, e);
		}
	}

	/**
	 * Hidden constructor.
	 */
	private ClasspathResourceToStringLoader() {
		// Do nothing
	}
}
