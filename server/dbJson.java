//class to fill db.json

package server;

import java.util.HashMap;
import java.util.Map;

class dbJson {
    private String key;
    private String value;

    //simulated database. Copy of db.json
    private static Map<String, String> dataBase;

    public dbJson(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //singleton for dataBase
    public static Map<String, String> getInstance() {
        if (dataBase == null) {
            return dataBase = new HashMap<>();
        }
        return dataBase;
    }
}
