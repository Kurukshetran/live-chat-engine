/*
 * Copyright 2015 Evgeny Dolganov (evgenij.dolganov@gmail.com).
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
package och.chat.web.filter;

import static och.util.Util.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import och.api.exception.InvalidInputException;
import och.api.exception.user.AccessDeniedException;
import och.chat.service.ChatsApp;
import och.chat.service.SecurityService;
import och.chat.web.ChatsAppProvider;
import och.util.servlet.BaseFilter;


//config by web.xml
public class AutoCreateUserSessionFilter extends BaseFilter {
	
	ChatsApp app;
	SecurityService security;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		app = ChatsAppProvider.get(filterConfig.getServletContext());
		security = app.security;
	}

	@Override
	protected void doFilter(HttpServletRequest req, HttpServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		String token = req.getParameter("sessionToken");
		if(hasText(token)) {
			try {
				security.initUserSession(req, token);
			}catch (InvalidInputException | AccessDeniedException e) {
				//skip
			}
		}
		
		chain.doFilter(req, resp);

	}

	@Override
	public void destroy() {
	}

}
