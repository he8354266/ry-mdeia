import { createApp } from 'vue'
import App from './App.vue'
import 'virtual:windi.css'
import 'amfe-flexible'

import service from '@/utils/request'
import router from '@/router/index'

import vant from 'vant'
import 'vant/lib/index.css';

const app = createApp(App)
app.use(router)
app.use(vant)
app.config.globalProperties.service = service // 挂载axios 实例
app.mount('#app')
