package com.boostcamp.mytwitter.mytwitter.timeline.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DaHoon on 2017-02-10.
 */

public class ProfileBannerDTO {

    @SerializedName("name") String name;
    @SerializedName("profile_image_url") String profile_image_url;
}
