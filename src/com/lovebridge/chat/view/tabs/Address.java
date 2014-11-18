
package com.lovebridge.chat.view.tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable, Bitmappable {
    private boolean loaded;
    public final String number;

    public Address(String number) {
        super();
        this.number = number;
    }

    private Address(Parcel in) {
        super();
        this.number = in.readString();
        this.loaded = (Boolean)in.readValue(null);
    }

    Address(Parcel arg1, Address arg2) {
        this(arg1);
    }

    public int compareTo(Address another) {
        return this.number.compareTo(another.number);
    }

    public int compareTo(Object arg2) {
        return this.compareTo(((Address)arg2));
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean bool = false;
        if (obj != null) {
            if ((((Address)obj)) == this) {
                bool = true;
            } else if ((obj instanceof Address)) {
                bool = this.number.equals(((Address)obj).number);
            }
        }

        return bool;
    }

    public String getFirstName() {

        return "FirstName";
    }

    public Bitmap getFullBitmap(boolean highRes) {
        Bitmap bitmap = null;
        return bitmap;
    }

    public String getName() {

        return "Name";
    }

    public boolean hasContact() {
        return false;
    }

    public int hashCode() {
        return this.number.hashCode();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.number);
        dest.writeValue(Boolean.valueOf(this.loaded));
    }

    @Override
    public Bitmap getBitmap(Context arg1) {
        // TODO Auto-generated method stub
        return null;
    }

}
