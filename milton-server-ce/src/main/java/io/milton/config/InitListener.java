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
package io.milton.config;

import io.milton.http.HttpManager;

/**
 * Listener interface to hook into the initialization process for HttpManagerBuilder
 *
 * @author brad
 */
public interface InitListener {
	
	/**
	 * Called just before init on HttpManagerBuilder
	 * @param b 
	 */
	void beforeInit(HttpManagerBuilder b);
	
	/**
	 * Called just after init, and before building the HttpManager instance
	 * 
	 * @param b 
	 */
	void afterInit(HttpManagerBuilder b);
	
	/**
	 * Called immediately after building the http manager
	 * 
	 * @param b
	 * @param m 
	 */
	void afterBuild(HttpManagerBuilder b, HttpManager m);

	/**
	 * Called immediately before building the protocol stack, ie the Http11Protocol etc
	 * 
	 * Note that building the protocol stack is done before building the HttpManager
	 * 
	 * @param aThis 
	 */
	void beforeProtocolBuild(HttpManagerBuilder b);
}
