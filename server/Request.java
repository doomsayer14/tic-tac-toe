//class for client's input
package server;

public class Request {

    //command, that user typed in
    private String type;

    //key of dataBase
    private String key;

    //value to save in the database
    private String value;

    //Constructors
    //-------------------------------
    private Request() {

    }

    public Request(String type) {
        this.type = type;
    }

    public Request(String type, String key) {
        this.type = type;
        this.key = key;
    }

    public Request(String type, String key, String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }
    //-------------------------------

    //getters and setters
    //-------------------------------
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //-------------------------------
}
