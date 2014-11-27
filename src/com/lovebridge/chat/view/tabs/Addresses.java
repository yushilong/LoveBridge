
package com.lovebridge.chat.view.tabs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.lovebridge.chat.utils.AvatarUtils;

public class Addresses {
    private List<Address> addresses;

    public Addresses(Address address) {
        super();
        this.addresses = new ArrayList<Address>();
        this.addresses.add(address);
    }

    public String[] getPhoneNumbers() {
        return null;
    }

    public Address getAddress() {
        return this.addresses.get(0);
    }

    public Address getAddress(int i) {
        return this.addresses.get(i);
    }

    public Bitmap getBitmap(Context context) {
        return AvatarUtils.getCircularBitmap(context, this);
    }

    public String getTitle() {
        String string;
        StringBuilder stringBuilder = new StringBuilder();
        if (this.isGroup()) {
            int i;
            for (i = 0; i < this.addresses.size(); ++i) {
                if (i != 0) {
                    stringBuilder.append(", ");
                }

                stringBuilder.append(this.addresses.get(i).getFirstName());
            }

            string = stringBuilder.toString();
        } else {
            string = this.getAddress().getName();
        }

        return string;
    }

    public int hashCode() {
        return this.addresses.hashCode();
    }

    public boolean isGroup() {
        boolean bool = true;
        if (this.size() <= 1) {
            bool = false;
        }

        return bool;
    }

    public int size() {
        return this.addresses.size();
    }
}
