<template>
<div class="commends-main">
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="我的提问" name="first">
      <Empty v-if="questions.length===0"/>
            <question-card :question="item" :key="item.id" v-for="(item,index) in questions"/>
    </el-tab-pane>
    <el-tab-pane label="我的回复" name="second">
      <Empty v-if="replys.length===0"/>
      <ReplyCard :reply="item" :key="item.id" v-for="(item,index) in replys"/>
    </el-tab-pane>
  </el-tabs>
</div>
</template>

<script>
import {nanoid} from "nanoid";
import QuestionCard from "../../components/Card/QuestionCard";
import ReplyCard from "../../components/Card/ReplyCard";
import Empty from "../../components/Empty/Empty";
export default {
  name: "commends",
  components: {Empty, QuestionCard,ReplyCard},
  data() {
    return {
      userId:localStorage['userId'],
      activeName: 'first',
      questions:[],
      replys:[],
    }
  },
  created() {
    this.getQuestion()
    this.getReplys()
  },
  methods:{
    getQuestion(){
      this.questions=[]
      this.api.post("/new_question/getQuestionByUser",{userId:this.userId}).then(res=>{
        console.log(res)
        res.map(item=>{
          let up=item.up_nums===undefined?0:item.up_nums
          let reply=item.replys===undefined?0:item.replys
          let accepts=item.accepts===undefined?0:item.accepts
          let tag=item.tag.split('&')
          this.questions.push({...item,up_nums:up,replys:reply,accepts:accepts,tag:tag})
        })
      })
    },
    getReplys(){
      this.replys=[]
      this.api.post("/new_reply/getUserReply",{userId:this.userId}).then(res=>{
        this.replys= res.map(item=>{
          let up=item.up===undefined?0:item.up
          let down=item.down===undefined?0:item.down
          let time=this.getDate(item.created_time)
          return {...item,up,down,created_time: time,button:nanoid(5)}
        })

      })
    },
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
}
</script>

<style scoped>
.commends-main{
  padding: 10px 20px;
}
.commends-main /deep/ .el-tabs__item{
  color: white;
}
.commends-main /deep/ .el-tabs__item.is-active{
  color: #5dcaad;
}
.commends-main /deep/ .el-tabs__active-bar{
  background-color: #5dcaad;
}
</style>
