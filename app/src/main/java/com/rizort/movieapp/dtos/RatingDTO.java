package com.rizort.movieapp.dtos;

import android.os.Parcel;
import android.os.Parcelable;

public class RatingDTO implements Parcelable {

    String Source;
    String Value;

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public RatingDTO(String source, String value) {
        Source = source;
        Value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Source);
        dest.writeString(this.Value);
    }

    protected RatingDTO(Parcel in) {
        this.Source = in.readString();
        this.Value = in.readString();
    }

    public static final Parcelable.Creator<RatingDTO> CREATOR = new Parcelable.Creator<RatingDTO>() {
        @Override
        public RatingDTO createFromParcel(Parcel source) {
            return new RatingDTO(source);
        }

        @Override
        public RatingDTO[] newArray(int size) {
            return new RatingDTO[size];
        }
    };
}
