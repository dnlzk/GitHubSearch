package pl.nalazek.githubsearch;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Entry point to all instrumented tests
 * @author Daniel Nalazek
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({EspressoTestSuite.class, ParcelTestSuite.class})
public class InstrumentedTestSuite {
}
