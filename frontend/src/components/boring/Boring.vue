<template>
        <!-- -->
  <v-card class="ma-4"
    raised
    style="min-height: 300px;"
  >
    <v-card-media @click="videoToggle" class="ma-2">
      <v-layout row wrap>
        <v-flex xs12>
          <span v-if="status == 'disconnected'">
            click anywhere to connect
          </span>
          <v-icon v-else-if="status == 'connecting'">fa-spin fa-spinner</v-icon>
          <span v-else-if="status == 'failed'">
            connection failed! click to try again
          </span>
          <span v-else-if="status == 'connected'">
            click anywhere to disconnect
          </span>
        </v-flex>
        <v-flex xs12>
          <video style="background-color: black;" height="300" id="video"></video>
        </v-flex>
      </v-layout>
    </v-card-media>

    <v-card-actions>
      <v-layout>
        <v-spacer></v-spacer>
          <v-btn :disabled="status != 'connected'" color="primary">
            <v-icon>fa-coffee</v-icon>
          </v-btn>
          <v-btn @click="call" :disabled="status != 'connected' || calling" color="primary">
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
    import axios from 'axios';
    import signal from './signalling';


    export default {
        name: "Boring",
        data() {
            return {
                calling: false,
                apiUrl: "http://www.imissodie.com:8090/",
                status: 'disconnected',
                simulationClickCount: 0,
                validPassword: null
            }
        },
        methods: {
            simulateConnection() {
                if (this.simulationClickCount == 0) {
                    this.status = 'connecting';
                } else if (this.simulationClickCount == 1) {
                    //randomly fail
                    var rand = Math.random();
                    if (rand < 0.5) {
                        this.status = 'failed';
                        this.simulationClickCount = 0;
                    } else {
                        this.status = 'connected';
                    }
                } else if (this.simulationClickCount == 2) {
                    this.status = 'disconnected'
                }
                console.log(this.status);
                if (this.status != 'failed' && ++this.simulationClickCount == 3) {
                    this.simulationClickCount = 0;
                }
            },
            videoToggle() {
              if (this.status == "disconnected" || this.status == "failed"){
                  this.video();
              } else if (this.status == "connected" || this.status == "connecting"){
                  signal.hangup();
              }
            },

            call() {
                console.log("calling...");
                this.calling = true;
                axios.get(this.apiUrl + "call")
                    .catch(function (error) {
                        console.log(error);
                    })
                    .finally(() => (this.calling = false));
            },
            video() {
                var vm = this;
                vm.status = "connecting";
                var password;
                if (this.validPassword){
                    password = this.validPassword;
                } else {
                    password = window.prompt("Please enter the password");
                }
                if (!password){
                    return;
                }

                var signalObj = signal.signal('wss://user:' + password + '@192.168.0.106:8443/stream/webrtc',
                    function (stream) {
                        var video = document.getElementById('video');
                        console.log('got a stream!');
                        vm.validPassword = password;
                        vm.status = "connected";
                        video.srcObject = stream;
                        video.play();
                    },
                    function (error) {
                        vm.status = "failed";
                        alert(error);
                    },
                    function () {
                        console.log('websocket closed. bye bye!');
                        vm.status = "disconnected";
                        var video = document.getElementById('video');
                        video.srcObject = null;
                    },
                    function (message) {
                        alert(message);
                    }
                );
            }

        }
    }
</script>

<style>

</style>