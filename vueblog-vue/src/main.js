import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

//导入element-ui
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

//导入axios
import axios from "axios";
import "./request/axios"

Vue.config.productionTip = false
//设置全局引用
Vue.prototype.$axios = axios

Vue.use(ElementUI);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

//axios的前置、后置拦截
