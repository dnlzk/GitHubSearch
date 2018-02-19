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
 **/

package pl.nalazek.githubsearch.data.ResultObjects;

import android.os.Parcelable;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This data class represents a parent for parsed search results.
 */

public abstract class SearchResult extends Result implements Parcelable {

    public final static String TYPE = "EmptySearchResult";

    private String title;
    private String description;
    private int id;

    protected SearchResult(String title, String description, ExchangeType exchangeType, int id ) {
        super(exchangeType);
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() { return id; }
}
