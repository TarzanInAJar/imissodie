<template>
  <v-app :dark="dark">
    <v-toolbar>
      <!-- menu button -->
      <v-toolbar-side-icon @click="menu = !menu">
        <v-icon>menu</v-icon>
      </v-toolbar-side-icon>

      <!-- title -->
      <v-toolbar-title>imissodie</v-toolbar-title>

      <v-spacer></v-spacer>

      <!-- toggle the 'lights' -->
      <v-btn icon :color="dark ? 'yellow' : 'black'" flat @click="dark=!dark">
        <v-icon>fa-lightbulb-o</v-icon>
      </v-btn>
    </v-toolbar>

    <!--TODO put in a nav bar-->

    <!-- main view -->
    <router-view/>

    <!--TODO make snackbar available everywhere-->
    <v-snackbar v-model="snackbar">{{snackbarText}}</v-snackbar>
  </v-app>
</template>

<script>
  export default {
    name: 'App',
    data: function () {
      return {
        menu: false,
        dark: true,
        snackbar: false,
        snackbarText: ''
      }
    },
    watch: {
      menu: function () {
        this.snackbar = true;
        this.snackbarText = "menu changed to: " + this.menu;
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
