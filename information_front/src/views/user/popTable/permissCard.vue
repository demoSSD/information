<template>
              <div class="permissCard">
                <div v-if="roleName===adminName" class="permiss-all">全部</div>
                <div v-else>
                  <div  v-for="menu in menus" class="menuItem">
                    <div class="menuItem-title">{{menu.menuName}}:</div>
                    <div class="menuItem-value">
                      <span v-for="perm in menu.permissions" :key="perm.permissionName" >
                        {{ perm.permissionName }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
</template>

<script>
export default {
  name: "permissCard",
  props:{
    menus:{
      type:Array,
      default:[]
    },
    roleName:{
      type:String,
      default: ""
    },
    adminName:{
      type:String,
      default: ""
    },
  }
}
</script>

<style scoped>
.permissCard{
  cursor:pointer;
  color: #99a9bf;
  max-width: 300px;
  max-height: 200px;
  overflow: auto;
}
.permiss-all{
  font-weight: bold;
}
.menuItem{
  display: grid;
  grid-template-columns: 80px auto;
  grid-column-gap: 10px;
  margin-bottom: 7px;
}
.menuItem-title{
 font-weight: bold;
  /*position:sticky;*/
  /*left:0;*/
}
.menuItem-value {
  font-family: 得意黑,sans-serif;
}
.menuItem-value span:hover{
  color: #03e9f4;

}
</style>
