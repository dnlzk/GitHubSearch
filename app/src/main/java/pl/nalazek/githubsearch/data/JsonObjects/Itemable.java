package pl.nalazek.githubsearch.data.JsonObjects;

import java.util.List;

/**
 * This interface is implemented by JSON object which has a list of items.
 * @author Daniel Nalazek
 */

public interface Itemable {

    List<? extends JsonItem> getItems();
}
