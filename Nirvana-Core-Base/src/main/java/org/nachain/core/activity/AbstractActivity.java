package org.nachain.core.activity;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class AbstractActivity implements IActivity {

    private String activityName;


    public AbstractActivity(String activityName) {
        if (!activityName.isEmpty()) {
            register(activityName);
        }
    }


    public void register(String activityName) {
        this.activityName = activityName;
        if (activityName == null || activityName.equals("")) {
            throw new RuntimeException("ActivityName cannot be empty");
        } else {

            ActivityHolder.register(activityName, this);
        }
    }


    public String getActivityName() {
        return this.activityName;
    }

}
