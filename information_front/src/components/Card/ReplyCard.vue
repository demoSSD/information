<template>
  <router-link :to="{path:'/questionPage', query:{id:reply.question_id}}" target="_blank">
    <div class="replyCard-main scale-in-center">
     <div class="replyCard-main-left">
     <div :class="{'replyCard-main-left-value':true,'replyCard-main-accept':reply.accept===1}" ><svg-icon icon-class="corret"/></div>
     <div :class="{'replyCard-main-left-label':true,'replyCard-main-accept':reply.accept===1}" >{{reply.accept===1?'已采纳':'未采纳'}}</div>
   </div>
   <div class="replyCard-main-right">
       <div class="replyCard-main-right-content" >
         {{reply.content.replace(/<\/?.+?\/?>/g,'')}}
       </div>
       <div class="replyCard-main-right-detail">
         <div class="replyCard-main-right-detail-icon" ><svg-icon icon-class="approve"/><span>{{reply.up}}</span></div>
         <div class="replyCard-main-right-detail-icon" ><svg-icon icon-class="oppose"/><span>{{reply.down}}</span></div>
         <div class="replyCard-main-right-detail-icon" ><svg-icon icon-class="time"/><span>{{reply.created_time}}</span></div>
       </div>
   </div>
 </div>
  </router-link>
</template>

<script>
export default {
  name: "ReplyCard",
  props:{
    reply:{}
  },
}
</script>
<!--#27D0D8-->
<style scoped>
.replyCard-main{
  padding: 5px;
  background-color: #ffffff08;
  width: 99%;
  margin: 10px auto;
  border-radius: 3px;
  display: grid;
  grid-template-columns: 10% 90%;
}
.replyCard-main-accept{
  color: #5dcaad;
}
.replyCard-main-left{
  padding: 10px 5px;
  display: grid;
  color: #99a9bf;
  grid-template-rows: 60% 40%;
}
.replyCard-main-left-value{
  text-align: center;
  font-size: 18px;
}
.replyCard-main-left-label{
  padding: 5px 0;
  text-align: center;
  font-size: 12px;
}
.replyCard-main-right{
  padding: 9px 5px;
  display: grid;
  min-width: 0;
  grid-template-rows: 80% 20%;
}
.replyCard-main-right-content{
  text-overflow: -o-ellipsis-lastline;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
}
.replyCard-main-right-detail{

}
.replyCard-main-right-detail-icon{
  display: inline-block;
  font-size: 12px;
  margin-right: 20px;
}
.replyCard-main-right-detail-icon svg{
  font-size: 16px;
  margin-right: 8px;
}
</style>
