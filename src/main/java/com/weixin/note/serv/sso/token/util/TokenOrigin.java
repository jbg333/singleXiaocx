
package com.weixin.note.serv.sso.token.util;

/**
 * <p>
 * Token 登录授权来源
 * </p>
 *
 * @author hubin
 * @since 2017-08-07
 */
public enum TokenOrigin {
    COOKIE("0", "cookie"),
    HTML5("1", "html5"),
    IOS("2", "apple ios"),
    ANDROID("3", "google android");

    /**
     * 主键
     */
    private final String value;

    /**
     * 描述
     */
    private final String desc;

    TokenOrigin(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static TokenOrigin fromValue(String value) {
        TokenOrigin[] its = TokenOrigin.values();
        for (TokenOrigin it : its) {
            if (it.value() == value) {
                return it;
            }
        }
        return COOKIE;
    }

    public String value() {
        return this.value;
    }

    public String desc() {
        return this.desc;
    }

}
