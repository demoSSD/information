<template>
<div class="test">
  <div class="box"  :class="this.todo===false?'':'flipped'">
    <div class="login front">
      <svg-icon icon-class="logo" style="font-size: 60px;margin-bottom: 20px"/>
      <h2>用户登录</h2>
      <div class="login_box">
        <!-- required就是不能为空 必须在css效果中有很大的作用 -->
        <!-- 可以简写为required -->
        <input type="text" required v-model="username.value" @blur="verify(username,loginRules.username)"/>
        <label><svg-icon icon-class="user"/>用户名<span>{{username.tip}}</span></label>
      </div>
      <div class="login_box">
        <input type="password" required="required" v-model="password.value" @blur="verify(password,loginRules.password)"/>
        <label><svg-icon icon-class="password"/>密码<span>{{password.tip}}</span></label>
      </div>
      <div class="login_but">
        <DivButton2 label="注册" @click.native="toTest"/>
        <DivButton1 label="登陆" @click.native="handleLogin"/>
      </div>

    </div>
    <div class="login back">
      <svg-icon icon-class="logo" style="font-size: 60px;margin-bottom: 20px"/>
      <h2>用户注册</h2>
      <div class="login_box">
        <!-- required就是不能为空 必须在css效果中有很大的作用 -->
        <!-- 可以简写为required -->
        <input type="text" required v-model="reg_username.value" @blur="verify(reg_username,loginRules.username)"/>
        <label><svg-icon icon-class="user"/>设置用户名<span>{{reg_username.tip}}</span></label>
      </div>
      <div class="login_box">
        <input type="password" required="required" v-model="reg_password.value" @blur="verify(reg_password,loginRules.password)"/>
        <label><svg-icon icon-class="password"/>设置密码<span>{{reg_password.tip}}</span></label>
      </div>
      <div class="login_but">
        <DivButton2 label="返回" @click.native="toTest"/>
        <DivButton1 label="确定" @click.native="handleRegister"/>
      </div>

    </div>
  </div>

</div>
</template>

<script>
import DivButton1 from "../../components/button/DivButton1";
import DivButton2 from "../../components/button/DivButton2";
import {Base64} from "js-base64";
export default {
  name: "index",
  components: {DivButton2, DivButton1},
  data() {
    return {
      username:{value:'',tip:''},
      password:{value:'',tip:''},
      reg_username:{value:'',tip:''},
      reg_password:{value:'',tip:''},
      loginRules: {
        username: [{pattern:/^[0-9]{10}$/,message: "请输入10位数字用户名"}],
        password: [{pattern:/^.{6,30}$/,message: "请输入至少6位密码"},{pattern:/^[0-9a-zA-Z]{6,30}$/,message: "密码只能包含数字，大小写字母至少一种"},]
      },
      loading: false,
      todo:false,
    }
  },
  created() {
    this.init()
  },
  methods: {
    init(){
      let username =localStorage.getItem("username");
      if (username) {
        this.username.value = localStorage.getItem("username");
        this.password.value = Base64.decode(localStorage.getItem("password"));// base64解密
      }
    },
    handleLogin() {
      const valid=this.verify(this.username,this.loginRules.username,true)&&this.verify(this.password,this.loginRules.password,true)
      const loginForm={
        username: this.username.value,
        password: this.password.value
      }
        if (valid) {
          this.loading = true
          this.$store.dispatch('Login', loginForm).then(data => {
            this.loading = false
            localStorage.setItem('userId',loginForm.username)
            this.savePassWord()
            this.$router.push({path: '/'})
          }).catch(() => {
            this.loading = false
          })
        }
    },
    handleRegister() {
      const valid=this.verify(this.reg_username,this.loginRules.username,true)&&this.verify(this.reg_password,this.loginRules.password,true)
      const loginForm={
        userId: this.reg_username.value,
        password: this.reg_password.value
      }
      if (valid) {
        this.loading = true
        this.api.post("/user/newAddUser",loginForm).then(res=>{
          this.loading=false
          this.$message.success(res)
          this.reg_username.value=this.reg_password=''
          this.toTest()
        }).catch(err=>{
          this.loading=false
        })
      }
    },
    verify(val,rules,flag=false){
      if(this.strIsNULL(val.value)&&flag){
        val.tip="请输入"
        return false
      }
      if(this.strIsNULL(val.value))
        return false
      for(let i=0;i<rules.length;i++){
       if(!this.strPattern(val.value,rules[i].pattern)){
         val.tip=rules[i].message
         return false
       }
     }
      val.tip=''
      return true
    },
    change(){
      this.$router.replace('/register')
    },
    toTest(){
      this.todo=!this.todo;
    },
    savePassWord(){
        let password = Base64.encode(this.password.value); // base64加密
        localStorage.setItem("username", this.username.value);
        localStorage.setItem("password", password);
    },
  }
}
</script>

<style scoped>
* {
  /* 初始化 清除页面元素的内外边距 */
  padding: 0;
  margin: 0;
  /* 盒子模型 */
  box-sizing: border-box;
}
.test{
  display: flex;
  justify-content: center;
  align-items: center;
  /* 让页面始终占浏览器可视区域总高度 */
  height: 100vh;
  background: url("../../assets/background1.jpg") no-repeat;
}
.box{
  transform-style: preserve-3d;
  transition: transform 1s;
  width: 400px;
  height: 400px;
}

.front{

}
.back{
  transform: rotateY(180deg);
}
.flipped{
  transform: rotateY(180deg);
}
.login {
  position: absolute;
  backface-visibility: hidden;
  /* 弹性布局 让子元素称为弹性项目 */
  display: flex;
  /* 让弹性项目垂直排列 原理是改变弹性盒子的主轴方向 父元素就是弹性盒子 现在改变后的主轴方向是向下了 */
  flex-direction: column;
  /* 让弹性项目在交叉轴方向水平居中 现在主轴的方向是向下 交叉轴的方向是与主轴垂直 交叉轴的方向是向右 */
  align-items: center;
  width: 100%;
  height: auto;
  padding: 40px;
  background: rgba(29,29,31,0.6);
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.4);
  backdrop-filter: saturate(180%) blur(20px);
  border-radius: 5px;
}
.login h2 {
  color: #fff;
  margin-bottom: 30px;
}
.login .login_box {
  /* 相对定位 */
  position: relative;
  width: 100%;
}
.login .login_box input {
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
.login .login_box label {
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
.login .login_box label span {
  color: #f0443c;
  margin-left: 10px;
}
.login .login_box label svg {
  margin-right: 10px;
}
/* :focus 选择器是当input获得焦点是触发的样式 + 是相邻兄弟选择器 去找与input相邻的兄弟label */
/* :valid 选择器是判断input框的内容是否合法,如果合法会执行下面的属性代码,不合法就不会执行,我们刚开始写布局的时候给input框写了required 我们删掉看对比 当没有required的话input框的值就会被认为一直合法,所以一直都是下方的样式 ,但是密码不会,密码框内的值为空,那么这句话局不合法,required不能为空 当我们给密码框写点东西的时候才会执行以下代码*/
.login .login_box input:focus + label,
.login .login_box input:valid + label {
  top: -20px;
  color: #03e9f4;
  font-size: 12px;
}
.login .login_but{
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
  justify-items: center;
}

</style>
