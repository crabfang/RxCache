package com.cabe.lib.cache.impl;

import com.cabe.lib.cache.CacheMethod;
import com.google.gson.reflect.TypeToken;

/**
 * 本地缓存用例实现<br>
 * Created by cabe on 16/4/26.
 */
public class DiskCacheUseCase<T> extends DoubleCacheUseCase<T> {
    public DiskCacheUseCase(TypeToken<T> typeT) {
        super(typeT, null, CacheMethod.DISK);
    }
}
