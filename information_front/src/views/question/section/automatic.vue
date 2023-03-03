<template>
<div class="auto-main">
   <div class="auto-main-left">
       <div class="auto-main-left-list">
         <ChatBubble :item="item" :itemId="item.id" v-for="(item,index) in chatList" :key="item.id"  @goToQuestion="goToQuestion" />
       </div>
       <div class="auto-main-left-footer">
         <div class="auto-main-left-footer-head">
           <div class="head-icon"><svg-icon  icon-class="email"/>留言</div>
           <div @click="reSet" class="head-icon"><svg-icon icon-class="clear1"/>清空</div>
           <div @click="clear" class="head-icon"><svg-icon icon-class="clear1"/>输入</div>
           <div @click="editOk(textarea)" class="auto-main-left-footer-head-ok head-icon" ><svg-icon icon-class="send"/>发送</div>
         </div>
         <div class="auto-main-left-footer-edit">
           <el-input type="textarea" resize="none"  placeholder="请输入问题(shift+Enter发送)" v-model="textarea"/>
         </div>
       </div>
   </div>
   <div class="auto-main-right">
     <el-tabs type="border-card" style="height: 100%;">
       <el-tab-pane label="常见问题" style="overflow-y: auto;height: 100%" >
         <div class="pan1-refresh">
           <i class="el-icon-refresh" ></i>
           <span @click="reFresh">换一批</span>
         </div>
         <el-empty description="请求超时..." v-if="sideQuestionList.length===0" />
         <div class="pane1-div" v-for="(item,index) in sideQuestionList">
           <span class="pane1-number">{{index+1}}</span>
           <span class="pane1-contain" @click="goToQuestion(item)">{{item}}</span>
         </div>
       </el-tab-pane>
       <el-tab-pane label="新增问题" style="overflow-y: auto;height: 100%" >
         <div slot="label" @click="getNewQuestion">新增问题</div>
         <el-empty description="暂无数据" v-if="newestQuestionList.length===0" />
         <div class="pane1-div" v-for="(item,index) in newestQuestionList">
           <span class="pane1-number">{{index+1}}</span>
           <span class="pane1-contain" @click="goToQuestion(item.question)">{{item.question}}</span>
         </div>
       </el-tab-pane>
     </el-tabs>
   </div>

</div>
</template>

<script>
import { nanoid } from "nanoid";
import ChatBubble from "../../../components/Chatbubble/ChatBubble";
export default {
  name: "automatic",
  components: {ChatBubble},
  data() {
    return {
      chatList:[{id:nanoid(),isUser:false,isList:false,contain:"您好，请问有什么可以帮您~"},
       ],
      textarea:'',
      sideQuestionList:[],
      newestQuestionList:[],
    }
  },
  watch:{
    'chatList': 'scrollToBottom',
  },
  created() {
    this.reFresh()
  },
  methods:{
    //将滚动条拉到底
     scrollToBottom: function () {
      this.$nextTick(() => {
        var div = document.getElementsByClassName('auto-main-left-list')[0]
        div.scrollTop = div.scrollHeight;
      })

    },
     //重置聊天记录
     reSet(){
      this.chatList=[];
      this.chatList.push({id:nanoid(),isUser:false,isList:false,contain:"您好，请问有什么可以帮您~"},)
    },
     editOk(val){
       if(val===""||val.match(/^\s+$/))
         return;
       this.chatList.push({id:nanoid(),isUser:true,isList:false,contain:val});
       this.api.get("/recommend/getCommendList",{params: {content:val}}).then(res=>{
         console.log(res)
         if(res.recommendList.length>0)
           this.chatList.push({id:nanoid(),isUser:false,isList:true,contain:res.recommendList})
         else
           this.chatList({id:nanoid(),isUser:false,isList:false,contain:"诶~人家没看懂耶，要不您换个说法呢~"})
       }).catch(err=>{
         console.log("Dialog/sendQuestion出错")
       })
       this.textarea="";
     },
     clear(){
      this.textarea=''
     },
     goToQuestion(val){
      if(val==="")
        return;
      this.chatList.push({id:nanoid(),isUser:true,isList:false,contain:val});
      this.api.get("/answer/"+val).then(res=>{
        const answer =res.answer;
        this.chatList.push({id:nanoid(),isUser:false,isList:false,contain:answer});
      }).catch(err=>{
        console.log(err)
      })
    },
     //得到侧边栏展示的推荐问题列表
     reFresh(){
      this.api.get("/recommend/getList",{params: {content:""}}).then(res=>{
        this.sideQuestionList=[];
        this.sideQuestionList=res.recommendList;
      }).catch(err=>{
        console.log("RecomendQuestion/reFresh出错")
      })
    },
    //得到最新问题
     getNewQuestion(){
      this.api.get("/answer/getNewQuestion").then(res=>{
        this.newestQuestionList=[];
        this.newestQuestionList=res;
      }).catch(err=>{
        console.log("RecomendQuestion/reFresh出错")
      })
    },

  }
}
</script>

<style scoped>
.auto-main{
  width: 100%;
  display: grid;
  grid-template-columns: 68% 30%;
  grid-column-gap:2%;
  grid-template-rows: 500px;
}
.auto-main-left{
  width: 100%;
  display: grid;
  grid-template-rows: 80% 20%;
  grid-template-columns: 100%;
}
.auto-main-left-list{
  width: 100%;
  height: 100%;
  overflow-y: auto;
  padding: 20px 0;
}
.auto-main-left-footer{
  display: grid;
  width: 100%;
  grid-template-rows: 22% 78%;
  grid-template-columns: 100%;
}
.auto-main-left-footer-head{
  display: grid;
  width: 100%;
  /*grid-template-rows: 30px;*/
  grid-template-columns: repeat(10,10%);
}
.head-icon{
  font-size: 16px;
  display: flex;
  align-items: center;
}
.head-icon svg{
  width: 20px;
  height: 20px;
  margin: 0 5px;
  color: white;
}
.head-icon:hover svg,.head-icon:hover{
  color: #59b729;
  font-size: 18px;
}

.auto-main-left-footer-head-ok{
  grid-row-start: 1;
  grid-column-start: 10;
  grid-row-end: 1;
  grid-column-end: 10;
}
.auto-main-left-footer-edit{
  width: 100%;
  height: 100%;
}
.auto-main-left-footer-edit /deep/ .el-textarea__inner{
  background-color: rgba(255, 255, 255, 0);
  color: white;
  height: 100px;
  border: none;
  border-bottom: white solid 1px;
  padding: 20px 10px;
}

.auto-main-right /deep/ .el-tabs--border-card{
  background-color: rgba(255, 255, 255, 0);
  border: none;
  box-shadow: none;
}
.auto-main-right /deep/ .el-tabs__header{
  background-color: rgba(255, 255, 255, 0);
}
.auto-main-right /deep/ .el-tabs__item.is-active{
  background-color: rgba(255, 255, 255, 0);
}
.pan1-refresh{
  text-align: center;
  font-size: 12px;
  color: darkgray;
}
.pane1-div{
  padding: 15px 20px;
  cursor: pointer;
  border-bottom: none;

}
.pane1-number{
  display: inline-block;
  width: 20px;
  height: 20px;
  line-height: 20px;
  text-align: center;
  border: 1px solid #e6e6e6;
  border-radius: 3px;
  margin-right: 10px;
  color: #008CEE;
}
.pane1-contain{
  padding: 0;
  color: white;
  font-family: "Microsoft YaHei", Arial, sans-serif;
  font-size: 14px;
  margin: 0.5rem 0.5rem;
  line-height: 30px;
}
.pane1-contain:hover,.pan1-refresh:hover{
  color: #409eff;
}
.pane1-contain:active,.pan1-refresh:active{
  color: rgb(240, 68, 60);
}

</style>
