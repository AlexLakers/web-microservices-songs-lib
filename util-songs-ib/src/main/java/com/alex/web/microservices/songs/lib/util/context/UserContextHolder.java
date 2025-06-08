package com.alex.web.microservices.songs.lib.util.context;


public class UserContextHolder {
    private static ThreadLocal<UserContext> contextThreadLocal = new ThreadLocal<>();

    public static void setUserContext(UserContext userContext) {
        contextThreadLocal.set(userContext);
    }
    public static UserContext getUserContext() {
        if(contextThreadLocal.get()==null){
            contextThreadLocal.set(new UserContext());
        }
        return contextThreadLocal.get();

    }
}
