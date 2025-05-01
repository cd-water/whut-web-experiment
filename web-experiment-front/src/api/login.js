import request from "@/utils/request";

export const loginApi = (lohinInfo) => {
  return request.post("/login", lohinInfo);
};

export const registerApi = (registerForm) => {
  return request.post("/register", registerForm);
};

export const forgetApi = (forgetForm) => {
  return request.put("/forget", forgetForm);
};
