/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.session.web.http;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A {@link HttpSessionIdResolver} that uses a header to obtain the session from.
 * Specifically, this implementation will allow specifying a header name using
 * {@link HeaderHttpSessionIdResolver#setHeaderName(String)}. The default is
 * "X-Auth-Token".
 *
 * When a session is created, the HTTP response will have a response header of the
 * specified name and the value of the session id. For example:
 *
 * <pre>
 * HTTP/1.1 200 OK
 * X-Auth-Token: f81d4fae-7dec-11d0-a765-00a0c91e6bf6
 * </pre>
 *
 * The client should now include the session in each request by specifying the same header
 * in their request. For example:
 *
 * <pre>
 * GET /messages/ HTTP/1.1
 * Host: example.com
 * X-Auth-Token: f81d4fae-7dec-11d0-a765-00a0c91e6bf6
 * </pre>
 *
 * When the session is invalidated, the server will send an HTTP response that has the
 * header name and a blank value. For example:
 *
 * <pre>
 * HTTP/1.1 200 OK
 * X-Auth-Token:
 * </pre>
 *
 * @author Rob Winch
 * @author Vedran Pavic
 * @since 1.0
 */
public class HeaderHttpSessionIdResolver implements HttpSessionIdResolver {

	private String headerName = "X-Auth-Token";

	@Override
	public List<String> resolveSessionIds(HttpServletRequest request) {
		String headerValue = request.getHeader(this.headerName);
		return headerValue != null ? Collections.singletonList(headerValue)
				: Collections.emptyList();
	}

	@Override
	public void setSessionId(HttpServletRequest request, HttpServletResponse response,
			String sessionId) {
		response.setHeader(this.headerName, sessionId);
	}

	@Override
	public void expireSession(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader(this.headerName, "");
	}

	/**
	 * The name of the header to obtain the session id from. Default is "X-Auth-Token".
	 *
	 * @param headerName the name of the header to obtain the session id from.
	 */
	public void setHeaderName(String headerName) {
		if (headerName == null) {
			throw new IllegalArgumentException("headerName cannot be null");
		}
		this.headerName = headerName;
	}

}
