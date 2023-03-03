<template>
<div class="userSetting-main">
   <div class="userSetting-main-avatar">
     <el-upload class="avatar-uploader" action="/api/user/addImage"
                :show-file-list="false"
                :data="sendData"
                name="file"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
       <img v-if="imageUrl" :src="'/api'+imageUrl" class="avatar" alt="">
       <i v-else class="el-icon-plus avatar-uploader-icon"></i>
     </el-upload>
   </div>
   <div class="userSetting-main-name">
       <span>{{ruleForm.nickName}}</span>
   </div>
   <div class="userSetting-main-set">
     <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
       <el-form-item label="昵称" prop="nickName">
         <el-input maxlength="30" show-word-limit v-model="ruleForm.nickName"></el-input>
       </el-form-item>
       <el-form-item label="性别" prop="sex">
         <el-radio-group v-model="ruleForm.sex">
           <el-radio label="男"></el-radio>
           <el-radio label="女"></el-radio>
         </el-radio-group>
       </el-form-item>
       <el-form-item label="签名" prop="sign">
         <el-input  maxlength="45" resize="none" show-word-limit type="textarea" v-model="ruleForm.sign" clearable></el-input>
       </el-form-item>
       <el-form-item style="text-align: right">
         <el-button round size="small" class="el-button-edit" type="primary" @click="submitForm('ruleForm')">修改</el-button>
         <el-button round size="small" class="el-button-approve" @click="resetForm('ruleForm')">重置</el-button>
       </el-form-item>
     </el-form>
   </div>
</div>
</template>

<script>
export default {
  name: "userSetting",
  data() {
    return {
      sendData:{userId:localStorage['userId']},
      imageUrl: '',
      ruleForm: {
        nickName: '',
        sex: '',
        sign:'',
      },
      rules: {
        nickName: [
          { required: true, message: '昵称', trigger: 'blur' },
          { min: 3, max:20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        resource: [
          { required: true, message: '性别', trigger: 'change' }
        ],
        desc: [
          { required: true, message: '签名', trigger: 'blur' }
        ]
      },
    };
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo(){
      this.api.post("/user/getUserInfo",{userId:this.sendData.userId}).then(res=>{
         this.ruleForm.nickName=res.name
         this.ruleForm.sex=res.sex===1?'男':'女'
         this.ruleForm.sign=res.sign
         this.imageUrl=res.url
      })
    },
    updateUserInfo(){
      let sex=this.ruleForm.sex==='男'?1:2
      this.api.post("/user/updateUserInfo",{userId:this.sendData.userId,...this.ruleForm,sex,url:this.imageUrl}).then(res=>{
        this.$message.success(res)
      })
    },
    handleAvatarSuccess(res, file) {
      console.log(res)
      this.imageUrl = res.info.relativeUrl
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG&&!isPNG) {
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
        return false
      }
      return  true;
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
             this.updateUserInfo()
        } else {
         this.$message.warning("格式错误")
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.getUserInfo()
    },
  }
}
</script>

<style scoped>

.userSetting-main{
  color: white;
  width: 100%;
  height: 50px;
  text-align:left;
  padding: 20px;
}
.userSetting-main /deep/ .el-upload{
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  width: 80px;
  height: 80px;
}
.userSetting-main /deep/ img{
  border-radius: 50%;
  width: 100%;
  height: 100%;
}
.userSetting-main /deep/ i{
   width: 80px;
  height: 80px;
  line-height: 80px;
}
.userSetting-main-avatar{
  display: flex;
  align-items: center;
  justify-content: center;
}
.userSetting-main-name{
  margin: 8px auto;
  text-align: center;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
.userSetting-main-set{
  width: 70%;
  margin: 0 auto;
}
.userSetting-main-set /deep/ .el-form-item__label{
  color: white;
}
.userSetting-main-set /deep/ .el-input__count,.userSetting-main-set /deep/  .el-input__count-inner {
  background-color: rgba(255, 255, 255, 0);
  color: white;
}
.userSetting-main-set /deep/ .el-input__inner{
  background-color: #fff0;
  border: none;
  border-bottom: 1px solid rgba(255, 255, 255, 0.27);
  color: white;
}
.userSetting-main-set /deep/ .el-textarea__inner{
  background-color: rgba(255, 255, 255, 0.05);
  color: white;
  height: 80px;
}
.userSetting-main-set /deep/ .el-radio{
  color: white;
}
.userSetting-main-set /deep/ .el-radio__input.is-checked+.el-radio__label{
  color: #5dcaad;
}
.userSetting-main-set /deep/ .el-radio__input.is-checked .el-radio__inner {
  border-color: #5dcaad;
  background: #5dcaad;
}


</style>
