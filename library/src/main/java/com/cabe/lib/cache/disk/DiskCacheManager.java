package com.cabe.lib.cache.disk;

import com.cabe.lib.cache.exception.DiskExceptionCode;
import com.cabe.lib.cache.exception.RxException;
import com.google.gson.Gson;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 *
 * Created by cabe on 16/4/12.
 */
public class DiskCacheManager {
    public static final String KEY_PROGRAM = "program";
    public static final String KEY_ALBUM = "album";
    public static final String KEY_ANCHOR = "anchor";
    Gson gson;
    DiskLruCache cache;

    public DiskCacheManager(DiskLruCache cache, Gson gson){
        this.cache = cache;
        this.gson = gson;
    }
    public synchronized boolean exits(String key){
        try {
            DiskLruCache.Snapshot snapshot = cache.get(key);
            if(snapshot!=null){
                return true;
            }
        } catch (IOException e) {
            throw new RxException(DiskExceptionCode.DISK_EXCEPTION_CHECK);
        }
        return false;
    }
    public synchronized <T> T get(String key, Type type){
        try {
            String data = cache.get(key).getString(0);
            T obj = gson.fromJson(data, type);
            return obj;
        }catch(Exception ignored){
            throw new RxException(DiskExceptionCode.DISK_EXCEPTION_GET);
        }
    }
    public synchronized boolean put(String key, Object obj){
        String json = gson.toJson(obj);
        DiskLruCache.Editor editor = null;
        try {
            editor = cache.edit(key);
            editor.set(0, json);
            editor.commit();
            return true;
        } catch (IOException e) {
            throw new RxException(DiskExceptionCode.DISK_EXCEPTION_SAVE);
        }
    }

}