import Vue from 'vue'
import Router from 'vue-router'
import weather from "../components/weather";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'weather',
      component: weather
    }
  ]
})
