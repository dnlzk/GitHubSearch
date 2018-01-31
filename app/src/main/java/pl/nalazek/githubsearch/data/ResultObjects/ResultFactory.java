package pl.nalazek.githubsearch.data.ResultObjects;

import java.io.InvalidObjectException;
import java.util.List;

import pl.nalazek.githubsearch.data.ResponsePackage;
import pl.nalazek.githubsearch.data.ResponsePartitioned;

/**
 * @author Daniel Nalazek
 */

interface ResultFactory {

    Result[] makeResults(ResponsePartitioned responsePartitioned) throws InvalidJsonObjectException;

    List<? extends Result> makeResults(ResponsePackage responsePackage) throws InvalidJsonObjectException, InvalidObjectException;
}
