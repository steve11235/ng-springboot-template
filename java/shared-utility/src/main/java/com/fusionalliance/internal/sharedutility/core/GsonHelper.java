/**
 * Copyright 2017 by Steve Page of Fusion Alliance
 *
 * Created May 19, 2017
 */
package com.fusionalliance.internal.sharedutility.core;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This utility class provides access to a {@link Gson} instance that can be used throughout the service layer.
 */
public final class GsonHelper {

	/**
	 * This instance is immutable and thread-safe.
	 * <ul>
	 * <li>Serialize nulls is set to make output more explicit</li>
	 * <li>Pretty printing is set to enhance readability for debugging</li>
	 * </ul>
	 */
	public static final Gson GSON;

	static {
		final GsonBuilder gsonBuilder = new GsonBuilder() //
				.serializeNulls() //
				.disableHtmlEscaping() //
				.setPrettyPrinting();
		GSON = gsonBuilder.create();
	}

	/**
	 * Return a Map<String, Object> from the JSON passed. This is a convenience method for parsing simple JSON.
	 * <p>
	 * <b>Note:</b> GSON tends to be lenient when parsing malformed JSON. For example, the following parses:
	 * 
	 * <pre>
	 * { key = value }
	 * </pre>
	 * 
	 * @param jsonParm
	 *            required, not blank
	 * @return
	 * @throws ValidationException
	 *             if JSON is null, blank, or unparsable
	 */
	public static Map<String, Object> jsonToMap(final String jsonParm) {
		ValidationUtility.checkStringNotBlank("JSON is blank", jsonParm);

		final Map<String, Object> map;
		try {
			// Silly dup definition due to compiler bug caused by annotation
			@SuppressWarnings("unchecked")
			Map<String, Object> tempMap = (Map<String, Object>) GSON.fromJson(jsonParm, HashedMap.class);
			map = tempMap;
		}
		catch (final Exception e) {
			throw new ValidationException("JSON could not be parsed: " + StringUtils.truncate(jsonParm, 500));
		}

		return map;
	}

	/**
	 * Return a JSON String build from a Map<String, Object>. This is a convenience method for creating simple JSON.
	 * 
	 * @param mapParm
	 *            required, not empty
	 * @return
	 */
	public static String mapToJson(final Map<String, Object> mapParm) {
		ValidationUtility.checkObjectNotNull("Map is null", mapParm);
		ValidationUtility.checkBadConditionNotMet("Map is empty", mapParm.isEmpty());

		final String json = GSON.toJson(mapParm);

		return json;
	}

	/**
	 * Hidden constructor
	 */
	private GsonHelper() {
		// Do nothing
	}
}
