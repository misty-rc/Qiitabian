package org.misty.rc.Qiitabian.models;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/24
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public class User extends BaseModel {
    public String name;
    public String url_name;
    public String profile_image_url;
    public String url;
    public String description;
    public String website_url;
    public String organization;
    public String location;
    public String facebook;
    public String linkedin;
    public String twitter;
    public String github;
    public int followers;
    public int followering_users;
    public int items;
    public Team[] teams;

    private class Team {
        public String name;
        public String url_name;
    }

}
