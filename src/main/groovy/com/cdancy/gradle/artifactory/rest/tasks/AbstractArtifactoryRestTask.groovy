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

import com.cdancy.gradle.artifactory.rest.utils.ThreadContextClassLoader
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

abstract class AbstractArtifactoryRestTask extends DefaultTask {

    @Input
    @Optional
    Closure<String> repo

    @Input
    @Optional
    Closure<String> artifactPath

    public String repo() {
        String possibleRepo = repo ? repo.call() : null
        if (possibleRepo?.trim()) {
            possibleRepo
        } else {
            throw new GradleException("repo does not resolve to a valid String: repo=" + possibleRepo)
        }
    }

    public String artifactPath() {
        String possibleArtifactPath = artifactPath ? artifactPath.call() : null
        if (possibleArtifactPath?.trim()) {
            possibleArtifactPath
        } else {
            throw new GradleException("artifactPath does not resolve to a valid String: artifactPath=" + possibleArtifactPath)
        }
    }

    ThreadContextClassLoader threadContextClassLoader

    @TaskAction
    void start() {
        runInArtifactoryClassPath { artifactoryClient ->
            runRemoteCommand(artifactoryClient)
        }
    }

    void runInArtifactoryClassPath(Closure closure) {
      threadContextClassLoader.withClosure(closure)
    }

    abstract void runRemoteCommand(artifactoryClient)
}
