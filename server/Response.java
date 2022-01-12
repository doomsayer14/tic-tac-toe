//class for output to client
package server;

class Response {
    private String response;
    private String reason;
    private String value;

    //Constructors
    //-------------------------------
    public Response() {
    }

    public Response(String response) {
        this.response = response;
    }

    public Response(String response, String reason) {
        this.response = response;
        this.reason = reason;
    }
    //-------------------------------

    //getters, setters
    //-------------------------------
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //-------------------------------
}
