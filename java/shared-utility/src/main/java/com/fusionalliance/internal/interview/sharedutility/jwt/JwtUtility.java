package com.fusionalliance.internal.interview.sharedutility.jwt;

import static com.fusionalliance.internal.interview.sharedutility.core.ValidationUtility.checkBadConditionNotMet;
import static com.fusionalliance.internal.interview.sharedutility.core.ValidationUtility.checkGoodConditionMet;
import static com.fusionalliance.internal.interview.sharedutility.core.ValidationUtility.checkObjectNotNull;
import static com.fusionalliance.internal.interview.sharedutility.core.ValidationUtility.checkStringNotBlank;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fusionalliance.internal.interview.sharedutility.core.GsonHelper;
import com.fusionalliance.internal.interview.sharedutility.core.ValidationException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;

/**
 * This class contains methods to support JSON Web Tokens (JWT).
 */
public final class JwtUtility {
	public static final String TOKEN_HEADER = "TOKEN_HEADER";
	public static final String TOKEN_PAYLOAD = "TOKEN_PAYLOAD";
	public static final String TOKEN_SIGNATURE = "TOKEN_SIGNATURE";
	public static final String JWT_HEADER = "{\"alg\": \"HS256\", \"typ\": \"JWT\"}";
	public static final String JWT_HEADER_ENCODED = base64UrlEncode(JWT_HEADER);
	private static final int PRINTABLE_CHAR_START = 33;
	private static final int PRINTABLE_CHAR_RANGE = 94;

	/**
	 * Generate a random 32 character (256 bit) secret composed of printable ASCII characters. This allows the secret to be copied as text.
	 * 
	 * @return
	 */
	public static String generateSecret() {
		final StringBuilder secretBuilder = new StringBuilder(32);

		int value = 0;
		for (int i = 0; i < 32; i++) {
			value = PRINTABLE_CHAR_START + (int) (PRINTABLE_CHAR_RANGE * Math.random());
			secretBuilder.append((char) value);
		}

		return secretBuilder.toString();
	}

	public static String buildToken(final Map<String, Object> payloadParm, final String secretParm) {
		checkObjectNotNull("Payload is null", payloadParm);
		checkBadConditionNotMet("Payload is empty", payloadParm.isEmpty());
		checkStringNotBlank("Secret is blank", secretParm);

		final String payloadJson = GsonHelper.mapToJson(payloadParm);

		return buildToken(payloadJson, secretParm);
	}

	public static String buildToken(String payloadJsonParm, final String secretParm) {
		checkStringNotBlank("Payload JSON is blank", payloadJsonParm);
		checkStringNotBlank("Secret is blank", secretParm);

		final String payloadJsonEncoded = base64UrlEncode(payloadJsonParm);
		final String message = JWT_HEADER_ENCODED + "." + payloadJsonEncoded;
		final String signature = doHmacSha256HashEncoded(message, secretParm);
		final String token = message + "." + signature;

		return token;
	}

	/**
	 * Retrieve the payload from the token. The token is checked that it is a valid format and that the header is valid. This <i>does not</i> verify
	 * the signature.
	 * <p>
	 * " The returned map will also contain keys {@link #TOKEN_HEADER}, {@link #TOKEN_PAYLOAD}, and {@link #TOKEN_SIGNATURE}. The corresponding values
	 * are the raw components of the token.
	 * <p>
	 * Numeric values are expressed as Double in the Map.
	 * 
	 * @param tokenParm
	 *            required, not blank
	 * @return
	 * @throws JwtException
	 *             if the JWT is malformed or was not signed by the secret
	 */
	public static Map<String, Object> retrieveTokenPayload(final String tokenParm) throws JwtException {
		try {
			checkStringNotBlank("Token is null or blank", tokenParm);

			final String[] tokenParts = tokenParm.split("\\.");
			checkGoodConditionMet("The token does not have three parts.", tokenParts.length == 3);

			final String header = tokenParts[0];
			final String payload = tokenParts[1];
			final String signature = tokenParts[2];

			verifyHeader(header);

			final String payloadJson = base64UrlDecode(payload);
			final Map<String, Object> payloadMap = GsonHelper.jsonToMap(payloadJson);

			payloadMap.put(TOKEN_HEADER, header);
			payloadMap.put(TOKEN_PAYLOAD, payload);
			payloadMap.put(TOKEN_SIGNATURE, signature);

			return payloadMap;
		}
		catch (final Exception e) {
			throw new JwtException(e.getMessage());
		}
	}

	/**
	 * Verify the JWT header. This simple implementation supports only JWT using HS256.
	 * <ul>
	 * <li>It must contain exactly two entries</li>
	 * <li>"alg": "HS256"</li>
	 * <li>"typ": "JWT"</li>
	 * </ul>
	 * 
	 * @param headerEncodedParm
	 *            required, not blank, must be Base64 URL encoded
	 * @throws ValidationException
	 *             if any condition is not met
	 */
	@VisibleForTesting
	static void verifyHeader(final String headerEncodedParm) {
		checkStringNotBlank("The encoded header is null or blank.", headerEncodedParm);

		final String headerJson = base64UrlDecode(headerEncodedParm);
		final Map<String, Object> headerMap = GsonHelper.jsonToMap(headerJson);

		checkGoodConditionMet("Header does not contain exactly two entries.", headerMap.size() == 2);
		checkGoodConditionMet("Header 'typ' is not 'JWT'.", "JWT".equals(headerMap.get("typ")));
		checkGoodConditionMet("Header 'alg' is not 'HS256'.", "HS256".equals(headerMap.get("alg")));
	}

	/**
	 * Return true if the JWT signature is valid. This ensures both that the correct secret was used and that the header and payload have not been
	 * modified.
	 * 
	 * @param headerParm
	 *            required, not blank
	 * @param payloadParm
	 *            required, not blank
	 * @param signatureParm
	 *            required, not blank
	 * @param secretParm
	 *            required, not blank
	 */
	public static boolean verifySignature(String headerParm, String payloadParm, String signatureParm, String secretParm) {
		checkStringNotBlank("The header is null or blank.", headerParm);
		checkStringNotBlank("The payload is null or blank.", payloadParm);
		checkStringNotBlank("The signature is null or blank.", signatureParm);
		checkStringNotBlank("The secret is null or blank.", secretParm);

		final String generatedSignature = doHmacSha256HashEncoded(headerParm + "." + payloadParm, secretParm);

		final boolean signatureValid = signatureParm.equals(generatedSignature);

		return signatureValid;
	}

	public static String doHmacSha256HashEncoded(final String messageParm, final String secretParm) {
		final Mac hmacSha256HashEngine;
		try {
			hmacSha256HashEngine = Mac.getInstance("HmacSHA256");
		}
		catch (NoSuchAlgorithmException e) {
			throw new ValidationException("Unable to instantiate hash engine.", e);
		}

		final SecretKeySpec secretKeySpec = new SecretKeySpec(secretToBytes(secretParm), "HmacSHA256");

		try {
			hmacSha256HashEngine.init(secretKeySpec);
		}
		catch (InvalidKeyException e) {
			throw new ValidationException("Unable to initialize hash engine due to bad secret.", e);
		}

		final String encodedHash = base64UrlEncode(hmacSha256HashEngine.doFinal(messageParm.getBytes(StandardCharsets.UTF_8)));

		return encodedHash;
	}

	/**
	 * Transform the String passed to bytes, using UTF-8 encoding.
	 * <p>
	 * When the String contains single-byte characters, the resulting byte array length will equal the String length. If double-byte characters are
	 * present, the byte array will be longer.
	 * 
	 * @param secretParm
	 *            required, not blank, must contain at least 32 characters
	 * @return
	 */
	public static byte[] secretToBytes(final String secretParm) {
		checkStringNotBlank("Secret is null or blank.", secretParm);
		checkGoodConditionMet("Secret is not at least 32 characters.", secretParm.length() >= 32);

		final byte[] secretBytes = secretParm.getBytes(StandardCharsets.UTF_8);

		return secretBytes;
	}

	/**
	 * Base64 URL encode a String.
	 * 
	 * @param messageParm
	 *            required, not blank
	 * @return
	 */
	public static String base64UrlEncode(final String messageParm) {
		checkStringNotBlank("Message may not be null or blank", messageParm);

		final String encodedMessage = base64UrlEncode(messageParm.getBytes(StandardCharsets.UTF_8));

		return encodedMessage;
	}

	/**
	 * Base64 URL encode the bytes from a String.
	 * 
	 * @param messageBytesParm
	 *            required, not empty
	 * @return
	 */
	public static String base64UrlEncode(final byte[] messageBytesParm) {
		checkObjectNotNull("Messages bytes are null", messageBytesParm);
		checkBadConditionNotMet("Messages bytes are empty", messageBytesParm.length == 0);

		// JWT spec requires padding "=" be omitted
		final String encodedMessage = Base64.getUrlEncoder().encodeToString(messageBytesParm).replace("=", "");

		return encodedMessage;
	}

	/**
	 * Base64 URL decode a String.
	 * 
	 * @param encodedMessageParm
	 *            required, not blank
	 * @return
	 */
	public static String base64UrlDecode(final String encodedMessageParm) {
		checkStringNotBlank("Encoded message may not be null or blank", encodedMessageParm);

		try {
			final String message = new String(Base64.getUrlDecoder().decode(encodedMessageParm), StandardCharsets.UTF_8);

			return message;
		}
		catch (final Exception e) {
			throw new ValidationException("Unable to Base64 URL decode the message: " + encodedMessageParm);
		}
	}

	/**
	 * Entry point prints 20 sample tokens for visual inspection.
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		final Map<String, Object> payloadMap = ImmutableMap.<String, Object>builder() //
				.put("foo", "bar") //
				.build();
		for (int i = 0; i < 20; i++) {
			System.out.println(buildToken(payloadMap, generateSecret()));
		}
	}

	/**
	 * Hidden constructor
	 */
	private JwtUtility() {
		// Do nothing
	}
}
