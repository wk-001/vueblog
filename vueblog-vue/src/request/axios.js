//axios的默认路径前缀前置、后置拦截

import axios from "axios";
import ElementUI from 'element-ui';
import router from "../router";
import store from "../store";

axios.defaults.baseURL = "http://localhost:8081/";

//axios请求拦截器
axios.interceptors.request.use(config=>{

    return config;      //拦截请求处理完成之后要放行请求
},error => {        //发送失败进入error
    console.log(error);
})

//axios响应拦截器
axios.interceptors.response.use(response=>{
    //处理结果
    let data = response.data;
    if (data.code===200){        //判断响应结果
        return response;         //拦截响应处理完成之后要放行
    }else {
        ElementUI.Message.error(data.msg)
        //拒绝请求向后执行并返回异常信息
        return Promise.reject(response.data.msg)
    }
},error => {        //后端的错误
    if (error.response.data) {
        error.message = error.response.data.msg
    }

    if (error.response.status === 401){
        store.commit("REMOVE_INFO");     //登录失败 清空用户信息
        router.push("/login");       //跳转到登录页面
    }

    ElementUI.Message.error(error.message)
    //拒绝请求向后执行并返回异常信息
    return Promise.reject(error)
})