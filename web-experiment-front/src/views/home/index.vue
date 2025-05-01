<script setup>
import { ref, onMounted } from 'vue';
import { selectAdminByUsernameApi, updateAdminInfoApi } from '@/api/admin';
import { ElMessage } from 'element-plus';
import { Edit, Position } from '@element-plus/icons-vue';

//查询管理员信息
onMounted(() => {
    const loginAdmin = JSON.parse(localStorage.getItem('loginAdmin'));
    if (loginAdmin && loginAdmin.username) {
        loginUsername.value = loginAdmin.username;
    }
    searchAdmin();
})
const loginUsername = ref('');
const searchAdmin = async () => {
    const result = await selectAdminByUsernameApi(loginUsername.value);
    if (result.code) {
        showInfo.value = result.data;
    }
}
const showInfo = ref({
    name: '',
    phone: '',
    email: '',
    birthday: '',
    address: '',
    money: '',
    avatar: ''
});

//日历时间
const value = ref(new Date());

//编辑管理员信息
const isEditing = ref(false);
const editFormRef = ref();
const editRules = ref({
    name: [{ min: 2, max: 10, message: '姓名长度应为2-10个字符', trigger: 'blur' }],
    phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号', trigger: 'blur' }],
    email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
    ]
});
const handleEdit = () => {
    isEditing.value = true;
}
const handleSave = () => {
    if (!editFormRef.value) return;
    editFormRef.value.validate(async (valid) => {
        if (valid) {
            const result = await updateAdminInfoApi(showInfo.value);
            if (result.code) {
                ElMessage.success('保存成功');
                isEditing.value = false;
            } else {
                ElMessage.error(result.msg);
            }
        } else {
            ElMessage.error('表单校验不通过');
        }
    })
}
const handleAvatarSuccess = (response) => {
    showInfo.value.avatar = response.data;
}
const beforeAvatarUpload = (file) => {
    if (file.type !== 'image/jpeg' && file.type !== 'image/png') {
        ElMessage.error('请上传JPG/PNG格式图片');
        return false;
    } else if (file.size / 1024 / 1024 > 10) {
        ElMessage.error('请上传大小不超过10M的图片');
        return false;
    }
    return true;
}
</script>

<template>
    <div class="home-container">
        <!-- 管理员信息 -->
        <el-form ref="editFormRef" :model="showInfo" :rules="editRules" class="description">
            <!-- 描述信息 -->
            <el-descriptions direction="vertical" border style="margin-top: 22%">
                <el-descriptions-item :rowspan="2" :width="10" label="头像" align="center">
                    <el-upload action="/api/upload" :show-file-list="false" :on-success="handleAvatarSuccess"
                        :before-upload="beforeAvatarUpload" v-if="isEditing">
                        <img v-if="showInfo.avatar" :src="showInfo.avatar" style="width: 120px; height: 120px" />
                    </el-upload>
                    <div v-else>
                        <img v-if="showInfo.avatar" :src="showInfo.avatar" style="width: 120px; height: 120px" />
                    </div>
                </el-descriptions-item>
                <el-descriptions-item label="姓名">
                    <el-form-item prop="name">
                        <el-input v-if="isEditing" v-model="showInfo.name" />
                        <span v-else>{{ showInfo.name }}</span>
                    </el-form-item>
                </el-descriptions-item>
                <el-descriptions-item label="联系方式">
                    <el-form-item prop="phone">
                        <el-input v-if="isEditing" v-model="showInfo.phone" />
                        <span v-else>{{ showInfo.phone }}</span>
                    </el-form-item>
                </el-descriptions-item>
                <el-descriptions-item label="邮箱">
                    <el-form-item prop="email">
                        <el-input v-if="isEditing" v-model="showInfo.email" />
                        <span v-else>{{ showInfo.email }}</span>
                    </el-form-item>
                </el-descriptions-item>
                <el-descriptions-item label="出生日期">
                    <el-form-item prop="birthday">
                        <el-input v-if="isEditing" v-model="showInfo.birthday" type="date" value-format="yyyy-MM-dd" />
                        <span v-else>{{ showInfo.birthday }}</span>
                    </el-form-item>
                </el-descriptions-item>
                <el-descriptions-item label="薪资">
                    <el-form-item prop="money">
                        <el-input v-if="isEditing" v-model="showInfo.money" />
                        <span v-else>{{ showInfo.money }}</span>
                    </el-form-item>
                </el-descriptions-item>
                <el-descriptions-item label="地址">
                    <el-form-item prop="address">
                        <el-input v-if="isEditing" v-model="showInfo.address" />
                        <span v-else>{{ showInfo.address }}</span>
                    </el-form-item>
                </el-descriptions-item>
            </el-descriptions>
            <!-- 编辑按钮 -->
            <div class="edit">
                <el-button type="primary" @click="handleEdit" :icon="Edit">编辑信息</el-button>
                <el-button type="primary" @click="handleSave" :icon="Position">保存信息</el-button>
            </div>
        </el-form>
        <!-- 日历时间 -->
        <div class="calendar">
            <el-calendar v-model="value" />
        </div>
    </div>
</template>

<style scoped>
@import '../../styles/home.css'
</style>