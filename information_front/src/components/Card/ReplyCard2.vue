<template>
  <div class="questionPage-show-replys"  >
    <div class="questionPage-show-replys-row1">
      <img style="border-radius: 50%;height: 36px;width: 36px;line-height: 36px" :src="'/api'+reply.avatar" alt=""/>
      <div class="questionPage-row1-detail">
        <div class="questionPage-row1-detail-nick">{{reply.name}}</div>
        <div class="questionPage-row1-detail-time">{{reply.created_time}}</div>
        <div class="questionPage-row1-detail-clicks" v-if="reply.accept===1">
          <span><svg-icon icon-class="corret2"/> 已被题主采纳</span>
        </div>
      </div>
    </div>
    <div class="questionPage-show-replys-row2">
      <div id="replyContent" :class="[showMore?'':'webkit-line-clamp']" v-html="reply.content">
        <!--                  举个例子，钢纤维混凝土的峰值应力受多个影响因素，如纤维长度，纤维形状，纤维方向，纤维含量的影响，该如何分析这些因素对混凝土峰值应力的贡献值，及敏感性分析。-->
      </div>
      <div v-if="show">
        <span @click="test">{{showMore?"...收起":"...展开"}}</span>
      </div>
    </div>
    <div class="questionPage-show-replys-row3">
      <el-button :id="reply.button+1" class="el-button-approve"  :class="{'el-button-approve':true,'el-button-selected':reply.type===1}"
                 @click="updateReplyThumb(reply)" size="mini" type="danger" round><svg-icon icon-class="approve"/>&nbsp;&nbsp;{{reply.up}} 解决</el-button>
      <el-button  :id="reply.button+2" class="el-button-oppose"  :class="{'el-button-oppose':true,'el-button-selected':reply.type===2}"
                  @click="updateReplyThumbNo(reply)" size="mini" type="danger" round><svg-icon icon-class="oppose"/>&nbsp;&nbsp;{{reply.down}} 无用</el-button>
      <el-popconfirm title="确定删除吗？" @onConfirm="deleteReply(reply)">
        <el-button slot="reference" type="danger" v-permission="'questionControl:deleteRep'" style="float: right " class="el-button-approve" size="mini">删除</el-button>
      </el-popconfirm>
      <el-button @click="updateAccept(reply)"  v-if="questionOwnerId===userId" style="float: right;margin: 0 20px " class="el-button-accept" size="mini">
        {{reply.accept===2?'采纳':'取消采纳'}}</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReplyCard2",
  props:{
    reply:{},
    questionOwnerId:'',
    questionId:0,
    questionTitle:'',

  },
  data() {
    return {
      userId:localStorage['userId'],
      showMore:false,
      show:false,
    }
  },
  mounted() {
    this.init()
  },
  methods:{
    init(){
      let box = document.querySelector('#replyContent')
      this.show = box.scrollHeight > box.clientHeight;
    },
    updateReplyThumb(item){
      let tem={type:1,userId:this.userId,questionUser:item.reply_id,questionId:item.id}
      let a=item.type===0?0:1
      if(item.type===1)
      {
        this.api.post("/new_thumbs/deleteThumb", tem).then(res=>{
          item.type=0
          item.up--
          document.getElementById(item.button+1).blur()
        })
      }else{
        this.api.post("/new_thumbs/addThumb", tem).then(res=>{
          item.type=1
          item.up++
          item.down=item.down-a
          document.getElementById(item.button+1).blur()
        })
      }
    },
    updateReplyThumbNo(item){
      let tem={type:2,userId:this.userId,questionUser:item.reply_id,questionId:item.id}
      let a=item.type===0?0:1
      if(item.type===2)
      {
        this.api.post("/new_thumbs/deleteThumb", tem).then(res=>{
          item.type=0
          item.down--
          document.getElementById(item.button+2).blur()
        })
      }else{
        this.api.post("/new_thumbs/addThumb",tem).then(res=>{
          item.type=2
          item.down++
          item.up=item.up-a
          document.getElementById(item.button+2).blur()
        })
      }
    },
    updateAccept(item){
      //let hef="<a href='http://localhost:9520/#/questionPage?id="+this.questionId+"' style='color:#3ac2bc9c' target='_blank'>链接直达</a>"
      let hef="<a href='https://150.158.22.107/#/questionPage?id="+this.questionId+"' style='color:#3ac2bc9c' target='_blank'>链接直达</a>"
      let tem={type:item.accept===1?2:1,id:item.id,isDelete:1,
        question:this.questionTitle,answer:hef}
      this.api.post("/new_reply/updateReply", tem).then(res=>{
          item.accept=tem.type
      })
    },
    deleteReply(item){
      let tem={type:item.accept,id:item.id,isDelete:2}
      this.api.post("/new_reply/deleteReply", tem).then(res=>{
         this.$message.success("删除成功")
         this.$emit('deleteReply',item)
      })
    },
    test(){
      this.showMore=!this.showMore
    }

  }

}
</script>

<style scoped>

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
  color: #46b44d;
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

.questionPage-show-replys{
  padding: 10px 24px 20px;
  font-size: 14px;
  display: grid;
  grid-template-rows: repeat(5,auto);
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
  margin-top: 10px;
  border-radius: 5px;
  transition: all 0.3s;
}
.questionPage-show-replys-row1{
  display: grid;
  grid-template-columns: 36px calc(100% - 51px);
  grid-column-gap: 15px;
  grid-template-rows: 36px;
}
.questionPage-show-replys-row2{
  padding: 5px 0 10px 0;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.99);
  font-weight: 400;
  line-height: 26px!important;
  margin-left: 50px;
  transition: all 0.3s;
}
.questionPage-show-replys-row3{
  margin-left: 50px;

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

#replyContent{
  display: -webkit-box;
  overflow: hidden;
  white-space: normal;
  text-overflow: ellipsis;
  word-wrap: break-word;
  -webkit-box-orient: vertical;
}
.webkit-line-clamp{
  -webkit-line-clamp: 3;
}
#replyContent+div{
  padding: 0 20px;
  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
  color: #5dcaad;
  font-size: 14px;
  font-weight: bold;
}
#replyContent+div span:hover{
  color: #3b990d;
}
#replyContent+div span:active{
  color: #35720f;
}
</style>
