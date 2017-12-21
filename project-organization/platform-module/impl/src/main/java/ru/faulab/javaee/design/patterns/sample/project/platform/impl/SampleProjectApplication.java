package ru.faulab.javaee.design.patterns.sample.project.platform.impl;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath(SampleProjectApplication.GLOBAL_REST_ADI_PREFIX)
public class SampleProjectApplication extends Application {
    public static final String GLOBAL_REST_ADI_PREFIX = "rest";
}
