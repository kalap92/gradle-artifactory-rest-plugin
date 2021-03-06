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
package com.cdancy.gradle.artifactory.rest.tasks.storage

import com.cdancy.gradle.artifactory.rest.tasks.ArtifactAware
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

class SetProperties extends ArtifactAware {

    @Input
    Map<String, List<String>> properties = [:]

    @Input
    @Optional
    long requestInterval = 1000

    @Input
    @Optional
    Map<String, List<String>> artifacts = new HashMap<>();

    @Override
    void runRemoteCommand(artifactoryClient) {
        if (properties) {
            if (repo != null && artifactPath != null) {
                onArtifact(repo(), artifactPath())
            }

            if (artifacts.size() > 0) {
                def api = artifactoryClient.api()
                artifacts.each { k, v ->
                    v.each { it ->
                        boolean success = api.storageApi().setItemProperties(k.toString(), it.toString(), gstringMapToStringMap(properties))
                        if (success) {
                            logger.quiet("Properties ${properties} set @ ${k}:${it}")
                        } else {
                            throw new GradleException("Could not successfully set properties '${properties}' @ " +
                                    "${k}:${it}")
                        }
                        sleep(requestInterval)
                    }
                }
            } else {
                logger.quiet "`artifacts` are empty. Nothing to do..."
            }
        } else {
            logger.quiet "`properties` are empty. Nothing to do..."
        }
    }

    @Deprecated
    void onArtifact(String repo, String artifactPath) {
        artifact(repo, artifactPath)
    }

    void artifact(String repo, String artifactPath) {
        def localRepo = checkString(repo);
        def localArtifactPath = checkString(artifactPath)
        List<String> possibleArtifacts = artifacts.get(localRepo);
        if (possibleArtifacts == null) {
            possibleArtifacts = new ArrayList<>()
            artifacts.put(localRepo, possibleArtifacts)
        }

        if (!possibleArtifacts.contains(localArtifactPath))
            possibleArtifacts.add(localArtifactPath)

        if (possibleArtifacts.isEmpty())
            artifacts.remove(localRepo)
    }
}

