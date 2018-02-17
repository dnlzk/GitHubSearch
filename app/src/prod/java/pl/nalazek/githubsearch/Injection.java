/*
 * Copyright (C) 2015 The Android Open Source Project
 * Copyright (C) 2018 Daniel Nalazek
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

package pl.nalazek.githubsearch;

import okhttp3.OkHttpClient;
import pl.nalazek.githubsearch.data.GitHubRepositories;
import pl.nalazek.githubsearch.data.GitHubRepository;
import pl.nalazek.githubsearch.data.GitHubRepositoryAPI;

/**
 * Enables injection of production implementations for {@link GitHubRepository} at compile time.
 */
public class Injection {

    public static GitHubRepository provideGitHubRepository() {
        return GitHubRepositories.getInstance(new GitHubRepositoryAPI(provideOkHttpClient()));
    }

    public static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient();
    }
}
