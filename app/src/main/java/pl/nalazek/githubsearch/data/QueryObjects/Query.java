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

import android.support.annotation.Nullable;

import java.net.URL;
import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This abstract class should be inherited by new query types.
 */
public abstract class Query {

    protected ExchangeType type;
    protected URL url;

    public abstract String getQueryType();

    public ExchangeType getExchangeType() { return type; }

    @Nullable
    public URL getURL() { return url; }
}
