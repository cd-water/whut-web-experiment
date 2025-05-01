import request from "@/utils/request";

export const insertUserApi = (user) => {
  return request.post("/users", user);
};

export const deleteUserApi = (id) => {
  return request.delete(`/users/${id}`);
};

export const updateUserApi = (user) => {
  return request.put("/users", user);
};

export const selectUserByPageApi = (name, page, pageSize) => {
  return request.get(`/users?name=${name}&page=${page}&pageSize=${pageSize}`);
};

export const selectUserByIdApi = (id) => {
  return request.get(`/users/${id}`);
};
