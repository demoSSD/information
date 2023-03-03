<template>
  <button class="divButton3"><span class="fa">{{label}}</span></button>
</template>

<script>
export default {
  name: "DivButton3",
  props:{
    label:{
      type:String,
      default:" "
    }

  }
}
</script>

<style scoped>
.divButton3 {
  /* 溢出隐藏 */
  overflow: hidden;
  /* 相对定位 */
  position: relative;
  width: 90px;
  height: 90px;
  text-align: center;
  margin: 0 10px;
  border-radius: 25px;
  background-color: #fff;
  /* 盒子阴影 */
  box-shadow: 0 5px 15px -5px rgba(0, 0, 0, 0.1);
  border: none;
}
.fa {
  /* 字体图标可以直接使用字体大小来设置大小 */
  font-size: 38px;
  /* 缩放0.8倍 */
  transform: scale(0.8);
  /* 行高 让这个字体图标垂直居中于盒子内 */
  line-height: 90px;
  /* 设置一下过渡 */
  /* 第三个值是贝塞尔曲线 让运动曲线有无限可能 可以到官网自行探索 https://cubic-bezier.com/ */
  transition: all 0.4s cubic-bezier(0.31, -0.1, 0.43, 1.59);
}
.divButton3::before {
  content: "";
  /* 绝对定位 */
  position: absolute;
  top: 90%;
  left: -110%;
  width: 120%;
  height: 120%;
  /* 顺时针旋转45度 */
  transform: rotate(45deg);
  transition: all 0.4s cubic-bezier(0.31, -0.1, 0.43, 1.59);
}

.divButton3 .fa {
  color: #3b5988;
}
.divButton3::before {
  background-color: #3b5988;
}
.divButton3:hover::before {
  top: -10%;
  left: -10%;
}
.divButton3:hover .fa {
  color: #fff;
  /* 鼠标移入 恢复到原来的大小 */
  transform: scale(1);
}

</style>
