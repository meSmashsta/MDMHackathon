package com.blazingmuffin.health.mdmsystem.utils;

import java.util.Enumeration;

import io.reactivex.Observable;

/**
 * Created by ekimus on 21/10/2017.
 */

final class ObservableUtils {

    static <T> Observable<T> fromEnumeration(Enumeration<T> enumeration) {
        return Observable.create(source -> {
            while (enumeration.hasMoreElements()) {
                source.onNext(enumeration.nextElement());
            }
            source.onComplete();
        });
    }
}