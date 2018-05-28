<template>
    <div>
        <video height="300" id="video"></video>

        <div class="row">
            <v-btn color="primary" @click="video">
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
    import signal from './signalling';


    export default {
        name: "Boring",
        data() {
            return {
                calling: false,
                apiUrl: "http://www.imissodie.com:8090/",
                videoPlaying: false
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
            },
            video() {
                var signalObj = signal.signal('wss://192.168.0.106:8443/webrtc',
                    function (stream) {
                        var video = document.getElementById('video');
                        console.log('got a stream!');
                        video.srcObject = stream;
                        video.play();
                    },
                    function (error) {
                        alert(error);
                    },
                    function () {
                        console.log('websocket closed. bye bye!');
                        var video = document.getElementById('video');
                        video.srcObject = null;
                        //video.src = ''; // deprecated
                        //ctx.clearRect(0, 0, canvas.width, canvas.height);
                        //isStreaming = false;
                    },
                    function (message) {
                        alert(message);
                    }
                );

            }
        },
        watch: {
            videoPlaying: function(playing) {
                if (playing){

                }
            }
        }
    }

</script>

<style scoped>

</style>