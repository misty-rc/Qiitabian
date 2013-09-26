package org.misty.rc.Qiitabian.models;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/24
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */
public class Auth extends BaseModel {
    public static final String TOKEN = "token";
    public static final String URL_NAME = "url_name";
    public static final String PASSWORD = "password";

    public String url_name;
    public String token;
}
