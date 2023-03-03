<template>
<div class="questionPage-main">
        <Header/>
        <div class="questionPage-show" @click.stop="imageBind">
            <div class="questionPage-show-que">
                 <div class="questionPage-show-que-row1">
                  <img style="border-radius: 50%;height: 36px;width: 36px;line-height: 36px" :src="'/api'+question.avatar" alt=""/>
                  <div class="questionPage-row1-detail">
                    <div class="questionPage-row1-detail-nick">{{question.nickname}}</div>
                    <div class="questionPage-row1-detail-time">{{this.getDate(question.created_time)}}</div>
                    <div class="questionPage-row1-detail-clicks">浏览 {{question.clicks}}</div>
                  </div>
                </div>
                 <div class="questionPage-show-que-row2">
                 {{question.title}}
               </div>
                 <div class="questionPage-show-que-row3">
                 <div class="questionPage-show-row3-TAG" >
                   <svg-icon icon-class="tag" />
                 </div>
                 <div class="questionPage-show-row3-tag" :key="item+index" v-for="(item,index) in question.tag">
                   <span >{{item}}</span>
                 </div>
               </div>
                 <div  class="questionPage-show-que-row4">
                 <div :id="'con'+question.id" v-html="question.content">
<!--                 举个例子，钢纤维混凝土的峰值应力受多个影响因素，如纤维长度，纤维形状，纤维方向，纤维含量的影响，该如何分析这些因素对混凝土峰值应力的贡献值，及敏感性分析。-->
               </div>
              </div>
                 <div class="questionPage-show-que-row5">
                   <el-button @click="startEdit" class="el-button-edit" icon="el-icon-edit" size="mini" type="danger" round>{{editShow?'取消编辑':'写回复'}}</el-button>
                   <el-button id="questionPage_addThumb" @click="upDateThumb"
                              :class="{'el-button-approve':true,'el-button-selected':question.type===1}" size="mini" type="danger" round><svg-icon icon-class="approve"/>&nbsp;&nbsp;{{question.up_nums}} 好问题</el-button>
                   <el-button id="questionPage_addNo" @click="updateThumbNo"
                              :class="{'el-button-oppose':true,'el-button-selected':question.type===2}"  size="mini" type="danger" round><svg-icon icon-class="oppose"/>&nbsp;&nbsp;不推荐</el-button>
                   <el-popconfirm title="确定删除吗？" @onConfirm="deleteQuestion(question)">
                     <el-button slot="reference" v-permission="'questionControl:deleteQue'" style="float: right " class="el-button-approve" size="mini">删除</el-button>
                   </el-popconfirm>
                 </div>
            </div>
            <div class="questionPage-edit" id="questionPage-edit">
              <MyEditor @editOk="setContent" ref="myEditor"/>
              <el-button @click="send" size="mini" class="el-button-edit questionPage-edit-button1">回复</el-button>
              <el-button @click="clear" size="mini" class="el-button-edit questionPage-edit-button2">清除</el-button>
            </div>
            <div class="questionPage-show-filter">
              <div class="questionPage-show-filter-left">{{question.replys}} 条回答</div>
              <div class="questionPage-show-filter-right">
                <div @click="setSort(item)" :class="{'questionPage-show-filter-right-div-sel':item.value===sortSelected}"
                      v-for="(item,index) in sort" :key="item.label" >
                   {{item.label}}
                </div>
              </div>
            </div>
            <ReplyCard2 @deleteReply="deleteReply"
                        :question-owner-id="question.owner_id"
                        :question-title="question.title"
                        :question-id="question.id"
                        v-for="(item,index) in replys" :key="item.id" :reply="item"/>

        </div>
  <!-- 图片弹窗 -->
  <div id="myModal" class="modal">

    <!-- 关闭按钮 -->
    <span class="close"      onclick="document.getElementById('myModal').style.display='none'">&times;</span>

    <!-- 弹窗内容 -->
    <img class="modal-content" id="img01">

    <!-- 文本描述 -->
    <div id="caption"></div>
  </div>
</div>
</template>

<script>
import Header from "../../components/Header/Header";
import {nanoid} from "nanoid";
import MyEditor from "../../components/Edit/MyEditor";
import ReplyCard2 from "../../components/Card/ReplyCard2";
export default {
  name: "questionPage",
  components: {ReplyCard2, MyEditor, Header},
  data() {
    return {
       userId:localStorage['userId'],
       sort:[{value:0,label:"默认"},{value:1,label: "最新"}],
       questionId:this.$route.query.id,
       replys:[],
       question:{id:123456,title:"",type:0,avatar:'',content:"",nickname:"",up_nums:0,accepts:0,clicks:0,replys:0,tag:[],created_time:"",owner_id:"",classify:0},
       sortSelected:0,
       editShow:false,
       content:''
    }
  },
  created() {
    this.getQuestion()
    this.getReplys()
  },
  mounted() {
  },
  methods:{
     setSort(val){
       this.sortSelected=val.value
       this.getReplys()
     },
     getReplys(){
       this.api.post("/new_reply/getQuestionReply",{userId:this.userId,questionId:this.questionId,sort:this.sortSelected}).then(res=>{
         this.replys= res.map(item=>{
           let up=item.up===undefined?0:item.up
           let down=item.down===undefined?0:item.down
           let time=this.getDate(item.created_time)
           return {...item,up,down,created_time: time,button:nanoid(5)}
         })

       })

     },
     getQuestion(){
       this.api.post("/new_question/getQuestionById",{userId:this.userId,questionId:this.questionId}).then(res=>{
         let up=res.up_nums===undefined?0:res.up_nums
         let reply=res.replys===undefined?0:res.replys
         let accepts=res.accepts===undefined?0:res.accepts
         let tag=res.tag.split('&')
         this.question={...res,up_nums:up,replys:reply,accepts:accepts,tag:tag}
         })
     },
     upDateThumb(){
       let tem={type:1,userId:this.userId,questionUser:this.question.owner_id,questionId:this.question.id}
       let a=this.question.type===0?0:1
       if(this.question.type===1)
        this.api.post("/new_thumbs/deleteThumb", tem).then(res=>{
          this.question.type=0
          this.question.up_nums--
          document.getElementById("questionPage_addThumb").blur()
        })
       else
        this.api.post("/new_thumbs/addThumb", tem).then(res=>{
          this.question.type=1
          this.question.up_nums++
          document.getElementById("questionPage_addThumb").blur()
        })

    },
     updateThumbNo(){
       let tem={type:2,userId:this.userId,questionUser:this.question.owner_id,questionId:this.question.id}
       let a=this.question.type===0?0:1
       if(this.question.type===2)
       {
         this.api.post("/new_thumbs/deleteThumb", tem).then(res=>{
           this.question.type=0
           document.getElementById("questionPage_addNo").blur()
         })
       }else{
         this.api.post("/new_thumbs/addThumb", tem).then(res=>{
           this.question.type=2
           this.question.up_nums=this.question.up_nums-a
           document.getElementById("questionPage_addNo").blur()
         })
       }
     },
     startEdit(){
       this.editShow=!this.editShow
       let tem=this.editShow?'block':'none'
       document.getElementById('questionPage-edit').style.display=tem
     },
     setContent(val){
      this.content=val
    },
     clear(){
      this.$refs.myEditor.clear()
    },
     send(){
       if(this.content===""||this.content==='<p><br></p>') {
         this.$message({
           showClose: true,
           message: '内容不能为空',
           type: 'warning'
         });
         return
       }
       this.api.post("/new_reply/addReply",{question_id:this.questionId,reply_id:this.userId,content:this.content}).then(res=>{
         this.$message.success("回复成功")
         this.content=''
         this.$refs.myEditor.clear()
         this.startEdit()
         this.getReplys()

       })
     },
     //为img标签绑定预览事件
     imageBind(){
        // 获取弹窗
        var modal = document.getElementById('myModal');
        // 获取图片插入到弹窗 - 使用 "alt" 属性作为文本部分的内容
        const img = document.getElementsByTagName("img");
        const modalImg = document.getElementById("img01");
        const captionText = document.getElementById("caption");
        for (let i=0; i<img.length; i++) {
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
    },
     deleteReply(item) {
       this.replys.splice(this.replys.indexOf(item), 1);
     },
     deleteQuestion(item){
       this.api.post("/new_question/deleteQuestion",{userId:this.userId,questionId:this.questionId}).then(res=>{
          this.$message.success("删除成功")
         this.$router.replace('/404')
       })
     }
  }
}
</script>

<style scoped>
.questionPage-main{
  background: url("../../assets/background1.jpg") no-repeat;
  height: 100vh;
  width: 100%;
  background-size: cover;
  position: fixed;
  color: white;
  overflow-y: auto;
}
.questionPage-show{
  z-index:0;
  width: 65%;
  margin: 80px auto;

}
.questionPage-show-que{
  padding: 24px;
  font-size: 14px;
  display: grid;
  grid-template-rows: repeat(5,auto);
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
  margin-bottom: 10px;
}
.questionPage-show-que-row1{
  display: grid;
  grid-template-columns: 36px calc(100% - 56px);
  grid-column-gap: 20px;
  grid-template-rows: 36px;
}
.questionPage-row1-detail{
  width: 100%;
  display: flex;
  position: relative;
}
.questionPage-row1-detail div{
  display: inline-block;
  margin: auto 10px auto 0;
}
.questionPage-row1-detail-nick{
  color: rgba(255, 255, 255, 0.99);
  font-weight: 400;
  max-width: 154px;
  overflow: hidden;
  max-height: 32px;
  word-break: break-all;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.questionPage-row1-detail-time{
  margin-left: 15px;
  color: #999aaa;
  font-size: 14px;
}
.questionPage-row1-detail-clicks{
  position: absolute;
  right: 0;
  top: 30%;
}
.questionPage-show-que-row2{
  display: flex;
  flex-direction: row;
  margin-bottom: 8px;
  margin-top: 8px;

  font-size: 21px;
  font-weight: bold;
  font-family: 微软雅黑;
  line-height: 30px;
}
.questionPage-show-que-row3{
  width: 100%;
  display: flex;
}
.questionPage-show-row3-TAG{
  color: #5dcaad;
  margin: 0 10px 0 0;
}
.questionPage-show-row3-tag{
  background: rgba(23, 177, 48, 0.33);;
  flex-shrink: 0;
  height: 20px;
  line-height: 19px;
  margin-right: 8px;
  border-radius: 2px;
  width: fit-content;
}
.questionPage-show-row3-tag span{
  width: fit-content;
  padding: 0 8px;
  font-size: 12px;
  color: #67c23a;
  font-weight: 400;
}
.questionPage-show-que-row4{
  padding: 5px 0 10px 0;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.99);
  font-weight: 400;
  line-height: 26px!important;
}
.questionPage-show-que-row5{

}

.questionPage-show-filter{
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
  position: relative;
  height: 48px;
  width: 100%;
  padding: 5px;
}
.questionPage-show-filter-left{
  position: absolute;
  left: 20px;
  top: 15px;

}
.questionPage-show-filter-right{
  position: absolute;
  right: 20px;
  top:8px;
  border: 1px solid rgba(153, 169, 191, 0.82);
  border-radius: 5px;
  padding: 3px 5px;
  background-color: rgba(0, 0, 0, 0.1);
}
.questionPage-show-filter-right div{
  padding: 4px 2px;
  display: inline-block;
  width: 45px;
  font-size: 13px;
  text-align: center;
  transition: background-color 0.5s 0s ease-in;
}
.questionPage-show-filter-right-div-sel{
   transition: background-color 0.5s 0s ease-in;
   background-color:#2edcf196;
   border-radius: 5px;
}


.questionPage-edit{
  display: none;
  position: relative;
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
  margin-bottom: 20px;
}
.questionPage-edit-button1{
  position: absolute;
  bottom:20px;
  right: 20px;
}
.questionPage-edit-button2{
  position: absolute;
  bottom:20px;
  right: 100px;
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

</style>
