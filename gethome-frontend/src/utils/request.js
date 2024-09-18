import axios from 'axios'
import Vue from 'vue'
import user from '../store/modules/user'
import router from '../router'

axios.defaults.baseURL = 'http://localhost:8088'

const request = axios.create({
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})
// response filter
request.interceptors.request.use(config => {
  let token = user.state.token
  if (token) {
    // jwt用的header
    config.headers['Authorization'] = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(response => {
  let res = response.data

  const code = response.status
  if (code === 200 || code === 201 || code === 204) {
    return response.data
  } else {
    // 權限不足，重導向登入頁面
    if (code === 301 || code === 401) {
      Vue.prototype.$message({message: '沒有權限，請重新登入', type: 'error'})
      router.push('/login')
      return Promise.reject(res.msg)
    }
    // 沒有權限
    if (code === 403) {
      Vue.prototype.$message({message: '請求被拒絕', type: 'error'})
      return Promise.reject(res.msg)
    }
    return Promise.reject(res.msg)
  }
}, error => {
  // 後端未啟動
  if (error.toString().indexOf('Network Error') !== -1) {
    Vue.prototype.$message({message: '後端伺服器未啟動', type: 'error'})
    return Promise.reject(error)
  }
  console.error('axios出錯:', error)
  return Promise.reject(error)
})
export default request
