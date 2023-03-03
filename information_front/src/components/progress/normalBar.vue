<template>
  <div class="normalBar" :style="{display:over?'none !important':'inline-block !important'}">
    <div class="normalBar-n" :style="{width:value+'%'}"/>
    <div class="normalBar-text" :style="{fontSize:fontSize+'px',visibility:showText?'visible':'hidden'}">
          {{value}}%
    </div>
  </div>
</template>

<script>
export default {
  name: "normalBar",
  props:{
    value:{
      type:Number,
      default:0
    },
    fontSize:{
      type:Number,
      default: 10
    },
    showText:{
      type:Boolean,
      default:false
    },
    over:{
      type:Boolean,
      default:false
    }
  }
}
</script>

<style scoped>
.normalBar{
  width: 100%;
  height: 100%;
  background: rgb(23, 44, 60);
  border-radius: 2px;
  display: inline-block !important;
}
.normalBar-n{
  width: 0;
  height: 100%;
  background: rgba(70, 180, 77, 0.65);
  border-radius: 2px;
  transition: width 1s ease;
}
.normalBar-text{
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-around;
  flex-direction: column;
  /*left:0;*/
  top:-100%;
  min-font-size: 8px;
  font-size: 14px;
  line-height: 100%;
  color: white;
}
</style>
