/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.milton.servlet;

import io.milton.http.ResourceFactory;
import io.milton.resource.Resource;
import java.io.File;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Provides access to servlet resources (ie files defined within the folder
 * which contains WEB-INF) in a milton friendly resource factory
 *
 * @author bradm
 */
public class ServletResourceFactory implements ResourceFactory {

	private final ServletContext servletContext;

	@Autowired
	public ServletResourceFactory(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public Resource getResource(String host, String path) {
		String contextPath = MiltonServlet.request().getContextPath();
		String localPath = path.substring(contextPath.length());
		String realPath = servletContext.getRealPath(localPath);
		if (realPath != null) {
			File file = new File(realPath);
			if (file.exists() && !file.isDirectory()) {
				return new ServletResource(file, localPath, MiltonServlet.request(), MiltonServlet.response());
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
