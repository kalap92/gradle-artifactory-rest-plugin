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

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

class FileList extends ArtifactAware {

    @Input
    @Optional
    Boolean deep

    @Input
    @Optional
    Integer depth = null

    @Input
    @Optional
    Boolean listFolders

    @Input
    @Optional
    Boolean includeRootPath

    private def fileList

    @Override
    void runRemoteCommand(artifactoryClient) {
        def api = artifactoryClient.api()
        fileList = api.storageApi().fileList(repo(),
                artifactPath(),
                deep ? 1 : 0,
                depth,
                listFolders ? 1 : 0,
                includeRootPath ? 1 : 0)
        logger.quiet "Found ${fileList.files().size()} files"
    }

    public fileList() { fileList }
}
