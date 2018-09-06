package com.weixin.note.serv.sso.util;

import com.weixin.note.serv.sso.token.SSOToken;

/**
 * 
 * @author admin
 *
 */
public class SSOTokenContext {
	private static final ThreadLocal<SSOTokenContext> REQUEST_HEADER_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();
	private String userId;
	private String token;
	private SSOToken ssoToken;
	

    public String getUserId() {  
        return userId;  
    }  
  
    public String getToken() {  
        return token;  
    }  
    
    
  
    public SSOToken getSSOToken() {
		return ssoToken;
	}
    
    
	public void setSsoToken(SSOToken ssoToken) {
		this.ssoToken = ssoToken;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static SSOTokenContext getInstance(){  
        return REQUEST_HEADER_CONTEXT_THREAD_LOCAL.get();  
    }  
  
    public void setContext(SSOTokenContext context){  
        REQUEST_HEADER_CONTEXT_THREAD_LOCAL.set(context);  
    }  
  
    public static void clean(){  
        REQUEST_HEADER_CONTEXT_THREAD_LOCAL.remove();  
    }  
  
    private SSOTokenContext(RequestHeaderContextBuild requestHeaderContextBuild){  
        this.userId=requestHeaderContextBuild.userId;  
        this.token=requestHeaderContextBuild.token;  
        this.ssoToken = requestHeaderContextBuild.ssoToken;
        setContext(this);  
    }  
  
    public static class RequestHeaderContextBuild{  
        private String userId;  
        private String token;  
        private SSOToken ssoToken;
  
        public RequestHeaderContextBuild userId(String userId){  
            this.userId=userId;  
            return this;  
        }  
  
        public RequestHeaderContextBuild token(String token){  
            this.token=token;  
            return this;  
        }  
        
        public RequestHeaderContextBuild ssoToken(SSOToken ssoToken){  
            this.ssoToken=ssoToken;  
            return this;  
        }  
          
  
  
        public SSOTokenContext bulid(){  
            return new SSOTokenContext(this);  
        }  
        
        
    } 
}
