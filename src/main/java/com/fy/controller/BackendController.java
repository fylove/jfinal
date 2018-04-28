package com.fy.controller;

import com.jfinal.core.Controller;

/**
 * @author:飞羽
 * @date:2018/3/26 13:20
 * @Description:
 */
public class BackendController extends Controller {

    public void index(){
        System.out.println("sdasdas");
        render("main.jsp");
    }
}
