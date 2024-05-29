import Cookies from 'js-cookie'

const TokenKey = 'flowMemo-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

const ExpiredKey = 'flowMemo-Token-Expired'

export function getExpired() {
  return Cookies.get(ExpiredKey)
}

export function setExpired(Expired) {
  return Cookies.set(ExpiredKey, Expired)
}

export function removeExpired() {
  return Cookies.remove(ExpiredKey)
}
