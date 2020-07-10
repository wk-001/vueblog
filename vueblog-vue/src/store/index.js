import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    //jwt token
    token: '',
    // 用户信息
    userInfo: JSON.parse(sessionStorage.getItem("userInfo"))
  },
  mutations: {
    //不能直接修改state中的属性，需在在mutation中修改 相当于setter
    SET_TOKEN:((state, payload) => {
      state.token = payload
      //将用户的token存放到浏览器缓存localstorage中 浏览器关闭后依然存在
      localStorage.setItem("token",payload)
    }),
    SET_USERINFO:((state, payload) => {
      state.userInfo = payload
      /*将用户的token存放到会话中 浏览器关闭后消失
      * sessionStorage无法存储对象，只能存字符串，需要将对象序列化*/
      sessionStorage.setItem("userInfo",JSON.stringify(payload))
    }),
    REMOVE_INFO:(state => { //清空token和userinfo
      state.token = ""
      state.userInfo = ""
      localStorage.removeItem("token")
      sessionStorage.removeItem("userInfo")
    })
  },
  getters:{
    //通过getters获取state中的数据
    getToken:state => {
      return state.token
    },
    getUser:state => {
      return state.userInfo
    }
  },
  actions: {
  },
  modules: {
  }
})
