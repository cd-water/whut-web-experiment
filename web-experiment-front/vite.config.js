import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { fileURLToPath, URL } from "node:url";

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      //定义路径别名
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  //配置开发服务器代理
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080", //代理的目标服务器地址
        secure: false,
        changeOrigin: true, //表示改变请求的源地址
        rewrite: (path) => path.replace(/^\/api/, ""), //重写请求路径
      },
    },
  },
});
