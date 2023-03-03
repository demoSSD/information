import Vue from 'vue'
import 'normalize.css/normalize.css'// A modern alternative to CSS resets
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN'
import App from './App'
import router from './router'
import store from './store'
import '@/icons' // icon
import '@/permission' // 权限
import {default as api} from './utils/api'
import directives from "@/directives";
import './fonts/font.css'
import './assets/css/cartoon.css'
import './assets/css/global.css'
import {hasPermission} from "./utils/hasPermission";
import base from './globalFun/base'//引用
Vue.use(ElementUI, {locale})
Vue.prototype.api = api
Vue.use(base);//将全局函数当做插件来进行注册
//全局的常量
Vue.prototype.hasPerm = hasPermission
Vue.use(directives);

//生产环境时自动设置为 false 以阻止 vue 在启动时生成生产提示。
Vue.config.productionTip = (process.env.NODE_ENV != 'production')
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: {App}
})
Vue.prototype.getDate = function (dateTimeStamp) {

  const minute = 1000 * 60;
  const hour = minute * 60;
  const day = hour * 24;
  const halfamonth = day * 15;
  const month = day * 30;
  const year = month * 12;
  if (dateTimeStamp === undefined) {
    return false;
  } else {
    dateTimeStamp = dateTimeStamp.replace(/\-/g, "/");
    // console.log(dateTimeStamp)
    // console.log( new Date(dateTimeStamp))
    const sTime = new Date(dateTimeStamp).getTime();//把时间pretime的值转为时间戳
    const now = new Date().getTime();//获取当前时间的时间戳

    const diffValue = now - sTime;

    if (diffValue < 0) {
      // console.log("结束日期不能小于开始日期！");
      return dateTimeStamp
    }

    const monthC = diffValue / month;
    const weekC = diffValue / (7 * day);
    const dayC = diffValue / day;
    const hourC = diffValue / hour;
    const minC = diffValue / minute;
    const yearC = diffValue / year;
    if (yearC >= 1) {
      // console.log(yearC + "年前");
      return dateTimeStamp
    } else if (monthC >= 1) {
      // console.log(parseInt(monthC) + "个月前");
      return dateTimeStamp
    }
    else if (weekC >= 1) {
      //  console.log(parseInt(weekC) + "周前")
      return dateTimeStamp
    }
    else if (dayC >= 1) {
      //  console.log(parseInt(dayC) + "天前")
      return dateTimeStamp
    }
    else if (hourC >= 1) {
      //  console.log(parseInt(hourC) + "个小时前")
      return parseInt(hourC) + "个小时前"
    }
    else if (minC >= 1) {
      // console.log(parseInt(minC) + "分钟前")
      return parseInt(minC) + "分钟前"
    } else {
      //  console.log("刚刚")
      return "刚刚"
    }
  }

}
