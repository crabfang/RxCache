package com.cabe.lib.cache;

/**
 * 获取缓存的方式<br>
 * 本地以及远程{@link CacheMethod#BOTH} <br>
 * 本地缓存{@link CacheMethod#DISK} <br>
 * 远程缓存{@link CacheMethod#HTTP} <br>
 * Created by cabe on 16/4/15.
 */
public enum  CacheMethod {
    BOTH, DISK, HTTP
}
