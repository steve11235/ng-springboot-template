package com.fusionalliance.internal.jwt.shared;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.stereotype.Component;

import com.fusionalliance.internal.sharedutility.jwt.JwtUtility;

/**
 * This class holds JWT secret information and is intended to avoid the need to share a common secret between a JWT provider and the application that
 * authenticates the JWT. Of course, this only works when the provider and the authenticator are running in the same Spring context.
 * <p>
 * This POC sets the secret when the context loads this class and associates it with {@link #BASE_SECRET_ID}. In the future, this class could update
 * the secret periodically, retaining deprecated secrets until any tokens created with them expire.
 */
@Component
public class SecretContainer {
	public static final String BASE_SECRET_ID = "sec0";

	private final ConcurrentMap<String, String> secretsById = new ConcurrentSkipListMap<>();
	private String currentSecretId = BASE_SECRET_ID;

	/**
	 * Constructor
	 */
	public SecretContainer() {
		secretsById.put(currentSecretId, JwtUtility.generateSecret());
	}

	/**
	 * Retrieve information about the current secret.
	 * 
	 * @return
	 */
	public SecretInfo getCurrentSecretInfo() {
		final SecretInfo currentSecretInfo = new SecretInfo(currentSecretId, secretsById.get(currentSecretId));

		return currentSecretInfo;
	}

	/**
	 * Retrieve the secret associated to the secret ID.
	 * <p>
	 * <b>Note:</b> In the future, new secrets will be generated periodically.
	 * 
	 * @param secretIdParm
	 *            required
	 * @return null if the secret ID is unknown
	 */
	public String retrieveSecretById(final String secretIdParm) {
		final String secret = secretsById.get(secretIdParm);

		return secret;
	}

	/**
	 * This immutable class contains information about a JWT secret.
	 */
	public static final class SecretInfo {
		private final String secretId;
		private final String secret;

		public SecretInfo(String secretIdParm, String secretParm) {
			secretId = secretIdParm;
			secret = secretParm;
		}

		public String getSecretId() {
			return secretId;
		}

		public String getSecret() {
			return secret;
		}
	}
}