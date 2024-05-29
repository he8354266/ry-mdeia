import { createRouter, createWebHistory } from 'vue-router'
const routes = [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/index',
    component: () => import(/* webpackChunkName: "index" */ '@/views/index/index.vue')
  },
	{
    path: '/detail',
    component: () => import(/* webpackChunkName: "detail" */ '@/views/detail/index.vue')
  },
  {
    path: '/login',
    component: () => import(/* webpackChunkName: "login" */ '@/views/login/index.vue')
  }
]
// Vue-router新版本中，需要使用createRouter来创建路由
const router = createRouter({
  // 指定路由的模式
  history: createWebHistory(),
  routes
})
router.beforeEach((to,from,next) => {
	if(to.fullPath !== '/login'){
		if(!sessionStorage.password){
			next('/login')
		}else{
			next()
		}
	}else{
		next()
	}
})
export default router