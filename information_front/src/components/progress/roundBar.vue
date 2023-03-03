<template>
<div class="roundBar">
  <div class="roundBar-n">

  </div>
</div>
</template>

<script>
export default {
  name: "roundBar"
}
</script>

<style scoped>

.roundBar{
  width: 100%;
  height: 100%;
  background: rgb(23, 44, 60);
  border-radius: 10% 10% 10% 10% /50% 50% 50% 50%;
}
.roundBar-n{
  width: 0;
  height: 100%;
  background: rgba(70, 180, 77, 0.65);
  border-radius: 10% 10% 10% 10% /50% 50% 50% 50%;
}
</style>
