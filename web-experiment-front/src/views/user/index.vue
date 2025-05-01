<script setup>
import { ref, onMounted, watch } from 'vue';
import { insertUserApi, deleteUserApi, updateUserApi, selectUserByPageApi, selectUserByIdApi } from '@/api/user';
import { ElMessage, ElMessageBox } from 'element-plus';
import { provinceAndCityData, CodeToText, TextToCode } from 'element-china-area-data';
import { CirclePlus, Close, Delete, EditPen, Position, Search } from '@element-plus/icons-vue';

onMounted(() => {
    handleSearch();
})

//用户分页查询相关数据
const searchUser = ref({ name: '' });
const userList = ref([]);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
watch(() => searchUser.value.name, () => {
    handleSearch();
})

//新增/编辑用户弹窗相关数据
const dialogVisible = ref(false);
const dialogTitle = ref('');
const userFormRef = ref();
const userTemp = ref({ name: '', selectedOptions: [], province: '', city: '', address: '', zip: '' });
const rules = ref({
    name: [
        { required: true, message: '姓名不能为空', trigger: 'blur' },
        { min: 2, max: 10, message: '姓名长度应在 2 到 10 个字符之间', trigger: 'blur' }
    ],
    address: [
        { required: true, message: '地址不能为空', trigger: 'blur' }
    ],
    selectedOptions: [
        { required: true, message: '请选择省市区', trigger: 'change' }
    ],
    zip: [
        { required: true, message: '邮编不能为空', trigger: 'blur' },
        { pattern: /^\d{6}$/, message: '邮编应为6位数字', trigger: 'blur' },
    ]
});
watch(() => userTemp.value.selectedOptions, (newVal, oldVal) => {
    if (newVal.length == 2) {
        userTemp.value.province = CodeToText[newVal[0]];
        userTemp.value.city = CodeToText[newVal[1]];
    } else {
        userTemp.value.province = '';
        userTemp.value.city = '';
    }
})

//分页查询用户
const handleSearch = async () => {
    const result = await selectUserByPageApi(searchUser.value.name, currentPage.value, pageSize.value);
    if (result.code) {
        userList.value = result.data.rows;
        total.value = result.data.total;
    }
}
const handleSizeChange = () => {
    handleSearch();
}
const handleCurrentChange = () => {
    handleSearch();
}

//跳出新增用户弹窗
const handleInsert = () => {
    dialogVisible.value = true;
    dialogTitle.value = '新增用户';
    userTemp.value = { name: '', selectedOptions: [], province: '', city: '', address: '', zip: '' };
    if (userFormRef.value) {
        userFormRef.value.resetFields();
    }
}

//跳出编辑用户弹窗
const handleUpdate = async (id) => {
    const result = await selectUserByIdApi(id);
    if (result.code) {
        dialogVisible.value = true;
        dialogTitle.value = '编辑用户';
        userTemp.value = result.data;
        try {
            userTemp.value.selectedOptions = [TextToCode[result.data.province].code, TextToCode[result.data.province][result.data.city].code];
        } catch {
            userTemp.value.selectedOptions = [];
        }
    }
    if (userFormRef.value) {
        userFormRef.value.resetFields();
    }
}

//删除用户
const handleDelete = (id) => {
    ElMessageBox.confirm('此操作将删除该用户，是否继续？', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
    }).then(async () => {
        const result = await deleteUserApi(id);
        if (result.code) {
            ElMessage.success('删除成功');
            handleSearch();
        } else {
            ElMessage.error(result.msg);
        }
    }).catch(() => {
        ElMessage.info('删除操作已取消');
    })
}

//保存新增/编辑用户操作
const handleSave = () => {
    if (!userFormRef.value) return;
    userFormRef.value.validate(async (valid) => {
        if (valid) {
            let result;
            if (userTemp.value.id) {
                result = await updateUserApi(userTemp.value);
            } else {
                result = await insertUserApi(userTemp.value);
            }
            if (result.code) {
                ElMessage.success('保存成功');
                dialogVisible.value = false;
                handleSearch();
            } else {
                ElMessage.error(result.msg);
            }
        } else {
            ElMessage.error('表单校验不通过');
        }
    })
}
</script>

<template>
    <!-- 用户管理界面 -->
    <el-card>
        <!-- 用户管理标题 -->
        <template #header>
            <h1 class="user-title">用户管理</h1>
        </template>
        <!-- 用户管理搜索、新增按钮 -->
        <div class="insert-search">
            <!-- 用户新增按钮 -->
            <div class="user-insert">
                <el-button type="primary" plain @click="handleInsert" size="large" :icon="CirclePlus">新增用户</el-button>
            </div>
            <!-- 用户搜索 -->
            <el-form :model="searchUser" class="user-search">
                <el-form-item>
                    <el-input v-model="searchUser.name" placeholder="请输入姓名" clearable></el-input>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
                </el-form-item>
            </el-form>
        </div>
        <!-- 用户管理表格 -->
        <el-table :data="userList" border style="width: 100%" height="500">
            <el-table-column type="index" label="序号" align="center" fixed="left" />
            <el-table-column prop="name" label="姓名" align="center" />
            <el-table-column prop="province" label="省份" align="center" />
            <el-table-column prop="city" label="市区" align="center" />
            <el-table-column prop="address" label="地址" align="center" />
            <el-table-column prop="zip" label="邮编" align="center" />
            <el-table-column prop="updateTime" label="最后修改时间" align="center" sortable show-overflow-tooltip />
            <el-table-column label="操作" align="center" fixed="right">
                <template #default="scope">
                    <el-button type="primary" size="small" @click="handleUpdate(scope.row.id)"
                        :icon="EditPen">编辑</el-button>
                    <el-button type="danger" size="small" @click="handleDelete(scope.row.id)"
                        :icon="Delete">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 用户管理分页 -->
        <template #footer>
            <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
                :page-sizes="[5, 10, 20, 30, 50, 75, 100]" background layout="total, sizes, prev, pager, next, jumper"
                :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" size="small"
                :pager-count="5" />
        </template>
    </el-card>

    <!-- 用户管理弹窗（新增、编辑） -->
    <el-dialog v-model="dialogVisible" width="420px" :close-on-press-escape="false" :close-on-click-modal="false">
        <!-- 弹窗标题 -->
        <template #header>
            <h1 class="dialog-title"> {{ dialogTitle }} </h1>
        </template>
        <!-- 弹窗表单 -->
        <el-form :model="userTemp" :rules="rules" ref="userFormRef" class="dialog-form">
            <el-form-item label="姓名" prop="name">
                <el-input v-model="userTemp.name" placeholder="请输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="省市区" prop="selectedOptions">
                <el-cascader v-model="userTemp.selectedOptions" :options="provinceAndCityData" placeholder="请选择省市区"
                    :props="{
                        expandTrigger: 'hover'
                    }" style="width: 100%">
                </el-cascader>
            </el-form-item>
            <el-form-item label="地址" prop="address">
                <el-input v-model="userTemp.address" placeholder="请输入地址"></el-input>
            </el-form-item>
            <el-form-item label="邮编" prop="zip">
                <el-input v-model="userTemp.zip" placeholder="请输入邮编"></el-input>
            </el-form-item>
        </el-form>
        <!-- 弹窗按钮 -->
        <template #footer>
            <div class="dialog-button">
                <el-button @click="dialogVisible = false" :icon="Close">取消</el-button>
                <el-button type="primary" @click="handleSave" :icon="Position">保存</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<style scoped>
@import '../../styles/user.css';
</style>