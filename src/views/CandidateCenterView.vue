<template>
  <div class="dashboard-page">
    <div class="page-header">
      <div>
        <h1>求职者工作台</h1>
        <p>管理个人资料、在线简历、投递记录和站内消息</p>
      </div>
      <div class="header-actions">
        <el-button @click="$router.push('/')">返回首页</el-button>
        <el-button type="danger" plain @click="logout">退出</el-button>
      </div>
    </div>

    <el-row :gutter="16">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-title">
              <span>个人统计</span>
              <el-button text type="primary" @click="downloadMyStatistics"
                >导出统计</el-button
              >
            </div>
          </template>
          <el-row :gutter="16">
            <el-col :span="6">
              <div class="stat-card">
                <strong>{{ statistics.applyCount }}</strong>
                <span>累计投递</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <strong>{{ statistics.interviewCount }}</strong>
                <span>面试次数</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <strong>{{ statistics.viewedCount }}</strong>
                <span>被查看次数</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <strong>{{ statistics.favoriteCount }}</strong>
                <span>收藏职位</span>
              </div>
            </el-col>
          </el-row>
          <el-table
            :data="statistics.applyTrend || []"
            stripe
            class="trend-table"
          >
            <el-table-column prop="day" label="日期" width="160" />
            <el-table-column prop="count" label="投递数" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>个人资料</template>
          <el-form label-position="top" :model="profileForm">
            <el-form-item label="姓名">
              <el-input v-model="profile.name" disabled />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="profileForm.email" />
            </el-form-item>
            <el-form-item label="学校">
              <el-input v-model="profileForm.school" />
            </el-form-item>
            <el-form-item label="专业">
              <el-input v-model="profileForm.major" />
            </el-form-item>
            <el-form-item label="现居城市">
              <el-input v-model="profileForm.currentCity" />
            </el-form-item>
            <el-button type="primary" @click="saveProfile">保存资料</el-button>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-title">
              <span>我的简历</span>
              <el-button type="primary" @click="saveResume"
                >保存默认简历</el-button
              >
            </div>
          </template>
          <el-form label-position="top" :model="resumeForm">
            <el-form-item label="简历标题">
              <el-input v-model="resumeForm.title" />
            </el-form-item>
            <el-row :gutter="12">
              <el-col :span="12">
                <el-form-item label="姓名">
                  <el-input v-model="resumeForm.basicInfo.name" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="电话">
                  <el-input v-model="resumeForm.basicInfo.mobile" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="12">
              <el-col :span="12">
                <el-form-item label="邮箱">
                  <el-input v-model="resumeForm.basicInfo.email" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="期望职位">
                  <el-input v-model="resumeForm.jobIntention.expectPosition" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="教育经历">
              <el-input
                v-model="resumeForm.educationList[0].school"
                placeholder="学校名称"
                class="inline-input"
              />
              <el-input
                v-model="resumeForm.educationList[0].major"
                placeholder="专业"
                class="inline-input"
              />
              <el-input
                v-model="resumeForm.educationList[0].degree"
                placeholder="学历"
                class="inline-input"
              />
            </el-form-item>
            <el-form-item label="自我评价">
              <el-input
                v-model="resumeForm.selfEvaluation"
                type="textarea"
                :rows="4"
              />
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="section-gap">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>投递记录</template>
          <el-table :data="applyList" stripe>
            <el-table-column prop="jobName" label="职位" />
            <el-table-column prop="companyName" label="企业" />
            <el-table-column prop="status" label="状态" width="120" />
            <el-table-column prop="applyTime" label="投递时间" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-title">
              <span>消息中心</span>
              <el-button text type="primary" @click="readAllMessages"
                >全部已读</el-button
              >
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="message in messages"
              :key="message.messageId"
              :timestamp="message.createTime"
              :type="message.readStatus === 'UNREAD' ? 'primary' : 'info'"
            >
              <strong>{{ message.title }}</strong>
              <p>{{ message.content }}</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import {
  createResume,
  exportStatistics,
  getCandidateStatistics,
  getProfile,
  listMessages,
  listMyApplies,
  listResumes,
  markMessagesRead,
  updateProfile,
  updateResume,
} from "@/api/recruit";
import { resolveAssetUrl } from "@/api/http";
import { clearAuth } from "@/utils/auth";

interface CandidateProfile {
  name?: string;
  mobile?: string;
  email?: string;
  school?: string;
  major?: string;
  currentCity?: string;
}

interface ApplyRecord {
  applyId: number;
  jobName: string;
  companyName: string;
  status: string;
  applyTime: string;
}

interface MessageRecord {
  messageId: number;
  title: string;
  content: string;
  createTime: string;
  readStatus: string;
}

interface ResumeForm {
  title: string;
  basicInfo: Record<string, unknown>;
  jobIntention: Record<string, unknown>;
  educationList: Array<Record<string, unknown>>;
  workList: Array<Record<string, unknown>>;
  skillList: Array<Record<string, unknown>>;
  selfEvaluation: string;
  privacy: string;
}

const router = useRouter();
const profile = reactive<CandidateProfile>({});
const profileForm = reactive({
  email: "",
  school: "",
  major: "",
  currentCity: "",
});
const resumeId = ref<number | null>(null);
const applyList = ref<ApplyRecord[]>([]);
const messages = ref<MessageRecord[]>([]);
const statistics = reactive({
  applyCount: 0,
  interviewCount: 0,
  viewedCount: 0,
  favoriteCount: 0,
  applyTrend: [] as Array<{ day: string; count: number }>,
});
const resumeForm = reactive<ResumeForm>({
  title: "默认简历",
  basicInfo: {
    name: "",
    gender: "MALE",
    birthday: "2001-01-01",
    mobile: "",
    email: "",
    currentCity: "成都",
  },
  jobIntention: {
    expectPosition: "Java开发工程师",
    expectIndustry: "互联网",
    expectSalaryMin: 8000,
    expectSalaryMax: 15000,
    expectCity: "成都",
  },
  educationList: [
    {
      school: "XX大学",
      major: "计算机科学与技术",
      degree: "本科",
      startDate: "2019-09",
      endDate: "2023-06",
    },
  ],
  workList: [],
  skillList: [],
  selfEvaluation: "熟悉 Java / Spring Boot / Vue 全栈开发。",
  privacy: "ENTERPRISE_ONLY",
});

async function loadProfile() {
  const data = await getProfile();
  Object.assign(profile, data);
  profileForm.email = data.email || "";
  profileForm.school = data.school || "";
  profileForm.major = data.major || "";
  profileForm.currentCity = data.currentCity || "";
  resumeForm.basicInfo.name = data.name || "";
  resumeForm.basicInfo.email = data.email || "";
}

async function loadResume() {
  const data = await listResumes();
  const firstResume = data.list?.[0];
  if (!firstResume) {
    return;
  }
  resumeId.value = firstResume.resumeId;
}

async function saveProfile() {
  await updateProfile(profileForm);
  ElMessage.success("资料已更新");
  await loadProfile();
}

async function saveResume() {
  resumeForm.basicInfo.mobile =
    profile.mobile?.replace(/\*/g, "") || resumeForm.basicInfo.mobile;
  if (resumeId.value) {
    await updateResume(resumeId.value, resumeForm);
  } else {
    const data = await createResume(resumeForm);
    resumeId.value = data.resumeId;
  }
  ElMessage.success("简历已保存");
  await loadResume();
}

async function loadApplies() {
  const data = await listMyApplies({ pageNum: 1, pageSize: 10 });
  applyList.value = data.list || [];
}

async function loadMessages() {
  const data = await listMessages({ pageNum: 1, pageSize: 10 });
  messages.value = data.list || [];
}

async function loadStatistics() {
  const data = await getCandidateStatistics();
  statistics.applyCount = data.applyCount || 0;
  statistics.interviewCount = data.interviewCount || 0;
  statistics.viewedCount = data.viewedCount || 0;
  statistics.favoriteCount = data.favoriteCount || 0;
  statistics.applyTrend = data.applyTrend || [];
}

async function downloadMyStatistics() {
  const data = await exportStatistics({
    type: "CANDIDATE",
    startDate: "2026-01-01",
    endDate: "2026-12-31",
    format: "csv",
  });
  window.open(resolveAssetUrl(data.downloadUrl), "_blank");
}

async function readAllMessages() {
  await markMessagesRead();
  ElMessage.success("消息已全部标记已读");
  await loadMessages();
}

function logout() {
  clearAuth();
  router.push("/login");
}

onMounted(async () => {
  await Promise.all([
    loadProfile(),
    loadResume(),
    loadApplies(),
    loadMessages(),
    loadStatistics(),
  ]);
});
</script>

<style scoped>
.dashboard-page {
  max-width: 1280px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0 0 8px;
}

.page-header p {
  margin: 0;
  color: #6b7280;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.section-gap {
  margin-top: 16px;
}

.card-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.inline-input {
  margin-right: 12px;
  width: calc(33.3% - 8px);
}

.stat-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 96px;
  padding: 16px;
  border-radius: 16px;
  background: linear-gradient(135deg, #eff6ff, #f0fdf4);
}

.stat-card strong {
  font-size: 28px;
  color: #1d4ed8;
}

.stat-card span {
  margin-top: 8px;
  color: #6b7280;
}

.trend-table {
  margin-top: 16px;
}
</style>
