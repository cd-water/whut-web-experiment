<script setup>
import { ElMessage } from 'element-plus';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { loginApi, registerApi, forgetApi } from '@/api/login';
import { Back, CirclePlus, Plus, Position, Sugar } from '@element-plus/icons-vue';

const router = useRouter();

//表单验证规则
const rules = ref({
    username: [{ required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 5, max: 21, message: '用户名长度在 5 到 21 个字符', trigger: 'blur' }
    ],
    password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
    email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
    ],
    newPassword: [{ required: true, message: '新密码不能为空', trigger: 'blur' }],
    confirmPassword: [{ required: true, message: '确认新密码不能为空', trigger: 'blur' }]
});

//登录相关
const loginForm = ref({ username: '', password: '' });
const loginFormRef = ref();
const qqLogin = () => { }
const vxLogin = () => { }
const aliLogin = () => { }
const comLogin = async () => {
    const result = await loginApi(loginForm.value);
    if (result.code) {
        ElMessage.success('登录成功');
        localStorage.setItem('loginAdmin', JSON.stringify(result.data));
        router.push('/home');
    } else {
        ElMessage.error(result.msg);
    }
}

//注册相关
const registerDialogFormVisible = ref(false);
const registerForm = ref({ username: '', password: '', email: '', birthday: '', avatar: '' });
const registerFormRef = ref();
const registerAdmin = () => {
    registerDialogFormVisible.value = true;
    registerForm.value = { username: '', password: '', email: '', birthday: '', avatar: '' };
    if (registerFormRef.value) {
        registerFormRef.value.resetFields();
    }
}
const handleAvatarSuccess = (response) => {
    registerForm.value.avatar = response.data;
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
const registerCheck = () => {
    if (!registerFormRef.value) return;
    registerFormRef.value.validate(async (valid) => {
        if (valid) {
            const result = await registerApi(registerForm.value);
            if (result.code) {
                ElMessage.success('注册成功');
                registerDialogFormVisible.value = false;
            } else {
                ElMessage.error(result.msg);
            }
        } else {
            ElMessage.error('表单校验不通过');
        }
    })
}

//忘记密码相关
const forgetDialogFormVisible = ref(false);
const forgetForm = ref({ username: '', email: '', newPassword: '', confirmPassword: '' });
const forgetFormRef = ref();
const forgetPassword = () => {
    forgetDialogFormVisible.value = true;
    forgetForm.value = { username: '', email: '', newPassword: '', confirmPassword: '' };
    if (forgetFormRef.value) {
        forgetFormRef.value.resetFields();
    }
}
const forgetCheck = () => {
    if (!forgetFormRef.value) return;
    forgetFormRef.value.validate(async (valid) => {
        if (valid) {
            const result = await forgetApi(forgetForm.value);
            if (result.code) {
                ElMessage.success('密码修改成功');
                forgetDialogFormVisible.value = false;
            } else {
                ElMessage.error(result.msg);
            }
        } else {
            ElMessage.error('表单校验不通过');
        }
    })
}

//返回登录
const returnLogin = () => {
    registerDialogFormVisible.value = false;
    forgetDialogFormVisible.value = false;
}
</script>

<template>
    <div class="login-container">
        <!-- 登录页面 -->
        <el-card class="login-ui">
            <!-- 登录标题 -->
            <template #header>
                <h1 class="login-title">登录</h1>
            </template>
            <!-- 登录主体 -->
            <div class="login-main">
                <!-- 第三方登录方法 -->
                <div class="login-method">
                    <el-button class="qq-btn" type="primary" @click="qqLogin">
                        <img src="../../assets/qq.png" alt="QQ登录"> QQ登录 </el-button>
                    <el-button class="vx-btn" type="success" @click="vxLogin">
                        <img src="../../assets/vx.png" alt="微信登录"> 微信登录 </el-button>
                    <el-button class="ali-btn" @click="aliLogin">
                        <img src="../../assets/ali.png" alt="支付宝登录"> 支付宝登录 </el-button>
                </div>
                <!-- 登录表单 -->
                <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="login-form">
                    <el-form-item prop="username">
                        <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input type="password" v-model="loginForm.password" placeholder="密码"
                            show-password></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button class="login-btn" type="success" @click="comLogin" :icon="Position">登录</el-button>
                    </el-form-item>
                </el-form>
            </div>
            <!-- 登录底部 -->
            <template #footer>
                <div class="login-footer">
                    <el-button style="background: rgb(80,80,80);color: white" text @click="registerAdmin"
                        :icon="CirclePlus">注册用户</el-button>
                    <el-button style="background: rgb(80,80,80);color: white" text @click="forgetPassword"
                        :icon="Sugar">忘记密码</el-button>
                </div>
            </template>
        </el-card>

        <!-- 注册页面 -->
        <el-dialog v-model="registerDialogFormVisible" align-center width="620px" :close-on-press-escape="false"
            :close-on-click-modal="false">
            <!-- 注册标题 -->
            <template #header>
                <h1 class="register-title">注册新用户</h1>
            </template>
            <!-- 注册主体 -->
            <div class="register-main">
                <!-- 注册表单 -->
                <el-form ref="registerFormRef" :model="registerForm" :rules="rules" class="register-form">
                    <el-form-item prop="username">
                        <el-input v-model="registerForm.username" placeholder="用户名"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="registerForm.password" placeholder="密码" type="password"
                            show-password></el-input>
                    </el-form-item>
                    <el-form-item prop="email">
                        <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
                    </el-form-item>
                    <el-form-item label="出生日期">
                        <el-date-picker v-model="registerForm.birthday" type="date" placeholder="年/月/日(选填)"
                            format="YYYY-MM-DD" style="width: 100%" />
                    </el-form-item>
                </el-form>
                <!-- 上传头像 -->
                <el-upload action="/api/upload" :show-file-list="false" :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload" class="register-upload">
                    <img v-if="registerForm.avatar" :src="registerForm.avatar" style="width: 200px; height: 200px" />
                    <el-icon v-else>
                        <Plus />
                    </el-icon>
                </el-upload>
            </div>
            <!-- 注册底部 -->
            <template #footer>
                <div class="register-footer">
                    <el-button style="background: rgb(80,80,80);color: white" text @click="registerCheck"
                        :icon="CirclePlus">注册用户</el-button>
                    <el-button style="background: rgb(80,80,80);color: white" text @click="returnLogin"
                        :icon="Back">返回登录</el-button>
                </div>
            </template>
        </el-dialog>

        <!-- 忘记密码 -->
        <el-dialog v-model="forgetDialogFormVisible" align-center width="620px" :close-on-press-escape="false"
            :close-on-click-modal="false">
            <!-- 忘记密码标题 -->
            <template #header>
                <h1 class="forget-title">忘记密码</h1>
            </template>
            <!-- 忘记密码主体 -->
            <el-form ref="forgetFormRef" :model="forgetForm" :rules="rules" class="forget-main">
                <div class="forget-old">
                    <el-form-item prop="username">
                        <el-input v-model="forgetForm.username" placeholder="用户名"></el-input>
                    </el-form-item>
                    <el-form-item prop="email">
                        <el-input v-model="forgetForm.email" placeholder="邮箱"></el-input>
                    </el-form-item>
                </div>
                <div class="forget-new">
                    <el-form-item prop="newPassword">
                        <el-input v-model="forgetForm.newPassword" placeholder="新密码" type="password"
                            show-password></el-input>
                    </el-form-item>
                    <el-form-item prop="confirmPassword">
                        <el-input v-model="forgetForm.confirmPassword" placeholder="确认新密码" type="password"
                            show-password></el-input>
                    </el-form-item>
                </div>
            </el-form>
            <!-- 忘记密码底部 -->
            <template #footer>
                <div class="forget-footer">
                    <el-button style="background: rgb(80,80,80);color: white" text @click="forgetCheck"
                        :icon="Sugar">修改密码</el-button>
                    <el-button style="background: rgb(80,80,80);color: white" text @click="returnLogin"
                        :icon="Back">返回登录</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<style scoped>
@import '../../styles/login.css';
@import '../../styles/register.css';
@import '../../styles/forget.css';

.login-container {
    background-image: url('../../assets/bg.jpg');
    position: fixed;
    background-position: center center;
    background-size: cover;
    height: 100vh;
    width: 100vw;
    top: 0;
    left: 0;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>