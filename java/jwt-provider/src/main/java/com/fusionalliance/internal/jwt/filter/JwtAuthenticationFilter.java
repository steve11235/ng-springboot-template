package com.fusionalliance.internal.jwt.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.fusionalliance.internal.jwt.business.JwtImpl;
import com.fusionalliance.internal.jwt.shared.SecretContainer;
import com.fusionalliance.internal.sharedutility.jwt.JwtException;
import com.fusionalliance.internal.sharedutility.jwt.JwtUtility;

/**
 * This class implements a security filter for all RESTful calls using JWT. If a valid token is found, then <code>login</code> and <code>admin</code>
 * attributes are added to the request. Otherwise, a 403 Forbidden response is sent, along a with a brief message describing the reason.
 */
@WebFilter(urlPatterns = { "/rs/*" })
public class JwtAuthenticationFilter implements Filter {
	public static final boolean DO_AUTHENTICATION = false;

	private final Pattern AUTHORIZATION_PATTERN = Pattern.compile("Bearer ([a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9_\\-]+\\.[a-zA-Z0-9_\\-]+)");
	private final SecretContainer secretContainer;

	@Autowired
	public JwtAuthenticationFilter(final SecretContainer secretContainerParm) {
		secretContainer = secretContainerParm;
	}

	@Override
	public void init(FilterConfig filterConfigParm) throws ServletException {
		// Do nothing
	}

	@Override
	public void doFilter(final ServletRequest requestParm, final ServletResponse responseParm, final FilterChain filterChainParm)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) requestParm;
		final HttpServletResponse response = (HttpServletResponse) responseParm;

		if (!DO_AUTHENTICATION) {
			request.setAttribute("login", "no auth");
			request.setAttribute("userName", "Default User");
			request.setAttribute("admin", Boolean.TRUE);

			filterChainParm.doFilter(request, response);

			return;
		}

		final String authorization = request.getHeader("Authorization");
		if (authorization == null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "This service requires authorization.");

			return;
		}

		final Matcher authorizationMatcher = AUTHORIZATION_PATTERN.matcher(authorization);
		if (!authorizationMatcher.matches()) {
		}

		final String jwt = authorizationMatcher.group(1);

		final JwtImpl jwtImpl;
		try {
			jwtImpl = new JwtImpl(jwt);
		}
		catch (JwtException e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "The JWT is invalid: " + e.getMessage());

			return;
		}

		final String secret = secretContainer.retrieveSecretById(jwtImpl.getSecretId());
		if (secret == null) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "The JWT sid (secret ID) is not known.");

			return;
		}

		if (!JwtUtility.verifySignature(jwtImpl.getHeader(), jwtImpl.getPayload(), jwtImpl.getSignature(), secret)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "The JWT signature is invalid.");

			return;
		}

		request.setAttribute("login", jwtImpl.getLogin());
		request.setAttribute("userName", jwtImpl.getName());
		request.setAttribute("admin", jwtImpl.isAdmin());

		filterChainParm.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// Do nothing
	}
}
