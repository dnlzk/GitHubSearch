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

package pl.nalazek.githubsearch.data.ResultObjects;

import pl.nalazek.githubsearch.data.ExchangeType;

/**
 * This abstract class is used as a parent for classes which are used to keep parsed data from JSON HTTP requests.
 * Instances of Result class should ve created via {@link ResultCreator}
 */

public abstract class Result {

    protected ExchangeType exchangeType;

    protected Result(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    /**
     * Gets the {@link ExchangeType} of the Resut
     * @return {@link ExchangeType}
     */
    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    /**
     * Gets the type of the Result.
     */
    public abstract String getResultType();
}
