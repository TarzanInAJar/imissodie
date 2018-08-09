<template>
  <v-app dark>
    <v-toolbar>
      <v-toolbar-side-icon @click="menu = !menu"><v-icon>menu</v-icon></v-toolbar-side-icon>
      <v-toolbar-title>imissodie</v-toolbar-title>
      <v-spacer></v-spacer>
      <span>menutoggle: {{menu}}</span>
    </v-toolbar>
      <!--TODO put in a nav bar-->
      <router-view/>
  </v-app>
</template>

<script>
  export default {
    name: 'App',
    data: function() {
        return {
            menu: false
        }
    }
  }
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
