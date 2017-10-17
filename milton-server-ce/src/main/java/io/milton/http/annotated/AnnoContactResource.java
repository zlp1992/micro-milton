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

import io.milton.resource.AddressResource;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author brad
 */
public class AnnoContactResource extends AnnoFileResource implements AddressResource{

	private static final Logger log = LoggerFactory.getLogger(AnnoContactResource.class);
	
	public AnnoContactResource(AnnotationResourceFactory outer, Object source, AnnoCollectionResource parent) {
		super(outer, source, parent);
	}

	@Override
	public boolean is(String type) {
		if( type.equals("contact")) {
			return true;
		}
		return super.is(type);
	}

	@Override
	public String getUniqueId() {
		String s = super.getUniqueId();
		if( s == null ) {
			log.warn("No unique ID for event class: " + source.getClass() + " Locking and other vital operations will not be available!!");
		}
		return s;
	}

	@Override
	public Date getModifiedDate() {
		Date dt = super.getModifiedDate();
		if( dt == null ) {
			log.warn("No ModifiedDate for event class: " + source.getClass() + " This will cause incorrect contact syncronisation!!!");
		}
		return dt;
	}

	@Override
	public String getContentType(String accepts) {
		return "text/vcard";
	}

	@Override
	public String getAddressData() {
		return annoFactory.contactDataAnnotationHandler.execute(this);
	}

	

	
}
