<template>
    <div id="edit">
        <Header/>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="标题" prop="title">
                <el-input v-model="ruleForm.title"></el-input>
            </el-form-item>
            <el-form-item label="描述" prop="description">
                <el-input v-model="ruleForm.description"></el-input>
            </el-form-item>
            <el-form-item label="内容" prop="content">
                <el-input type="textarea" v-model="ruleForm.content"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitForm('ruleForm')">立即创建</el-button>
                <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import Header from "../components/Header";
    export default {
        name: "BlogEdit",
        components: {Header},
        data() {
            return {
                ruleForm: {
                    id:'',
                    title: '',
                    description: '',
                    content: ''
                },
                rules: {
                    title: [
                        { required: true, message: '请输入标题', trigger: 'blur' },
                        { min: 3, max: 15, message: '长度在 3 到 5 个字符', trigger: 'blur' }
                    ],
                    description: [
                        { required: true, message: '请输入摘要', trigger: 'blur' }
                    ],
                    content: [
                        { required: true, message: '请输入内容', trigger: 'blur' }
                    ]
                }
            };
        },
        created() {
            const blogId = this.$route.params.blogId
            if (blogId) {
                this.$axios.get("/blog/"+blogId).then(res=>{
                    const blog = res.data.data
                    this.ruleForm.id = blog.id
                    this.ruleForm.title = blog.title
                    this.ruleForm.description = blog.description
                    this.ruleForm.content = blog.content

                })
            }
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post("blog",this.ruleForm,{
                            headers:{
                                "Authorization":localStorage.getItem("token")
                                // "Authorization":_this.$store.getters.getToken
                            }
                        }).then(res=>{
                            this.$message.success('博客发布成功');
                            this.$router.push("/blogs")
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style scoped>
    #edit{
        text-align: center;
    }
</style>