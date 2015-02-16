package com.icehousecorp.jsonapi.sample.model;

/**
 * Created by imrenagi on 2/16/15.
 */
public class Assets {

    private String poster;
    private String thumbnail;
    private Trailer trailer;

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Trailer getTrailer() {
        return trailer;
    }

    public void setTrailer(Trailer trailer) {
        this.trailer = trailer;
    }
}
