<template>
    <div>
        <Header/>
        <div class="detail">
            <h2>{{blog.title}}</h2><hr>
            <h2>{{blog.content}}</h2>
            <span>{{blog.description}}</span>
        </div>
    </div>
</template>

<script>
    import Header from "../components/Header";
    export default {
        name: "BlogDetail",
        components: {Header},
        data(){
            return{
                blog:{}
            }
        },
        created() {
            const blogId = this.$route.params.blogId
            const _this = this
            this.$axios.get("/blog/"+blogId).then(res=>{
                this.blog = res.data.data
            })
        }
    }
</script>

<style scoped>
    .detail{
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
    }
</style>