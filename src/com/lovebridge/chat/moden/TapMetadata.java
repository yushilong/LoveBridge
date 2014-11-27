package com.lovebridge.chat.moden;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

public class TapMetadata implements Parcelable
{
    public static Parcelable.Creator<TapMetadata> CREATOR;
    public final float aspectRatio;
    public final int degreesToRotateBy;
    public final PointF previewPosition;

    static
    {
        TapMetadata.CREATOR = new Parcelable.Creator<TapMetadata>()
        {
            public TapMetadata createFromParcel(Parcel in)
            {
                return new TapMetadata(in, null);
            }

            public TapMetadata[] newArray(int size)
            {
                return new TapMetadata[size];
            }
        };
    }

    public TapMetadata(float aspectRatio, PointF previewPosition, int degreesToRotateBy)
    {
        super();
        this.aspectRatio = aspectRatio;
        this.previewPosition = previewPosition;
        this.degreesToRotateBy = degreesToRotateBy;
    }

    private TapMetadata(Parcel in)
    {
        super();
        this.aspectRatio = in.readFloat();
        this.previewPosition = in.readParcelable(PointF.class.getClassLoader());
        this.degreesToRotateBy = in.readInt();
    }

    TapMetadata(Parcel arg1, TapMetadata arg2)
    {
        this(arg1);
    }

    public int describeContents()
    {
        return 0;
    }

    public boolean recordedInPortrait()
    {
        boolean bool = this.previewPosition.x == 0f ? true : false;
        return bool;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeFloat(this.aspectRatio);
        dest.writeParcelable(this.previewPosition, flags);
        dest.writeInt(this.degreesToRotateBy);
    }
}