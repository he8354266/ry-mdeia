<script setup>
import { ref, getCurrentInstance } from "vue"
import {showNotify} from 'vant'
import { useRouter } from 'vue-router'
const { service } = getCurrentInstance().proxy
const router = useRouter()
const password = ref("")
const login = () => {
  if(!password.value){
    showNotify({
      type:'warning',
      message:'请输入密码'
    })
    return false
  }
  service.post('/waterfall/manager/pwdCheck',{password: password.value}).then(res => {
    if(res.code === 200){
      showNotify({
        type:'success',
        message: res.msg
      })
      sessionStorage.setItem('password',password.value)
      router.push('/index')
    }
  })
}
</script>
<template>
  <div class="relative h-screen bg-gray-200">
    <div>
      <img
        src="../../assets/105514lx9f9dlddedhhdh6.webp"
        class="w-full h-auto"
        alt=""
      />
    </div>
    <div
      class="w-[90%] h-[4rem] absolute left-[5%] top-[4rem] bg-white rounded-[10px] flex flex-col justify-center px-[0.5rem]"
    >
      <van-cell-group inset border>
        <van-field
          v-model="password"
          left-icon="shield-o"
          type="password"
          clearable
          class="border mb-[0.5rem] mt-[0.5rem] !text-[0.35rem]"
          placeholder="请输入密码"
        />
      </van-cell-group>
      <div class="px-[0.3rem]">
        <van-button block round size="small" type="primary" class="!text-[0.35rem]" @click="login()">确认</van-button>
      </div>
    </div>
  </div>
</template>
