import axios from "axios";
import { ElMessage } from "element-plus";
import router from "../router";

const request = axios.create({
  baseURL: "/api",
  timeout: 600000,
});

request.interceptors.request.use(
  (config) => {
    const loginAdmin = JSON.parse(localStorage.getItem("loginAdmin"));
    if (loginAdmin && loginAdmin.token) {
      config.headers.token = loginAdmin.token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    if (error.response.status === 401) {
      ElMessage.error("登录超时，请重新登录");

      // 401 未授权，跳转到登录页面
      router.push("/login");
    } else {
      ElMessage.error("接口访问异常");
    }
    return Promise.reject(error);
  }
);

export default request;
