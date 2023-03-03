<template>
<div class="voiceText">
  <Header/>
  <div class="voiceText-main">
     <div class="voiceText-selects">
     <div :class="[modeFlag?'voiceText-selects-selected':'voiceText-selects-normal']" @click="changeMode(true)"><svg-icon icon-class="voiceFile"/>现有音频</div>
     <div :class="[!modeFlag?'voiceText-selects-selected':'voiceText-selects-normal']" @click="changeMode(false)"><svg-icon icon-class="record"/>在线录制</div>
   </div>
     <div id="drag"  v-show="modeFlag" class="voiceText-contain" >
       <div v-show="fileListIsNull"  class="voiceText-show">
         <div><svg-icon icon-class="FileAdd"/></div>
         <div>拖入音频文件或</div>
         <div>
           <div>
             <el-button @click="selectFolder(true)" round class="el-button-edit voiceText-but-font" icon="el-icon-folder-add">选择文件夹</el-button>
             <el-button @click="selectFile" round class="el-button-edit voiceText-but-font" icon="el-icon-document-add">选择文件</el-button>
           </div>
         </div>
         <div>
           支持<span>MP3、M4A、WMA、AAC、FLAC、AC3、M4R、APE、OGG、WAV</span>格式
         </div>
         <div>
           最多可批量上传8个文件
         </div>
       </div>
       <div v-show="!fileListIsNull"   class="voiceText-list">
         <div class="voiceText-list-header voiceText-list-grid">
           <div>序号</div>
           <div>原文件名</div>
           <div>大小</div>
           <div>音频时长</div>
           <div>状态</div>
           <div>操作</div>
         </div>
         <div :class="['voiceText-list-item','voiceText-list-grid','scale-in-center',showResult.id===item.id?'voiceText-list-item-selected':'']"
              @click="setShowResult(item)" v-for="(item,index) in fileList" :key="index">
           <div><SortTag :value="index+1"/></div>
           <div>{{item.name}}</div>
           <div>{{item.size}}</div>
           <div>{{item.time}}</div>
           <div>
             <div>
               {{item.status}}
               <normal-bar  :over="item.upFlag" :value="item.upProgress"  style="margin:0 10px;height: 8px;width: 120px"/>
               <svg-icon v-if="item.startTrans&&!item.transFlag" class="rotateAlways" icon-class="wait"/>
             </div>
           </div>
           <div>
             <div @click="deleteItem(index)"><svg-icon class="voiceText-icon-warn" icon-class="delete"/></div>
             <div @click="transOneFile(item)"><svg-icon class="voiceText-icon-normal"  icon-class="trans"/></div>
           </div>
         </div>
         <div class="voiceText-list-foot">
           <div><el-button @click="cleanAll" class="el-button-edit voiceText-but-font" round icon="el-icon-delete">清空文件</el-button></div>
           <div><el-button @click="selectFolder(true)" class="el-button-edit voiceText-but-font" round icon="el-icon-folder-add">选择文件夹</el-button></div>
           <div><el-button @click="selectFile" class="el-button-edit voiceText-but-font" round icon="el-icon-document-add">添加文件</el-button></div>
           <div><el-button :disabled="false" @click="startTransform(true)" class="el-button-edit voiceText-but-font" round icon="el-icon-refresh">开始转换</el-button></div>
         </div>
       </div>
     </div>
     <div v-show="!modeFlag" class="voiceText-contain">
      <div class="voiceText-record-show">
        <div class="voiceText-record-card">
          <div id="record-tip"><svg-icon icon-class="recordIng"/>录音中...</div>
          <div><audio id='audio' controls autoplay></audio></div>
          <div>
            <div style="width: 300px; display: inline-flex">
              <el-button id="btn-start-recording" class="el-button-edit" round size="mini"><svg-icon icon-class="record"/>开始录音</el-button>
              <el-button id="btn-stop-recording"    round size="mini"><svg-icon icon-class="over"/>结束录音</el-button>
              <el-button id="btn-trans-recording" :disabled="recordIng"  @click="startTransform(false)"  class="el-button-accept" round size="mini"><svg-icon icon-class="start"/>开始装换</el-button>
            </div>
          </div>
          <div>
            {{file.status}}
          </div>
        </div>
      </div>
     </div>
     <div :class="['voiceText-result',isShow?'result-show':'hide']">
       <div>
          <span style="float: left;">{{showResult.name | showResultTitle}}</span>
           <div @click="copy">
           <svg-icon icon-class="copy" />
           </div>
          <div @click="changeIsShow(false)" >
            <svg-icon icon-class="hidden"/>
          </div>
      </div>
       <div>
           <div>
            状态： {{showResult.status}}
            <normal-bar  :over="showResult.upFlag" :value="showResult.upProgress" :show-text="true" style="height: 18px;width: 50%;margin: auto 10px"/>
            <svg-icon v-if="showResult.startTrans&&!showResult.transFlag" style="margin: auto 10px" class="rotateAlways" icon-class="wait"/>
          </div>
           <div style="text-indent: 2em;" id="voiceTextPan" v-if="showResult.transFlag">{{showResult.result}}</div>

       </div>
       <div class="copy-tip">
         <div id="copy_tip">复制成功</div>
       </div>
     </div>
  </div>

</div>
</template>

<script>
import Header from "../../components/Header/Header";
import SortTag from "../../components/SortTag/SortTag";
import {mapGetters} from "vuex";
import {nanoid} from "nanoid";
import RoundBar from "../../components/progress/roundBar";
import NormalBar from "../../components/progress/normalBar";

export default {
  name: "voiceText",
  components: {NormalBar, RoundBar, SortTag, Header},
  data() {
    return {
        modeFlag:true,
        isShow:false,
        fileList:[],
        file:{id:1,name:'临时录音.wav',size:'0.00MB',time:'00:00:00',status:'待转换',file:null,upFlag:false,upProgress:0,transFlag:false,startTrans:false,url:'',result:''},
        fromat:['.mp3','.m4a','.wma','.aac','.flac','.ac3','.m4r','.ape','.ogg','.wav'],
        showResult:{},
    };
  },
  computed:{
    ...mapGetters(['temId']),
    fileListIsNull(){
      return this.fileList.length===0
    },
    recordIng(){
      return this.file.file === null;
    },
  },

  mounted() {
     this.audioInit()
     this.initDrag()
  },
  methods: {
      initDrag(){
        document.getElementById("copy_tip").addEventListener('animationend',function(){
          this.classList.remove('show2s');
        });
        let _this=this
        const drag = document.getElementById('drag')
        drag.addEventListener('drop', e => {
          e.preventDefault()
          _this.addFiles(e.dataTransfer.files)
        },false)
        drag.addEventListener("dragover", e => {
          e.preventDefault();
        });
      },
      audioInit(){
      let _this=this
      const startButton = document.getElementById('btn-start-recording');
      const stopButton = document.getElementById('btn-stop-recording');
      const tip = document.getElementById('record-tip');
      const audio = document.getElementById('audio');
      let file;
      let mediaRecorder;
      tip.style.opacity="0"
      stopButton.style.display="none"
      startButton.style.display="block"
      //开始按钮
      startButton.onclick = function() {
        startButton.style.display="none"
        stopButton.style.display="block"
        tip.style.opacity="1"
        _this.file.url=''
        _this.file.result=null
        _this.file.upProgress=0
        _this.file.startTrans=false
        _this.file.status='录音中'
        _this.file.file=null
        _this.file.transFlag=false
        _this.file.upFlag=false
        navigator.mediaDevices.getUserMedia({
          audio: true
        })
          .then(onMicrophoneCaptured)
          .catch(onMicrophoneCaptureError);
      };
      //停止按钮
      stopButton.onclick = function() {
        stopButton.style.display="none"
        startButton.style.display="block"
        tip.style.opacity="0"
        mediaRecorder.stop();
        mediaRecorder.onstop = function(e) {
          audio.controls = true;
          audio.src = window.URL.createObjectURL(file);
        }
      }
      function onMicrophoneCaptured(stream) {
        mediaRecorder = new MediaRecorder(stream);
        mediaRecorder.start();
        mediaRecorder.ondataavailable = function(e) {
          file = new File([e.data], '临时录音.wav',{
            type: 'audio/wav',lastModified: Date.now(),
          });
          _this.file.file=file
          _this.file.status='待上传'
          _this.changeIsShow(true)
          _this.showResult=_this.file
        }
      }
      function onMicrophoneCaptureError() {
        alert('浏览器不支持或是用户不允许，请关闭浏览器再来一次')
      }
    },
      changeMode(value) {
        if(!value)
          this.showResult=this.file
        this.modeFlag=value
      },
      cleanAll() {
        this.fileList=[]
        this.showResult={}
        this.changeIsShow(false)
      },
      selectFolder(dir){

        // 创建input标签
        const inputObj = document.createElement('input');
        // 设置属性
        inputObj.setAttribute('id','_ef');
        inputObj.setAttribute('type','file');
        inputObj.setAttribute("style",'visibility:hidden');
        if(dir){
          // 如果要选择路径，则添加以下两个属性
          inputObj.setAttribute('webkitdirectory', "");
          inputObj.setAttribute('directory', "");
        }
        // 添加到DOM中
        document.body.appendChild(inputObj);
        // 添加事件监听器
        inputObj.addEventListener("change",this.updatePath);
        // 模拟点击
        inputObj.click();
      },
      // 打开文件选择器input标签的change事件响应
      updatePath(){
        const inputObj = document.getElementById("_ef");
        let files = inputObj.files;
        this.addFiles(files)
        try{
          // 移除事件监听器
           inputObj.removeEventListener("change",function(){});
           // 从DOM中移除input
           document.body.removeChild(inputObj);
           // document.body.removeChild(audio);
          }catch (error) {
        this.$message({
          message: error,
          type: 'error'
        });
      }
    },
      selectFile(){
          if(this.fileList.length>=8){
            this.$message({
              message: "最多支持上传8个音频文件",
              type: 'warning'
            });
            return
          }
          let _this=this
          let input = document.createElement('input');
          input.value = '选择文件';
          input.type = 'file';
          input.accept='.mp3,.m4a,.wma,.aac,.flac,.ac3,.m4r,.ape,.ogg,.wav'
          let file;
          input.onchange = event => {
            file = event.target.files[0];
            const tem=file.name.substring(file.name.lastIndexOf("."))
            if(this.fromat.includes(tem)){
              let audio=document.createElement("audio");
              audio.src =  window.URL.createObjectURL(file); //音乐的路径
              let duration = 0;
              audio.addEventListener("canplay", function () {
                duration=(audio.duration).toFixed(0)
                const h=(duration/3600).toFixed(0)
                const m=((duration%3600)/60).toFixed(0)
                const s=duration%60
                const H=h<10?'0'+h:h
                const M=m<10?'0'+m:m
                const S=s<10?'0'+s:s
                _this.addOneFile({
                  id:nanoid(5),
                  name:file.name,
                  size:(file.size/(1024*1024)).toFixed(2)+'MB',
                  time:H+':'+M+':'+S,
                  status:'正在上传',
                  file:file,
                  upFlag:false,
                  transFlag:false,
                  url:'',
                  result:null,
                  upProgress:0,
                  startTrans:false,
                })
              })
            }
            else {
              this.$message({
                message: '不支持文件格式',
                type: 'error'
              });
            }

          };
          input.click();
      },
      startTransform(value){
        if(value)
          this.fileList.map(item=>{
            this.transOneFile(item)
          })
        else{
          this.upLoadOneFile(this.file,true)
        }

      },
      deleteItem(index){
        this.fileList.splice(index,1)
      },
      addFiles(files){
        let oldCount=this.fileList.length
        let count=this.fileList.length
        for(let i=0; i<files.length; i++){
          if(count>= 8) {
            this.$message({
              message: '最多支持8个音频文件',
              type: 'warning'
            });
            break
          }
          const tem=files[i].name.substring(files[i].name.lastIndexOf("."))
          if(this.fromat.includes(tem)){
            count++
            let audio=document.createElement("audio"); //生成一个audio元素
            audio.src =  window.URL.createObjectURL(files[i]); //音乐的路径
            let h,m,s,H,M,S
            audio.load()
            audio.addEventListener("canplay", () =>{
              const duration=(audio.duration).toFixed(0)
              h=(duration/3600).toFixed(0)
              m=((duration%3600)/60).toFixed(0)
              s=duration%60
              H=h<10?'0'+h:h
              M=m<10?'0'+m:m
              S=s<10?'0'+s:s
              this.addOneFile({
                id:nanoid(5),
                name:files[i].name,
                size:(files[i].size/(1024*1024)).toFixed(2)+'MB',
                time:H+':'+M+':'+S,
                status:'正在上传',
                file:files[i],
                upFlag:false,
                transFlag:false,
                url:'',
                result:null,
                upProgress:0,
                startTrans:false,
              })
            })
          }
        }
        if(count===oldCount&&count<8){
          this.$message({
            message: '无相关音频文件',
            type: 'warning'
          });
        }
        return count
      },
      addOneFile(file){

        if(this.fileList.length>=8){
          this.$message({
            message: "最多支持上传8个音频文件",
            type: 'warning'
          });
          return
        }
        this.fileList.push(file)
        this.upLoadOneFile(file)
        return this.fileList.length
      },
      upLoadOneFile(item,flag=false){
        if(item.upFlag)
          return
        let temId=this.temId
        if(temId===null){
          temId=sessionStorage.getItem("temId")
        }
        let formData = new FormData()
        formData.append("file", item.file);
        formData.append("temId",temId)
        formData.append("fileName",item.file.name)
        let config={
          timeout:60 * 1000,
          headers:{"Content-Type": "multipart/form-data"},
          onUploadProgress: progressEvent => {
            const value = (progressEvent.loaded / progressEvent.total * 100 | 0)		//上传进度百分比
            item.status="正在上传"
            item.upProgress=value
          }
        }
        this.api.post("/voiceText/uploadVoice",formData,config).then(res=>{
          item.status="待转换"
          item.upFlag=true
          //item.url='/api'+res.relativeUrl
          item.url=res.relativeUrl
          if(flag)
            this.transOneFile(item)
        }).catch(e=>{
          item.status="上传失败"
          item.progress=0
          this.$message.error("上传失败")
        })
      },
      transOneFile(item){
        if(!item.upFlag||item.startTrans)
          return
        //const url="http://localhost:9520"+item.url
        //const url="http://150.158.22.107"+item.url
        const url=item.url
        item.status="转换中"
        let config={
          timeout:10 * 60 * 1000
        }
        item.startTrans=true
        this.api.post("/voiceText/voiceToText",{url},config).then(res=>{
          item.transFlag=true
          item.status="转换完成"
          item.result=res.result
          item.startTrans=true
        }).catch(e=>{
           item.status="转换失败"
           item.startTrans=false
           item.transFlag=false
           this.$message.error("转换失败")
        })
      },
      setShowResult(item){
         this.showResult=item
         this.changeIsShow(true)
      },
      changeIsShow(value){
        this.isShow=value
      },
      copy(){
        const copyText = document.getElementById("voiceTextPan").innerText;
        const aux = document.createElement("input");
        aux.setAttribute("value", copyText);
        document.body.appendChild(aux);
        aux.select();
        document.execCommand("copy");
        document.body.removeChild(aux);
        document.getElementById("copy_tip").classList.add('show2s');
      }

  },
  filters:{
     showResultTitle(value){
      if(value===undefined)
        return "--"
       let tem=value
       if(value.length>8)
         tem=value.substring(0,8)+"..."
       return tem
     }
   }
}
</script>

<style scoped>
*{
  font-family:得意黑,sans-serif;
}
.voiceText{
  background: url("../../assets/background/background8.jpg") no-repeat;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
  text-align: center;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-direction: column;
}
.voiceText-main{
  --select-w:120px;
  --select-h: 50px;
  --show-mar-top:10px;
  --show-w:52%;
  --show-h:500px;
  width:var(--show-w);
  height:var(--show-h);
}
.voiceText-selects{
  width: 100%;
  display: flex;
}
.voiceText-selects div{
  height:var(--select-h);
  width: var(--select-w);
  border-radius: 5px;
  margin: 0 20px 0 0;
  display: inline-flex;
  align-items: center;
  text-align: center;
  justify-content: space-around;
  padding: 0 10px;
  font-weight: bold;
  font-size: 18px;
}
.voiceText-selects-normal{
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
}
.voiceText-selects-normal:hover{
  background: rgba(14, 24, 39, 0.64);
}
.voiceText-selects-selected{
  background: #3b990d;
}
.voiceText-contain{
  width: 100%;
  height: calc(100% - var(--select-h) - var(--show-mar-top));
  margin-top: var(--show-mar-top);
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
  border-radius: 5px;
}
.voiceText-show{
  --row1-h:200px;
  --row2-h:20px;
  --row3-h:90px;
  --row4-h:65px;
  --row5-h:65px;
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-columns: 100%;
  grid-template-rows: var(--row1-h) var(--row2-h) var(--row3-h) var(--row4-h) var(--row5-h);
}
.voiceText-show div{
  width: 100%;
  height: 100%;
  padding: 0;
  display: flex;
  align-items: center;
  text-align: center;
  justify-content: space-around;
  flex-direction: column;
}
.voiceText-show div:nth-child(1) svg{
  width:var(--row1-h);
  height: var(--row1-h);
}
.voiceText-show div:nth-child(2){
  font-size: 18px;
  font-weight: bold;
}
.voiceText-show div:nth-child(3) div{
  width: 40%;
  --but-w:48%;
  display: grid;
  grid-template-rows: 100%;
  grid-template-columns: var(--but-w) var(--but-w);
  grid-column-gap: calc(100% - var(--but-w)*2);
}
.voiceText-but-font{
  font-weight: bold;
  font-size: 16px;
}
.voiceText-show div:nth-child(4){
  display: inline;
  font-size: 16px;
  font-weight: bold;
  padding: 20px 0;
}
.voiceText-show div:nth-child(4) span{
 color: #5dcaad;
}
.voiceText-show div:nth-child(5){
  display: inline;
  font-size: 16px;
  font-weight: bold;
}
.voiceText-list{
  border-radius: 5px;
  --list-h:9.5%;
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-columns:100%;
  grid-template-rows:repeat(9,var(--list-h)) calc(100% - var(--list-h)*9);
}
.voiceText-list-grid{
  display: grid;
  grid-template-columns: 8% 30% 10% 14% 23% 15%;
  grid-template-rows: 100%;
}
.voiceText-list-header{
  border-radius: 5px 5px 0 0;
  grid-row-start: 1;
  grid-column-start: 1;
  background: rgba(14, 24, 39, 0.65);
}
.voiceText-list-header div{
  display: inline-flex;
  align-items: center;
  text-align: center;
  justify-content: space-around;
  font-weight: bold;
  font-size: 18px;
  font-family:得意黑,sans-serif;
  border-radius: 5px;
}
.voiceText-list-item{

}
.voiceText-list-item-selected{
  background: #35969e78;
}
.voiceText-list-item-selected:hover{
  background: #35969e78!important;
}

.voiceText-list-item div{
  display: inline-flex;
  align-items: center;
  text-align: center;
  justify-content: space-around;
  white-space: nowrap;/*设置不换行*/
  overflow: hidden; /*设置隐藏*/
  text-overflow: ellipsis; /*设置隐藏部分为省略号*/
}
.voiceText-list-item:hover{
  background: rgba(14, 24, 39, 0.65);
}
.voiceText-list-item div:nth-child(6){
  font-size: 24px;
  color: #ff4c4c;
}
.voiceText-list-foot{
  grid-row-start: 10;
  grid-column-start: 1;
  display: grid;
  grid-template-rows: 100%;
  grid-template-columns: repeat(4,1fr);
}
.voiceText-list-foot div{
  display: flex;
  align-items: center;
  text-align: center;
  justify-content: space-around;
  flex-direction: column;
}
.voiceText-record-show{
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-direction: column;
}
.voiceText-record-card{
  width: 70%;
  height: 310px;
  display: grid;
  grid-template-columns: 100%;
  grid-template-rows: 30px 50px 30px 200px;
  grid-row-gap: 10px;
}
#record-tip{
  display: flex;
  align-items: center;
  justify-content: space-around;
  font-size: 18px;
  font-weight: bold;
  padding: 0 210px;
}
.voiceText-record-card div:nth-child(3){
  display: flex;
  align-items: center;
  justify-content: space-around;
}
#btn-start-recording{
  font-weight: bold;
  font-size: 14px;
}
#btn-start-recording svg{
 margin-right: 5px;
}
#btn-stop-recording{
  font-weight: bold;
  font-size: 14px;
  background-color: rgba(255, 76 ,76,0.62);
  border-color: #ff4c4c;
  transition: background-color 1s 0s ease-out;
  color: white;
}
#btn-stop-recording svg{
  margin-right: 5px;
}
#btn-trans-recording{
  font-weight: bold;
  font-size: 14px;
}
#btn-trans-recording svg{
  margin-right: 5px;
}
.voiceText-record-card div:nth-child(4){
  display: flex;
  align-items: center;
  justify-content: space-around;
  padding: 20px;
  font-size: 18px;
  font-family:得意黑,sans-serif;
}
.voiceText-result{
  --mar-left:25px;
  position: relative;
  top:calc(var(--select-h) + var(--show-mar-top) - var(--show-h));
  left: calc(100% + var(--mar-left));
  margin-right:100px;
  width:300px;
  height: calc(var(--show-h) - var(--select-h) - var(--show-mar-top));
  border-radius: 5px;
  background: rgba(29,29,31,0.6);
  backdrop-filter: saturate(180%) blur(20px);
  padding: 10px;
  font-size: 18px;
  line-height: 30px;
  text-align: left;
}
.result-show{
  opacity: 1;
  transition: opacity 0.3s ease;
}
.voiceText-result>div:nth-child(1){
  height: 35px;
  width: 100%;
  border-bottom: rgba(145,132,169,0.63) 1px solid;
}
.voiceText-result>div:nth-child(1) svg{
  color: #5dcaad;
  margin: 0 10px;
  font-size: 19px;
  line-height: 100%;
  float: right;
}
.voiceText-result>div:nth-child(1) svg:hover{
   color: #46b44d;
}
.voiceText-result>div:nth-child(2){
  padding: 10px 5px;
  height: calc(100% - 35px);
  width: 100%;
  overflow-y: auto;
  overflow-x: hidden;
}
.voiceText-result>div:nth-child(2)>div:nth-child(1){
  display: flex;
  align-items: flex-start;
}
.copy-tip{
  position: relative;
  height: calc(100% - 35px);
  width: 100%;
  top:calc(35px - 100%);
  z-index: -2;
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-direction: column;
}
.copy-tip div{
  opacity: 0;
  width: 80px;
  font-size: 16px;
  font-weight: bold;
  height: 25px;
  line-height: 20px;
  background: white;
  color: #3b990d;
  border-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-direction: column;
}
.voiceText-icon-normal{
  color: white;
  font-size: 17px;
}
.voiceText-icon-normal:hover{
  color: #46b44d;
}
.voiceText-icon-warn{
  color: #f0443c;
}
.voiceText-icon-warn:hover{
  color: #a62323;
}

</style>



