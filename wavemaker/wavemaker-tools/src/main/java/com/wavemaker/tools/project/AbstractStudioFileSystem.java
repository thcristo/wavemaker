/*
 *  Copyright (C) 2012-2013 CloudJee, Inc. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wavemaker.tools.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.util.WebUtils;

import com.wavemaker.common.CommonConstants;
import com.wavemaker.common.WMRuntimeException;
import com.wavemaker.common.util.FileAccessException;
import com.wavemaker.common.util.IOUtils;
import com.wavemaker.runtime.RuntimeAccess;
import com.wavemaker.tools.io.local.LocalFolder;

/**
 * Abstract base implementation of {@link StudioFileSystem}.
 * 
 * @see GridFSStudioFileSystem
 * @see LocalStudioFileSystem
 * 
 * @author Ed Callahan
 * @author Jeremy Grelle
 * @author Joel Hare
 * @author Matt Small
 * @author Phillip Webb
 */
public abstract class AbstractStudioFileSystem implements StudioFileSystem, ServletContextAware {

    private static final String PROJECTHOME_KEY = "projectsDir";

    public static final String PROJECTHOME_PROP_KEY = CommonConstants.WM_SYSTEM_PROPERTY_PREFIX + PROJECTHOME_KEY;

    public static final String PROJECTS_DIR = "projects/";

    public static final String COMMON_DIR = "common/";

    public static final String TEMPLATES_DIR = "templates/";

    public static final String DEMOHOME_KEY = "demoHome";

    private ServletContext servletContext;

    private LocalFolder studioWebAppRootFolder;

    @Override
    public LocalFolder getStudioWebAppRootFolder() {
        if (this.studioWebAppRootFolder == null) {
            try {
                File servletPath = new File(WebUtils.getRealPath(this.servletContext, "/"));
                this.studioWebAppRootFolder = new LocalFolder(servletPath);
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
        return this.studioWebAppRootFolder;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public Resource getStudioWebAppRoot() {
        if (this.servletContext == null) {
            this.servletContext = RuntimeAccess.getInstance().getRequest().getSession().getServletContext();
        }
        return new ServletContextResource(this.servletContext, "/");
    }

    protected abstract Resource createResource(String path);

    @Override
    public Resource getProjectsDir() {
        String projectsProp = null;
        if (isRuntime()) {
            projectsProp = (String) RuntimeAccess.getInstance().getSession().getAttribute(PROJECTHOME_PROP_KEY);
        } else {
            projectsProp = System.getProperty(PROJECTHOME_PROP_KEY, null);
        }
        if (projectsProp != null && 0 != projectsProp.length()) {
            projectsProp = projectsProp.endsWith("/") ? projectsProp : projectsProp + "/";
            return createResource(projectsProp);
        }
        try {
            Resource projectsDir = getWaveMakerHome().createRelative(PROJECTS_DIR);
            if (!projectsDir.exists()) {
                makeDirectories(projectsDir);
            }
            return projectsDir;
        } catch (IOException ex) {
            throw new WMRuntimeException(ex);
        }
    }

    private boolean isRuntime() {
        try {
            if (RuntimeAccess.getInstance() != null && RuntimeAccess.getInstance().getRequest() != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    protected abstract void makeDirectories(Resource dir) throws FileAccessException, IOException;

    @Override
    public Resource getCommonDir() throws IOException {
        Resource common = getWaveMakerHome().createRelative(COMMON_DIR);
        if (!common.exists() && getWaveMakerHome().exists()) {
            createCommonDir(common);
        }
        return common;
    }

    private synchronized void createCommonDir(Resource common) throws IOException {
        if (!common.exists()) {
            Resource templateFile = getStudioWebAppRoot().createRelative("lib/wm/" + COMMON_DIR);
            if (templateFile.exists()) {
                copyRecursive(templateFile, common, IOUtils.DEFAULT_EXCLUSION);
            }
        }
    }

    
    @Override
    public Resource getTemplatesDir() throws IOException {
        Resource templates = getWaveMakerHome().createRelative(TEMPLATES_DIR);
        if (!templates.exists() && getWaveMakerHome().exists()) {
            createTemplatesDir(templates);
        }
        return templates;
    }

    private synchronized void createTemplatesDir(Resource templates) throws IOException {
        if (!templates.exists()) {
        	this.makeDirectories(templates);
        }
    }
    
    
    @Override
    public Resource getResourceForURI(String uri) {
        return createResource(uri);
    }

    @Override
    public List<Resource> listChildren(Resource resource) {
        return listChildren(resource, ResourceFilter.NO_FILTER);
    }

    protected abstract String getFSType();

    @Override
    public String getStudioEnv() {
        return getFSType();
    }
}
