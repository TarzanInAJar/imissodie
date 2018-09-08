import Vue from 'vue'
import Router from 'vue-router'
import Boring from '@/components/boring/Boring'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Boring',
      component: Boring
    }
  ]
})
