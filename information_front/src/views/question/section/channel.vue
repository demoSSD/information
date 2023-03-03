<template>
 <div class="channel-main">
   <div class="channel-main-left">
     <div class="channel-main-left-head">
       <div :class="{'sort-selected':item.value===sortSelected}" @click="setSort(item.value)" :key="item.label" v-for="(item,index) in sort">
         <span>{{item.label}}</span></div>
     </div>
     <div class="channel-main-left-list">
       <div class="infinite-list-wrapper" style="overflow:auto">
         <div class="list" v-infinite-scroll="load" infinite-scroll-disabled="disabled">
           <question-card :question="item" :show-accept="sortSelected===3"  :key="item.id" v-for="(item,index) in question" class="list-item"/>
         </div>
         <p v-if="loading" class="channel-main-left-list-loading">加载中...</p>
         <p v-if="noMore" class="channel-main-left-list-noMore">没有更多了</p>
       </div>
     </div>
   </div>
   <div class="channel-main-right">
     <div class="channel-main-right-head">
        <h4>频道<span><svg-icon icon-class="classify"/></span>
               <span class="channel-main-right-head-sel">{{classifySelected.label}}</span></h4>
        <div class="channel-main-right-head-classify">
           <div @click="setClassify(item)" class="channel-classify scale-in-center" v-for="(item,index) in classifyOptions">{{item.label}}</div>
        </div>
     </div>
     <div class="channel-main-right-head">
       <h4>标签<span><svg-icon icon-class="tag"/></span>
               <span class="channel-main-right-head-sel">{{tagSelected}}</span></h4>
       <div class="channel-main-right-head-tags">
         <el-tooltip content="请先选择一个频道" placement="top" :disabled="toolTip" :key="item" effect="light" v-for="(item,index) in tagOptions">
           <div @click="setTag(item)" class="channel-tag scale-in-center" >{{item}}</div>
         </el-tooltip>

       </div>
     </div>
   </div>
 </div>
</template>

<script>
import QuestionCard from "../../../components/Card/QuestionCard";
export default {
  name: "channel",
  components: {QuestionCard},
  data() {
    return {
       userId:localStorage['userId'],
       sort:[{label:'最新',value:0},
         {label:'点击量',value:1},
         {label:'回复数',value:2},
         {label:'精华',value:3},
         {label:'待回答',value:4},],
       classifyOptions:[],
       tagOptions:[],
       count: 50,
       loading: false,
       toolTip:false,
       question:[{id:123456,title:"",isClicked:false,content:"",nickname:"",up_nums:0,accepts:0,clicks:0,replys:0,tag:[],created_time:"",owner_id:"",classify:0}],
       sortSelected:0,
       classifySelected:{value:0,label:"全部"},
       tagSelected:"全部",

    }
  },
  watch:{
    'classifySelected':function (val,old){
      this.toolTip = val.value !== 0;
    }
  },
  created() {
    this.question=[]
    this.getQuestion()
    this.getClassify()
    this.getAllTags()

  },
  computed: {
    noMore () {
      return this.count >= 100
    },
    disabled () {
      return this.loading || this.noMore
    },
  },
  methods:{
    setSort(val){
      this.sortSelected=val
      this.getQuestion()
    },
    load () {
      this.loading = true
      setTimeout(() => {
        this.count += 50
        this.loading = false
      }, 2000)
    },
    getQuestion(){
      this.question=[]
      let tem={userId:this.userId,tag:this.tagSelected,classify_id:this.classifySelected.value,sort:this.sortSelected}
      this.api.post("/new_question/getQuestion",{...tem}).then(res=>{
       res.map(item=>{
          let up=item.up_nums===undefined?0:item.up_nums
          let reply=item.replys===undefined?0:item.replys
          let accepts=item.accepts===undefined?0:item.accepts
          let tag=item.tag.split('&')
          this.question.push({...item,up_nums:up,replys:reply,accepts:accepts,tag:tag})

        })
      })
    },
    getClassify(){
      this.api.get("/new_classify/getAllClassify").then(res=>{
        this.classifyOptions=res.map(item=>{
          return {value:item.id,label:item.name}
        })
        this.classifyOptions.unshift({value:0,label:"全部"})
      })


    },
    getTags(val){
      this.tagOptions=[]
      this.api.post("/new_classify/getTags",{classify_id:val}).then(res=>{
        this.tagOptions=res.map(item=>{
          return item.name
        })
        this.tagOptions.unshift("全部")
      })
    },
    getAllTags(){
      this.api.get("/new_classify/getAllTags").then(res=>{
        this.tagOptions=res.map(item=>{
          return item.name
        })
        this.tagOptions.unshift("全部")
      })
    },
    setClassify(val){
      this.classifySelected=val
      if(val.value===0){
        this.setTag("全部")
        this.getAllTags()
      }
      else
        this.getTags(val.value)
      this.getQuestion()
    },
    setTag(val){
      if(this.toolTip){
        this.tagSelected=val
        this.getQuestion()
      }
    }
  }
}
</script>

<style scoped>
.scale-in-center:hover{
  background-color: rgba(70, 180, 77, 0.28);
}
.channel-main{
   width: 100%;
   overflow: hidden;
   display: grid;
   grid-template-columns: 69% 30%;
   grid-column-gap: 2%;

}
.channel-main-left-head{
  width: 100%;
  display: grid;
  grid-template-columns: repeat(8,9%);
  grid-template-rows: 40px;
  grid-column-gap: 4%;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}
.sort-selected{
  font-weight: bold;
  color: #3b990d;
  border-bottom: 2px solid green;
}
.channel-main-left-head div{
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-direction: column;
}
.channel-main-left-list{
 overflow-y: auto;
 height: 500px;
}
.channel-main-left-list-loading{
  width: 100px;
  text-align: center;
  margin: 20px auto;
  color: rgba(255, 255, 255, 0.43);
  font-size: 13px;
}
.channel-main-left-list-noMore{
  width: 100px;
  text-align: center;
  margin: 20px auto;
  color: #ffffff57;
  font-size: 13px;
}
.channel-main-right{
  display: grid;
  grid-template-rows: 45% 50%;
  grid-row-gap: 5%;
}
.channel-main-right-head span{
  margin: 0 7px;
}
.channel-main-right-head-sel{
  color: #5dcaad;
}
.channel-main-right-head{
  height: 100%;
}
.channel-main-right-head-classify{
  width:100%;
  height: 200px;
  overflow-y: auto;
}
.channel-main-right-head-tags{
  width:100%;
  height: 200px;
  overflow-y: auto;

}
.channel-classify{
  display: inline-block;
  width: fit-content;
  padding: 5px 8px;
  background: rgba(120, 213, 229 ,0.38);
  margin: 5px;
  border-radius: 2px;
  font-size: 14px;
  color: #09e0efcc;
  font-weight: bold;
}
.channel-tag{
  display: inline-block;
  width: fit-content;
  padding: 5px 8px;
  background: #f56c6c47;
  margin: 5px;
  border-radius: 2px;
  font-size: 12px;
  color: #f56c6c;
  font-weight: bold;
}
</style>
