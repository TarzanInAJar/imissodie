<template>
    <v-card raised style="min-height: 300px;">
        <v-card-media >
            <div class="row">
                <img src="http://www.imissodie.com:8080/stream/video.mjpeg" >
            </div>
        </v-card-media>


        <v-card-actions>

          <!--<v-btn color="primary" dark>-->
          <!--<v-icon>fa-play</v-icon>-->
          <!--</v-btn>-->
          <v-btn color="primary">
            <v-icon>fa-coffee</v-icon>
          </v-btn>
          <v-btn color="primary">
            <v-icon>fa-bullhorn</v-icon>
          </v-btn>
          <!--
                      <v-btn color="primary">
                          <v-icon>fa-microphone</v-icon>
                      </v-btn>
          -->
        </v-card-actions>

    </v-card>

</template>

<script>
    export default {
        name: "Boring"
    }
</script>

<style scoped>

</style>