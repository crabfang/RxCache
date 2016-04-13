package com.cabe.lib.cache.exception;

/**
 * Http Exception Code
 * Created by cabe on 16/4/12.
 */
public class HttpExceptionCode extends ExceptionCode {
    // -1xx(本地错误)
    /** 请求参数为空 */
    public final static int HTTP_STATUS_LOACL_REQUEST_NONE = -1;
    //1xx（临时响应）
    //表示临时响应并需要请求者继续执行操作的状态代码。
    /** （继续） 请求者应当继续提出请求。 服务器返回此代码表示已收到请求的第一部分，正在等待其余部分。  */
    public final static int HTTP_STATUS_CONTINUE = 100;
    /** （切换协议） 请求者已要求服务器切换协议，服务器已确认并准备切换。 */
    public final static int HTTP_STATUS_PROTOCOL_CHANGE = 101;

    //2xx （成功）
    //表示成功处理了请求的状态代码。
    /** （成功） 服务器已成功处理了请求。 通常，这表示服务器提供了请求的网页。 */
    public final static int HTTP_STATUS_SUCCESS_OK = 200;
    /** （已创建） 请求成功并且服务器创建了新的资源。  */
    public final static int HTTP_STATUS_SUCCESS_CREATED = 201;
    /** （已接受） 服务器已接受请求，但尚未处理。  */
    public final static int HTTP_STATUS_SUCCESS_ACCESSED = 202;
    /** （非授权信息） 服务器已成功处理了请求，但返回的信息可能来自另一来源。  */
    public final static int HTTP_STATUS_SUCCESS_NO_PERMISSION = 203;
    /** （无内容） 服务器成功处理了请求，但没有返回任何内容。  */
    public final static int HTTP_STATUS_SUCCESS_CONTENT_NONE = 204;
    /** （重置内容） 服务器成功处理了请求，但没有返回任何内容。 */
    public final static int HTTP_STATUS_SUCCESS_CONTENT_RESET = 205;
    /** （部分内容） 服务器成功处理了部分 GET 请求。 */
    public final static int HTTP_STATUS_SUCCESS_CONTENT_PART = 206;

    //3xx （重定向）
    //表示要完成请求，需要进一步操作。 通常，这些状态代码用来重定向。
    /** （多种选择） 针对请求，服务器可执行多种操作。 服务器可根据请求者 (user agent) 选择一项操作，或提供操作列表供请求者选择。 */
    public final static int HTTP_STATUS_REDIRECT_MULTIPLE = 300;
    /** （永久移动） 请求的网页已永久移动到新位置。 服务器返回此响应（对 GET 或 HEAD 请求的响应）时，会自动将请求者转到新位置。 */
    public final static int HTTP_STATUS_REDIRECT_MOVE_FOREVER = 301;
    /** （临时移动） 服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求。 */
    public final static int HTTP_STATUS_REDIRECT_MOVE_TMP = 302;
    /** （查看其他位置） 请求者应当对不同的位置使用单独的 GET 请求来检索响应时，服务器返回此代码。 */
    public final static int HTTP_STATUS_REDIRECT_CHECK_OTHER = 303;
    /** （未修改） 自从上次请求后，请求的网页未修改过。 服务器返回此响应时，不会返回网页内容。  */
    public final static int HTTP_STATUS_REDIRECT_NO_MODIFY = 304;
    /** （使用代理） 请求者只能使用代理访问请求的网页。 如果服务器返回此响应，还表示请求者应使用代理。  */
    public final static int HTTP_STATUS_REDIRECT_PROXY = 305;
    /** （临时重定向） 服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求。 */
    public final static int HTTP_STATUS_REDIRECT_TMP = 307;

    //4xx（客户端请求错误）
    //这些状态代码表示请求可能出错，妨碍了服务器的处理。
    /** （错误请求） 服务器不理解请求的语法 */
    public final static int HTTP_STATUS_CLIENT_ERROR = 400;
    /**
     * （未授权） 请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应
     * HTTP 401.1 - 未授权：登录失败
     * HTTP 401.2 - 未授权：服务器配置问题导致登录失败
     * HTTP 401.3 - ACL 禁止访问资源
     * HTTP 401.4 - 未授权：授权被筛选器拒绝
     * HTTP 401.5 - 未授权：ISAPI 或 CGI 授权失败
     **/
    public final static int HTTP_STATUS_CLIENT_NO_PERMISSION = 401;
    /**
     * （禁止） 服务器拒绝请求
     * HTTP 403.1 - 禁止访问：禁止可执行访问
     * HTTP 403.2 - 禁止访问：禁止读访问
     * HTTP 403.3 - 禁止访问：禁止写访问
     * HTTP 403.4 - 禁止访问：要求 SSL
     * HTTP 403.5 - 禁止访问：要求 SSL 128
     * HTTP 403.6 - 禁止访问：IP 地址被拒绝
     * HTTP 403.7 - 禁止访问：要求客户证书
     * HTTP 403.8 - 禁止访问：禁止站点访问
     * HTTP 403.9 - 禁止访问：连接的用户过多
     * HTTP 403.10 - 禁止访问：配置无效
     * HTTP 403.11 - 禁止访问：密码更改
     * HTTP 403.12 - 禁止访问：映射器拒绝访问
     * HTTP 403.13 - 禁止访问：客户证书已被吊销
     * HTTP 403.15 - 禁止访问：客户访问许可过多
     * HTTP 403.16 - 禁止访问：客户证书不可信或者无效
     * HTTP 403.17 - 禁止访问：客户证书已经到期或者尚未生效
     **/
    public final static int HTTP_STATUS_CLIENT_REFUSE = 403;
    /** （未找到） 服务器找不到请求的网页 */
    public final static int HTTP_STATUS_CLIENT_NOT_FOUND = 404;
    /** （方法禁用） 禁用请求中指定的方法 */
    public final static int HTTP_STATUS_CLIENT_DISABLED = 405;
    /** （不接受） 无法使用请求的内容特性响应请求的网页 */
    public final static int HTTP_STATUS_CLIENT_NO_ACCPET = 406;
    /** （需要代理授权） 此状态代码与 401（未授权）类似，但指定请求者应当授权使用代理 */
    public final static int HTTP_STATUS_CLIENT_PROXY = 407;
    /** （请求超时） 服务器等候请求时发生超时 */
    public final static int HTTP_STATUS_CLIENT_REQUEST_TIMEOUT = 408;
    /** （冲突） 服务器在完成请求时发生冲突。 服务器必须在响应中包含有关冲突的信息 */
    public final static int HTTP_STATUS_CLIENT_CLASH = 409;
    /** （已删除） 如果请求的资源已永久删除，服务器就会返回此响应 */
    public final static int HTTP_STATUS_CLIENT_DELETED = 410;
    /** （需要有效长度） 服务器不接受不含有效内容长度标头字段的请求 */
    public final static int HTTP_STATUS_CLIENT_KEY_VAILD = 411;
    /** （未满足前提条件） 服务器未满足请求者在请求中设置的其中一个前提条件 */
    public final static int HTTP_STATUS_CLIENT_UNMET_TERMS = 412;
    /** （请求实体过大） 服务器无法处理请求，因为请求实体过大，超出服务器的处理能力 */
    public final static int HTTP_STATUS_CLIENT_BODY_LARGE = 413;
    /** （请求的 URI 过长） 请求的 URI（通常为网址）过长，服务器无法处理 */
    public final static int HTTP_STATUS_CLIENT_URL_LONG = 414;
    /** （不支持的媒体类型） 请求的格式不受请求页面的支持 */
    public final static int HTTP_STATUS_CLIENT_MEDIA_NOT_SUPPORT = 415;
    /** （请求范围不符合要求） 如果页面无法提供请求的范围，则服务器会返回此状态代码 */
    public final static int HTTP_STATUS_CLIENT_RANG_UNMATCH = 416;
    /** （未满足期望值） 服务器未满足"期望"请求标头字段的要求 */
    public final static int HTTP_STATUS_CLIENT_EXPECT_UNMATCH = 417;

    //5xx（服务器错误）
    //这些状态代码表示服务器在尝试处理请求时发生内部错误。 这些错误可能是服务器本身的错误，而不是请求出错
    /**
     * （服务器内部错误） 服务器遇到错误，无法完成请求
     * HTTP 500.100 - 内部服务器错误 - ASP 错误
     * HTTP 500-11 服务器关闭
     * HTTP 500-12 应用程序重新启动
     * HTTP 500-13 - 服务器太忙
     * HTTP 500-14 - 应用程序无效
     * HTTP 500-15 - 不允许请求 global.asa
     **/
    public final static int HTTP_STATUS_SERVER_ERROR = 500;
    /** （尚未实施） 服务器不具备完成请求的功能。 例如，服务器无法识别请求方法时可能会返回此代码 */
    public final static int HTTP_STATUS_SERVER_NO_IMPLEMENT = 501;
    /** （错误网关） 服务器作为网关或代理，从上游服务器收到无效响应 */
    public final static int HTTP_STATUS_SERVER_GETWAY_ERROR = 502;
    /** （服务不可用） 服务器目前无法使用（由于超载或停机维护）。 通常，这只是暂时状态 */
    public final static int HTTP_STATUS_SERVER_INVAILD = 503;
    /** （网关超时） 服务器作为网关或代理，但是没有及时从上游服务器收到请求 */
    public final static int HTTP_STATUS_SERVER_GETWAY_TIMEOUT = 504;
    /** （HTTP 版本不受支持） 服务器不支持请求中所用的 HTTP 协议版本 */
    public final static int HTTP_STATUS_SERVER_VERSION_NO_SUPPORT = 505;

    @Override
    public String getInfo(int code) {
        String info = null;

        if(ExceptionCode.isHttpException(code)) {
            switch (code) {
                case HTTP_STATUS_LOACL_REQUEST_NONE:
                    info = "请求参数异常";
                    break;
                case HTTP_STATUS_SUCCESS_OK:
                    info = "成功";
                    break;
                case HTTP_STATUS_SUCCESS_NO_PERMISSION:
                    info = "没有权限";
                    break;
                case HTTP_STATUS_CLIENT_REQUEST_TIMEOUT:
                    info = "请求超时";
                    break;
                default:
                    info = "网络异常";
                    break;
            }
        }

        return info;
    }
}
