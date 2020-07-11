import Vue from 'vue'
import VueRouter from 'vue-router'

const Login=()=>import('views/Login')
const Blogs=()=>import('views/Blogs')
const BlogEdit=()=>import('views/BlogEdit')
const BlogDetail=()=>import('views/BlogDetail')
Vue.use(VueRouter)

  const routes = [
  {
    path: '',
    redirect: '/blogs'
  },
  {
    path: '/blogs',
    name: 'Blogs',
    component: Blogs
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/blog/add',
    name: 'BlogEdit',
    component: BlogEdit,
    meta:{
      requireAuth:true    //登录后才能跳转到该路由
    }
  },
  {
    path: '/blog/:blogId',
    name: 'BlogDetail',
    component: BlogDetail
  },
  {
    path: '/blog/edit/:blogId',
    name: 'BlogEdit',
    component: BlogEdit,
    meta:{
      requireAuth:true
    }
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
