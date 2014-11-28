package com.deyan.news.funkynews.test;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by Deyan on 15/09/2014.
 */
public class FullTestSuite extends TestSuite {

    public FullTestSuite() {
        super();
    }

    public static Test suit() {

        return new TestSuiteBuilder(FullTestSuite.class).includeAllPackagesUnderHere().build();
    }
}
