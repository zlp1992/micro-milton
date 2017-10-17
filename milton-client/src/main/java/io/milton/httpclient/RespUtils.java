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

package io.milton.httpclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author mcevoyb
 */
public class RespUtils {

    private static final Logger log = LoggerFactory.getLogger( RespUtils.class );
    
    public static Namespace NS_DAV = Namespace.getNamespace("D", "DAV:");
    
    public static QName davName(String localName) {
        return new QName(NS_DAV.getURI(), localName, NS_DAV.getPrefix());
    }    
    
    
    public static String asString( Element el, String name ) {
        Element elChild = el.getChild( name, NS_DAV  );
        if( elChild == null ) {
            //log.debug("No child: " + name + " of " + el.getName());            
            return null;
        }
        return elChild.getText();
    }

    public static String asString( Element el, String name, Namespace ns ) {
//        System.out.println("asString: " + qname + " in: " + el.getName());
//        for( Object o : el.elements() ) {
//            Element e = (Element) o;
//            System.out.println(" - " + e.getQualifiedName());
//        }
        Element elChild = el.getChild( name, ns );
        if( elChild == null ) return null;
        return elChild.getText();
    }    
    
    public static Long asLong( Element el, String name ) {
        String s = asString( el, name );
        if( s == null || s.length()==0 ) return null;
        long l = Long.parseLong( s );
        return l;
    }
    
    public static Long asLong( Element el, String name, Namespace ns ) {
        String s = asString( el, name, ns );
        if( s == null || s.length()==0 ) return null;
        long l = Long.parseLong( s );
        return l;
    }    

    public static boolean hasChild( Element el, String name ) {
        if( el == null ) return false;
        List<Element> list = getElements(el, name);
        
        return !list.isEmpty();
    }    
    

    public static  List<Element> getElements(Element root, String name) {
        List<Element> list = new ArrayList<Element>();
        Iterator it = root.getDescendants(new ElementFilter(name));
        while(it.hasNext()) {
            Object o = it.next();
            if( o instanceof Element) {
                list.add((Element)o);
            }
        }
        return list;
    }    
          
}
