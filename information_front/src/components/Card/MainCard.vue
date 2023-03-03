<template>
 <div class="app">
   <div class="left" >
     <div class="left-user">
       <img  alt="" src="../../assets/logo.png">
       <div>湖工校园交流平台</div>
     </div>
     <div class="left-tip1">
        <div>
          <i>“</i>
          倾听对方的任何一种意见或议论就是尊重，因为这说明我们认为对方有卓见、口才和聪明机智，反之，打瞌睡、走开或乱扯就是轻视。
          <span style="color: rgba(255,255,255,0.51);font-size: 20px">——霍布斯（英）</span>
          <i>”</i>
        </div>
     </div>
     <div class="left-icon">
       <svg-icon icon-class="QQ" class="hover1" />
       <svg-icon icon-class="github-fill"  class="hover1"/>
       <svg-icon icon-class="email" class="hover1"/>
       <svg-icon icon-class="weixin" class="hover1"/>
     </div>
   </div>
   <div class="right">
     <div class="right-tip2">
        <div class="right-tip2-card text">
          ”我喜欢风的透彻，像能望穿的快乐。”
        </div>
        <div class="right-tip2-card">
          <div class="right-tip2-card-date">{{date}}  {{week}}</div>
          <div class="right-tip2-card-time">{{time}}</div>
          <div class="right-tip2-card-weather">
            <span>天气  晴转多云</span>
            <span><svg-icon icon-class="weather_tempeature" /> 22~25</span>
          </div>
       </div>
     </div>
     <div class="right-module">
       <div class="right-module-card hover1" @click="goTo('/login')">
         <svg-icon icon-class="user" />
         <span>个人中心</span>
       </div>
       <div class="right-module-card hover1" @click="goTo('/qaCommunity')">
         <svg-icon icon-class="ques_com" />
         <span>问答社区</span>
       </div>
       <div class="right-module-card hover1" @click="goTo('/voiceText')">
         <svg-icon icon-class="voiceText" />
         <span>语音转文字</span>
       </div>
       <div class="right-module-card hover2">
         <svg-icon icon-class="network" />
         <span>资源网址集</span>
       </div>
       <div class="right-module-card hover2">
         <svg-icon icon-class="await" />
         <span>等待上线</span>
       </div>
       <div class="right-module-card hover2">
         <svg-icon icon-class="more" />
         <span>更多功能</span>
       </div>
     </div>
   </div>
 </div>
</template>

<script>

export default {
  name: "MainCard",
  components: {},
  data() {
    return {
      time: "",
      date: "",
      week: "",
      timerHelper: false
    };
  },
  mounted() {
    this.timerHelper = setInterval(this.updateTime, 1000);
  },
  beforeDestroy() {
    if (this.timerHelper) {
      clearInterval(this.timerHelper);
      this.timerHelper = false;
    }
  },
  methods: {
    zeroPadding(num, digit) {
      let zero = "";
      for (let i = 0; i < digit; i++) {
        zero += "0";
      }
      return (zero + num).slice(-digit);
    },
    updateTime() {
      const week = [
        "星期日",
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六"
      ];
      const cd = new Date();
      this.time =
        this.zeroPadding(cd.getHours(), 2) +
        ":" +
        this.zeroPadding(cd.getMinutes(), 2) +
        ":" +
        this.zeroPadding(cd.getSeconds(), 2);
      this.date =
        this.zeroPadding(cd.getFullYear(), 4) +
        " 年 " +
        this.zeroPadding(cd.getMonth() + 1, 2) +
        " 月 " +
        this.zeroPadding(cd.getDate(), 2) +
        " 日 ";

      this.week = week[cd.getDay()];
    },
    goTo(val){
      this.$router.push({
        path:val,
      })
    }
  }
}
</script>

<style lang="scss" scoped>
/*pc端*/
@media only screen and (min-width: 1200px) {
  .app{
    display: flex;
    height: 520px;
    width: 1100px;
    justify-content: space-between;
    background: none;
  }
  .left{
    width: 40%;
    height: 100%;
  }
  .left-user{
    margin-top: 100px;
    min-height: 150px;
    width: 100%;
    display: flex;
  }
  .left-user img{
    width: 100px;
    height: 100px;
    /*margin: 0 20px;*/
    margin-right: 20px;
    overflow: hidden;
    box-shadow: 4px 4px 3px rgba(0, 0 ,0 ,0.1);
  }
  .left-user div{
    line-height:100px;
    font-family: 钢笔小楷,sans-serif;
    font-weight: bold;
    font-size: 44px;
  }
  .left-tip1{
    min-height: 170px;
    font-size: 22px;
    line-height: 30px;
    font-family: 华文行楷,serif;
    background: rgba(29,29,31,0.6);
    backdrop-filter: saturate(180%) blur(20px);
    padding: 20px;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    flex-direction: column;
  }
  .left-tip1 i{
    font-weight: bold;
    font-size: 30px;
    font-family: 方正舒体,sans-serif;
    color: rgba(255, 255, 255, 0.46);
  }
  .left-icon{
    padding: 20px 5px;
    display: flex;

  }
  .left-icon svg{
    margin: 0 5px;
    width: 28px;
    height: 25px;
  }
  .right{
    width: 50%;
    height: 100%;
    /*background: rgba(255, 255, 255, 0.38);*/
  }
  .right-tip2{
    margin-top: 50px;
    display: flex;
    justify-content: space-between;
  }

  .right-tip2-card{
    background: rgba(29,29,31,0.6);
    backdrop-filter: saturate(180%) blur(20px);
    padding: 20px;
    text-align: center;
    height: 140px;
    width: 262px;
    border-radius: 5px;
  }
  .right-tip2-card-date{
    font-size: 15px;
    line-height: 30px;
  }
  .right-tip2-card-time{
    font-size: 40px;
    font-weight: bold;
    font-family: 隶书,sans-serif;
  }
  .right-tip2-card-weather{
    display: flex;
    margin-top: 5px;
    font-size: 13px;
    line-height: 30px;
  }
  .right-tip2-card-weather span{
    margin: 0 20px;
  }
  .right-tip2-card-weather svg{
    width: 20px;
    height: 20px;
  }
  .right-module{
    margin-top: 50px;
    display: grid;
    justify-content: space-between;
    grid-template-rows: repeat(2,100px);
    grid-template-columns: repeat(3,30%);
    grid-column-gap: 5%;
    grid-row-gap: 20px;
  }
  .right-module-card{
    background: rgba(29,29,31,0.6);
    backdrop-filter: saturate(180%) blur(20px);
    padding: 10px;
    text-align: center;
    width: 100%;
    height: 100px;
    border-radius: 5px;
    display: flex;
    align-items: center;
    /*justify-content: space-around;*/
    /*flex-direction: column;*/
  }
  .right-module-card svg{
    margin: 0 10px;
    width: 35px;
    height: 35px;
  }
  .text{
    display: flex;
    align-items: center;
    justify-content: space-around;
    flex-direction: column;
    font-family: 方正姚体,serif;
    font-size: 18px;
    line-height: 30px;
  }
  .hover1:hover{
    background: linear-gradient(220.55deg, #24CFC5 0%, #001C63 100%);
    font-weight: bold;
    border-radius: 5px;
  }
  .hover2:hover{
    background: linear-gradient(220.55deg, #FFC328 0%, #E20000 100%);
    font-weight: bold;
  }
}
/*pad端*/
@media only screen and (min-width: 768px) and(max-width: 1199px) {
  .app{
    display: flex;
    height: 520px;
    width: 1100px;
    justify-content: space-between;
    background: none;
  }
  .left{
    width: 40%;
    height: 100%;
  }
  .left-user{
    margin-top: 100px;
    height: 150px;
    width: 100%;
    /*居中*/
    display: flex;
    /*justify-content: center;!*垂直居中*!*/
    /*align-items: center;!*水平居中*!*/
  }
  .left-user img{
    width: 100px;
    height: 100px;
    /*margin: 0 20px;*/
    margin-right: 20px;
    overflow: hidden;
    box-shadow: 4px 4px 3px rgba(0, 0 ,0 ,0.1);
  }
  .left-user div{
    line-height:100px;
    font-family: 钢笔小楷,sans-serif;
    font-weight: bold;
    font-size: 44px;
  }
  .left-tip1{
    min-height: 170px;
    font-size: 22px;
    line-height: 30px;
    font-family: 华文行楷,serif;
    background: rgba(29,29,31,0.6);
    backdrop-filter: saturate(180%) blur(20px);
    padding: 20px;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    flex-direction: column;
  }
  .left-tip1 i{
    font-weight: bold;
    font-size: 30px;
    font-family: 方正舒体,sans-serif;
    color: rgba(255, 255, 255, 0.46);
  }
  .left-icon{
    padding: 20px 5px;
    display: flex;

  }
  .left-icon svg{
    margin: 0 5px;
    width: 28px;
    height: 25px;
  }
  .right{
    width: 50%;
    height: 100%;
    /*background: rgba(255, 255, 255, 0.38);*/
  }
  .right-tip2{
    margin-top: 50px;
    display: flex;
    justify-content: space-between;
  }

  .right-tip2-card{
    background: rgba(29,29,31,0.6);
    backdrop-filter: saturate(180%) blur(20px);
    padding: 20px;
    text-align: center;
    height: 140px;
    width: 262px;
    border-radius: 5px;
  }
  .right-tip2-card-date{
    font-size: 15px;
    line-height: 30px;
  }
  .right-tip2-card-time{
    font-size: 40px;
    font-weight: bold;
    font-family: 隶书,sans-serif;
  }
  .right-tip2-card-weather{
    display: flex;
    margin-top: 5px;
    font-size: 13px;
    line-height: 30px;
  }
  .right-tip2-card-weather span{
    margin: 0 20px;
  }
  .right-tip2-card-weather svg{
    width: 20px;
    height: 20px;
  }
  .right-module{
    margin-top: 50px;
    display: grid;
    justify-content: space-between;
    grid-template-rows: repeat(2,100px);
    grid-template-columns: repeat(3,30%);
    grid-column-gap: 5%;
    grid-row-gap: 20px;
  }
  .right-module-card{
    background: rgba(29,29,31,0.6);
    backdrop-filter: saturate(180%) blur(20px);
    padding: 10px;
    text-align: center;
    width: 100%;
    height: 100px;
    border-radius: 5px;
    display: flex;
    align-items: center;
    /*justify-content: space-around;*/
    /*flex-direction: column;*/
  }
  .right-module-card svg{
    margin: 0 10px;
    width: 35px;
    height: 35px;
  }
  .text{
    display: flex;
    align-items: center;
    justify-content: space-around;
    flex-direction: column;
    font-family: 方正姚体,serif;
    font-size: 18px;
    line-height: 30px;
  }
  .hover1:hover{
    background: linear-gradient(220.55deg, #24CFC5 0%, #001C63 100%);
    font-weight: bold;
    border-radius: 5px;
  }
  .hover2:hover{
    background: linear-gradient(220.55deg, #FFC328 0%, #E20000 100%);
    font-weight: bold;
  }
}
/*手机端*/
@media only screen and (max-width: 767px) {
  .app{
    overflow-y: auto;
    width: 100%;
    display: grid;
    grid-template-columns: 100%;
  }
  .left{
    padding: 40px;
    width: 100%;
    height: 520px;
  }
  .left-user{
    margin-top: 100px;
    height: 150px;
    width: 100%;
    display: grid;
    grid-template-columns:100px auto ;
    grid-template-rows: 100px;
    grid-column-gap: 20px;
    justify-items: center;
    justify-content: space-around;
  }
  .left-user img{
    width: 100%;
    height: 100%;
    overflow: hidden;
    box-shadow: 4px 4px 3px rgba(0, 0 ,0 ,0.1);
  }
  .left-user div{
    display: flex;
    align-items: center;
    justify-content: space-around;
    font-family: 钢笔小楷,sans-serif;
    font-weight: bold;
    font-size: 44px;
  }
  .left-tip1{
    min-height: 170px;
    font-size: 22px;
    line-height: 30px;
    font-family: 华文行楷,serif;
    background: rgba(29,29,31,0.6);
    backdrop-filter: saturate(180%) blur(20px);
    padding: 20px;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    flex-direction: column;
  }
  .left-tip1 i{
    font-weight: bold;
    font-size: 30px;
    font-family: 方正舒体,sans-serif;
    color: rgba(255, 255, 255, 0.46);
  }
  .left-icon{
    padding: 20px 5px;
    display: flex;

  }
  .left-icon svg{
    margin: 0 5px;
    width: 28px;
    height: 25px;
  }
  .right{
    width: 100%;
    display: grid;
    grid-template-columns: 100%;
    grid-template-rows:140px  auto;
    grid-row-gap: 40px;
    padding: 40px;
  }
  .right-tip2{
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-template-rows: 140px;
    grid-column-gap: 40px;
    //justify-content: space-between;
  }

  .right-tip2-card{
    background: rgba(29,29,31,0.6);
    backdrop-filter: saturate(180%) blur(20px);
    padding: 20px;
    text-align: center;
    height: 100%;
    width: 100%;
    border-radius: 5px;
  }
  .right-tip2-card-date{
    font-size: 15px;
    line-height: 30px;
  }
  .right-tip2-card-time{
    font-size: 40px;
    font-weight: bold;
    font-family: 隶书,sans-serif;
  }
  .right-tip2-card-weather{
    display: flex;
    margin-top: 5px;
    font-size: 13px;
    line-height: 30px;
  }
  .right-tip2-card-weather span{
    margin: 0 20px;
  }
  .right-tip2-card-weather svg{
    width: 20px;
    height: 20px;
  }
  .right-module{
    margin-top: 50px;
    display: grid;
    justify-content: space-between;
    grid-template-rows: repeat(2,100px);
    grid-template-columns: repeat(3,30%);
    grid-column-gap: 5%;
    grid-row-gap: 20px;
  }
  .right-module-card{
    background: rgba(29,29,31,0.6);
    backdrop-filter: saturate(180%) blur(20px);
    padding: 10px;
    text-align: center;
    width: 100%;
    height: 100px;
    border-radius: 5px;
    display: flex;
    align-items: center;
    /*justify-content: space-around;*/
    /*flex-direction: column;*/
  }
  .right-module-card svg{
    margin: 0 10px;
    width: 35px;
    height: 35px;
  }
  .text{
    display: flex;
    align-items: center;
    justify-content: space-around;
    flex-direction: column;
    font-family: 方正姚体,serif;
    font-size: 18px;
    line-height: 30px;
  }
  .hover1:hover{
    background: linear-gradient(220.55deg, #24CFC5 0%, #001C63 100%);
    font-weight: bold;
    border-radius: 5px;
  }
  .hover2:hover{
    background: linear-gradient(220.55deg, #FFC328 0%, #E20000 100%);
    font-weight: bold;
  }
}

</style>
