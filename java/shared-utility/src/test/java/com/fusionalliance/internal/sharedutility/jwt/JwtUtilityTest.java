package com.fusionalliance.internal.sharedutility.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.fusionalliance.internal.sharedutility.core.ValidationException;
import com.fusionalliance.internal.sharedutility.jwt.JwtException;
import com.fusionalliance.internal.sharedutility.jwt.JwtUtility;
import com.google.common.collect.ImmutableMap;

public class JwtUtilityTest {

	@Test
	public void generateSecretTest() {
		final String secret = JwtUtility.generateSecret();

		Assert.assertEquals("Secret not 32 characters", 32, secret.length());
	}

	@Test
	public void secretToBytesTest() {
		final byte[] secretBytes = JwtUtility.secretToBytes(JwtUtility.generateSecret());

		Assert.assertTrue("Secret bytes not 32 long", secretBytes.length == 32);
	}

	@Test
	public void doHmacSha256HashEncodedTest() {
		final String payload = "{\"name\": \"Jane Bloggs\", \"admin\": true}";
		final String payloadEncoded = JwtUtility.base64UrlEncode(payload);
		final String message = JwtUtility.JWT_HEADER_ENCODED + "." + payloadEncoded;
		final String secret = "<-8B\"rN)\"D/bh5WW;B]?2Y)ON`,`I!W7";

		try {
			final String encodedHash = JwtUtility.doHmacSha256HashEncoded(message, secret);

			Assert.assertEquals("Hash does not match externally verified value", "A5TZvJPmLQ1gB8QFPcu9ch8soEHYAQbZ_sumVw0HlJg", encodedHash);
		}
		catch (ValidationException e) {
			Assert.fail("Validation exception: " + e.getMessage());
		}
	}

	@Test
	public void base64UrlDecodeTest() {
		final String message = JwtUtility.generateSecret();
		final String messageEncoded = JwtUtility.base64UrlEncode(message);

		final String decodedMessage = JwtUtility.base64UrlDecode(messageEncoded);

		assertEquals("Decoded message does not match", message, decodedMessage);
	}

	@Test(expected = ValidationException.class)
	public void base64UrlDecodeTestBadEncoding() {
		final String messageEncoded = JwtUtility.base64UrlEncode("some message") + "0";

		JwtUtility.base64UrlDecode(messageEncoded);
	}

	@Test
	public void verifyHeaderTest() {
		JwtUtility.verifyHeader(JwtUtility.JWT_HEADER_ENCODED);

		// No assertions are required
	}

	@Test
	public void buildRetrieveTokenRoundTripTest() {
		final String name = "Joe Bloggs";
		final Boolean admin = Boolean.FALSE;
		final long expires = System.currentTimeMillis() / 1000;
		final Map<String, Object> payloadMap = ImmutableMap.<String, Object>builder() //
				.put("name", name) //
				.put("admin", admin) //
				.put("exp", expires) //
				.build();
		final String secret = JwtUtility.generateSecret();

		final String token = JwtUtility.buildToken(payloadMap, secret);
		Map<String, Object> retrievedPayloadMap;
		try {
			retrievedPayloadMap = JwtUtility.retrieveTokenPayload(token);
		}
		catch (JwtException e) {
			fail(e.getMessage());
			// Unreachable, but the compiler doesn't know that
			throw new RuntimeException();
		}

		assertTrue("Header missing", retrievedPayloadMap.containsKey(JwtUtility.TOKEN_HEADER));
		assertTrue("Payload missing", retrievedPayloadMap.containsKey(JwtUtility.TOKEN_PAYLOAD));
		assertTrue("Signature missing", retrievedPayloadMap.containsKey(JwtUtility.TOKEN_SIGNATURE));

		assertEquals("Name does not match", name, retrievedPayloadMap.get("name"));
		assertEquals("Admin does not match", admin, retrievedPayloadMap.get("admin"));
		assertEquals("Name does not match", expires, ((Number) retrievedPayloadMap.get("exp")).longValue());

		assertTrue("Signature not valid", JwtUtility.verifySignature((String) retrievedPayloadMap.get(JwtUtility.TOKEN_HEADER),
				(String) retrievedPayloadMap.get(JwtUtility.TOKEN_PAYLOAD), (String) retrievedPayloadMap.get(JwtUtility.TOKEN_SIGNATURE), secret));
	}
}