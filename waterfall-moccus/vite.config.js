import { defineConfig } from "vite"
import vue from "@vitejs/plugin-vue"
import WindiCSS from "vite-plugin-windicss"
import { fileURLToPath, URL } from "node:url"

import autoprefixer from "autoprefixer"
import postCssPxToRem from "postcss-pxtorem"

// import { baseUrl } from './src/utils/request'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    WindiCSS(),
  ],
  server: {
    // proxy: {
    //   '/api': {
    //     target: baseUrl,
    //     changeOrigin: true,
    //     rewrite: (path) => path.replace(/^\/api/, '') // 不可以省略rewrite
    //   }
    // }
  },
  css: {
    postcss: {
      plugins: [
        autoprefixer({
          overrideBrowserslist: [
            "Android 4.1",
            "iOS 7.1",
            "Chrome > 31",
            "ff > 31",
            "ie >= 8",
          ],
        }),
        postCssPxToRem({
          // 自适应，px>rem转换
          rootValue: 75, // 75表示750设计稿，37.5表示375设计稿
          propList: ["*"], // 需要转换的属性，这里选择全部都进行转换
          selectorBlackList: [".van"], // 过滤掉van-开头的class，不进行rem转换
          exclude: "/node_modules", // 忽略包文件转换rem
        }),
      ],
    },
  },
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
      "~`": fileURLToPath(new URL("./", import.meta.url)),
    },
  },
})
