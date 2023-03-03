export default{
  install(Vue)  {
    //是否包含空格
    Vue.prototype.strContainSpace = function (str){
      return !/^\S*$/.test(str);
    };
    //是否是空串
    Vue.prototype.strIsNULL = function (str){
      return str === '' || !/[^\s]/.test(str) || str === null || str === undefined;
    };
    //数组去重
    Vue.prototype.doWeight = function (arr){
      return Array.from(new Set(arr))
    };
    //是否是数字
    Vue.prototype.IsNumber = function (value){
      return (typeof value === 'number' && !isNaN(value));
    };
    //是否是正整数
    Vue.prototype.isPositiveInt = function (num){
      if(num==="")
        return false
      return /(^[1-9]\d*$)/.test(num);
  };
    //密码验证^[0-9a-zA-Z]{4,12}$
    Vue.prototype.confirmPassWord = function (str){
      return /^[0-9a-zA-Z]{6,30}$/.test(str);
    };
    //设置dom高度
    Vue.prototype.setDomHeight = function (name){
      const panel = document.getElementById(name);
      if (panel.style.maxHeight) {
        panel.style.maxHeight = null;
      } else {
        panel.style.maxHeight = panel.scrollHeight + "px";
      }
    };
    Vue.prototype.strPattern = function (str,pattern){
      return pattern.test(str);
    };
  }
}
