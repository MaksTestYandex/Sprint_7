package com.github.mablinov.sprint7.courier;

public class RequestCourierLoginBody {
    private String login;
    private String password;

    public RequestCourierLoginBody(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public RequestCourierLoginBody() {
    }

    public static RequestCourierLoginBody from(RequestCourierBody courierBody) {
        return new RequestCourierLoginBody(courierBody.getLogin(), courierBody.getPassword());
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}





