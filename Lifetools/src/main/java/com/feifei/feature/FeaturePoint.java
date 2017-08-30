package com.feifei.feature;

import android.os.Parcel;
import android.os.Parcelable;


public class FeaturePoint implements Parcelable {
    String name;
    String address;
    double lat;// 纬度
    double lon;// 经度
    //附加字段
    String url;
    String price;
    String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FeaturePoint(String name, String address, double lat, double lon) {
        this.address = address;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "FeaturePoint{" + "name='" + name + '\'' + ", address='" + address
                + '\'' + ", lat=" + lat + ", lon=" + lon + '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
        parcel.writeString(url);
        parcel.writeString(price);
        parcel.writeString(type);

    }

    public static final Parcelable.Creator<FeaturePoint> CREATOR = new Parcelable.Creator<FeaturePoint>() {

        @Override
        public FeaturePoint createFromParcel(Parcel parcel) {
            FeaturePoint featurePoint = new FeaturePoint(parcel.readString(),
                    parcel.readString(), parcel.readDouble(),
                    parcel.readDouble());
            featurePoint.url = parcel.readString();
            featurePoint.price = parcel.readString();
            featurePoint.type = parcel.readString();
            return featurePoint;
        }

        @Override
        public FeaturePoint[] newArray(int i) {
            return new FeaturePoint[0];
        }
    };
}
