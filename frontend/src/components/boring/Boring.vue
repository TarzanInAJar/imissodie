<template>
  <v-card class="ma-4"
    raised
    style="min-height: 300px;"
  >
    <v-card-media @click="simulateConnection" class="ma-2">
      <v-layout>
        <v-spacer></v-spacer>
        <v-flex xs10>
          <span v-if="status == 'disconnected'">
            click here to connect
          </span>
          <v-icon v-else-if="status == 'connecting'">fa-spin fa-spinner</v-icon>
          <img v-else-if="status == 'connected'" src="http://www.imissodie.com:8080/stream/video.mjpeg">
          <span v-else-if="status == 'failed'">
            connection failed! click to try again
          </span>
        </v-flex>
        <v-spacer></v-spacer>
      </v-layout>
    </v-card-media>

    <v-card-actions>
      <v-layout>
        <v-spacer></v-spacer>
          <v-btn :disabled="status != 'connected'" color="primary">
            <v-icon>fa-coffee</v-icon>
          </v-btn>
          <v-btn :disabled="status != 'connected'" color="primary">
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
        status: 'disconnected',
        simulationClickCount: 0
      }
    },
    methods: {
      simulateConnection(){
        if (this.simulationClickCount == 0){
          this.status = 'connecting';
        } else if (this.simulationClickCount == 1){
          //randomly fail
          var rand = Math.random();
          if (rand < 0.5) {
            this.status = 'failed';
            this.simulationClickCount = 0;
          } else {
            this.status = 'connected';
          }
        } else if (this.simulationClickCount == 2){
          this.status = 'disconnected'
        }
        console.log(this.status);
        if(this.status != 'failed' && ++this.simulationClickCount  == 3){
          this.simulationClickCount = 0;
        }

      }
    }
  }
</script>

<style>
  <!--TODO - update vue/vuetify take advantage of vue selectors-->
  v-card-media: {
    min-height: 300px;
  }


</style>