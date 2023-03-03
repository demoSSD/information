<template>
<div class="questionControl-main">
  <div class="questionControl-main-classify">
    <h4>频道<span><svg-icon icon-class="classify"/></span>
      <el-button @click="addClassify" size="mini" round class="el-button-edit">确定添加</el-button></h4>
    <el-tag class="scale-in-center" @click="classifyClick(item)" :key="item.label" closable v-for="item in classifyOptions" :disable-transitions="false" @close="handleClose1(item)">
      {{item.label}}
    </el-tag>
    <el-input
      class="input-new-tag"
      v-if="inputVisibleClassify"
      v-model="inputValueClassify"
      ref="saveTagInputClassify"
      size="small"
      @keyup.enter.native="handleInputConfirm1"
      @blur="handleInputConfirm1"
    >
    </el-input>
    <el-button v-else class="button-new-tag" size="small" @click="showInput1">+ New Tag</el-button>
  </div>
  <div class="questionControl-main-tag">
    <h4>标签<span><svg-icon icon-class="tag"/></span>{{classifySelected.label}} /
      <el-button size="mini" @click="addTags" round class="el-button-edit">确定添加</el-button></h4>
    <div v-if="classifySelected.value!==0">
      <el-tag class="scale-in-center" :key="item" closable v-for="item in tagOptions" :disable-transitions="false" @close="handleClose2(item)">
        {{item}}
      </el-tag>
      <el-input
        class="input-new-tag"
        v-if="inputVisibleTag&&classifySelected.value!==0"
        v-model="inputValueTag"
        ref="saveTagInputTag"
        size="small"
        @keyup.enter.native="handleInputConfirm2"
        @blur="handleInputConfirm2"
      >
      </el-input>
      <el-button v-else class="button-new-tag" size="small" @click="showInput2">+ New Tag</el-button>
    </div>
    <div v-else>
      <h5>请选择一个频道</h5>
    </div>
  </div>

</div>
</template>

<script>
export default {
  name: "questionControl",
  data() {
    return {
      classifyOptions:[],
      tagOptions:[],
      inputVisibleClassify: false,
      inputVisibleTag: false,
      inputValueClassify: '0',
      inputValueTag: '',
      classifySelected:{value:0,label:'#'},
      newTags:[],
      newClassify:[],
    }
  },
  created() {
    this.getClassify()
    this.getAllTags()
  },
  methods:{
    getClassify(){
      this.api.get("/new_classify/getAllClassify").then(res=>{
        this.classifyOptions=res.map(item=>{
          return {value:item.id,label:item.name}
        })
      })
    },
    getTags(val){
      this.tagOptions=[]
      this.api.post("/new_classify/getTags",{classify_id:val}).then(res=>{
        this.tagOptions=res.map(item=>{
          return item.name
        })
      })
    },
    classifyClick(val){
      if(val.value===this.classifySelected.value)
        return
      this.classifySelected=val
      this.newTags=[]
      this.getTags(val.value)
    },
    addClassify(){
      if(this.newClassify.length===0){
        this.$message.warning("没有新增频道")
        return
      }
      this.api.post("/new_classify/addClassify",{list:this.newClassify}).then(res=>{
        this.$message.success("添加频道成功")
        this.newClassify=[]
        this.getClassify()
      })
    },
    addTags(){
      if(this.newTags.length===0){
        this.$message.warning("没有新增标签")
        return
      }
      this.api.post("/new_classify/addTagsByClassify",{list:this.newTags}).then(res=>{
        this.$message.success("添加标签成功")
        this.newTags=[]
        // this.getTags()
      })
    },
    handleClose1(tag) {
      this.classifyOptions.splice(this.classifyOptions.indexOf(tag), 1);
      this.newClassify.splice(this.newClassify.indexOf(tag), 1);
    },
    showInput1() {
      this.inputVisibleClassify = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInputClassify.$refs.input.focus();
      });
    },
    handleInputConfirm1() {
      let inputValue = this.inputValueClassify;
      if (inputValue) {
        this.classifyOptions.push({label:inputValue});
        this.newClassify.push(inputValue)
      }
      this.inputVisibleClassify = false;
      this.inputValueClassify = '';
    },
    handleClose2(tag) {
      this.tagOptions.splice(this.tagOptions.indexOf(tag), 1);
      this.newTags.splice(this.newTags.indexOf({tag:tag,classify_id:this.classifySelected.value}), 1);
      this.api.post("/new_classify/deleteTag",{tagName:tag,classifyId:this.classifySelected.value}).then(res=>{
        this.$message.success("删除成功")
        // this.getTags()
      })
    },
    showInput2() {
      this.inputVisibleTag = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInputTag.$refs.input.focus();
      });
    },
    handleInputConfirm2() {
      let inputValue = this.inputValueTag;
      if (inputValue) {
        this.tagOptions.push(inputValue);
        this.newTags.push({tag:inputValue,classify_id:this.classifySelected.value})
      }
      this.inputVisibleTag = false;
      this.inputValueTag = '';
    }
  }
}
</script>

<style scoped>
.questionControl-main{
  padding: 10px 20px;
}
.questionControl-main span{
  margin-right: 10px;
}

.questionControl-main svg{
  color: #5dcaad;
  margin-left: 10px;
}
.questionControl-main .el-button-edit{
 float: right;
}
.el-tag {
  background-color: rgba(93, 202, 173, 0.3);
  color: #5dcaad;
  margin: 5px 5px;
  border: #5dcaad;
  font-size: 13px;
  font-weight: bold;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
  background-color: rgba(153, 169, 191, 0.16);
  color: white;
  border: none;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
  background-color: rgba(255, 255, 255, 0.45);

}

</style>
