/*
 * Copyright 2015-2019 the original author or authors.
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

import com.wuxp.codegen.server.vcs.SourceCodeRepositoryAccessProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.springframework.util.ResourceUtils.FILE_URL_PREFIX;

/**
 * copy to spring-cloud-config
 * <p>
 * Base class for components that want to access a source control management system.
 *
 * @author Dave Syer
 */
@Slf4j
public abstract class AbstractScmAccessor implements ResourceLoaderAware {

    /**
     * 本地仓库地址模板url
     */
    private static final String LOCAL_REPOSITORY_TEMPLATE_URL = "{basedir}/{projectName}/{branch}";

    /**
     * Base directory for local working copy of repository.
     */
    private String basedir;

    /**
     * URI of remote repository.
     */
    private String uri;
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
    private boolean strictHostKeyChecking;

    /**
     * Search paths to use within local working copy. By default searches only the root.
     */
    private String[] searchPaths;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();


    protected AbstractScmAccessor(SourceCodeRepositoryAccessProperties properties) {
        this.setBasedir(properties.getBasedir() == null ? createBaseDir()
                : properties.getBasedir());
        this.passphrase = properties.getPassphrase();
        this.password = properties.getPassword();
        this.strictHostKeyChecking = properties.isStrictHostKeyChecking();
        this.uri = properties.getUri();
        this.username = properties.getUsername();
    }

    @Override
    public void setResourceLoader(@NotNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    protected String createBaseDir() {
        try {
            Path tempDirectory = Files.createTempDirectory("source_code");
            addDeleteHook(tempDirectory);
            return tempDirectory.toString();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create temp dir", e);
        }
    }

    private void addDeleteHook(Path tempDirectory) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileSystemUtils.deleteRecursively(tempDirectory);
            } catch (IOException e) {
                log.warn("Failed to delete temporary directory on exit: ", e);
            }
        }));
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        while (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        int index = uri.indexOf("://");
        if (index > 0 && !uri.substring(index + "://".length()).contains("/")) {
            // If there's no context path add one
            uri = uri + "/";
        }
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

    protected File getWorkingDirectory(String projectName, String branch) {
        Assert.notNull(uri, "source repository uri must not null");
        if (uri.startsWith(FILE_URL_PREFIX)) {
            try {
                String path = StringUtils.cleanPath(uri);
                return new UrlResource(this.getLocalRepositoryUrl(path, projectName, branch)).getFile();
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Cannot convert uri to file: " + uri);
            }
        }
        return new File(this.getLocalRepositoryUrl(this.basedir, projectName, branch));
    }

    private List<String> matchingDirectories(File dir, String value) {
        List<String> result = new ArrayList<>();
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(
                    this.resourceLoader);
            String path = new File(dir, value).toURI().toString();
            for (Resource resource : resolver.getResources(path)) {
                if (resource.getFile().isDirectory()) {
                    result.add(resource.getURI().toString());
                }
            }
        } catch (IOException e) {
            if (log.isInfoEnabled()) {
                log.debug("path match failure", e);
            }
        }
        return result;
    }

    private String getLocalRepositoryUrl(String basedir, String projectName, String branch) {
        return LOCAL_REPOSITORY_TEMPLATE_URL
                .replace("{basedir}", basedir)
                .replace("{projectName}", projectName)
                .replace("{branch}", branch);
    }

}
