<template>
  <div :class="{app:true,'app-no-top':!isTop}">
    <div class="nav">
      <ul class="item">
        <li><span> <router-link to="/home">首页</router-link></span></li>
        <li><span> <router-link to="/">个人中心</router-link></span></li>
        <li><span> <router-link to="/qaCommunity">问答社区</router-link></span></li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {
      isTop:true,
    }
  },
  mounted () {
    window.addEventListener('scroll', this.scroll,true)
  },
  methods:{
    scroll(e) {
     let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      this.isTop = e.target.scrollTop === 0;
    }
  }
}
</script>

<style scoped>
.app-no-top{
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
  transition: background-color 1s 0s ease-in;
}
.app{
  padding: 0;
  font-family: sans-serif;
  height: 60px;
  z-index:9;
  position: fixed;
  width: 100%;
  top:0;
  transition: background-color 1s 0s ease-in;
}
.nav{
  width: 100%;
  height: 100%;
}
.item{
  position: fixed;/*固定在页面顶部*/
  right:20px;
  margin: 0;
  padding: 0;
  display: flex;/*弹性布局，使li排成一行*/
}
.item li{
  list-style: none;
}
.item li span{
  position: relative;
  display: block;
  padding: 10px 20px;
  margin: 20px 0;
  text-decoration: none;
  text-transform: uppercase;/*将字符转为大写*/
  color: rgba(255, 255, 255, 0.99);
  font-weight: bold;
  /* transition: 0.5s; */
}
.item li span:hover{
  color: #0e68e2;
}
.item li span:before{
  /*a:before，在a链接之前添加新内容，这里添加上下边框*/
  content:'';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-top:1px solid rgba(255, 255, 255, 0.99);
  border-bottom:1px solid rgba(255, 255, 255, 0.99);
  transform: scaleY(2);/*拉长边框两倍*/
  opacity: 0;/*边距不显示*/
  transition: 0.5s;
  z-index: -1;
}
.item li span:hover:before{
  transform: scaleY(1);/*拉长边框两倍*/
  opacity:1;
}
.item li span:after{
  content:'';
  position: absolute;
  top: 1px;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.99);
  transform: scale(0);
  transition: 0.5s;
  z-index: -1;
}
.item li span:hover:after{
  transform: scale(1);
}

</style>
