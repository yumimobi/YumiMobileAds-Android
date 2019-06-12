package com.yumi.android.mobile.ads.utils.other;


import java.util.Collection;

public final class NullCheckUtils {

    public static boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
