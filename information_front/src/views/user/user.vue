<template>
  <div class="container">
    <table v-show="!dialogFormVisible">
      <thead>
      <tr>
        <th>序号</th>
        <th>昵称</th>
        <th>用户名</th>
        <th>角色</th>
        <th>注册时间</th>
        <!--        <th>更新时间</th>-->
        <th>操作 <span v-permission="'user:add'" @click="showCreate"><svg-icon  icon-class="addUser"/></span></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(Item,index) in list" :key="Item.username">
        <td>{{index+1}}</td>
        <td>{{Item.nickname}}</td>
        <td>{{Item.username}}</td>
        <td><i v-for="item in Item.roles" :key="item.roleName">{{item.roleName}} </i> </td>
        <td>{{Item.createTime}}</td>
        <!--        <td>{{Item.updateTime}}</td>-->
        <td>
          <el-button type="text"  @click="showUpdate(index)" v-permission="'user:update'">修改</el-button>
          <el-button type="text" @click="removeUser(index)" v-permission="'user:update'" v-if="Item.userId!==userId ">删除</el-button>
        </td>
      </tr>
      </tbody>
    </table>
    <div v-show="dialogFormVisible" class="dialog">
      <div class="card" v-if="dialogStatus==='create'">
        <h4>添加用户</h4>
        <div class="card_box">
          <input type="text" required v-model="createUser.username.value" @blur="verify(createUser.username,rules.username)"/>
          <label><svg-icon icon-class="user"/>设置用户名<span>{{createUser.username.tip}}</span></label>
        </div>
        <div class="card_box">
          <input type="password" required="required"  v-model="createUser.password.value" @blur="verify(createUser.password,rules.password)"/>
          <label><svg-icon icon-class="password"/>设置密码<span>{{createUser.password.tip}}</span></label>
        </div>
        <div class="card_box">
          <input required="required"  v-model="createUser.nickname.value" @blur="verify(createUser.nickname,rules.nickname)"/>
          <label><svg-icon icon-class="nickIcon"/>设置昵称<span>{{createUser.nickname.tip}}</span></label>
        </div>
        <div class="card_box">
          <el-select v-model="createUser.roleIds.value" @change="verifyRoleIds(createUser.roleIds)" multiple placeholder="支持多角色" style="width: 300px">
            <el-option
              v-for="item in roles"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId">
            </el-option>
          </el-select>
          <span style="color: #ff4c4c">{{createUser.roleIds.tip}}</span>
        </div>
        <div class="card_but">
          <DivButton2 label="取消" @click.native="dialogFormVisible = false"/>
          <DivButton1 label="创建" @click.native="addUser"/>
        </div>

      </div>
      <div class="card" v-else>
        <h4>修改信息</h4>
        <div class="card_box">
          <input type="password" required="required"  v-model="updateUser.password.value"
                 @blur="verify(updateUser.password,rules.password)"/>
          <label><svg-icon icon-class="password"/>新密码<span>{{updateUser.password.tip}}</span></label>
        </div>
        <div class="card_box">
          <input required="required"  v-model="updateUser.nickname.value"
                 @blur="verify(updateUser.nickname,rules.nickname)"/>
          <label><svg-icon icon-class="nickIcon"/>新昵称<span>{{updateUser.nickname.tip}}</span></label>
        </div>
        <div class="card_box">
          <el-select v-model="updateUser.roleIds.value"  @change="verifyRoleIds(updateUser.roleIds)" multiple placeholder="支持多角色" style="width: 300px">
            <el-option
              v-for="item in roles"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId">
            </el-option>
          </el-select>
          <span style="color: #ff4c4c">{{updateUser.roleIds.tip}}</span>
        </div>
        <div class="card_but">
          <DivButton2 label="取消" @click.native="dialogFormVisible = false"/>
          <DivButton1 label="更新" @click.native="changeUser"/>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import DivButton2 from "../../components/button/DivButton2";
import DivButton1 from "../../components/button/DivButton1";
export default {
  name: "user",
  components: {DivButton2,DivButton1},
  data() {
    return {
      list: [],//表格的数据
      roles: [],//角色列表
      dialogStatus: 'create',
      dialogFormVisible: false,
      rules: {
        username: [{pattern:/^[0-9]{10}$/,message: "请输入10位数字用户名"}],
        password: [{pattern:/^.{6,30}$/,message: "请输入至少6位密码"},{pattern:/^[0-9a-zA-Z]{6,30}$/,message: "密码只能包含数字，大小写字母至少一种"},],
        nickname: [{pattern:/^.{1,30}$/,message: "请输入1-30为字符"},{pattern:/^\S{1,30}$/,message: "昵称不能包含空格"},],
        roleIds:  []
      },
      tempUser: {
        username: '',
        password: '',
        nickname: '',
        roleIds: [],
        userId: ''
      },
      createUser:{
        username: {value:'',tip:''},
        password: {value:'',tip:''},
        nickname: {value:'',tip:''},
        roleIds:  {value:[],tip:''},
      },
      updateUser:{
        username: {value:'',tip:''},
        password: {value:'',tip:''},
        nickname: {value:'',tip:''},
        roleIds:  {value:[],tip:''},
        userId: '',
      },
      totalCount: 0, //分页组件--数据总条数
      listQuery: {
        pageNum: 1,//页码
        pageRow: 50,//每页条数
      },
      listLoading: false,//数据加载等待动画
    }
  },
  created() {
    this.getList();
    if (this.hasPerm('user:add') || this.hasPerm('user:update')) {
      this.getAllRoles();
    }
  },
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  methods: {
    getAllRoles() {
      this.api({
        url: "/user/getAllRoles",
        method: "get"
      }).then(data => {
        this.roles = data.list;
      })
    },
    getList() {
      //查询列表
      this.listLoading = true;
      this.api({
        url: "/user/list",
        method: "get",
        params: this.listQuery
      }).then(data => {
        this.listLoading = false;
        this.list = data.list;
        this.totalCount = data.totalCount;
      })
    },

    handleSizeChange(val) {
      //改变每页数量
      this.listQuery.pageRow = val
      this.handleFilter();
    },
    handleFilter() {
      //查询事件
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleCurrentChange(val) {
      //改变页码
      this.listQuery.pageNum = val
      this.getList();
    },
    getIndex($index) {
      //表格序号
      return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
    },

    showCreate() {
      //显示新增对话框
      this.createUser={
        username: {value:'',tip:''},
        password: {value:'',tip:''},
        nickname: {value:'',tip:''},
        roleIds:  {value:[],tip:''},
      }
      this.dialogStatus = "create"
      this.dialogFormVisible = true
    },
    showUpdate($index) {
      let user = this.list[$index];
      this.updateUser.password.value = '';
      this.updateUser.roleIds.value = user.roles.map(x => x.roleId);
      this.updateUser.nickname.value = user.nickname;

      this.updateUser.username.value = user.username;
      this.updateUser.userId = user.userId;
      this.updateUser.deleteStatus = '1';

      this.dialogStatus = "update"
      this.dialogFormVisible = true
    },

    verify(val,rules,flag=false){
      if(this.strIsNULL(val.value)&&flag){
        val.tip="请输入"
        return false
      }
      if(this.strIsNULL(val.value)){
        val.tip=""
        return false
      }

      for(let i=0;i<rules.length;i++){
        if(!this.strPattern(val.value,rules[i].pattern)){
          val.tip=rules[i].message
          return false
        }
      }
      val.tip=''
      return true
    },
    verifyRoleIds(roleIds){
      if(roleIds.value.length<=0)
      {
        roleIds.tip="请选择权限"
        return false
      }else {
        roleIds.tip=""
        return true
      }

    },
    addUser() {
      const valid=this.verify(this.createUser.username,this.rules.username,true)&&
        this.verify(this.createUser.password,this.rules.password,true)&&
        this.verify(this.createUser.nickname,this.rules.nickname,true)&&
        this.verifyRoleIds(this.createUser.roleIds)
      const form={
        username: this.createUser.username.value,
        password: this.createUser.password.value,
        nickname: this.createUser.nickname.value,
        roleIds: this.createUser.roleIds.value,
        userId: ''
      }
      //添加新用户
      if(valid)
        this.api({
          url: "/user/addUser",
          method: "post",
          data:form
        }).then(() => {
          this.$message.success('添加成功')
          this.getList();
          this.dialogFormVisible = false
        })
    },
    changeUser() {
      let valid=false
      if(this.strIsNULL(this.updateUser.password.value)){
        valid=this.verify(this.updateUser.nickname,this.rules.nickname,true)&&
          this.verifyRoleIds(this.updateUser.roleIds)
      }else{
        valid= this.verify(this.updateUser.password,this.rules.password,true)&&
          this.verify(this.updateUser.nickname,this.rules.nickname,true)&&
          this.verifyRoleIds(this.updateUser.roleIds)
      }
      const form={
        username: this.updateUser.username.value,
        password: this.updateUser.password.value,
        nickname: this.updateUser.nickname.value,
        roleIds: this.updateUser.roleIds.value,
        userId: this.updateUser.userId,
        deleteStatus : '1'
      }
      //修改用户信息
      let _vue = this;
      if(valid)
        this.api({
          url: "/user/updateUser",
          method: "post",
          data: form
        }).then(() => {
          this.$message.success('修改成功')
          this.dialogFormVisible = false
          _vue.getList();
        })
    },
    removeUser($index) {
      let _vue = this;
      this.$confirm('确定删除此用户?', '提示', {
        confirmButtonText: '确定',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        let user = _vue.list[$index];
        user.deleteStatus = '2';
        user.roleIds = user.roles.map(x => x.roleId)
        _vue.api({
          url: "/user/updateUser",
          method: "post",
          data: user
        }).then(() => {
          this.$message.success('删除成功')
          _vue.getList()
        }).catch(() => {
          _vue.$message.error("删除失败")
        })
      })
    },
  }
}
</script>

<style scoped>
* {
  /* 页面初始化 清除元素的内外边距 */
  /*padding: 0;*/
  margin: 0;
  /* 盒子模型 */
  box-sizing: border-box;
}
svg{
  color: #5dcaad;

}
svg:hover{
  color: #46b44d;
}
.container {
  /* 溢出隐藏 */
  width: 100%;
  overflow: hidden;
}
.container table {
  width: 100%;
  /* 让表格的边框合并在一起 */
  border-collapse: collapse;

}
/* 在做pc端 不注释掉看不到效果 */
.container table thead tr th {
  color: #fff;
  font-size: 2vm;
  /*background-color: #5dcaad;*/
}
/* 还是取单数 */
.container table thead tr th:nth-child(odd) {
  /*background-color: #324960;*/
}
.container table thead tr th,
.container table tbody tr td {
  text-align: center;
  /* 先不加 大屏就正常了 */
  padding: 10px 20px;
}
.container table tbody tr td {
  border: 1px solid rgba(199, 248, 249, 0.13);
}
.container table tbody tr:nth-child(odd) {
  /*background-color: #f8f8f8;*/
}
.dialog{
  display: flex;
  align-items: center;
  justify-content: space-around;
}
.card {
  /* 弹性布局 让子元素称为弹性项目 */
  display: flex;
  /* 让弹性项目垂直排列 原理是改变弹性盒子的主轴方向 父元素就是弹性盒子 现在改变后的主轴方向是向下了 */
  flex-direction: column;
  /* 让弹性项目在交叉轴方向水平居中 现在主轴的方向是向下 交叉轴的方向是与主轴垂直 交叉轴的方向是向右 */
  align-items: center;
  width: 50%;
  height: auto;
  padding: 30px;
  /*background: rgba(29,29,31,0.6);*/
  border-radius: 5px;
}
.card h4 {
  color: #fff;
  margin-bottom: 20px;
}
.card .card_box {
  /* 相对定位 */
  position: relative;
  width: 100%;
}
.card .card_box input {
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
.card .card_box label {
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
.card .card_box label span {
  color: #f0443c;
  margin-left: 10px;
}
.card .card_box label svg {
  margin-right: 10px;
}
.card .card_box input:focus + label,
.card .card_box input:valid + label {
  top: -20px;
  color: #03e9f4;
  font-size: 12px;
}
.card .card_but{
  margin-top: 40px;
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
  justify-items: center;
}


.card .card_box /deep/ .el-input__inner{
  color: white;
  background-color: rgba(255, 255, 255, 0);
}
.card .card_box /deep/ .el-popper{
  position:absolute !important;
  background-color: #1c273394;
  border: dodgerblue 1px solid;
}
.card .card_box /deep/ .el-select-dropdown__item.hover{
  background-color: rgba(110, 247, 42, 0.67);
  font-size: 20px;
}
.card .card_box /deep/ .el-select-dropdown__item{
  color: white;
}
</style>
