package com.cabe.lib.demo.rxcache;

/**
 *
 * Created by cabe on 16/4/13.
 */
public class SplashInfo {
    private static final long serialVersionUID = 7555326059741917029L;
    private  String entryurl;
    private String loadingImageUrl;

    public String getEntryurl() {
        return entryurl;
    }

    public void setEntryurl(String entryurl) {
        this.entryurl = entryurl;
    }

    public String getLoadingImageUrl() {
        return loadingImageUrl;
    }

    public void setLoadingImageUrl(String loadingImageUrl) {
        this.loadingImageUrl = loadingImageUrl;
    }

    @Override
    public String toString() {
        return loadingImageUrl + "#" + entryurl;
    }
}
