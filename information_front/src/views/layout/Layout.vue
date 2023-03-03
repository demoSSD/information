<template>

<div class="layout">
  <div class="main">
    <div class="top">
      <div class="top-avatar">
        <img class="top-avatar-img" :src="'/api'+userInfo.url" alt='..'/>
      </div>
      <div class="top-content">
        <div class="top-content-row1">
          <span class="top-content-row1-name">{{userInfo.name}}</span>
          <router-link :replace="true" to="/user/userSetting"  class="side-ul-li-a">
              <Button class="top-content-row1-button" >编辑</Button>
          </router-link>
        </div>
        <div class="top-content-row2">
          <span class="top-content-row2-id">用户ID: {{userInfo.id}}</span>
        </div>
        <div class="top-content-row3">
          <span class="top-content-row3-icon"><svg-icon icon-class="sign"></svg-icon></span>
          <span class="top-content-row3-sign" > {{userInfo.sign}}</span>
        </div>
        <div class="top-content-row4">
          <span class="top-content-row4-icon"><svg-icon icon-class="local"></svg-icon></span>
          <span class="top-content-row4-sign" >IP属地：湖北</span>
        </div>
        <div class="top-content-row5">
          <div class="top-content-row5-data">
            <span class="top-content-row5-data-value">{{userInfo.replys}}</span>
            <span class="top-content-row5-data-label">回复</span>
          </div>
          <div class="top-content-row5-data">
            <span class="top-content-row5-data-value">{{userInfo.questions}}</span>
            <span class="top-content-row5-data-label">提问</span>
          </div>
          <div class="top-content-row5-data">
            <span class="top-content-row5-data-value">{{userInfo.approve}}</span>
            <span class="top-content-row5-data-label">获赞</span>
          </div>

        </div>
      </div>
    </div>
    <div class="grid">
      <div class="side">
        <div class="side-header">个人中心</div>
        <ul class="side-ul" >

        <div v-if="item.path==='/user'"  v-for="item in permission_routers" >
          <li v-for="child in item.children" class="side-ul-li" >
            <router-link :replace="true" :to="item.path+'/'+child.path" :key="child.name" class="side-ul-li-a">
              <span class="side-ul-li-a-icon" ><svg-icon :icon-class="child.meta.icon" style="color: white"/></span>
              <span class="side-ul-li-a-label">{{child.meta.title}}</span>
            </router-link>
          </li>
        </div>


          <li class="side-ul-li"  style="border-top: 1px solid #f0f0f0">
            <router-link  :replace="true" to="/home"  class="side-ul-li-a">
              <span class="side-ul-li-a-icon"><svg-icon icon-class="home" /></span>
              <span class="side-ul-li-a-label">返回首页</span>
            </router-link>
          </li>
          <li class="side-ul-li"  >
            <a href='#' class="side-ul-li-a" @click="outLogin">
              <span class="side-ul-li-a-icon"><svg-icon icon-class="logout" /></span>
              <span class="side-ul-li-a-label">退出登录</span>
            </a>
          </li>
        </ul>
      </div>
      <div class="show">
        <div class="show-header">
          <span>{{this.levelList[1].name}}</span>
        </div>
        <div class="show-content">
          <router-view/>
        </div>
      </div>
    </div>
  </div>
</div>



</template>

<script>
import { Navbar, Sidebar, AppMain} from '@/views/layout/components'
import {mapGetters} from "vuex";
import Header from "../../components/Header/Header";
export default {
  name: 'layout',
  components: {
    Header,
    Navbar,
    Sidebar,
    AppMain,
  },
  data() {
    return {
      userId:localStorage['userId'],
      levelList: null,
      userInfo:{id:'',name:'',sign:'',url:'',sex:1,approve:0,questions:0,replys:0},
    }
  },
  created() {
    this.getBreadcrumb()
    this.getUserInfo()
  },
  watch: {
    $route() {
      this.getBreadcrumb()
    }
  },
  computed: {
    ...mapGetters([
      'permission_routers',
      'sidebar'
    ]),
    sidebar() {
      return this.$store.state.app.sidebar
    }
  },
  methods:{
    getUserInfo(){
      this.api.post("/user/getUserInfo",{userId:this.userId}).then(res=>{
        this.userInfo=res
        // this.userInfo.id=res.id
        // this.userInfo.name=res.name
        // this.userInfo.sex=res.sex
        // this.userInfo.sign=res.sign
        // this.userInfo.url=res.url
      })
    },
    goto(val){
      this.$router.replace({
        path:val,
      })
    },
    outLogin(){
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    },
    getBreadcrumb() {
      let matched = this.$route.matched.filter(item => item.name)
      const first = matched[0]
      if (first && first.name !== 'dashboard') {
        matched = [{ path: '/dashboard', meta: { title: 'Dashboard' }}].concat(matched)
      }
      this.levelList = matched
    }
  }
}
</script>

<style scoped>
.layout{
  background: url("../../assets/background1.jpg") no-repeat;
  height: 100vh;
  width: 100%;
  background-size: cover;
  position: fixed;
  color: white;
  overflow-y: auto;
}
.main{
  width: 80%;
  margin:auto;
  padding: 30px 0;
}
.top{
  padding: 20px 50px 24px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  margin-bottom: 20px;
  /*background-color: #fff;*/
  border-radius: 4px;

  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
}
.top-avatar{
  width: 120px;
  height: 120px;
  margin-right: 24px;
  flex-shrink: 0;
  display: inline-block;
  position: relative;
}
.top-avatar-img{
  width: 100%;
  height: 100%;
  border-radius: 50%;
  /*border: 1px solid #ebebeb;*/
  vertical-align: top;
  box-shadow: rgba(0, 0 ,0,0.1) 0px 2px 12px 0px;
}
.top-content{
  -webkit-box-flex: 1;
  flex-grow: 1;
  height: 100%;
}
.top-content-row1{
  height: 32px;
  display: flex;
  -webkit-box-pack: justify;
  justify-content: space-between;
}
.top-content-row1-name{
  vertical-align: middle;
  font-size: 16px;
  line-height: 32px;
  font-weight:bold;
}
.top-content-row1-button{
  vertical-align: middle;
  background-color: #4bbde2;
  border-radius: 5px;
  color: white;
}
.top-content-row1-button:hover{
  background-color: #5dcaad;
}
.top-content-row2{
  display: flex;
  -webkit-box-align: center;
  align-items: center;
}
.top-content-row2-id{
  font-size: 12px;
  color: #ccc;
}
.top-content-row3{
  display: flex;
  margin-top: 8px;
  color: #666;
  line-height: 18px;
}
.top-content-row3-icon{

}
.top-content-row3-sign{
  margin: 0 10px;
  line-height: 18px;
  font-size: 14px;
  color: white;
}
.top-content-row4{
  display: flex;
  margin-top: 8px;
  color: #666;
  line-height: 18px;
}
.top-content-row4-icon{

}
.top-content-row4-sign{
  margin: 0 10px;
  line-height: 18px;
  font-size: 14px;
  color: white;
}
.top-content-row5{
  display: flex;
  padding-top: 19px;
  -webkit-box-align: center;
  align-items: center;
  align-self: center;
  flex-shrink: 0;
  margin-top: 20px;
  border-top: 1px solid #f0f0f0;
}
.top-content-row5-data{
  display: inline-block;
  padding-right: 40px;
  align-items: center;
  height: 23px;

}
.top-content-row5-data-value{
  margin-left: 10px;
  line-height: 23px;
  color: white;
}
.top-content-row5-data-label{
  color: white;
  font-size: 20px;
  line-height: 23px;
}
.grid{
  display: grid;
  grid-template-columns: 20% 77%;
  /*grid-template-rows: 500px;*/
  grid-auto-rows: minmax(500px, auto);
  grid-column-gap: 3%;
}
.side{
  min-height: 400px;
  float: left;
  border-radius: 4px;
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
}
.side-header{
  width: 100%;
  padding: 0 30px;
  line-height: 50px;
  border-bottom: 1px solid #ebebeb;
  font-size: 16px;
}
.side-ul{
  padding: 0 30px;
  list-style: none;
}
/*.side-ul-li:hover/deep/.side-ul-li-a-label .side-ul-li-a-icon{*/
/*  color: #5dcaad;*/
/*}*/
.side-ul-li{
  list-style: none;
}
.side-ul-li-a{
  padding: 0 20px;
  line-height: 50px;
  font-size: 16px;
  color: #666;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  cursor: pointer;
}
.side-ul-li-a-icon{
  margin-right: 15px;
}
.side-ul-li-a-label{
  color: white;
}
li:hover span{
  color: #5dcaad;
  font-weight: bold;
  font-size: 20px;
}

.show{
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
  float: right;
  border-radius: 4px;
  height: 100%;
}
.show-header{
  height: 50px;
  width: 100%;
  padding: 0 30px;
  line-height: 50px;
  border-bottom: 1px solid #ebebeb;
  font-size: 16px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
}
.show-content{

  height: 420px;
  width: 100%;
  padding: 0px;
  /*overflow-y:scroll;*/
  overflow:auto;
}
</style>
