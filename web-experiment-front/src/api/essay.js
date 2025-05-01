import request from "@/utils/request";

export const insertEssayApi = (essay) => {
  return request.post("/essays", essay);
};

export const deleteEssayApi = (id,adminId) => {
  return request.delete(`/essays/${id}?adminId=${adminId}`);
};

export const updateEssayApi = (essay) => {
  return request.put("/essays", essay);
};

export const selectEssayByPageApi = (adminId, title, page, pageSize) => {
  return request.get(
    `/essays?adminId=${adminId}&title=${title}&page=${page}&pageSize=${pageSize}`
  );
};

export const selectEssayByIdApi = (id) => {
  return request.get(`/essays/${id}`);
};
