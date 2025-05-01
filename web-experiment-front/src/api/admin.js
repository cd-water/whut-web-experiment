import request from "@/utils/request";

export const selectAdminByUsernameApi = (username) => {
  return request.get(`/admins/${username}`);
};

export const updateAdminInfoApi = (showInfo) => {
  return request.put("/admins/info", showInfo);
};

export const updateAdminPasswordApi = (changePasswordForm) => {
  return request.put("/admins/password", changePasswordForm);
};

export const selectAdminByPageApi = (page, pageSize) => {
  return request.get(`/admins?page=${page}&pageSize=${pageSize}`);
};
