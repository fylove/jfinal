package com.fy.config;

import com.fy.controller.BackendController;
import com.fy.controller.FrontController;
import com.jfinal.config.*;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

/**
 * @author:飞羽
 * @date:2018/3/8 14:03
 * @Description:
 */
public class WebConfig extends JFinalConfig{
    public void configConstant(Constants constants) {
        constants.setDevMode(true);
        constants.setViewType(ViewType.JSP);
    }

    //配置访问路由
    public void configRoute(Routes routes) {
        routes.setBaseViewPath("/WEB-INF/views");
        routes.add("/front", FrontController.class,"front");
        routes.add("/backend", BackendController.class,"backend");
    }

    public void configEngine(Engine engine) {

    }

    public void configPlugin(Plugins plugins) {

    }

    public void configInterceptor(Interceptors interceptors) {

    }

    public void configHandler(Handlers handlers) {

    }
}
