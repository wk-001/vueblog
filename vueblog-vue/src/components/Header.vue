<template>
    <div class="head">
        <h2>博客</h2>

        <div class="block"><el-avatar :size="50" :src="user.avatar"></el-avatar></div>
        <div>{{user.username}}</div>

        <div class="divid">
            <span><el-link type="info" href="/blogs">主页</el-link></span>
            <el-divider direction="vertical"></el-divider>
            <span><el-link type="success" href="/blog/add">发表博客</el-link></span>
            <el-divider direction="vertical"></el-divider>
            <span v-show="hasLogin"><el-link type="danger" @click="logout">退出</el-link></span>
            <span v-show="!hasLogin"><el-link type="primary" href="/login">登录</el-link></span>
        </div>

    </div>
</template>

<script>
    export default {
        name: "Header",
        data(){
            return{
                user:{
                    username:"请登录",
                    avatar:"https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                },
                hasLogin:false
            }
        },
        created() {
            //如果用户名不为空则回显用户信息
            const name =  this.$store.getters.getUser
            if(name){
                this.user.username = name.username
                this.user.avatar = name.avatar

                this.hasLogin = true
            }
        },
        methods:{
            logout(){
                const _this = this
                //logout方法需要登陆后才能访问，所以发送请求需要在head中携带jwt token
                _this.$axios.get("/logout",{
                    headers:{
                        "Authorization":localStorage.getItem("token")
                        // "Authorization":_this.$store.getters.getToken
                    }
                }).then(res=>{
                    //清除localStorage中的用户信息并跳转到用户登录页面
                    _this.$store.commit("REMOVE_INFO")
                    _this.$router.push("/login")
                })
            }
        }
    }
</script>

<style scoped>
    .head{
        margin: 0 auto;
        text-align: center;
    }

    .divid{
        margin: 10px 0;
    }
</style>