package com.fy.controller;

import com.jfinal.core.Controller;

/**
 * @author:飞羽
 * @date:2018/3/8 14:06
 * @Description:
 */
public class FrontController extends Controller {
    public void index(){
        System.out.println("sdadsssa");
        render("index.jsp");
    }
}
