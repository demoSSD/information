<!-- Use preprocessors via the lang attribute! e.g. <template lang="pug"> -->
<template>
  <div class="main">
    <Header/>
    <MainCard class="main_card"/>
  </div>
</template>

<script>

import MainCard from "../../components/Card/MainCard";
import Header from "../../components/Header/Header";
export default {
  name: "home",
  components: {Header, MainCard},
  data() {
    return {

    };
  },
  mounted() {

  },

  methods: {

  }
};
</script>

<style scoped>

.main{
  background: url("../../assets/background1.jpg") no-repeat;
  min-height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
  text-align: center;
  color: white;

}
.main_card{
  margin: 50px auto;
}
</style>
