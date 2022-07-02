package com.foreflight.apphelper.integrationtests;

import com.foreflight.apphelper.domain.MenuChoice;
import com.foreflight.apphelper.domain.Metric;
import com.foreflight.apphelper.domain.Resource;
import com.foreflight.apphelper.domain.Source;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;

public class MockDatabaseEntries {
    public Source source1;
    public Resource resource1;
    public Resource resource2;
    public MenuChoice menuChoice1;
    public MenuChoice menuChoice2;
    public MenuChoice menuChoice3;
    public MenuChoice menuChoice4;
    public MenuChoice menuChoice5;
    public MenuChoice menuChoice6;
    public Metric metric1;


    public void initEntries() {
        source1 = new Source("source1", "sourcelink1");


        resource1 = new Resource("resource1", "resourcelink1", source1);

        resource2 = new Resource("resource2", "resourcelink2", source1);


        // no parent
        // no resources
        menuChoice1 = new MenuChoice("menuChoice1", null, Collections.emptyList());

        // parent: menuChoice1
        // no resources
        menuChoice2 = new MenuChoice("menuChoice2", menuChoice1, Collections.emptyList());

        // no parent
        // resources: resource1
        menuChoice3 = new MenuChoice("menuChoice3", null, Collections.singletonList(resource1));

        // parent: menuChoice2
        // resources: resource1
        menuChoice4 = new MenuChoice("menuChoice4", menuChoice2, Collections.singletonList(resource1));

        // parent: menuChoice1
        // resources: resource1
        menuChoice5 = new MenuChoice("menuChoice5", menuChoice1, Collections.singletonList(resource1));

        // no parent
        // resources: resource1, resource2
        menuChoice6 = new MenuChoice("menuChoice6", null, Arrays.asList(resource1, resource2));


        metric1 = new Metric(false, OffsetDateTime.parse("2022-07-01T15:20:30+05:00"),
                "metriclink1", "user", "user_feedback",
                Collections.singletonList(menuChoice1), Collections.singletonList(resource1));
    }
}
