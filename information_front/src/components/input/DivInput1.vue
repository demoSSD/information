<template>
  <div class="login_box">
    <!-- required就是不能为空 必须在css效果中有很大的作用 -->
    <!-- 可以简写为required -->
    <input type="text" required />
    <label><svg-icon icon-class="user"/>设置用户名<span>{{}}</span></label>
  </div>
</template>

<script>
export default {
  name: "DivInput1",
}
</script>

<style scoped>
.login_box {
  /* 相对定位 */
  position: relative;
  width: 100%;
}
.login_box input {
  /* 清除input框自带的边框和轮廓 */
  outline: none;
  border: none;
  width: 100%;
  padding: 10px 0;
  margin-bottom: 30px;
  color: #fff;
  font-size: 16px;
  border-bottom: 1px solid #fff;
  /* 背景颜色为透明色 */
  background-color: transparent;
}
.login_box label {
  position: absolute;
  top: 0;
  left: 0;
  padding: 10px 0;
  color: #fff;
  /* 这个属性的默认值是auto 默认是这个元素可以被点击 但是如果我们写了none 就是这个元素不能被点击 , 就好像它可见但是不能用 可望而不可即 */
  /* 这个就是两者的区别 */
  pointer-events: none;
  /* 加个过渡 */
  transition: all 0.5s;
}
.login_box label span {
  color: #f0443c;
  margin-left: 10px;
}
.login_box label svg {
  margin-right: 10px;
}
/* :focus 选择器是当input获得焦点是触发的样式 + 是相邻兄弟选择器 去找与input相邻的兄弟label */
/* :valid 选择器是判断input框的内容是否合法,如果合法会执行下面的属性代码,不合法就不会执行,我们刚开始写布局的时候给input框写了required 我们删掉看对比 当没有required的话input框的值就会被认为一直合法,所以一直都是下方的样式 ,但是密码不会,密码框内的值为空,那么这句话局不合法,required不能为空 当我们给密码框写点东西的时候才会执行以下代码*/
.login_box input:focus + label,
.login_box input:valid + label {
  top: -20px;
  color: #03e9f4;
  font-size: 12px;
}
</style>
