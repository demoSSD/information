<template>
<div class="sortTag">
  {{value}}
</div>
</template>

<script>
export default {
  name: "SortTag",
  props:{
    value:{
      type:Number,
      default:0
    }
  }
}
</script>

<style scoped>
.sortTag{
  width: 18px;
  height: 18px;
  background-color: #3ac2bc9c;
  color:white;
  font-size: 14px;
  text-align: center;
  font-family: 楷体,sans-serif;
  font-weight: bold;
  border: 2px solid #3ac2bc9c;
  display: inline-flex;
  align-items: center;
  justify-content: center;/*垂直居中*/
  white-space: nowrap;
}
</style>
