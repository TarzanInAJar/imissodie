<template>
    <div>


        <div class="row">
                <img src="http://www.imissodie.com:8080/stream/video.mjpeg" >
        </div>


        <div class="row">
            <v-btn color="primary" dark>
                <v-icon>fa-play</v-icon>
            </v-btn>
        </div>

        <div class="row">
            <v-btn color="primary">
                <v-icon>fa-coffee</v-icon>
            </v-btn>
            <v-btn color="primary">
                <v-icon>fa-bullhorn</v-icon>
            </v-btn>
            <v-btn color="primary">
                <v-icon>fa-microphone</v-icon>
            </v-btn>
        </div>

    </div>

</template>

<script>
    export default {
        name: "Boring"
    }
</script>

<style scoped>

</style>