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
package com.mycompany;

import io.milton.http.Auth;
import io.milton.http.Request;
import io.milton.http.Request.Method;
import io.milton.http.http11.auth.DigestGenerator;
import io.milton.http.http11.auth.DigestResponse;
import io.milton.resource.DigestResource;
import io.milton.resource.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * BM: added reportable so that all these resource classes work with REPORT
 *
 * @author alex
 */
public class AbstractResource implements Resource, DigestResource {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AbstractResource.class);
    protected UUID id;
    protected String name;
    protected Date modDate;
    protected Date createdDate;
    protected TFolderResource parent;

    public AbstractResource(TFolderResource parent, String name) {
        id = UUID.randomUUID();
        this.parent = parent;
        this.name = name;
        modDate = new Date();
        createdDate = new Date();
        if (parent != null) {
            checkAndRemove(parent, name);
            parent.children.add(this);
        }
    }


    @Override
    public Object authenticate(String user, String requestedPassword) {
        String p = TResourceFactory.credentialsMap.get(user);
        if (p != null) {
            if (p.equals(requestedPassword)) {
                return Boolean.TRUE;
            } else {
                log.warn("that password is incorrect. Try:" + p);
            }
        } else {
            log.warn("user not found: " + user + " - try 'userA'");
        }
        return null;

    }

    @Override
    public Object authenticate(DigestResponse digestRequest) {
        String p = TResourceFactory.credentialsMap.get(digestRequest.getUser());
        if (p != null) {
            DigestGenerator gen = new DigestGenerator();
            String actual = gen.generateDigest(digestRequest, p);
            if (actual.equals(digestRequest.getResponseDigest())) {
                return p;
            } else {
                log.warn("that password is incorrect. Try 'password'");
            }
        } else {
            log.warn("user not found: " + digestRequest.getUser() + " - try 'userA'");
        }
        return null;

    }

    @Override
    public String getUniqueId() {
        return this.id.toString();
    }

    @Override
    public String checkRedirect(Request request) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean authorise(Request request, Method method, Auth auth) {
        log.debug("authorise");
        return auth != null;
    }

    @Override
    public String getRealm() {
        return "testrealm";
    }

    @Override
    public Date getModifiedDate() {
        return modDate;
    }

    private void checkAndRemove(TFolderResource parent, String name) {
        TResource r = (TResource) parent.child(name);
        if (r != null) {
            parent.children.remove(r);
        }
    }

    @Override
    public boolean isDigestAllowed() {
        return true;
    }
}
