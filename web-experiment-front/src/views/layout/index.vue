<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useRouter } from 'vue-router';
import { updateAdminPasswordApi } from '@/api/admin';
import { Bicycle, CircleCheck, CircleClose, Cpu, Document, EditPen, House, IceCream, Menu, Switch, SwitchButton, User, Watermelon } from '@element-plus/icons-vue';

const router = useRouter();

onMounted(() => {
    const loginAdmin = JSON.parse(localStorage.getItem('loginAdmin'));
    if (loginAdmin && loginAdmin.username) {
        changePasswordForm.value.username = loginAdmin.username;
    }
})

// 主题切换
const currentTheme = ref(new URL('../../assets/summer.png', import.meta.url).href);
const themeTitle = ref('linear-gradient(to right, #58a6e6, #28b171, #3cc7da, #f1f7e4, #87a3b6)');
const changeTheme = (theme) => {
    currentTheme.value = new URL(`../../assets/${theme}.png`, import.meta.url).href;
    if (theme === 'spring') {
        themeTitle.value = 'linear-gradient(to right, #0ddb17, #c2e48b, #b1eb96, #3aeb43, #7dad7f)';
    } else if (theme === 'summer') {
        themeTitle.value = 'linear-gradient(to right, #58a6e6, #28b171, #3cc7da, #f1f7e4, #87a3b6)';
    } else if (theme === 'autumn') {
        themeTitle.value = 'linear-gradient(to right, #e6ba2a, #dbcd4f, #d4a713, #bfd448, #c07f1d)';
    } else if (theme === 'winter') {
        themeTitle.value = 'linear-gradient(to right, #a3cfe4, #62bfdb, #2eb8c2, #4f95d6, #13498f)';
    }
}

//修改密码
const changePasswordDialogVisible = ref(false);
const changePasswordForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' });
const changePasswordFormRef = ref();
const changePasswordRules = ref({
    oldPassword: [
        { required: true, message: '旧密码不能为空', trigger: 'blur' }
    ],
    newPassword: [
        { required: true, message: '新密码不能为空', trigger: 'blur' }
    ],
    confirmPassword: [
        { required: true, message: '确认新密码不能为空', trigger: 'blur' }
    ]
});
const change = () => {
    changePasswordDialogVisible.value = true;
    if (changePasswordFormRef.value) {
        changePasswordFormRef.value.resetFields();
    }
}
const changeCheck = () => {
    if (!changePasswordFormRef.value) return;
    changePasswordFormRef.value.validate(async (valid) => {
        if (valid) {
            const result = await updateAdminPasswordApi(changePasswordForm.value);
            if (result.code) {
                ElMessage.success('密码修改成功,请重新登录');
                changePasswordDialogVisible.value = false;
                router.push('/login');
            } else {
                ElMessage.error(result.msg);
            }
        } else {
            ElMessage.error('表单校验不通过');
        }
    })
}

//退出登录
const logout = () => {
    ElMessageBox.confirm('确认退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        ElMessage.success('退出登录成功');
        localStorage.removeItem('loginAdmin');
        router.push('/login');
    }).catch(() => {
        ElMessage.error('取消退出登录');
    })
}
</script>

<template>
    <!-- 布局背景 -->
    <div class="layout-background" :style="{ backgroundImage: 'url(' + currentTheme + ')' }"></div>

    <!-- 页面布局 -->
    <div class="common-layout">
        <el-container>
            <!-- 顶栏 -->
            <el-header class="header" :style="{ backgroundImage: themeTitle }">
                <span class="title">cdwater-web</span>
                <span class="right_tool">
                    <el-button style="color: black" @click="change" text :icon="EditPen">修改密码</el-button>
                    <el-button style="color: black" @click="logout" text :icon="SwitchButton">退出登录</el-button>
                </span>
            </el-header>
            <el-container>
                <!-- 侧边栏 -->
                <el-aside class="aside">
                    <el-menu router active-text-color="#1092c5" default-active="/home">
                        <!-- 首页 -->
                        <el-menu-item index="/home">
                            <el-icon>
                                <House />
                            </el-icon>首页
                        </el-menu-item>
                        <!-- 功能菜单 -->
                        <el-sub-menu index="/function">
                            <template #title>
                                <el-icon>
                                    <Menu />
                                </el-icon>功能菜单
                            </template>
                            <el-menu-item index="/user">
                                <el-icon>
                                    <User />
                                </el-icon>用户管理
                            </el-menu-item>
                            <el-menu-item index="/essay">
                                <el-icon>
                                    <Document />
                                </el-icon>文章管理
                            </el-menu-item>
                        </el-sub-menu>
                        <!-- 主题切换 -->
                        <el-sub-menu index="/theme">
                            <template #title>
                                <el-icon>
                                    <Switch />
                                </el-icon>主题切换
                            </template>
                            <el-menu-item @click="changeTheme('spring')">
                                <el-icon>
                                    <Bicycle />
                                </el-icon>春季主题
                            </el-menu-item>
                            <el-menu-item @click="changeTheme('summer')">
                                <el-icon>
                                    <Watermelon />
                                </el-icon>夏季主题
                            </el-menu-item>
                            <el-menu-item @click="changeTheme('autumn')">
                                <el-icon>
                                    <Cpu />
                                </el-icon>秋季主题
                            </el-menu-item>
                            <el-menu-item @click="changeTheme('winter')">
                                <el-icon>
                                    <IceCream />
                                </el-icon>冬季主题
                            </el-menu-item>
                        </el-sub-menu>
                    </el-menu>
                </el-aside>
                <!-- 主要区域 -->
                <el-main>
                    <router-view />
                </el-main>
            </el-container>

            <!-- 修改密码弹窗 -->
            <el-dialog v-model="changePasswordDialogVisible" align-center width="620px" :close-on-press-escape="false"
                :close-on-click-modal="false">
                <!-- 弹窗标题 -->
                <template #header>
                    <h1 class="change-title">修改密码</h1>
                </template>
                <!-- 弹窗表单 -->
                <el-form ref="changePasswordFormRef" :rules="changePasswordRules" :model="changePasswordForm"
                    class="change-form" label-width="auto" label-position="left">
                    <el-form-item label="旧密码" prop="oldPassword">
                        <el-input type="password" v-model="changePasswordForm.oldPassword" placeholder="请输入旧密码"
                            show-password />
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPassword">
                        <el-input type="password" v-model="changePasswordForm.newPassword" placeholder="请输入新密码"
                            show-password />
                    </el-form-item>
                    <el-form-item label="确认新密码" prop="confirmPassword">
                        <el-input type="password" v-model="changePasswordForm.confirmPassword" placeholder="请确认新密码"
                            show-password />
                    </el-form-item>
                </el-form>
                <!-- 弹窗按钮 -->
                <template #footer>
                    <div class="change-btn">
                        <el-button type="primary" @click="changePasswordDialogVisible = false"
                            :icon="CircleClose">取消</el-button>
                        <el-button type="primary" @click="changeCheck" :icon="CircleCheck">确定</el-button>
                    </div>
                </template>
            </el-dialog>
        </el-container>
    </div>
</template>

<style scoped>
@import '../../styles/layout.css';
@import '../../styles/change.css';

.layout-background {
    position: fixed;
    background-position: center center;
    background-size: cover;
    height: 100vh;
    width: 100vw;
    top: 0;
    left: 0;
    display: flex;
}
</style>