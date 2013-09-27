package org.misty.rc.Qiitabian.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: arai
 * Date: 13/09/26
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
public class Item extends BaseModel {

    public int id;
//    public String uuid;
    public User user;
    public String title;
//    public String body;
//    public String created_at;
//    public String updated_at;
    public String created_at_in_words;
//    public String updated_at_in_words;
    public Tag[] tags;
//    public int stock_count;
//    public String[] stock_users;
//    public int comment_count;
    public String url;
//    public String gist_url;
    public boolean tweet;
//    @SerializedName("private") public boolean private_flag;
//    public boolean stocked;
}

