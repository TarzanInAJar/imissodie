<template>
    <div>


        <div class="row">
            <video controls>
                <source src="./TextInMotion-Sample-720p.mp4" type="video/mp4">
                Your browser sucks
            </video>
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