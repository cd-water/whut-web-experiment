<script setup>
import { ref, onMounted } from 'vue';
import { selectAdminByPageApi, selectAdminByUsernameApi } from '@/api/admin';
import { Back, CirclePlus, Close, Delete, EditPen, Position, Search, Ship } from '@element-plus/icons-vue';
import { insertEssayApi, deleteEssayApi, updateEssayApi, selectEssayByPageApi, selectEssayByIdApi } from '@/api/essay';
import { ElMessage, ElMessageBox } from 'element-plus';
import * as echarts from 'echarts';

onMounted(() => {
    handleAdminSearch();
})
//存储初始化报表数据
const nameList = ref([]);
const countList = ref([]);
//进入文章管理页面控制
const isntEnter = ref(true);
//记录正在操作的作者（管理员）id与用户名
const operateAdminId = ref();
const operateAdminUsername = ref();

//查询作者（管理员）信息
const searchAdmin = async () => {
    const result = await selectAdminByUsernameApi(operateAdminUsername.value);
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

//作者（管理员）表格
const adminList = ref([]);
const currentPageAdmin = ref(1);
const pageSizeAdmin = ref(10);
const totalAdmin = ref(0);
const handleAdminSearch = async () => {
    const result = await selectAdminByPageApi(currentPageAdmin.value, pageSizeAdmin.value);
    if (result.code) {
        adminList.value = result.data.rows;
        totalAdmin.value = result.data.total;
        //初始化报表数据
        nameList.value = adminList.value.map(admin => admin.name);
        countList.value = adminList.value.map(admin => admin.essayCount);
        initReportChart(nameList.value, countList.value);
    }
}
const handleSizeChangeAdmin = () => {
    handleAdminSearch();
}
const handleCurrentChangeAdmin = () => {
    handleAdminSearch();
}
const enterEssay = (id, username) => {
    isntEnter.value = false;
    operateAdminId.value = id;
    operateAdminUsername.value = username;
    searchAdmin();
    handleEssaySearch();
}

//文章管理
const searchEssay = ref({ title: '' });
const essayList = ref([]);
const currentPageEssay = ref(1);
const pageSizeEssay = ref(10);
const totalEssay = ref(0);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const essayTemp = ref({ title: '', content: '', adminId: '' });
const essayFormRef = ref();
const rules = ref({
    title: [
        { required: true, message: '文章标题不能为空', trigger: 'blur' }
    ],
    content: [
        { required: true, message: '文章内容不能为空', trigger: 'blur' }
    ]
});
const handleEssaySearch = async () => {
    const result = await selectEssayByPageApi(operateAdminId.value, searchEssay.value.title, currentPageEssay.value, pageSizeEssay.value);
    if (result.code) {
        essayList.value = result.data.rows;
        totalEssay.value = result.data.total;
    }
}
const handleSizeChangeEssay = () => {
    handleEssaySearch();
}
const handleCurrentChangeEssay = () => {
    handleEssaySearch();
}

//跳出新增文章弹窗
const handleInsert = () => {
    dialogVisible.value = true;
    dialogTitle.value = '新增文章';
    essayTemp.value = { title: '', content: '', adminId: operateAdminId.value };
    if (essayFormRef.value) {
        essayFormRef.value.resetFields();
    }
}

//跳出编辑文章弹窗
const handleUpdate = async (id) => {
    const result = await selectEssayByIdApi(id);
    if (result.code) {
        dialogVisible.value = true;
        dialogTitle.value = '编辑文章';
        essayTemp.value = result.data;
    }
    if (essayFormRef.value) {
        essayFormRef.value.resetFields();
    }
}

//删除文章
const handleDelete = (id) => {
    ElMessageBox.confirm('此操作将删除该文章，是否继续？', '提示', {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
    }).then(async () => {
        const result = await deleteEssayApi(id, operateAdminId.value);
        if (result.code) {
            ElMessage.success('删除成功');
            handleEssaySearch();
        } else {
            ElMessage.error(result.msg);
        }
    }).catch(() => {
        ElMessage.info('删除操作已取消');
    })
}

//保存新增/编辑文章操作
const handleSave = () => {
    if (!essayFormRef.value) return;
    essayFormRef.value.validate(async (valid) => {
        if (valid) {
            let result;
            if (essayTemp.value.id) {
                result = await updateEssayApi(essayTemp.value);
            } else {
                result = await insertEssayApi(essayTemp.value);
            }
            if (result.code) {
                ElMessage.success('保存成功');
                dialogVisible.value = false;
                handleEssaySearch();
                handleAdminSearch();
            } else {
                ElMessage.error(result.msg);
            }
        } else {
            ElMessage.error('表单校验不通过');
        }
    })
}

//初始化柱状图报表
function initReportChart(nameList, countList) {
    //初始化echarts实例
    const myChart = echarts.init(document.getElementById('report'));
    //绘制图表
    myChart.setOption({
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: nameList,
                axisTick: {
                    alignWithLabel: true
                },
                axisLabel: {
                    fontSize: 14,
                }
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '文章数量',
                axisLabel: {
                    formatter: '{value} 篇',
                    fontSize: 14
                }
            }
        ],
        series: [
            {
                name: 'Direct',
                type: 'bar',
                barWidth: '60%',
                data: countList,
            }
        ]
    });
}
</script>

<template>
    <div class="essay-container">
        <!-- 未进入文章管理页面 -->
        <div class="essay-isntEnter" v-show="isntEnter">
            <!-- 管理员表格 -->
            <el-card class="admin-table">
                <!-- 表格标题 -->
                <template #header>
                    <h1 class="title">作者列表</h1>
                </template>
                <!-- 表格内容 -->
                <el-table :data="adminList" border style="width: 100%" height="500">
                    <el-table-column type="index" label="序号" align="center" fixed="left" />
                    <el-table-column prop="name" label="作者姓名" align />
                    <el-table-column prop="essayCount" label="文章数量" align />
                    <el-table-column label="操作" align fixed="right">
                        <template #default="scope">
                            <el-button type="primary" @click="enterEssay(scope.row.id, scope.row.username)"
                                :icon="Ship">进入文章管理</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- 表格分页 -->
                <template #footer>
                    <el-pagination v-model:current-page="currentPageAdmin" v-model:page-size="pageSizeAdmin"
                        :page-sizes="[5, 10, 20, 30, 50, 75, 100]" background
                        layout="total, sizes, prev, pager, next,jumper" :total="totalAdmin"
                        @size-change="handleSizeChangeAdmin" @current-change="handleCurrentChangeAdmin" size="small"
                        :pager-count="5" />
                </template>
            </el-card>

            <!-- 文章报表 -->
            <el-card class="essay-report">
                <!-- 图表标题 -->
                <template #header>
                    <h1 class="title">作者发布文章统计图表</h1>
                    <div id="report" style="width: 100%; height: 500px;"></div>
                </template>
            </el-card>
        </div>

        <!-- 进入文章管理页面 -->
        <div class="essay-isEnter" v-show="!isntEnter">
            <!-- 作者信息 -->
            <el-card class="admin-info">
                <!-- 作者信息标题 -->
                <template #header>
                    <h1 class="title">作者基本信息</h1>
                </template>
                <!-- 作者信息内容 -->
                <el-descriptions direction="vertical" border>
                    <el-descriptions-item :rowspan="2" :width="140" label="头像" align="center">
                        <el-image style="width: 120px; height: 120px" :src="showInfo.avatar" />
                    </el-descriptions-item>
                    <el-descriptions-item label="姓名">{{ showInfo.name }}</el-descriptions-item>
                    <el-descriptions-item label="联系方式">{{ showInfo.phone }}</el-descriptions-item>
                    <el-descriptions-item label="邮箱">{{ showInfo.email }}</el-descriptions-item>
                    <el-descriptions-item label="出生日期">{{ showInfo.birthday }}</el-descriptions-item>
                    <el-descriptions-item label="薪资">{{ showInfo.money }}</el-descriptions-item>
                    <el-descriptions-item label="地址">{{ showInfo.address }}</el-descriptions-item>
                </el-descriptions>
                <!-- 返回按钮 -->
                <template #footer>
                    <el-button type="primary" @click="isntEnter = true" :icon="Back">返回</el-button>
                </template>
            </el-card>
            <!-- 文章管理表格 -->
            <el-card class="essay-table">
                <!-- 文章管理搜索、新增按钮 -->
                <div class="insert-search">
                    <!-- 文章新增按钮 -->
                    <div class="essay-insert">
                        <el-button type="primary" plain @click="handleInsert" size="large"
                            :icon="CirclePlus">新增文章</el-button>
                    </div>
                    <!-- 文章搜索 -->
                    <el-form :model="searchEssay" class="essay-search">
                        <el-form-item>
                            <el-input v-model="searchEssay.title" placeholder="请输入文章标题" clearable></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="handleEssaySearch" :icon="Search">搜索</el-button>
                        </el-form-item>
                    </el-form>
                </div>
                <!-- 文章管理表格 -->
                <el-table :data="essayList" border style="width: 100%" height="500">
                    <el-table-column type="index" label="序号" align="center" fixed="left" />
                    <el-table-column prop="title" label="文章标题" align="center" />
                    <el-table-column prop="content" label="文章内容" align="center" sortable show-overflow-tooltip />
                    <el-table-column label="操作" align fixed="right">
                        <template #default="scope">
                            <el-button type="primary" size="small" @click="handleUpdate(scope.row.id)"
                                :icon="EditPen">编辑</el-button>
                            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)"
                                :icon="Delete">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- 文章管理分页 -->
                <template #footer>
                    <el-pagination v-model:current-page="currentPageEssay" v-model:page-size="pageSizeEssay"
                        :page-sizes="[5, 10, 20, 30, 50, 75, 100]" background
                        layout="total, sizes, prev, pager, next ,jumper" :total="totalEssay"
                        @size-change="handleSizeChangeEssay" @current-change="handleCurrentChangeEssay" size="small"
                        :pager-count="5" />
                </template>
            </el-card>
        </div>

        <!-- 文章管理弹窗（新增、编辑） -->
        <el-dialog v-model="dialogVisible" width="420px" :close-on-press-escape="false" :close-on-click-modal="false">
            <!-- 弹窗标题 -->
            <template #header>
                <h1 class="dialog-title"> {{ dialogTitle }} </h1>
            </template>
            <!-- 弹窗表单 -->
            <el-form :model="essayTemp" :rules="rules" ref="essayFormRef" class="dialog-form">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="essayTemp.title" placeholder="请输入文章标题"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                    <el-input v-model="essayTemp.content" placeholder="请输入文章内容" type="textarea"
                        :autosize="{ minRows: 8, maxRows: 15 }" size="large"></el-input>
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
    </div>
</template>

<style scoped>
@import '../../styles/essay.css';
</style>