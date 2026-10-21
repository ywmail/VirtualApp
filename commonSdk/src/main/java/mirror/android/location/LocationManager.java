/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.location.Location
 *  android.location.LocationListener
 *  android.os.Bundle
 *  android.os.IInterface
 */
package mirror.android.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IInterface;
import com.lody.virtual.StringFog;
import java.util.HashMap;
import java.util.Map;
import mirror.MethodParams;
import mirror.RefClass;
import mirror.RefMethod;
import mirror.RefObject;

public class LocationManager {
    public static Class<?> TYPE = RefClass.load(LocationManager.class, "android.location.LocationManager");
    public static RefObject<IInterface> mService;
    public static RefObject<Map> mGnssNmeaListeners;
    public static RefObject<Map> mGnssStatusListeners;
    public static RefObject<Map> mGpsNmeaListeners;
    public static RefObject<Map> mGpsStatusListeners;
    public static RefObject<HashMap> mListeners;
    public static RefObject<HashMap> mNmeaListeners;

    public static class LocationListenerTransport {
        public static Class<?> TYPE = RefClass.load(LocationListenerTransport.class, "android.location.LocationManager$LocationListenerTransport");
        public static RefObject<LocationListener> mListener;
        @MethodParams(value={Location.class})
        public static RefMethod<Void> onLocationChanged;
        @MethodParams(value={String.class})
        public static RefMethod<Void> onProviderDisabled;
        @MethodParams(value={String.class})
        public static RefMethod<Void> onProviderEnabled;
        @MethodParams(value={String.class, int.class, Bundle.class})
        public static RefMethod<Void> onStatusChanged;
        public static RefObject<Object> this$0;
    }

    public static class ListenerTransport {
        public static Class<?> TYPE = RefClass.load(ListenerTransport.class, "android.location.LocationManager$ListenerTransport");
        public static RefObject<LocationListener> mListener;
        @MethodParams(value={Location.class})
        public static RefMethod<Void> onLocationChanged;
        @MethodParams(value={String.class})
        public static RefMethod<Void> onProviderDisabled;
        @MethodParams(value={String.class})
        public static RefMethod<Void> onProviderEnabled;
        @MethodParams(value={String.class, int.class, Bundle.class})
        public static RefMethod<Void> onStatusChanged;
        public static RefObject<Object> this$0;
    }

    public static class GpsStatusListenerTransportVIVO {
        public static Class<?> TYPE = RefClass.load(GpsStatusListenerTransportVIVO.class, "android.location.LocationManager$GpsStatusListenerTransport");
        @MethodParams(value={int.class, int[].class, float[].class, float[].class, float[].class, int.class, int.class, int.class, long[].class})
        public static RefMethod<Void> onSvStatusChanged;
    }

    public static class GpsStatusListenerTransportSumsungS5 {
        public static Class<?> TYPE = RefClass.load(GpsStatusListenerTransportSumsungS5.class, "android.location.LocationManager$GpsStatusListenerTransport");
        @MethodParams(value={int.class, int[].class, float[].class, float[].class, float[].class, int.class, int.class, int.class, int[].class})
        public static RefMethod<Void> onSvStatusChanged;
    }

    public static class GpsStatusListenerTransportOPPO_R815T {
        public static Class<?> TYPE = RefClass.load(GpsStatusListenerTransportOPPO_R815T.class, "android.location.LocationManager$GpsStatusListenerTransport");
        @MethodParams(value={int.class, int[].class, float[].class, float[].class, float[].class, int[].class, int[].class, int[].class, int.class})
        public static RefMethod<Void> onSvStatusChanged;
    }

    public static class GpsStatusListenerTransport {
        public static Class<?> TYPE = RefClass.load(GpsStatusListenerTransport.class, "android.location.LocationManager$GpsStatusListenerTransport");
        public static RefObject<Object> mListener;
        public static RefObject<Object> mNmeaListener;
        @MethodParams(value={int.class})
        public static RefMethod<Void> onFirstFix;
        public static RefMethod<Void> onGpsStarted;
        @MethodParams(value={long.class, String.class})
        public static RefMethod<Void> onNmeaReceived;
        @MethodParams(value={int.class, int[].class, float[].class, float[].class, float[].class, int.class, int.class, int.class})
        public static RefMethod<Void> onSvStatusChanged;
        public static RefObject<Object> this$0;
    }

    public static class GnssStatusListenerTransportO {
        public static Class<?> TYPE = RefClass.load(GnssStatusListenerTransportO.class, "android.location.LocationManager$GnssStatusListenerTransport");
        @MethodParams(value={int.class, int[].class, float[].class, float[].class, float[].class, float[].class})
        public static RefMethod<Void> onSvStatusChanged;
    }

    public static class GnssStatusListenerTransport {
        public static Class<?> TYPE = RefClass.load(GnssStatusListenerTransport.class, "android.location.LocationManager$GnssStatusListenerTransport");
        public static RefObject<Object> mGpsListener;
        public static RefObject<Object> mGpsNmeaListener;
        @MethodParams(value={int.class})
        public static RefMethod<Void> onFirstFix;
        public static RefMethod<Void> onGnssStarted;
        @MethodParams(value={long.class, String.class})
        public static RefMethod<Void> onNmeaReceived;
        @MethodParams(value={int.class, int[].class, float[].class, float[].class, float[].class})
        public static RefMethod<Void> onSvStatusChanged;
        public static RefObject<Object> this$0;
    }
}

