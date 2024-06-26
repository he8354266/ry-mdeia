import axios from 'axios'
import { getToken, getExpired } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import {showNotify} from 'vant'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

const baseUrl = window.location.protocol + '//' + window.location.hostname + ':8080'
// 创建axios实例
const service = axios.create({
  baseURL: baseUrl,
  // 超时
  timeout: 60000
})
// request拦截器
service.interceptors.request.use(config => {
  // 是否需要设置 token
  const isToken = (config.headers || {}).isToken === false
  // if (getToken() && !isToken) {
  //   config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
  // }
  // if (getToken() && Number(getExpired() || 0) - new Date().getTime()/1000 < 10 * 60) {
  //   store.dispatch("user/refreshToken")
  // }
  // get请求映射params参数
  if (config.method === 'get' && config.params) {
    let url = config.url + '?';
    for (const propName of Object.keys(config.params)) {
      const value = config.params[propName];
      var part = encodeURIComponent(propName) + "=";
      if (value !== null && typeof(value) !== "undefined") {
        if (typeof value === 'object') {
          for (const key of Object.keys(value)) {
            if (value[key] !== null && typeof (value[key]) !== 'undefined') {
              let params = propName + '[' + key + ']';
              let subPart = encodeURIComponent(params) + '=';
              url += subPart + encodeURIComponent(value[key]) + '&';
            }
          }
        } else if(!!value.length || !!value.toString().length) {
          url += part + encodeURIComponent(value) + "&";
        }
      }
    }
    url = url.slice(0, -1);
    config.params = {};
    config.url = url;
  }
  if(config.method === 'post' && config.data) {
    const bodyData = {}
    for(const key of Object.keys(config.data)) {
      if(config.data[key] === 0 || config.data[key] === false || !!config.data[key]){ // 判断为0 或 为false  或 非空值，非null，非undefined值
        bodyData[key] = config.data[key]
      }
    }
    config.data = bodyData
  }
  return config
}, error => {
    console.log(error)
    Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;
    // 获取错误信息
    const msg = errorCode[code] || res.data.msg || errorCode['default']
    if (code === 401) {
      // MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
      //     confirmButtonText: '重新登录',
      //     cancelButtonText: '取消',
      //     type: 'warning'
      //   }
      // ).then(() => {
      //   store.dispatch('user/LogOut').then(() => {
      //     location.href = '/';
      //   })
      // }).catch(() => {});
			showNotify({
				type:'warning',
				message:'密码不正确，请重新输入'
			})
      return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    } else if (code === 500) {
      showNotify({
				type: 'danger',
				message:msg
			})
      return Promise.reject(new Error(msg))
    } else if (code !== 200) {
      showNotify({
				type: 'danger',
				message:msg
			})
      return Promise.reject('error')
    } else {
      return res.data
    }
  },
  error => {
    let { message } = error;
    if (message == "Network Error") {
      message = "后端接口连接异常";
    }
    else if (message.includes("timeout")) {
      message = "系统接口请求超时";
    }
    else if (message.includes("Request failed with status code")) {
      message = "系统接口" + message.substr(message.length - 3) + "异常";
    }
		showNotify({
			type: 'danger',
			message:message
		})
    return Promise.reject(error)
  }
)

export default service

export {baseUrl}