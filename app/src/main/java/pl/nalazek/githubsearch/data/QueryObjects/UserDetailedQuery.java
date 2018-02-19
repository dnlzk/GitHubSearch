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

package pl.nalazek.githubsearch.data.QueryObjects;

import java.net.URL;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * Use this query in order to get details about user
 */

public class UserDetailedQuery extends Query {


    public static final String TYPE = "Detailed";


    public UserDetailedQuery(URL url, ExchangeType exchangeType) {
        this.url = url;
        this.type = exchangeType;
    }


    @Override
    public String getQueryType() {
        return TYPE;
    }
}
