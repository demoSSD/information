<template>
  <table>
    <thead>
      <tr>
      <th>序号</th>
      <th>昵称</th>
      <th>用户名</th>
<!--      <th>操作</th>-->
    </tr>
    </thead>
    <tbody v-if="list.length===0">
      <tr >
      <td colspan="4">暂无数据</td>
    </tr>
    </tbody>
    <tbody v-else>
     <tr v-for="(Item,index) in list" :key="Item.nickname">
       <td>{{index+1}}</td>
       <td>{{Item.nickname}}</td>
       <td>{{Item.username}}</td>
<!--       <td>暂无数据</td>-->
    </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  name: "PopTable",
  props:{
    list:{
      type:Array,
      default:[]
    }
  }
}
</script>

<style scoped>
*{
  cursor:pointer
}
table {
  width: 100%;
  /* 让表格的边框合并在一起 */
  border-collapse: collapse;
  display: block;
  max-height: 150px;
  overflow-y: auto;
  overflow-x: hidden;
  table-layout: fixed;
}
thead tr th {
  position:sticky;
  top:0;
}


/* 在做pc端 不注释掉看不到效果 */
table thead tr th {
  color: #fff;
  font-size: 2vm;
  background: rgb(21, 37, 64);
}
/* 还是取单数 */
table thead tr th:nth-child(odd) {
  /*background-color: #324960;*/
}
table thead tr th,
table tbody tr td {
  color: #99a9bf;
  text-align: center;
  /* 先不加 大屏就正常了 */
  padding: 10px 20px;
}
table tbody tr td {
  border: 1px solid rgba(199, 248, 249, 0.13);
}
table tbody tr:nth-child(odd) {
  /*background-color: #f8f8f8;*/
}
</style>
