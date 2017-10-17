/*
 * Copyright 2015 McEvoy Software Ltd.
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
package io.milton.resource;

import java.util.Map;

/**
 *
 * @author Lee YOU
 */
public interface OAuth2Resource extends Resource {

    /**
     * Called when an oauth2 login response has been received, with details received
     * from the remote server. The method should return an application specific
     * object representing the user if one exists OR if the application chooses to create one.
     *
     * Or return null to indicate that this resource cannot authenticate the request. In that
     * case the AuthenticationService may continue looking for other authentication providers
     * which are able to authenticate the request.
     *
     * A typical workflow is that an OAuth response will be received, the current user
     * will be authenticated from the CookieAuthenticationHandler, and the application
     * will then choose to link the oauth credentials to the current user. Subsequently
     * the user is then able to authenticat with oauth.
     *
     * @param profile - the details about the current user as provided by the
     * remote authentication server
     * @return an object which represents the current principal, or null to
     * reject the login
     */
    Object authenticate(OAuth2ProfileDetails profile);

    Map<String, OAuth2Provider> getOAuth2Providers();


    /**
     * This contains the information about the authenticated profile
     */
    public static class OAuth2ProfileDetails {

        private String tokenLocation;
        private String providerId;
        private String accessToken;
        private String code;
        private String returnUrl; // this is the local page to redirect to after authentication

        private Map details;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTokenLocation() {
            return tokenLocation;
        }

        public void setTokenLocation(String tokenLocation) {
            this.tokenLocation = tokenLocation;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Map getDetails() {
            return details;
        }

        public void setDetails(Map details) {
            this.details = details;
        }

        public String getReturnUrl() {
            return returnUrl;
        }

        public void setReturnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
        }
    }

}
