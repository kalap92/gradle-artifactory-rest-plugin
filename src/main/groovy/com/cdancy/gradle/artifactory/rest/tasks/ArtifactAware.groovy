/*
 * Copyright 2014 the original author or authors.
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
package com.cdancy.gradle.artifactory.rest.tasks

import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

abstract class ArtifactAware extends AbstractArtifactoryRestTask {

    @Input
    @Optional
    Closure<String> repo

    @Input
    @Optional
    Closure<String> artifactPath

    public String repo() {
        String var = repo ? repo.call() : null
        if (var?.trim()) {
            var
        } else {
            throw new GradleException("repo does not resolve to a valid String: repo=" + var)
        }
    }

    public String artifactPath() {
        String var = artifactPath ? artifactPath.call() : null
        if (var?.trim()) {
            var
        } else {
            throw new GradleException("artifactPath does not resolve to a valid String: artifactPath=" + var)
        }
    }
}

