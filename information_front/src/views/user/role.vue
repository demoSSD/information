<template>
  <div class="container">
    <table v-show="!dialogFormVisible">
      <thead>
      <tr>
        <th>序号</th>
        <th>角色</th>
        <th>用户数量</th>
        <th>菜单&权限</th>
        <th>操作 <span v-permission="'role:add'" @click="showCreate"><svg-icon icon-class="addUser"/></span></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(Item,index) in list" :key="Item.roleName">
        <td>{{index+1}}</td>
        <td>{{Item.roleName}}</td>
        <td>
          <el-popover popper-class="popover" placement="right"  trigger="hover" >
            <PopTable :list="Item.users"/>
            <el-button slot="reference" size="mini" round class="el-button-approve">{{Item.users.length}}</el-button>
          </el-popover>
        </td>
        <td>
          <el-popover popper-class="popover" placement="right"    trigger="hover" >
            <permiss-card :menus="Item.menus" :role-name="Item.roleName" :admin-name="adminName"/>
            <el-button slot="reference" size="mini" class="el-button-approve" >
              <svg-icon icon-class="more"/>
            </el-button>
          </el-popover>
        </td>
        <td>
          <el-button type="text"  @click="showUpdate(index)" v-permission="'role:update'">修改</el-button>
          <el-button type="text" @click="removeRole(index)" v-permission="'role:delete'" >删除</el-button>
        </td>
      </tr>
      </tbody>
    </table>
    <div v-show="dialogFormVisible" class="dialog">
      <h4>添加角色</h4>
      <div>
        <div class="login_box">
          <input type="text" required v-model="tempRole.roleName"/>
          <label><svg-icon icon-class="user"/>填写角色名<span></span></label>
        </div>
        <div class="permiss" v-for=" (menu,index) in allPermission" :key="menu.menuName">
          <el-checkbox :indeterminate="isMenuAll(index)" :value="isMenuAll(index)" @change="checkAll(index)"
                       style="font-weight: bold">{{menu.menuName}}</el-checkbox>
          <div>
            <el-checkbox-group v-model="tempRole.permissions">
              <el-checkbox style="margin-right: 10px" v-for="perm in menu.permissions" :label="perm.id" @change="checkRequired(perm,_index)"
                           :key="perm.id">
                <span  :class="{requiredPerm:perm.requiredPerm===1,'default':perm.requiredPerm!==1}">{{perm.permissionName}}</span>
              </el-checkbox>
            </el-checkbox-group>
          </div>
        </div>
        <div class="permiss_tip">说明:黄色权限为对应菜单默认必选权限</div>
        <div class="permiss_but">
          <DivButton2 label="取消" @click.native="dialogFormVisible = false"></DivButton2>
          <DivButton1 label="创建"  v-if="dialogStatus==='create'" @click.native="createRole"></DivButton1>
          <DivButton1 label="修改" v-else @click.native="updateRole"></DivButton1>
        </div>
      </div>


    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import DivButton2 from "../../components/button/DivButton2";
import DivButton1 from "../../components/button/DivButton1";
import PopTable from "./popTable/PopTable";
import PermissCard from "./popTable/permissCard";
export default {
  name: "role",
  components: {PermissCard, PopTable, DivButton2,DivButton1},
  data() {
    return {
      isIndeterminate: true,
      list: [],//表格的数据
      allPermission: [],
      listLoading: false,//数据加载等待动画
      dialogStatus: 'create',
      dialogFormVisible: false,
      textMap: {
        update: '编辑',
        create: '新建角色'
      },
      tempRole: {
        roleName: '',
        roleId: '',
        permissions: [],
      },
      adminName: '管理员'
    }
  },
  created() {
    this.getList();
    this.getAllPermisson();
  },
  methods: {
    getAllPermisson() {
      //查询所有权限
      this.api({
        url: "/user/listAllPermission",
        method: "get"
      }).then(data => {
        this.allPermission = data.list;
      })
    },
    getList() {
      //查询列表
      this.listLoading = true;
      this.api({
        url: "/user/listRole",
        method: "get"
      }).then(data => {
        this.listLoading = false;
        this.list = data.list;
      })
    },
    getIndex($index) {
      //表格序号
      return $index + 1
    },
    showCreate() {
      //显示新增对话框
      this.tempRole.roleName = '';
      this.tempRole.roleId = '';
      this.tempRole.permissions = [];
      this.dialogStatus = "create"
      this.dialogFormVisible = true
    },
    showUpdate($index) {
      let role = this.list[$index];
      this.tempRole.roleName = role.roleName;
      this.tempRole.roleId = role.roleId;
      this.tempRole.permissions = [];
      for (let i = 0; i < role.menus.length; i++) {
        let perm = role.menus[i].permissions;
        for (let j = 0; j < perm.length; j++) {
          this.tempRole.permissions.push(perm[j].permissionId);
        }
      }
      this.dialogStatus = "update"
      this.dialogFormVisible = true
    },
    createRole() {
      if (!this.checkRoleNameUnique()) {
        return;
      }
      if (!this.checkPermissionNum()) {
        return;
      }
      //添加新角色
      this.api({
        url: "/user/addRole",
        method: "post",
        data: this.tempRole
      }).then(() => {
        this.getList();
        this.dialogFormVisible = false
      })
    },
    updateRole() {
      if (!this.checkRoleNameUnique(this.tempRole.roleId)) {
        return;
      }
      if (!this.checkPermissionNum()) {
        return;
      }
      //修改角色
      this.api({
        url: "/user/updateRole",
        method: "post",
        data: this.tempRole
      }).then(() => {
        this.$message.success("修改成功")
        this.getList();
        this.dialogFormVisible = false
      })
    },
    checkPermissionNum() {
      //校验至少有一种权限
      if (this.tempRole.permissions.length === 0) {
        this.$message.error("请至少选择一种权限");
        return false;
      }
      return true;
    },
    checkRoleNameUnique(roleId) {
      //校验名称重复
      let roleName = this.tempRole.roleName;
      if (!roleName) {
        this.$message.error("请填写角色名称");
        return false;
      }
      let roles = this.list;
      let result = true;
      for (let j = 0; j < roles.length; j++) {
        if (roles[j].roleName === roleName && (!roleId || roles[j].roleId !== roleId  )) {
          this.$message.error("角色名称已存在");
          result = false;
          break;
        }
      }
      return result;
    },
    removeRole($index) {
      let _vue = this;
      this.$confirm('确定删除此角色?', '提示', {
        confirmButtonText: '确定',
        showCancelButton: false,
        type: 'warning'
      }).then(() => {
        let role = _vue.list[$index];
        _vue.api({
          url: "/user/deleteRole",
          method: "post",
          data: {
            roleId: role.roleId
          }
        }).then(() => {
          _vue.getList()
        }).catch(e => {
        })
      })
    },
    isMenuNone(_index) {
      //判断本级菜单内的权限是否一个都没选
      let menu = this.allPermission[_index].permissions;
      let result = true;
      for (let j = 0; j < menu.length; j++) {
        if (this.tempRole.permissions.indexOf(menu[j].id) > -1) {
          result = false;
          break;
        }
      }
      return result;
    },
    isMenuAll(_index) {
      //判断本级菜单内的权限是否全选了
      let menu = this.allPermission[_index].permissions;
      let result = true;
      for (let j = 0; j < menu.length; j++) {
        if (this.tempRole.permissions.indexOf(menu[j].id) < 0) {
          result = false;
          break;
        }
      }
      return result;
    },
    checkAll(_index) {
      //点击菜单   相当于全选按钮
      let v = this;
      if (v.isMenuAll(_index)) {
        //如果已经全选了,则全部取消
        v.noPerm(_index);
      } else {
        //如果尚未全选,则全选
        v.allPerm(_index);
      }
    },
    allPerm(_index) {
      //全部选中
      let menu = this.allPermission[_index].permissions;
      for (let j = 0; j < menu.length; j++) {
        this.addUnique(menu[j].id, this.tempRole.permissions)
      }
    },
    noPerm(_index) {
      //全部取消选中
      let menu = this.allPermission[_index].permissions;
      for (let j = 0; j < menu.length; j++) {
        let idIndex = this.tempRole.permissions.indexOf(menu[j].id);
        if (idIndex > -1) {
          this.tempRole.permissions.splice(idIndex, 1);
        }
      }
    },
    addUnique(val, arr) {
      //数组内防重复地添加元素
      let _index = arr.indexOf(val);
      if (_index < 0) {
        arr.push(val);
      }
    },
    checkRequired(_perm, _index) {
      //本方法会在勾选状态改变之后触发
      let permId = _perm.id;
      if (this.tempRole.permissions.indexOf(permId) > -1) {
        //选中事件
        //如果之前未勾选本权限,现在勾选完之后,tempRole里就会包含本id
        //那么就要将必选的权限勾上
        this.makeReuqiredPermissionChecked(_index);
      } else {
        //取消选中事件
        if (_perm.requiredPerm === 1) {
          //如果是必勾权限,就把本菜单的权限全部移出
          //(其实也可以提示用户本权限是菜单里的必选,请先取消勾选另外几个权限,交互太麻烦,此处就直接全部取消选中了)
          this.noPerm(_index);
        }
      }
    },
    makeReuqiredPermissionChecked(_index) {
      //将本菜单必选的权限勾上
      let menu = this.allPermission[_index].permissions;
      for (let j = 0; j < menu.length; j++) {
        let perm = menu[j];
        if (perm.requiredPerm === 1) {
          //找到本菜单的必选权限,将其勾上
          this.addUnique(perm.id, this.tempRole.permissions)
        }
      }
    }
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
  padding: 10px 20px;
  display: grid;
  grid-template-columns: 100%;
  grid-row-gap: 15px;
  justify-items: center;
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

.permiss{
  display: grid;
  grid-template-columns: 100px auto;
  grid-column-gap: 20px;
  margin-bottom: 12px;
}
.permiss /deep/ .el-checkbox__label{
  color: #ccc;
}
.permiss /deep/ .el-checkbox__input.is-checked+.el-checkbox__label{
  color: #5dcaad;
}
.permiss /deep/  .el-checkbox__input.is-checked .el-checkbox__inner{
  background: #5dcaad;
  border-color: #5dcaad;
}
.permiss_tip{
  color: #99a9bf;
  font-size: 14px;
}
.permiss_but{
  margin-top: 10px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  justify-items: center;
}
.requiredPerm {
  color: #c9dc97;
}
.default{
  color: white;
}
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
.login_box input:focus + label,
.login_box input:valid + label {
  top: -20px;
  color: #03e9f4;
  font-size: 12px;
}
</style>
