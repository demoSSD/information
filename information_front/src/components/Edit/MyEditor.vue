<template>
  <div class="myEdit" style="border: 1px solid #ccc;border-radius:0 0 5px 5px">
    <Toolbar
      style="border-bottom: 1px solid #ccc;"
      :editor="editor"
      :defaultConfig="toolbarConfig"
      :mode="mode"

    />
    <Editor
      style="height:400px; overflow-y: hidden;border-radius:0 0 5px 5px"
      v-model="html"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="onCreated"
    />
  </div>
</template>
<script>
import "@wangeditor/editor/dist/css/style.css"
import Vue from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import {mapGetters} from "vuex";
import {getToken} from "../../utils/auth";

export default Vue.extend({
  components: { Editor, Toolbar },
  data() {
    return {
      editor: null,
      html: '',
      toolbarConfig: { },
      editorConfig: { placeholder:'' },
      mode: 'default', // or 'simple'
    }
  },
  created() {
    this.editorConfig={
      placeholder: '###### 问题遇到的现象和发生背景<br/><br/>' +
                   '###### 用代码块功能插入代码，请勿粘贴截图<br/><br/>' +
                   '###### 运行结果及报错内容<br/><br/>' +
                   '###### 我的解答思路和尝试过的方法<br/><br/>' +
                   '###### 我想要达到的结果<br/>',
      MENU_CONF: {
        // 配置上传图片
        uploadImage: {
          server: "/api/question/addImage",
          fieldName: 'file',
          // 自定义增加 http  header
          headers: {
            "user":getToken()
          },
          // 单个文件上传成功之后
          onSuccess(file, res) {  // TS 语法
            // onSuccess(file, res) {          // JS 语法
            console.log(`${file.name} 上传成功`, res)
          },
          // 单个文件上传失败
          onFailed(file, res) {   // TS 语法
            console.log(`${file.name} 上传失败`, res)
          },
          // 上传错误，或者触发 timeout 超时
          onError(file, err, res) {  // TS 语法
            console.log(`${file.name} 上传出错`, err, res)
          },
        },
      },
    }
  },
  watch:{
    'html':function (val,old){
       // console.log(val,old)
       this.$emit('editOk',val)
    }
  },
  methods: {
    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },
    clear(){
      this.html=''
    }
  },
  mounted() {

  },
  beforeDestroy() {
    const editor = this.editor
    if (editor == null) return
    editor.destroy() // 组件销毁时，及时销毁编辑器
  }
})
</script>
<style scoped>
.myEdit /deep/ .w-e-text-container {
  background-color: #fff0;
  color: white;
  height: 100%;
  position: relative;
}
.myEdit /deep/ .w-e-text-container [data-slate-editor] pre>code {
  background-color: #303133;
  border: 1px solid #606266;
  border-radius: 4px 4px;
  display: block;
  font-size: 14px;
  padding: 10px;
  text-indent: 0;
}
</style>
