<script setup>
import { ref, reactive, getCurrentInstance } from "vue"
import { useRouter } from "vue-router"
import { baseUrl } from "@/utils/request"

const { service } = getCurrentInstance().proxy
const router = useRouter()
// 表单参数
const queryParams = reactive({
  searchVal: "",
  category: "",
  address: "",
  feature: "",
  groundTime: "",
  pageSize: 10,
  pageNum: 1,
})
const dataList = ref([]) // 数据集合`
const loading = ref(false) // 加载loading
const finished = ref(false) // 是否已加载全部
const refreshing = ref(false) // 是否重新刷新
// 配置参数
const categoryList = ref({ raw: [], map: {} }) // text value 分类
const positionList = ref({ raw: [], map: {} }) // 位置
const featureList = ref({ raw: [], map: {} }) // 特色
const timeList = ref({ raw: [], map: {} }) // 上新时间
const validateList = ref({ raw: [], map: {} }) // 验证分类
const funDictGet = () => {
  service
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
  service.get("/system/dict/data/type/ry_details_address").then((res) => {
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
  service.get("/system/dict/data/type/ry_details_feature").then((res) => {
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
  service.get("/system/dict/data/type/ry_create_time").then((res) => {
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
  service
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

funDictGet()

const funSearch = () => {
  queryParams.category = ""
  queryParams.address = ""
  queryParams.feature = ""
  queryParams.groundTime = ""
  queryParams.pageNum = 1
  funRefresh()
} // 搜索

// 下拉刷新
const funRefresh = () => {
  finished.value = false
  loading.value = false
  refreshing.value = true
  funLoad()
}

const funLoad = () => {
  service.get("/waterfall/info/list", { params: queryParams }).then((res) => {
    if (res.code === 200) {
      if (refreshing.value) {
        dataList.value = []
        queryParams.pageNum = 1
        refreshing.value = false
      }
      // 数据全部加载完成
      if (dataList.value.length >= res.total) {
        finished.value = true
        loading.value = false
        return false
      } else {
        queryParams.pageNum++
      }

      for (let i = 0; i < res.rows.length; i++) {
        const row = res.rows[i]
        dataList.value.push({
          ...row,
          address: positionList.value.map[row.address],
          validateInfo: row.validateInfo.split(",").map((o) => {
            return validateList.value.map[o]
          }),
        })
      }

      // 加载状态结束
      loading.value = false
    }
  })
}
// 点击数据单元格
const funDataClick = (row) => {
  router.push({
    path: "/detail",
    query: {
      id: row.id,
    },
  })
}

const noticeText = ref("")
// 获取通知内容
service.get("/system/notice/list?pageNum=1&pageSize=10").then((res) => {
  if (res.code === 200) {
    noticeText.value = res.rows[0].noticeContent
  }
})
</script>
<template>
  <div>
    <van-notice-bar left-icon="volume-o" scrollable :text="noticeText" />
    <div class="flex items-center pr-[0.2rem]">
      <van-search
        class="flex-1"
        v-model="queryParams.searchVal"
        shape="round"
        placeholder="搜索编号,昵称"
      />
      <van-button round type="success" size="small" @click="funSearch"
        >搜索</van-button
      >
    </div>
    <div>
      <van-dropdown-menu :overlay="false">
        <van-dropdown-item
          title="分类"
          v-model="queryParams.category"
          :options="categoryList.raw"
          @change="funRefresh"
        />
        <van-dropdown-item
          title="位置"
          v-model="queryParams.address"
          :options="positionList.raw"
          @change="funRefresh"
        />
        <van-dropdown-item
          title="特色"
          v-model="queryParams.feature"
          :options="featureList.raw"
          @change="funRefresh"
        />
        <van-dropdown-item
          title="上新时间"
          v-model="queryParams.groundTime"
          :options="timeList.raw"
          @change="funRefresh"
        />
      </van-dropdown-menu>
    </div>
    <div>
      <van-pull-refresh v-model="refreshing" @refresh="funRefresh">
        <van-list
          v-model:loading="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="funLoad"
        >
          <van-grid :column-num="2" :border="false">
            <van-grid-item
              v-for="item in dataList"
              :key="item.id"
              @click="funDataClick(item)"
            >
              <div class="rounded-[20px] text-[0.32rem]">
                <div>
                  <van-image
                    width="100%"
                    radius="10"
                    height="5rem"
                    :src="baseUrl + item.detailsImg"
                  ></van-image>
                </div>
                <div
                  class="mt-[20px] bg-green-100 rounded-b-[20px] rounded-t-[10px] flex flex-col h-[2.1rem] p-[12px]"
                >
                  <div class="flex items-center justify-between">
                    <span
                      class="overflow-hidden overflow-ellipsis whitespace-nowrap inline-block max-w-[3rem]"
                      >{{ item.nickName }}</span
                    >
                    <span class="font-bold">{{ item.address }}</span>
                  </div>
                  <div class="flex items-center justify-between mt-[0.2rem]">
                    <span
                      class="text-white text-[0.28rem] bg-red-500 inline-block rounded-[10px] p-[8px] leading-none align-middle"
                      v-if="item.suggest"
                      >推荐</span
                    >
                    <span
                      class="text-white text-[0.28rem] bg-green-500 inline-block rounded-[10px] p-[8px] leading-none align-middle"
                      v-else
                      >不推荐</span
                    >
                    <span class="text-green-500 font-bold" v-if="item.free"
                      >空闲</span
                    >
                    <span class="text-red-500 font-bold" v-else>忙碌</span>
                  </div>
                  <div class="flex items-center justify-between mt-[0.2rem]">
                    <span
                      class="text-gray-500 inline-block overflow-hidden whitespace-nowrap overflow-ellipsis max-w-[1.9rem]"
                      >编号:{{ item.number || 12345 }}</span
                    >
                    <div
                      class="max-w-[2.5rem] overflow-hidden overflow-ellipsis whitespace-nowrap"
                    >
                      <span
                        v-for="(item, index) in item.validateInfo"
                        :key="index"
                        class="bg-[rgb(51,120,245)] text-white p-[4px] ml-[8px] inline-block"
                        >{{ item }}</span
                      >
                    </div>
                  </div>
                </div>
              </div>
            </van-grid-item>
          </van-grid>
        </van-list>
      </van-pull-refresh>
    </div>
  </div>
</template>
