import {createApp} from 'vue'
import {createPinia} from 'pinia'
import App from './App.vue'
import router from './router'
import installElementPlus from './plugins/element'
import './assets/css/icon.css'
import axios from "axios";
import {ElMessage} from "element-plus";
import * as echarts from 'echarts';

const app = createApp(App)
installElementPlus(app)
app.use(createPinia())
    .use(router)
    .mount('#app')


app.config.globalProperties.$echarts = echarts

axios.defaults.baseURL = "http://localhost:8080/"
axios.defaults.withCredentials = true
app.config.globalProperties.$axios = axios
// app.config.globalProperties.$jq = jquery

axios.interceptors.request.use(
    config => {
        let authorization = localStorage.getItem('Authorization')
        if (authorization) {
            config.headers.common['Authorization'] = authorization
        } else {
            let path = router.currentRoute._rawValue.fullPath
            console.log(path)
            if (path !== "/login" && path !== "/logout") {
                ElMessage.error("请先登录！")
                router.push("/login")
            }
        }
        return config
    }
)
