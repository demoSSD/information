<template>
  <a href="javascript:void(0)">
    {{label}}
  </a>
</template>

<script>
export default {
  name: "DivButton2",
  props:{
    label:{
      type:String,
      default:" "
    }

  }
}
</script>

<style scoped>
a {
  overflow: hidden;
  position: relative;
  padding: 10px 20px;
  color: #03e9f4;
  /* 取消a表现原有的下划线 */
  text-decoration: none;
  /* 同样加个过渡 */
  transition: all 0.5s;
}
a:hover {
  color: #fff;
  border-radius: 5px;
  background-color: #03e9f4;
  box-shadow: 0 0 5px #03e9f4, 0 0 25px #03e9f4, 0 0 50px #03e9f4,
  0 0 100px #03e9f4;
}

</style>
