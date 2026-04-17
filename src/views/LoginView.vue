<template>
  <div class="login-page">
    <el-card class="login-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div>
            <h2>校企慧统一登录</h2>
            <p>个人求职者、企业用户与管理员共用入口</p>
          </div>
          <el-button text @click="$router.push('/')">返回首页</el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" stretch>
        <el-tab-pane label="求职者" name="candidate">
          <el-form label-position="top" :model="candidateForm">
            <el-form-item label="手机号">
              <el-input v-model="candidateForm.mobile" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input
                v-model="candidateForm.password"
                type="password"
                show-password
              />
            </el-form-item>
            <div class="action-row">
              <el-button
                type="primary"
                :loading="candidateLoading"
                @click="handleCandidateLogin"
                >登录</el-button
              >
              <el-button @click="handleCandidateRegister">快速注册</el-button>
            </div>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="企业用户" name="enterprise">
          <el-form label-position="top" :model="enterpriseForm">
            <el-form-item label="手机号">
              <el-input v-model="enterpriseForm.mobile" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input
                v-model="enterpriseForm.password"
                type="password"
                show-password
              />
            </el-form-item>
            <el-form-item label="企业名称">
              <el-input
                v-model="enterpriseForm.companyName"
                placeholder="注册时必填，登录时可不填"
              />
            </el-form-item>
            <div class="action-row">
              <el-button
                type="primary"
                :loading="enterpriseLoading"
                @click="handleEnterpriseLogin"
                >登录</el-button
              >
              <el-button @click="handleEnterpriseRegister">快速注册</el-button>
            </div>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="管理员" name="admin">
          <el-form label-position="top" :model="adminForm">
            <el-form-item label="手机号">
              <el-input v-model="adminForm.mobile" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input
                v-model="adminForm.password"
                type="password"
                show-password
              />
            </el-form-item>
            <div class="action-row">
              <el-button
                type="primary"
                :loading="adminLoading"
                @click="handleAdminLogin"
                >登录后台</el-button
              >
            </div>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import {
  adminLogin,
  candidateLogin,
  candidateRegister,
  enterpriseLogin,
  enterpriseRegister,
} from "@/api/recruit";
import { setAccessToken, setUserName, setUserType } from "@/utils/auth";

const route = useRoute();
const router = useRouter();
const activeTab = ref((route.query.tab as string) || "candidate");
const candidateLoading = ref(false);
const enterpriseLoading = ref(false);
const adminLoading = ref(false);

const candidateForm = reactive({
  mobile: "13812345678",
  password: "Abc@123456",
});

const enterpriseForm = reactive({
  mobile: "13912345678",
  password: "Abc@123456",
  companyName: "成都校企科技有限公司",
});

const adminForm = reactive({
  mobile: "18800000000",
  password: "Admin@123456",
});

async function handleCandidateLogin() {
  candidateLoading.value = true;
  try {
    const data = await candidateLogin(candidateForm);
    setAccessToken(data.accessToken);
    setUserType("CANDIDATE");
    setUserName(data.userName || "求职者");
    ElMessage.success("登录成功");
    router.push("/candidate");
  } finally {
    candidateLoading.value = false;
  }
}

async function handleCandidateRegister() {
  candidateLoading.value = true;
  try {
    const data = await candidateRegister({
      mobile: candidateForm.mobile,
      password: candidateForm.password,
      identity: "STUDENT",
      name: "测试求职者",
      agreeProtocol: true,
    });
    setAccessToken(data.accessToken);
    setUserType("CANDIDATE");
    setUserName("测试求职者");
    ElMessage.success("注册成功");
    router.push("/candidate");
  } finally {
    candidateLoading.value = false;
  }
}

async function handleEnterpriseLogin() {
  enterpriseLoading.value = true;
  try {
    const data = await enterpriseLogin({
      mobile: enterpriseForm.mobile,
      password: enterpriseForm.password,
    });
    setAccessToken(data.accessToken);
    setUserType("ENTERPRISE");
    setUserName(enterpriseForm.companyName || "企业用户");
    ElMessage.success("登录成功");
    router.push("/enterprise");
  } finally {
    enterpriseLoading.value = false;
  }
}

async function handleEnterpriseRegister() {
  enterpriseLoading.value = true;
  try {
    const data = await enterpriseRegister({
      contactMobile: enterpriseForm.mobile,
      password: enterpriseForm.password,
      contactName: "企业联系人",
      companyName: enterpriseForm.companyName,
      agreeProtocol: true,
    });
    setAccessToken(data.accessToken);
    setUserType("ENTERPRISE");
    setUserName(enterpriseForm.companyName || "企业用户");
    ElMessage.success("注册成功");
    router.push("/enterprise");
  } finally {
    enterpriseLoading.value = false;
  }
}

async function handleAdminLogin() {
  adminLoading.value = true;
  try {
    const data = await adminLogin(adminForm);
    setAccessToken(data.accessToken);
    setUserType("ADMIN");
    setUserName(data.userName || "平台管理员");
    ElMessage.success("管理员登录成功");
    router.push("/admin");
  } finally {
    adminLoading.value = false;
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 24px;
  background: linear-gradient(135deg, #eff6ff, #ecfeff);
}

.login-card {
  width: 100%;
  max-width: 560px;
  border-radius: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
}

.card-header h2 {
  margin: 0 0 8px;
}

.card-header p {
  margin: 0;
  color: #6b7280;
}

.action-row {
  display: flex;
  gap: 12px;
}
</style>
