<template>
  <v-card class="ma-4"
    raised
    style="min-height: 300px;"
  >
    <v-card-media @click="video = !video" class="ma-2" style="min-height: 300px;">
      <v-layout>
        <v-spacer></v-spacer>
        <v-flex xs10>
          <img src="http://www.imissodie.com:8080/stream/video.mjpeg">
        </v-flex>
        <v-spacer></v-spacer>
      </v-layout>
    </v-card-media>

    <v-card-actions>
      <v-layout>
        <v-spacer></v-spacer>
          <v-btn :disabled="!connected" color="primary">
            <v-icon>fa-coffee</v-icon>
          </v-btn>
          <v-btn :disabled="!connected" color="primary">
            <v-icon>fa-bullhorn</v-icon>
          </v-btn>

        <v-spacer></v-spacer>

      <!--
        <v-btn color="primary" dark>
          <v-icon>fa-play</v-icon>
        </v-btn>
      -->

      <!--
        <v-btn color="primary">
          <v-icon>fa-microphone</v-icon>
        </v-btn>
      -->
      </v-layout>
    </v-card-actions>

  </v-card>

</template>

<script>
  export default {
    name: "Boring",
    data: function() {
      return {
        video: false,
        connected: false
      }
    }
  }
</script>

<style scoped>

</style>