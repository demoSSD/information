<template>
  <div class="all">
      <Header/>
      <div class="main">
         <div class="main-header">
            <router-link :replace="true" :to="getRoutes.path+'/'+item.path" :key="item.path" v-for="(item,index) in getRoutes.children" >
              <span :class="{select:index===selected}" @click="change(index)">
                <svg-icon :icon-class="item.meta.icon"/>{{item.meta.title}}
              </span>
            </router-link>
            <div class="search">
                  <el-input placeholder="请输入内容" v-model="input" :clearable="true">
                    <i slot="suffix" class="el-input__icon el-icon-search"/>
                  </el-input>
             </div>
         </div>
         <div class="main-show">
            <router-view/>
         </div>
      </div>
  </div>
</template>

<script>
import Header from "../../components/Header/Header";
import {mapGetters} from "vuex";
export default {
  name: "qaCommunity",
  components: {Header},
  computed:{
    ...mapGetters([
      'permission_routers',
    ]),
    getRoutes(){
      let routes=this.permission_routers.filter(item=>item.path==='/qaCommunity')
      return routes[0]
    }
  },
  data() {
    return {
       input:'',
       selected: 0,
    }
  },
  created() {

  },
  methods:{

       getSelected(){
         let p_routes=this.getRoutes
         let c_routes=this.getRoutes.children
         c_routes.map((item,index)=>{
           if(p_routes.path+'/'+item.path===this.$route.path)
             this.selected=index
         })
       },
       change(index){
         this.selected=index
       }
  },
}
</script>

<style scoped>
.all{
  background: url("../../assets/background1.jpg") no-repeat;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
  color: white;
  overflow-y: auto;
}
.main{
   padding: 5px;
   margin: 80px auto;
   width: 80%;
   min-height: 600px;
   color: white;
   border-radius: 5px;
   background: rgba(29,29,31,0.6);
   backdrop-filter: saturate(180%) blur(20px);
}
.main-header{
  width: 100%;
  height: 50px;
  border-bottom:solid 1px rgba(255, 255, 255, 0.64);
  display: grid;
  grid-template-columns: repeat(8,9%) 20%;
  grid-auto-rows: 50px;
  grid-column-gap: 1%;
}
.main-header  svg{
  margin: 0 6px;
}
.main-header span{
  width: 100%;
  height: 100%;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.5s 0s linear;
  box-shadow: 0 2px 4px RGBA(0, 0, 0, .12), 0 0 6px RGBA(0, 0, 0, .04);
}
.main-header span:hover{
  background:#00A92A7A;
  color: white;
  font-weight: bold;
  transition: background-color 1s 0s linear;
  border-radius: 3px;
}
.search{
  display: flex;
  justify-content: center;
  align-items: center;
  grid-row-start: 1;
  grid-column-start: 9;
  grid-row-end: 1;
  grid-column-end: 9;
}
.search el-input{
  width: 80%;
}
.search /deep/ .el-input__inner{
  padding: 0;
  background: #0000ff00;
  border: none;
  color: rgba(255, 255, 255, 0.64);
  border-bottom: 1px solid white;
  border-radius: 0px;
  transition: color 1s 0s ease-in;
}
.search:hover /deep/ .el-input__inner{
  background: #0000ff00;
  color: white;
  transition: color 1s 0s ease-in;
}
.main-show{
  width: 100%;
  padding: 10px;
}
.select{
  box-shadow: none;
  background: #45be09cf;
  color: white;
  font-weight: bold;
  transition: background-color 1s 0s linear;
  border-radius: 3px;
}
</style>
