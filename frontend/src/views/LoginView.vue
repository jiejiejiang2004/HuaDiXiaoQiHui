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
          <el-radio-group v-model="candidateMode" class="mode-switch">
            <el-radio-button label="password">密码登录</el-radio-button>
            <el-radio-button label="email">邮箱验证码登录</el-radio-button>
          </el-radio-group>
          <el-form label-position="top" :model="candidateForm">
            <el-form-item label="手机号">
              <el-input v-model="candidateForm.mobile" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="candidateForm.email" />
            </el-form-item>
            <el-form-item v-if="candidateMode === 'password'" label="密码">
              <el-input
                v-model="candidateForm.password"
                type="password"
                show-password
              />
            </el-form-item>
            <el-form-item v-else label="邮箱验证码">
              <div class="verify-row">
                <el-input v-model="candidateForm.emailCode" />
                <el-button
                  @click="
                    runSafely(() => sendCode(candidateForm.email, 'LOGIN'))
                  "
                  >获取验证码</el-button
                >
              </div>
            </el-form-item>
            <el-form-item label="注册邮箱验证码">
              <div class="verify-row">
                <el-input
                  v-model="candidateForm.registerEmailCode"
                  placeholder="注册时填写"
                />
                <el-button
                  @click="
                    runSafely(() => sendCode(candidateForm.email, 'REGISTER'))
                  "
                  >发送验证码</el-button
                >
              </div>
            </el-form-item>
            <div class="action-row">
              <el-button
                type="primary"
                :loading="candidateLoading"
                @click="runSafely(handleCandidateSubmit)"
              >
                {{ candidateMode === "password" ? "登录" : "邮箱验证码登录" }}
              </el-button>
              <el-button @click="runSafely(handleCandidateRegister)"
                >快速注册</el-button
              >
              <el-button text @click="openResetDialog('candidate')"
                >找回密码</el-button
              >
            </div>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="企业用户" name="enterprise">
          <el-radio-group v-model="enterpriseMode" class="mode-switch">
            <el-radio-button label="password">密码登录</el-radio-button>
            <el-radio-button label="email">邮箱注册</el-radio-button>
          </el-radio-group>
          <el-form label-position="top" :model="enterpriseForm">
            <el-form-item label="手机号">
              <el-input v-model="enterpriseForm.mobile" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="enterpriseForm.email" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input
                v-model="enterpriseForm.password"
                type="password"
                show-password
              />
            </el-form-item>
            <el-form-item label="邮箱验证码">
              <div class="verify-row">
                <el-input
                  v-model="enterpriseForm.emailCode"
                  :placeholder="
                    enterpriseMode === 'password'
                      ? '找回密码或注册时使用'
                      : '注册时必填'
                  "
                />
                <el-button
                  @click="
                    runSafely(() =>
                      sendCode(
                        enterpriseForm.email,
                        enterpriseMode === 'password' ? 'RESET_PWD' : 'REGISTER'
                      )
                    )
                  "
                  >获取验证码</el-button
                >
              </div>
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
                @click="runSafely(handleEnterpriseLogin)"
                >密码登录</el-button
              >
              <el-button @click="runSafely(handleEnterpriseRegister)"
                >快速注册</el-button
              >
              <el-button text @click="openResetDialog('enterprise')"
                >找回密码</el-button
              >
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
                @click="runSafely(handleAdminLogin)"
                >登录后台</el-button
              >
            </div>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <el-alert
        v-if="lastDebugCode"
        class="debug-alert"
        type="success"
        :closable="false"
        :title="`当前调试验证码：${lastDebugCode}`"
      />
    </el-card>

    <el-dialog v-model="resetDialogVisible" title="找回密码" width="420px">
      <el-form label-position="top" :model="resetForm">
        <el-form-item label="邮箱">
          <el-input v-model="resetForm.email" />
        </el-form-item>
        <el-form-item label="邮箱验证码">
          <div class="verify-row">
            <el-input v-model="resetForm.emailCode" />
            <el-button
              @click="runSafely(() => sendCode(resetForm.email, 'RESET_PWD'))"
              >获取验证码</el-button
            >
          </div>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input
            v-model="resetForm.newPassword"
            type="password"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="runSafely(submitResetPassword)"
          >完成</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import {
  adminLogin,
  candidateLogin,
  candidateEmailLogin,
  candidateRegister,
  enterpriseLogin,
  enterpriseRegister,
  resetEnterprisePassword,
  resetPassword,
  sendEmailCode,
} from "@/api/recruit";
import {
  setAccessToken,
  setRefreshToken,
  setUserName,
  setUserType,
} from "@/utils/auth";

const route = useRoute();
const router = useRouter();
const activeTab = ref((route.query.tab as string) || "candidate");
const candidateMode = ref("password");
const enterpriseMode = ref("password");
const candidateLoading = ref(false);
const enterpriseLoading = ref(false);
const adminLoading = ref(false);
const resetDialogVisible = ref(false);
const resetTarget = ref<"candidate" | "enterprise">("candidate");
const lastDebugCode = ref("");

const candidateForm = reactive({
  mobile: "13812345678",
  email: "candidate@example.com",
  password: "Abc@123456",
  emailCode: "",
  registerEmailCode: "",
});

const enterpriseForm = reactive({
  mobile: "13912345678",
  email: "enterprise@example.com",
  password: "Abc@123456",
  companyName: "成都校企科技有限公司",
  emailCode: "",
});

const adminForm = reactive({
  mobile: "18800000000",
  password: "Admin@123456",
});

const resetForm = reactive({
  email: "",
  emailCode: "",
  newPassword: "",
});

function saveLoginState(
  userType: "CANDIDATE" | "ENTERPRISE" | "ADMIN",
  data: Record<string, unknown>,
  userName: string,
  redirectPath: string
) {
  const accessToken = String(data.accessToken || "");
  const refreshToken = String(data.refreshToken || "");
  setAccessToken(accessToken);
  if (refreshToken) {
    setRefreshToken(refreshToken);
  }
  setUserType(userType);
  setUserName(userName);
  router.push(redirectPath);
}

async function sendCode(email: string, scene: string) {
  if (!email) {
    ElMessage.warning("请先输入邮箱");
    return;
  }
  const data = await sendEmailCode({
    email,
    scene,
    captcha: "A3F9",
    captchaKey: "debug-captcha",
  });
  lastDebugCode.value = data.debugCode || "";
  ElMessage.success(`验证码已发送，有效期 ${data.expireSeconds} 秒`);
}

async function runSafely(task: () => Promise<void>) {
  try {
    await task();
  } catch {
    // 拦截器已统一提示，这里阻止未处理 Promise 继续冒泡到 dev overlay
  }
}

async function handleCandidateLogin() {
  candidateLoading.value = true;
  try {
    const data = await candidateLogin({
      mobile: candidateForm.mobile,
      password: candidateForm.password,
    });
    saveLoginState("CANDIDATE", data, data.userName || "求职者", "/candidate");
    ElMessage.success("登录成功");
  } finally {
    candidateLoading.value = false;
  }
}

async function handleCandidateEmailLogin() {
  candidateLoading.value = true;
  try {
    const data = await candidateEmailLogin({
      email: candidateForm.email,
      emailCode: candidateForm.emailCode,
    });
    saveLoginState("CANDIDATE", data, "求职者", "/candidate");
    ElMessage.success("登录成功");
  } finally {
    candidateLoading.value = false;
  }
}

async function handleCandidateSubmit() {
  if (candidateMode.value === "password") {
    await handleCandidateLogin();
    return;
  }
  await handleCandidateEmailLogin();
}

async function handleCandidateRegister() {
  candidateLoading.value = true;
  try {
    const data = await candidateRegister({
      mobile: candidateForm.mobile,
      email: candidateForm.email,
      emailCode: candidateForm.registerEmailCode,
      password: candidateForm.password,
      identity: "STUDENT",
      name: "测试求职者",
      agreeProtocol: true,
    });
    saveLoginState("CANDIDATE", data, "测试求职者", "/candidate");
    ElMessage.success("注册成功");
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
    saveLoginState(
      "ENTERPRISE",
      data,
      enterpriseForm.companyName || "企业用户",
      "/enterprise"
    );
    ElMessage.success("登录成功");
  } finally {
    enterpriseLoading.value = false;
  }
}

async function handleEnterpriseRegister() {
  enterpriseLoading.value = true;
  try {
    const data = await enterpriseRegister({
      contactMobile: enterpriseForm.mobile,
      email: enterpriseForm.email,
      emailCode: enterpriseForm.emailCode,
      password: enterpriseForm.password,
      contactName: "企业联系人",
      companyName: enterpriseForm.companyName,
      agreeProtocol: true,
    });
    saveLoginState(
      "ENTERPRISE",
      data,
      enterpriseForm.companyName || "企业用户",
      "/enterprise"
    );
    ElMessage.success("注册成功");
  } finally {
    enterpriseLoading.value = false;
  }
}

async function handleAdminLogin() {
  adminLoading.value = true;
  try {
    const data = await adminLogin(adminForm);
    saveLoginState("ADMIN", data, data.userName || "平台管理员", "/admin");
    ElMessage.success("管理员登录成功");
  } finally {
    adminLoading.value = false;
  }
}

function openResetDialog(target: "candidate" | "enterprise") {
  resetTarget.value = target;
  resetForm.email =
    target === "candidate" ? candidateForm.email : enterpriseForm.email;
  resetForm.emailCode = "";
  resetForm.newPassword = "";
  resetDialogVisible.value = true;
}

async function submitResetPassword() {
  try {
    if (resetTarget.value === "candidate") {
      await resetPassword(resetForm);
    } else {
      await resetEnterprisePassword(resetForm);
    }
    ElMessage.success("密码重置成功，请重新登录");
    resetDialogVisible.value = false;
  } catch {
    // 由响应拦截器处理提示
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
  max-width: 640px;
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
  align-items: center;
  flex-wrap: wrap;
}

.mode-switch {
  margin-bottom: 16px;
}

.verify-row {
  display: flex;
  width: 100%;
  gap: 12px;
}

.debug-alert {
  margin-top: 16px;
}
</style>
