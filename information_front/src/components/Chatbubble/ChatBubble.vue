<template>
  <div>
      <!-- 聊天面板-->
      <div style="margin: auto" class="fadeInDown" >
       <el-row v-if="!item.isUser">
        <el-col :span="2">
          <img  src="../../assets/picture/robat.png" alt=""  style="padding: 10px;"/>
        </el-col>
        <el-col :span="22">
          <div :id=itemId class="chatPop1">
            <span class="chatPop1-span1" v-if="item.isList">
               <h4 >您要找的是不是：</h4>
               <li  v-for="(con,index) in item.contain" :key="index" class="ans" @click="$emit('goToQuestion',con)">{{con}}</li>
           </span>
            <span class="chatPop1-span2" v-if="!item.isList">
               <p style="margin: 0" v-html="item.contain" />
           </span>
          </div>
        </el-col>
      </el-row>
       <el-row v-if="item.isUser">
        <el-col :span="22">
        <div class="chatPop2" >
          <span class="textStyle"  style="font-size: 14px" :id="itemId">

          </span>
        </div>
        </el-col>
        <el-col :span="2">
          <img src="../../assets/picture/user.png" alt=""  style="padding: 10px"/>
        </el-col>
      </el-row>
      </div>

      <!-- 图片弹窗 -->
      <div id="myModal" class="modal">

      <!-- 关闭按钮 -->
      <span class="close"  onclick="document.getElementById('myModal').style.display='none'">&times;</span>

      <!-- 弹窗内容 -->
      <img class="modal-content" id="img01">

      <!-- 文本描述 -->
      <div id="caption"></div>
    </div>
  </div>
</template>

<script >
export default {
  name: "chatBubble",
  data() {
    return {
      isMobile:true,
    }
  },
  created() {
  },
  mounted() {
    this.initUserContain()
    this.initRobatContain()
  },
  props: {
    itemId:"",
    item:{},
  },
  methods:{
    initUserContain(){
      if(!this.item.isUser)
        return;
      const tem1=this.item.contain;
      let tem2="";
      var flag=false,index=0;
      for(var i=0;i<tem1.length;i++)
      {
        if(tem1[i]==="【")
        {
          flag=true;
          index=i;
        }
        if(tem1[i]==="】")
        {
           flag=false;
           var str=tem1.substring(index+1,i);
           var html=`<img style="vertical-align: sub;height: 24px;width: 24px" class='${str}'  alt=""/>`;
           tem2+=html;
        }
        if(!flag&&tem1[i]!=="】")
          tem2=tem2+tem1[i];
      }
      document.getElementById(this.itemId).innerHTML=tem2;
     },
    initRobatContain(){
      if(this.item.isUser||this.item.isList)
        return;
      this.imageBind()
    },
    //为img标签绑定预览事件
    imageBind(){
      this.$nextTick(()=>{
        // 获取弹窗
        var modal = document.getElementById('myModal');
        // 获取图片插入到弹窗 - 使用 "alt" 属性作为文本部分的内容
        var img = document.getElementById(this.itemId).getElementsByTagName("img");
        var modalImg = document.getElementById("img01");
        var captionText = document.getElementById("caption");

        for (var i=0;i<img.length;i++)
        {
          img[i].onclick= function(){

            modal.style.display = "block";
            modalImg.src = this.src;
            captionText.innerHTML = this.alt;
          }
        }
        // 获取 <span> 元素，设置关闭按钮
        var span = document.getElementsByClassName("close")[0];
        // 当点击 (x), 关闭弹窗
        span.onclick = function() {
          modal.style.display = "none";
        }
      })

    },




  },
}
</script>

<style scoped>

.textStyle{
  line-height: 23px;
  min-width:25px;
  max-width:400px;
  text-align: left;
  min-height:30px;
}
.chatPop1{
  padding: 10px 10px 10px 0;
  color: white;
}
.chatPop1-span1{
  line-height: 23px;
  max-width:400px;
  text-align: left;
  min-height:30px;
}
.chatPop1-span1 li{
  font-size: 15px;
  margin: 2px;
}
.chatPop1-span1 li:hover{
  color: #3b990d;
  font-weight: bold;
}
.chatPop1-span1 h4{
  margin: 0 0 5px;
}
.chatPop1:hover span {
  background-color: rgba(250, 250, 250, 0.15);
}
.chatPop1:hover span:before {
  border-right: 8px solid rgba(250, 250, 250, 0.15);
}
.chatPop1 span {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.09);
  padding:10px;
  display: inline-block;
  border-radius: 5px;
  position: relative;
  margin-left: 0;
  max-width: 600px;
}
.chatPop1 span:before{
  content: '';
  border: 8px solid #ffffff00;
  border-right: 8px solid rgba(255, 255, 255, 0.09);
  /*background-color: rgba(255, 255, 255, 0.09);*/
  position: absolute;
  top: 8px;
  left: -16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}


.chatPop2{
  padding: 10px 10px 10px 0;
}
.chatPop2:hover span{
  background-color: rgba(31,125,6, 0.82);
}
.chatPop2:hover span:after{
  border-right: 8px solid rgba(31,125,6, 0.82);
}
.chatPop2 span {
  word-break: break-all;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: #1ead28d9;
  padding: 10px;
  display: inline-block;
  border-radius: 5px;
  position: relative;
  /*max-width: 95%;*/
  float: right;
  color: #fff;
}
.chatPop2 span:after {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  content: '';
  border: 8px solid #ffffff00;
  border-right: 8px solid #1ead28d9;
  position: absolute;
  top: 8px;
  right: -16px;
  transform: rotateY(180deg)
}




/* 弹窗背景 */
.modal {
  display: none; /* Hidden by default */
  position: fixed; /* Stay in place */
  z-index: 1; /* Sit on top */
  padding-top: 100px; /* Location of the box */
  left: 0;
  top: 0;
  width: 100%;
  /* Full width */
  height: 100vh; /* Full height */
  overflow: auto; /* Enable scroll if needed */
  background-color: rgb(0,0,0); /* Fallback color */
  background-color: rgba(0,0,0,0.9); /* Black w/ opacity */
}

/* 图片 */
.modal-content {
  margin: auto;
  display: block;
  width: 80%;
  max-width: 700px;
}

/* 文本内容 */
#caption {
  margin: auto;
  display: block;
  width: 80%;
  max-width: 700px;
  text-align: center;
  color: #ccc;
  padding: 10px 0;
  height: 150px;
}

/* 添加动画 */
.modal-content, #caption {
  -webkit-animation-name: zoom;
  -webkit-animation-duration: 0.6s;
  animation-name: zoom;
  animation-duration: 0.6s;
}

@-webkit-keyframes zoom {
  from {-webkit-transform:scale(0)}
  to {-webkit-transform:scale(1)}
}

@keyframes zoom {
  from {transform:scale(0)}
  to {transform:scale(1)}
}

/* 关闭按钮 */
.close {
  position: absolute;
  bottom: 15px;
  right: 35px;
  color: #f1f1f1;
  font-size: 40px;
  font-weight: bold;
  transition: 0.3s;
  z-index: 99;
}

.close:hover,
.close:focus {
  color: #bbb;
  text-decoration: none;
  cursor: pointer;
}

/* 小屏幕中图片宽度为 100% */
@media only screen and (max-width: 700px){
  .modal-content {
    width: 100%;
  }
}
.chatPop1/deep/ ul{
  padding-left: 20px;
}
.chatPop1/deep/ ol{
  padding-left: 20px;
}
.chatPop1/deep/ img{
  max-width: 300px;
  min-width: 50px;
}


</style>
