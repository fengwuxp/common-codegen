/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wuxp.codegen.server.vcs.support;

import org.springframework.core.Ordered;

/**
 * copy to spring-cloud-config
 *
 * @author Dylan Roberts
 */
public abstract class AbstractScmAccessorProperties implements Ordered {

    static final String[] DEFAULT_LOCATIONS = new String[]{"/"};

    /**
     * URI of remote repository.
     */
    private String uri;

    /**
     * Base directory for local working copy of repository.
     */
    private String basedir;

    /**
     * Search paths to use within local working copy. By default searches only the root.
     */
    private String[] searchPaths = DEFAULT_LOCATIONS.clone();

    /**
     * Username for authentication with remote repository.
     */
    private String username;

    /**
     * Password for authentication with remote repository.
     */
    private String password;

    /**
     * Passphrase for unlocking your ssh private key.
     */
    private String passphrase;

    /**
     * Reject incoming SSH host keys from remote servers not in the known host list.
     */
    private boolean strictHostKeyChecking = true;

    /**
     * The order of the environment repository.
     */
    private int order = Ordered.LOWEST_PRECEDENCE;


    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBasedir() {
        return this.basedir;
    }

    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }

    public String[] getSearchPaths() {
        return this.searchPaths;
    }

    public void setSearchPaths(String... searchPaths) {
        this.searchPaths = searchPaths;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassphrase() {
        return this.passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public boolean isStrictHostKeyChecking() {
        return this.strictHostKeyChecking;
    }

    public void setStrictHostKeyChecking(boolean strictHostKeyChecking) {
        this.strictHostKeyChecking = strictHostKeyChecking;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


}
