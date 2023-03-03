<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import {nanoid} from "nanoid";

export default {
  name: 'app',
  created() {
    let tem=sessionStorage.getItem("temId")
    if(tem===null)
       sessionStorage.setItem("temId",nanoid(10))
    this.$store.dispatch('setTemId',sessionStorage.getItem("temId"))
  }
}

</script>

<style lang="scss">
  @import '~normalize.css/normalize.css';// normalize.css 样式格式化
  @import './styles/index.scss'; // 全局自定义的css样式

</style>
