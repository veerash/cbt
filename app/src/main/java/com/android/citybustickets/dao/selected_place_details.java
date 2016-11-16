package com.android.citybustickets.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table SELECTED_PLACE_DETAILS.
 */
public class selected_place_details {

    private Long id;
    private String place_id;
    private String place_name;
    private String place_address;
    private String place_phone_number;
    private String place_website_url;
    private Double place_latitude;
    private Double place_longitude;

    public selected_place_details() {
    }

    public selected_place_details(Long id) {
        this.id = id;
    }

    public selected_place_details(Long id, String place_id, String place_name, String place_address, String place_phone_number, String place_website_url, Double place_latitude, Double place_longitude) {
        this.id = id;
        this.place_id = place_id;
        this.place_name = place_name;
        this.place_address = place_address;
        this.place_phone_number = place_phone_number;
        this.place_website_url = place_website_url;
        this.place_latitude = place_latitude;
        this.place_longitude = place_longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_address() {
        return place_address;
    }

    public void setPlace_address(String place_address) {
        this.place_address = place_address;
    }

    public String getPlace_phone_number() {
        return place_phone_number;
    }

    public void setPlace_phone_number(String place_phone_number) {
        this.place_phone_number = place_phone_number;
    }

    public String getPlace_website_url() {
        return place_website_url;
    }

    public void setPlace_website_url(String place_website_url) {
        this.place_website_url = place_website_url;
    }

    public Double getPlace_latitude() {
        return place_latitude;
    }

    public void setPlace_latitude(Double place_latitude) {
        this.place_latitude = place_latitude;
    }

    public Double getPlace_longitude() {
        return place_longitude;
    }

    public void setPlace_longitude(Double place_longitude) {
        this.place_longitude = place_longitude;
    }

}