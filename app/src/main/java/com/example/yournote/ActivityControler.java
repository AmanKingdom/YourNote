package com.example.yournote;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityControler{
    private static List<Activity> activities = new ArrayList<>();

    static void addActivity(Activity activity){
        activities.add(activity);
    }

    static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    // 调用这个方法可以彻底退出整个程序
    public static void finishAll(){
        for(Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
