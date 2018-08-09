<template>
  <v-app :dark="dark">
    <v-toolbar>
      <v-toolbar-side-icon @click="menu = !menu"><v-icon>menu</v-icon></v-toolbar-side-icon>
      <v-toolbar-title>imissodie</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-switch  v-model="dark"></v-switch>
    </v-toolbar>
      <!--TODO put in a nav bar-->
      <router-view/>
      <!--TODO make snackbar available everywhere-->
      <v-snackbar v-model="snackbar">{{snackbarText}}</v-snackbar>
  </v-app>
</template>

<script>
  export default {
    name: 'App',
    data: function() {
        return {
            menu: false,
            dark: true,
            snackbar: false,
            snackbarText: ''
        }
    },
    watch: {
        menu: function() {
          this.snackbar = true;
          this.snackbarText= "menu changed to: " + this.menu;
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
