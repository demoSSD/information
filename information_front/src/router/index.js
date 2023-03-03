import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
export const constantRouterMap = [
  {path: '/home', component: _import('home/home'), hidden: true},
  {path: '/questionPage', component: _import('questionPage/questionPage'), hidden: true},
  {
    path: '/qaCommunity',
    component: _import('question/qaCommunity'),
    redirect: '/qaCommunity/channel',
    hidden: true,
    name:'问答社区',
    children: [{
        path: 'channel',
        meta: {title: '问答广场', icon: 'squere'},
        component: _import('question/section/channel')
    },
      {
        path: 'edit',
        meta: {title: '发帖求助', icon: 'issue'},
        component: _import('question/section/edit')
      },{
        path: 'automatic',
        meta: {title: '智能回答', icon: 'automatic'},
        component: _import('question/section/automatic')
      }]
  },
  {path: '/voiceText', component: _import('voiceText/voiceText'), hidden: true},
  {path: '/login', component: _import('login/index'), hidden: true},
  // {path: '/register', component: _import('login/register'), hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: '首页',
    hidden: true,
    children: [{
      path: 'dashboard', component: _import('dashboard/index')
    }]
  }
]
export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export const asyncRouterMap = [
  {
    path: '/system',
    component: Layout,
    redirect: '/system/article',
    name: '功能模块',
    meta: {title: '功能模块', icon: 'tree'},
    children: [
      {
        path: 'article',
        name: '文章',
        component: _import('article/article'),
        meta: {title: '文章', icon: 'example'},
        menu: 'article',
      },
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/',
    name: '',
    meta: {title: '用户权限', icon: 'table'},
    children: [
      {
        path: '', name: '用户列表', component: _import('user/user'), meta: {title: '用户列表', icon: 'user'}, menu: 'user'
      },
      // {
      //   path: 'webSetting',
      //   name: '网站设置',
      //   component: _import('user/detailSetting'),
      //   meta: {title: '网站设置', icon: 'web'},
      //   menu: 'webSetting',
      // },
      {
        path: 'role',
        name: '权限管理',
        component: _import('user/role'),
        meta: {title: '权限管理', icon: 'password'},
        menu: 'role',
      },
      {
        path: 'userSetting',
        name: '用户设置',
        component: _import('user/userSetting'),
        meta: {title: '用户设置', icon: 'setting'},
        menu: 'userSetting',
      },
      {
        path: 'accountSetting',
        name: '账户设置',
        component: _import('user/accountSetting'),
        meta: {title: '账户设置', icon: 'setting'},
        menu: 'accountSetting',
      },
      {
        path: 'commends',
        name: '评论记录',
        component: _import('user/commends'),
        meta: {title: '评论记录', icon: 'commends'},
        menu: 'commends',
      },
      {
        path: 'questionControl',
        name: '问题管理',
        component: _import('user/questionControl'),
        meta: {title: '问题管理', icon: 'questionControl'},
        menu: 'questionControl',
      },
    ]
  },
  {path: '*', redirect: '/404', hidden: true}
]
