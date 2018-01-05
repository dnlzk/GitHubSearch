/*
 * Copyright 2015, The Android Open Source Project
 * Copyright 2017, Daniel Nalazek
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

package pl.nalazek.githubsearch.data;

import android.support.annotation.NonNull;

/**
 * Singleton class to provide GitHubRepository instance depending on the passed object implementing GitHubRepositoryInterface
 */
public class GitHubRepositories {

    private GitHubRepositories() {
    }

    private static GitHubRepository repository = null;

    public static synchronized GitHubRepository getInstance(@NonNull GitHubRepositoryInterface gitHubRepositoryInterface) {
        if(repository == null) {
            repository = new GitHubRepositoryInstance(gitHubRepositoryInterface);
        }
        return repository;
    }

}
