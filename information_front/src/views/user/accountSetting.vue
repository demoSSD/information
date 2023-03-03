<template>
  <div class="account-main">
    <div class="account-main-card">
      <h4>修改密码: </h4>
      <div class="login_box">
        <input type="password" required v-model="oldPassWord.value" @blur="verifyOld(oldPassWord,true)"/>
        <label><svg-icon icon-class="password"/>旧密码<span>{{oldPassWord.tip}}</span></label>
      </div>
      <div class="login_box">
        <input type="password" required  v-model="newPassWord.value" @blur="verify(newPassWord,rules.newPassword)"/>
        <label><svg-icon icon-class="password"/>新密码<span>{{newPassWord.tip}}</span></label>
      </div>
      <div class="login_box">
        <input type="password" required  v-model="newPassWord2.value" @blur="confirm(newPassWord,newPassWord2)"/>
        <label><svg-icon icon-class="password"/>确认新密码<span>{{newPassWord2.tip}}</span></label>
      </div>
      <div class="login_but">
        <DivButton2 label="取消" @click.native="clear"/>
        <DivButton1 label="确定" @click.native="ok"/>
      </div>
    </div>

<!--      <div class="account-edit">-->
<!--        <div>-->
<!--          <el-input type="password" placeholder="请输入旧密码" prefix-icon="el-icon-lock" v-model="oldPassWord"/>-->
<!--        </div>-->
<!--        <div>-->
<!--          <el-input type="password" placeholder="请输入新密码" prefix-icon="el-icon-lock" v-model="newPassWord"/>-->
<!--        </div>-->
<!--        <div>-->
<!--          <el-input type="password"  placeholder="请确认新密码" prefix-icon="el-icon-lock" v-model="newPassWord2"/>-->
<!--        </div>-->
<!--        <div style="text-align: center">-->
<!--          <el-button style="width: 100px"  @click="clear" class="el-button-approve" size="small" round>清空</el-button>-->
<!--          <el-button style="width: 100px"  @click="ok" class="el-button-edit" size="small" round>修改</el-button>-->
<!--        </div>-->
<!--      </div>-->
  </div>
</template>

<script>
import DivButton1 from "../../components/button/DivButton1";
import DivButton2 from "../../components/button/DivButton2";
export default {
  name: "AccountSetting",
  components: {DivButton2, DivButton1},
  data() {
    return {
      userId:localStorage['userId'],
      oldPassWord:{value:'',tip:''},
      newPassWord:{value:'',tip:''},
      newPassWord2:{value:'',tip:''},
      rules: {
        newPassword: [{pattern:/^.{6,30}$/,message: "请输入至少6位密码"},{pattern:/^[0-9a-zA-Z]{6,30}$/,message: "密码只能包含数字，大小写字母至少一种"},],
      },
    }
  },
  methods:{
    verifyOld(val,flag=false){
      if(this.strIsNULL(val.value)&&flag)
      {
        val.tip=""
        return false
      }
      if(this.strIsNULL(val.value))
      {
        val.tip="请输入"
        return false
      }
      val.tip=''
      return true
    },
    verify(val,rules,flag=false){
      if(this.strIsNULL(val.value)&&flag){
        val.tip="请输入"
        return false
      }
      if(this.strIsNULL(val.value)){
        val.tip=""
        return false
      }

      for(let i=0;i<rules.length;i++){
        if(!this.strPattern(val.value,rules[i].pattern)){
          val.tip=rules[i].message
          return false
        }
      }
      val.tip=''
      return true
    },
    confirm(val,val2){
      if(val2.value===''){
        val2.tip=''
        return false
      }
      if(val.value===(val2.value)){
        val2.tip=''
        return true
      }else{
        val2.tip="两次密码不一致"
        return false
      }
    },
    clear(){
        this.oldPassWord={value:'',tip:''}
        this.newPassWord={value:'',tip:''}
        this.newPassWord2={value:'',tip:''}
    },
    ok(){
      const valid=this.verifyOld(this.oldPassWord)&&
                  this.verify(this.newPassWord,this.rules.newPassword)&&
                  this.confirm(this.newPassWord,this.newPassWord2)
       if(valid){
         this.api.post("/user/upDatePassWord",{userId:this.userId,oldPassWord:this.oldPassWord.value,newPassWord:this.newPassWord.value}).then(res=>{
           this.$message.success(res)
           this.clear()
         })
       }

    }
  }
}
</script>

<style scoped>
.account-main{
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-around;
}
.account-main-card{
  width: 40%;
}
.account-edit{
   width: 40%;
   height: fit-content;
   margin: 10px auto 0 auto;
}
.account-edit div {
   margin-top:15px;
}


.login_box {
  /* 相对定位 */
  position: relative;
  width: 100%;
}
.login_box input {
  /* 清除input框自带的边框和轮廓 */
  outline: none;
  border: none;
  width: 100%;
  padding: 10px 0;
  margin-bottom: 30px;
  color: #fff;
  font-size: 16px;
  border-bottom: 1px solid #fff;
  /* 背景颜色为透明色 */
  background-color: transparent;
}
.login_box label {
  position: absolute;
  top: 0;
  left: 0;
  padding: 10px 0;
  color: #fff;
  /* 这个属性的默认值是auto 默认是这个元素可以被点击 但是如果我们写了none 就是这个元素不能被点击 , 就好像它可见但是不能用 可望而不可即 */
  /* 这个就是两者的区别 */
  pointer-events: none;
  /* 加个过渡 */
  transition: all 0.5s;
}
.login_box label span {
  color: #f0443c;
  margin-left: 10px;
}
.login_box label svg {
  margin-right: 10px;
}
.login_box input:focus + label,
.login_box input:valid + label {
  top: -20px;
  color: #03e9f4;
  font-size: 12px;
}
.login_but{
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
  justify-items: center;
}
</style>
