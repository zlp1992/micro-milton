/*
 *
 * Copyright 2014 McEvoy Software Ltd.
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
package io.milton.http.annotated;

import io.milton.annotations.DisplayName;

/**
 *
 * @author brad
 */
public class DisplayNameAnnotationHandler extends AbstractAnnotationHandler {

	public DisplayNameAnnotationHandler(final AnnotationResourceFactory outer) {
		super(outer, DisplayName.class);
	}

	public String executeRead(AnnoResource res) {
		Object source = res.getSource();
		try {
			ControllerMethod cm = getBestMethod(source.getClass());
			if (cm == null) {
				Object o = attemptToReadProperty(source, "displayName", "title");
				if( o != null ) {
					return o.toString();
				}
				return res.getName();
			}

			return (String) cm.method.invoke(cm.controller, source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
