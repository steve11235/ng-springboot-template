package com.fusionalliance.internal.sharedutility.core;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.fusionalliance.internal.sharedutility.core.GsonHelper;
import com.fusionalliance.internal.sharedutility.core.ValidationException;
import com.google.common.collect.ImmutableMap;

public class GsonHelperTest {

	@Test
	public void jsonToMapTest() {
		// The JSON is deliberately malformed; demonstrates that GSON is lenient by default
		final String json = "{ \"name\": \"Joe Bloggs\", \"admin\": false, \"exp\": 1521750056, key = value }";

		final Map<String, Object> map = GsonHelper.jsonToMap(json);

		final String name = (String) map.get("name");
		assertEquals("Name did not match", "Joe Bloggs", name);

		final Boolean admin = (Boolean) map.get("admin");
		assertEquals("Admin did not match", Boolean.FALSE, admin);

		final Double expires = (Double) map.get("exp");
		assertEquals("Expires did not match", Double.valueOf("1521750056"), expires);

		final String value = (String) map.get("key");
		assertEquals("Value did not match", "value", value);
	}

	@Test(expected = ValidationException.class)
	public void jsonToMapTestUnparsable() {
		// JSON missing value
		final String json = "{ \"name\": \"Joe Bloggs\", \"admin\": false, \"exp\": 1521750056, \"key\": }";

		GsonHelper.jsonToMap(json);
	}

	@Test
	public void mapToJsonTest() {
		final Map<String, Object> map = ImmutableMap.<String, Object>builder() //
				.put("name", "Joe Bloggs") //
				.put("admin", Boolean.TRUE) //
				.put("exp", Long.valueOf("1521750056")) //
				.build();

		final String json = GsonHelper.mapToJson(map);

		assertEquals("JSON does not match", "{\n" + "  \"name\": \"Joe Bloggs\",\n" + "  \"admin\": true,\n" + "  \"exp\": 1521750056\n" + "}", json);
	}
}
