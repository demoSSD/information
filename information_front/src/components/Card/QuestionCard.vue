<template>

   <div class="question-card scale-in-center" @click="addClick">
    <div :class="{'question-card-left':true,'question-card-accept':showAccept}">
          <div v-if="!showAccept" class="question-card-left-value">{{question.replys}}</div>
          <div v-if="!showAccept" class="question-card-left-label">回答</div>


          <div v-if="showAccept" class="question-card-accept-label">{{question.accepts}}</div>
          <div v-if="showAccept" class="question-card-accept-value"><svg-icon icon-class="corret"/></div>
    </div>
   <div class="question-card-right">
     <router-link :to="{path:'/questionPage', query:{id:this.question.id}}" target="_blank">
      <div class="question-card-right-title">
        {{question.title}}
      </div>
     </router-link>
      <div class="question-card-right-content">
      <div >{{question.content.replace(/<\/?.+?\/?>/g,'')}}</div>
      </div>
      <div class="question-card-right-detail">
        <div style="float: left">
          <div class="question-card-right-detail-tag" :key="question.id+item" v-for="(item,index) in question.tag"><span>{{item}}</span></div>
          <div class="question-card-right-detail-icon" ><svg-icon icon-class="click"/><span>{{question.clicks}}</span></div>
          <div class="question-card-right-detail-icon" @click.s.stop="upDateThumb"><svg-icon  :class="{question_card_approved:question.type===1}" icon-class="approve"/><span>{{question.up_nums}}</span></div>
          <div class="question-card-right-detail-icon"><svg-icon icon-class="time"/><span>{{this.getDate(question.created_time)}}</span></div>
          <div class="question-card-right-detail-icon question-reply-button"><svg-icon icon-class="reply"/><span>回复</span></div>
        </div>
         <div class="question-owner">{{question.nickname}}</div>
      </div>
  </div>
   </div>

</template>

<script>
export default {
  name: "QuestionCard",
  props:{
    question:{},
    showAccept:false
  },
  data() {
    return {
       userId:localStorage['userId'],
    }
  },
  methods:{
    upDateThumb(){
     // console.log(this.question)
       if(this.question.type===1)
       {
         this.api.post("/new_thumbs/deleteThumb",{userId:this.userId,questionUser:this.question.owner_id,questionId:this.question.id}).then(res=>{
            this.question.type=0
            this.question.up_nums--
         })
       }else{
         this.api.post("/new_thumbs/addThumb",{type:1,userId:this.userId,questionUser:this.question.owner_id,questionId:this.question.id}).then(res=>{
           this.question.type=1
           this.question.up_nums++
         })
       }
    },
    addClick(){
      this.api.post("/new_question/addClick",{questionId:this.question.id}).then(res=>{
        this.question.clicks++
      })
    }
  }
}
</script>

<style scoped>
.question-card{
  padding: 5px;
  background-color: #ffffff08;
  width: 99%;
  /*height: 100px;*/
  margin: 10px auto;
  border-radius: 3px;
  display: grid;
  grid-template-columns: 10% 90%;
}
.question-card-left{
  display: grid;
  grid-template-rows: 60% 40%;
}
.question-card-left-value{
  text-align: center;
  margin-top: 20px;
  font-size: 18px;
}
.question-card-left-label{
   text-align: center;
   font-size: 12px;
}

.question-card-right{
  display: grid;
  width: 100%;
  grid-template-rows: repeat(3,auto);
  /*grid-row-gap: 0.5%;*/
}
.question-card-right div{
  display: flex;
  vertical-align: center;
}
.question-card-right-title{
  padding: 10px 0 5px 0;
  font-weight: 700;
  font-size: 18px;
  color: rgba(255, 255, 255, 0.99);
  margin: 0;
  word-break: break-all;
  cursor: pointer;
  display: inline;
}
.question-card-right-content{
  min-width: 0;
  color: rgba(255, 255, 255, 0.99);
}
.question-card-right-content div{
  display: block;
  width: 98%;
  height: 20px;
  overflow: hidden;
  text-overflow:ellipsis;
  white-space: nowrap;
}
.question-card-right-detail{
  position: relative;
  display: grid;
  grid-template-columns: repeat(2,auto);
}

.question-card-right-detail-tag{
  background: rgba(23, 177, 48, 0.33);;
  flex-shrink: 0;
  height: 20px;
  line-height: 19px;
  margin-right: 8px;
  border-radius: 2px;
}
.question-card-right-detail-tag span{
  padding: 0 8px;
  font-size: 12px;
  color: #67c23a;
  font-weight: 400;
}

.question-card-right-detail-icon{
  display: flex;
  align-items: center;
  justify-content: space-around;
  text-align: center;
  vertical-align: center;
  color: white;
  margin-left: 20px;
}
.question-card-right-detail-icon svg-icon{
  font-size: 16px;
  padding: 5px;
}
.question-card-right-detail-icon span{
  padding: 5px;
  font-size: 12px;
}
.question-owner{
  position: absolute;
  right: 0;
  padding: 5px 10px;
  font-size: 12px;
}
.question-card:hover .question-reply-button{
  opacity: 1;
  transition: opacity 1s 0s ease;
}
.question-reply-button{
   opacity: 0;
  transition: opacity 1s 0s ease;
}
.question_card_approved{
  color: #5dcaad;
}


.question-card-accept{
  border: 1px solid #7ed6bf87;
  margin: 10px 10px 5px 10px;
  border-radius: 4px;
}
.question-card-accept-value{
  text-align: center;
  font-size: 18px;
  color: #5dcaad;
  /*display: flex;*/
  /*align-items: center;*/
  /*justify-content: space-around;*/
  /*flex-direction: column;*/
}
.question-card-accept-label{
  padding: 5px 0;
  text-align: center;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-direction: column;
}
</style>
