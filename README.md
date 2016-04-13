<img src="./resource/Repository.png"  width="564" height="315"/>

### Integration
``` xml

dependencies {
    compile 'com.cabe.lib:RxCache:0.1 beta'
}

``` 

### Init config
```java

        StringHttpFactory.logLevel = RestAdapter.LogLevel.NONE;
        CacheUseCase.DISK_CACHE_PATH = getExternalCacheDir() + File.separator + "data";
        
```java

### Usage
```java

        RequestParams params = new RequestParams();
        params.host = "https://www.github.com";
        params.path = "crabfang/RxCache";

        CacheUseCase<GitHubBean> useCase = new CacheUseCase<>(new TypeToken<GitHubBean>(){}, params);
        useCase.execute(new ViewPresenter<GitHubBean>() {
            @Override
            public void error(CacheSource from, int code, String info) {
            }
            @Override
            public void load(CacheSource from, GitHubBean data) {
            }
            @Override
            public void complete(CacheSource from) {
            }
        });
        
```java