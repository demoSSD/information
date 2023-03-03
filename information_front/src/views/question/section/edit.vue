<template>
 <div class="edit-main">
   <div class="edit-main-title">
     <el-input v-model="question.title" resize="none" placeholder="请输入问题标题..." />
     <el-button @click="issue" icon="el-icon-s-promotion" class="edit-main-title-button">发布</el-button>
   </div>
   <div class="edit-main-classify">
     <span>选择问题分类：</span>
     <el-select v-model="question.classify" placeholder="请选择" :popper-append-to-body="false">
       <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"/>
     </el-select>
   </div>
   <div class="edit-main-tags" v-show="tagOptions!==null">
     <div class="edit-main-label">请为问题选择标签：</div>
     <el-checkbox-group v-model="tags">
       <el-checkbox-button v-for="item in tagOptions" :label="item" :key="item">{{item}}</el-checkbox-button>
     </el-checkbox-group>
   </div>
   <div class="edit-main-content">
     <div class="edit-main-label">请输入问题描述：</div>
     <div>
          <MyEditor @editOk="setContent" ref="myEditor"/>
     </div>
   </div>

 </div>
</template>

<script>

import MyEditor from "../../../components/Edit/MyEditor";
import {mapGetters} from "vuex";
export default {
  name: "forum",
  components: {MyEditor},
  computed:{
    ...mapGetters([
      'userId',
    ]),
  },
  data() {
    return {
      question:{user_id:localStorage['userId'],classify:'',tags:'',title:'',content:''},
      options: [{
        value: 12,
        label: '黄金糕'
      }],
      tagOptions:null,
      tags:[]
    }
  },
  watch:{
    'question.classify':function (val,old){
      this.getTags(val)
    }
  },
  created() {
    this.getClassify()
  },
  methods:{
     setContent(val){
       this.question.content=val
     },
     getClassify(){
       this.api.get("/new_classify/getAllClassify").then(res=>{
         this.options=res.map(item=>{
           return {value:item.id,label:item.name}
         })
       })
     },
     getTags(val){
       this.api.post("/new_classify/getTags",{classify_id:val}).then(res=>{
         this.tagOptions=res.map(item=>{
           return item.name
         })
       })
     },
     issue(){
       let tem=''
       for(let i=0;i<this.tags.length;i++)
       {
         if(i<this.tags.length-1)
           tem=tem+this.tags[i]+'&'
         else
           tem=tem+this.tags[i]
       }
       this.question.tags=tem
       if(this.question.title===""||this.question.title.match(/^\s+$/)) {
         this.$message({
           showClose: true,
           message: '标题不能为空',
           type: 'warning'
         });
         return
       }
       if(this.question.content===""||this.question.content==='<p><br></p>') {
         this.$message({
           showClose: true,
           message: '内容不能为空',
           type: 'warning'
         });
         return
       }
       if(this.question.content===''){
         this.$message({
           showClose: true,
           message: '请选择一个分类',
           type: 'warning'
         });
         return
       }
       this.api.post("/new_question/addQuestion",{...this.question}).then(res=>{
         this.$message.success(res)
         this.question={user_id:localStorage['userId'],classify:'',tags:'',title:'',content:''}
         this.$refs.myEditor.clear()
       })
     }
  }
}
</script>

<style scoped>
.edit-main{
  padding: 20px 40px;
  margin: 0;
}
.edit-main-title-button{
  background-color: #4bbde2;
  color: white;
  border: none;
  font-size: 18px;
  padding: 0;
  width:100px;
}
.edit-main-title-button:hover{
  font-weight: bold;
  background-color: #46b44d;
}
.edit-main-title{
  width: 100%;
  display: grid;
  grid-template-columns: 80% 10%;
  /*grid-template-rows: 35px;*/
  grid-column-gap: 10%;

}
.edit-main-title /deep/ .el-input__inner{
  font-size: 20px;
  padding: 0;
  background-color: #0000ff00;
  border: none;
  font-weight: bolder;
  color: rgba(255, 255, 255,1);
  border-bottom: 1px solid white;
  border-radius: 0px;
  /*width: 80%;*/
}
.edit-main-classify{
  color: white;
  margin: 20px 0;
}
.edit-main-classify /deep/ .el-input__inner{
  color: white;
  background-color: rgba(255, 255, 255, 0);
}
.edit-main-classify /deep/ .el-popper{
  position:absolute !important;
  background-color: #1c273394;
  border: dodgerblue 1px solid;
}
.edit-main-classify /deep/ .el-select-dropdown__item.hover{
  background-color: rgba(110, 247, 42, 0.67);
  font-size: 20px;
}
.edit-main-classify /deep/ .el-select-dropdown__item{
  color: white;
}
.edit-main-label{
  margin: 20px 0;
}
.edit-main-tags /deep/  .el-checkbox-button__inner:hover{
  background-color: #3b990d;
  color: white;
}
.edit-main-tags /deep/ .el-checkbox-button{
  line-height: 30px;
  font-size: 12px;
  color: #3b990d;
  border: #67c23a54;
  border-radius: 4px;
  box-sizing: border-box;
  white-space: nowrap;
  margin:0 10px 0 0 ;
}
.edit-main-tags /deep/ .el-checkbox-button__inner{
  line-height: unset;
  padding: 0 10px;
  border-radius: 4px;
  color: #3b990d;
  border: rgba(103, 194, 58, 0);
}
.edit-main-tags /deep/ .el-checkbox-button.is-checked .el-checkbox-button__inner{
  background-color: #46b44d;
  color: rgba(255, 255, 255, 0.99);
  font-weight: bold;
  border: rgba(103, 194, 58, 0);
}
.edit-main-content /deep/ .w-e-text-container,.edit-main-content /deep/ .w-e-toolbar{

}

</style>
