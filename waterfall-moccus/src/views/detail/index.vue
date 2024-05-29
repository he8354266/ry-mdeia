<script setup>
import { ref, reactive, getCurrentInstance, onMounted, watch } from "vue"
import clipboard from "clipboard"
import { useRouter, useRoute } from "vue-router"
import { showNotify } from "vant"
import { baseUrl } from "@/utils/request"
const { service } = getCurrentInstance().proxy
const router = useRouter()
const route = useRoute()

const form = ref({
  nickName: "",
  number: "",
  video: "",
  detailsImg: "",
  category: "",
	feature: '',
  address: "",
  free: false,
  validateInfo: [],
})

// 配置参数
const categoryList = ref({ raw: [], map: {} }) // text value 分类
const positionList = ref({ raw: [], map: {} }) // 位置
const featureList = ref({ raw: [], map: {} }) // 特色
const timeList = ref({ raw: [], map: {} }) // 上新时间
const validateList = ref({ raw: [], map: {} }) // 验证分类
const funDictGet = async () => {
  await service
    .get("/system/dict/data/type/ry_details_category")
    .then((res) => {
      if (res.code === 200) {
        categoryList.value.raw = res.data.map((o) => {
          categoryList.value.map = {
            ...categoryList.value.map,
            [o.dictValue]: o.dictLabel,
          }
          return {
            text: o.dictLabel,
            value: o.dictValue,
          }
        })
      }
    })
  await service.get("/system/dict/data/type/ry_details_address").then((res) => {
    if (res.code === 200) {
      positionList.value.raw = res.data.map((o) => {
        positionList.value.map = {
          ...positionList.value.map,
          [o.dictValue]: o.dictLabel,
        }
        return {
          text: o.dictLabel,
          value: o.dictValue,
        }
      })
    }
  })
  await service.get("/system/dict/data/type/ry_details_feature").then((res) => {
    if (res.code === 200) {
      featureList.value.raw = res.data.map((o) => {
        featureList.value.map = {
          ...featureList.value.map,
          [o.dictValue]: o.dictLabel,
        }
        return {
          text: o.dictLabel,
          value: o.dictValue,
        }
      })
    }
  })
  await service.get("/system/dict/data/type/ry_create_time").then((res) => {
    if (res.code === 200) {
      timeList.value.raw = res.data.map((o) => {
        timeList.value.map = {
          ...timeList.value.map,
          [o.dictValue]: o.dictLabel,
        }
        return {
          text: o.dictLabel,
          value: o.dictValue,
        }
      })
    }
  })
  await service
    .get("/system/dict/data/type/ry_validate_category")
    .then((res) => {
      if (res.code === 200) {
        validateList.value.raw = res.data.map((o) => {
          validateList.value.map = {
            ...validateList.value.map,
            [o.dictValue]: o.dictLabel,
          }
          return {
            text: o.dictLabel,
            value: o.dictValue,
          }
        })
      }
    })
}
// 获取详情
const funDetail = async (id) => {
  await funDictGet()
  service.get("/waterfall/info/" + id).then((res) => {
    if (res.code === 200) {
      form.value = res.data
      form.value.address = positionList.value.map[res.data.address]
      form.value.category = categoryList.value.map[res.data.category]
      form.value.video = baseUrl + res.data.video
      form.value.detailsImg = baseUrl + res.data.detailsImg
      form.value.validateInfo = res.data.validateInfo.split(",").map((o) => {
        return validateList.value.map[o]
      })
			form.value.feature = featureList.value.map[res.data.feature]
    }
  })
}

funDetail(route.query.id)

watch(
  () => route,
  () => {
    funDetail(route.query.id)
  }
)

const onClickLeft = () => {
  router.back()
}

const funCopy = () => {
  showNotify({
    type: "success",
    message: "复制成功！",
  })
}

onMounted(() => {
  new clipboard(".clip")
})
</script>

<template>
  <div>
    <van-nav-bar
      class="!bg-[rgb(97,107,218)] !text-white"
      :border="false"
      @click-left="onClickLeft"
    >
      <template #title>
        <span class="text-white">{{ form.nickName }}</span>
      </template>
      <template #left>
        <van-icon name="arrow-left" size="24px" color="#fff" />
      </template>
    </van-nav-bar>
    <div>
      <video class="w-full h-[40vh]" controls :src="form.video" autoplay></video>
    </div>
    <div
      class="pb-[30px] m-[30px] text-[0.35rem] flex items-center justify-between border-b border-gray-300"
    >
      <div class="flex items-center">
        <div
          class="w-[1rem] h-[1rem] rounded-[50%] bg-[rgb(183,232,194)] flex justify-center items-center"
        >
          <van-icon name="gem" size="24px" />
        </div>
        <div class="ml-[20px]">{{ form.category }}</div>
      </div>
      <div class="flex items-center">
        <div
          class="w-[1rem] h-[1rem] rounded-[50%] bg-[rgb(183,232,194)] flex justify-center items-center"
        >
          <van-icon name="location" size="24px" />
        </div>
        <div class="ml-[20px]">{{ form.address }}</div>
      </div>

      <div
        class="w-[1rem] h-[1rem] rounded-[50%] bg-[rgb(183,232,194)] flex justify-center items-center text-[0.3rem] font-bold"
      >
        编号
      </div>
      <div class="flex flex-col">
        <div
          class="text-[rgb(147,188,229)] clip"
          data-clipboard-target="#cliparea"
          @click="funCopy"
        >
          (点击复制)
        </div>
        <div class="mt-[10px]">
          编号 <span id="cliparea">{{ form.number }}</span>
        </div>
      </div>
    </div>
    <div class="flex mx-[30px] text-[0.38rem] items-center">
      <div class="flex items-center">
        <div class="font-bold">状态：</div>
        <div>
          <span v-if="form.free" class="text-green-500">空闲</span>
          <span v-else class="text-red-500">忙碌</span>
        </div>
      </div>
      <div class="flex items-center ml-[1rem]">
        <div class="font-bold">特色：</div>
        <div>
          <span
            class="text-white p-[10px] bg-[rgb(51,120,245)] mr-[10px] inline-block text-[0.3rem] h-[0.6rem] leading-[0.35rem]"
            >{{form.feature}}</span
          >
        </div>
      </div>
    </div>
    <div class="mx-[30px] mt-[30px]">
      <van-image
        width="100%"
        radius="10"
        height="auto"
        :src="form.detailsImg"
      ></van-image>
    </div>
  </div>
</template>
