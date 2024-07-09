import request from '../utils/request'

export function signUp (roleId, data) {
  return request({
    method: 'post',
    url: '/users/sign-up/' + roleId,
    data: data
  })
}

export function userInfo (id) {
  return request({
    method: 'get',
    url: '/users/' + id
  })
}

// token查詢
export function getUserInfo () {
  return request({
    method: 'post',
    url: '/users/userInfo'
  })
}

// token取得權限
export function getAuth (data) {
  return request({
    method: 'post',
    url: '/users/Auth',
    data: data
  })
}
