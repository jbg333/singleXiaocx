
package com.weixin.note.serv.sso.token;
/**
 * <p>
 * SSO Token 票据
 * </p>
 *
 * @author hubin
 * @since 2017-07-17
 */
public interface Token {

    String getToken(); // 生成 Token 字符串
}
