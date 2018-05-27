<template>
    <div>


        <div class="row">
                <img src="http://www.imissodie.com:8080/stream/video.mjpeg" >
        </div>

        <div class="row">
            <v-btn color="primary">
                <v-icon>fa-coffee</v-icon>
            </v-btn>
            <v-btn v-bind:disabled="calling" color="primary" @click="call">
                <v-icon v-if="calling" class="fa-spin">fa-spinner</v-icon>
                <v-icon v-else>fa-bullhorn</v-icon>
            </v-btn>
        </div>
    </div>

</template>

<script>
    import axios from 'axios';
    export default {
        name: "Boring",
        data() {
            return {
                calling: false,
                apiUrl: "http://www.imissodie.com:8090/"
            }
        },
        methods: {
            call(){
                this.calling = true;
                axios.get(this.apiUrl + "/call")
                    .catch(function(error) {
                        console.log(error);
                    })
                    .finally(() => (this.calling = false));
            }
        }
    }

</script>

<style scoped>

</style>