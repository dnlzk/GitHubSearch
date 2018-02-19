/**
 *  Copyright 2018 Daniel Nalazek

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package pl.nalazek.githubsearch.data;

/**
 * This enum hold types of exchanges between host and client
 * <li>{@link #USER_SEARCH} used when searching for users</li>
 * <li>{@link #USER_PAGE} used when requesting a page from a user search</li>
 * <li>{@link #USER_DETAILED} used when requesting for detailed info about user</li>
 * <li>{@link #USER_DETAILED_STARS} used when requesting user starred urls</li>
 * <li>{@link #USER_DETAILED_AVATAR} used when requesting user avatar</li>
 * <li>{@link #REPOS_SEARCH} used when searching for repositories</li>
 * <li>{@link #REPOS_PAGE} used when requesting a page from a repository search</li>
 **/
public enum ExchangeType { USER_SEARCH, REPOS_SEARCH, USER_PAGE,
    REPOS_PAGE, USER_DETAILED, USER_DETAILED_STARS, USER_DETAILED_AVATAR
}
