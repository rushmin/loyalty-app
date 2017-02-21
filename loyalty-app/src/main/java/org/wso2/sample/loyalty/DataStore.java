package org.wso2.sample.loyalty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rushmin on 2/21/17.
 */
public class DataStore {

    private static DataStore instance = null;

    private Map<String, Integer> points;

    private DataStore(){
        points = new HashMap<String, Integer>();
    }

    public static DataStore getInstance(){

        if (instance == null) {
            synchronized (DataStore.class) {
                if (instance == null) {
                    instance = new DataStore();
                }
            }
        }
        return instance;
    }

    public int getPoints(String username) {

        Integer points = this.points.get(username);

        if(points == null){
            points = 0;
        }

        return points;
    }

    public void updatePoints(String username, int points) {
        this.points.put(username, points);
    }
}
