<template>
  <div class="dashboard-page">
    <div class="page-header">
      <div>
        <h1>企业招聘工作台</h1>
        <p>维护企业信息、发布岗位、查看投递并处理简历</p>
      </div>
      <div class="header-actions">
        <el-button @click="$router.push('/')">返回首页</el-button>
        <el-button type="danger" plain @click="logout">退出</el-button>
      </div>
    </div>

    <el-row :gutter="16" class="section-gap">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-title">
              <span>招聘统计</span>
              <el-button
                text
                type="primary"
                @click="downloadEnterpriseStatistics"
                >导出报表</el-button
              >
            </div>
          </template>
          <el-row :gutter="16">
            <el-col :span="6">
              <div class="stat-card">
                <strong>{{ statistics.jobViewCount }}</strong>
                <span>职位浏览量</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <strong>{{ statistics.resumeReceivedCount }}</strong>
                <span>收到简历数</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <strong>{{ statistics.interviewCount }}</strong>
                <span>面试邀约数</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-card">
                <strong>{{ statistics.activeJobCount }}</strong>
                <span>招聘中职位</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>企业信息</template>
          <el-form label-position="top" :model="enterpriseForm">
            <el-alert
              :title="`认证状态：${enterprise.authStatus || 'UNAUTH'}`"
              :type="
                enterprise.authStatus === 'PASS'
                  ? 'success'
                  : enterprise.authStatus === 'REJECT'
                  ? 'error'
                  : 'warning'
              "
              :closable="false"
              class="auth-alert"
            />
            <el-alert
              v-if="enterprise.rejectReason"
              :title="`驳回原因：${enterprise.rejectReason}`"
              type="error"
              :closable="false"
              class="auth-alert"
            />
            <el-form-item label="企业名称">
              <el-input v-model="enterprise.companyName" disabled />
            </el-form-item>
            <el-form-item label="所属行业">
              <el-input v-model="enterpriseForm.industry" />
            </el-form-item>
            <el-form-item label="企业规模">
              <el-input v-model="enterpriseForm.scale" />
            </el-form-item>
            <el-form-item label="地址">
              <el-input v-model="enterpriseForm.address" />
            </el-form-item>
            <el-form-item label="官网">
              <el-input v-model="enterpriseForm.website" />
            </el-form-item>
            <el-form-item label="企业简介">
              <el-input
                v-model="enterpriseForm.introduction"
                type="textarea"
                :rows="4"
              />
            </el-form-item>
            <el-button type="primary" @click="saveEnterpriseInfo"
              >保存企业信息</el-button
            >
          </el-form>

          <el-divider>企业认证资料</el-divider>
          <el-form label-position="top" :model="authForm">
            <el-form-item label="企业全称">
              <el-input v-model="authForm.companyName" />
            </el-form-item>
            <el-form-item label="统一社会信用代码">
              <el-input v-model="authForm.creditCode" />
            </el-form-item>
            <el-form-item label="法人姓名">
              <el-input v-model="authForm.legalPerson" />
            </el-form-item>
            <el-form-item label="营业执照">
              <el-upload
                action="#"
                :show-file-list="false"
                :http-request="uploadLicenseRequest"
              >
                <el-button>上传营业执照</el-button>
              </el-upload>
              <div v-if="authPreview.licenseUrl" class="upload-preview">
                <a
                  :href="resolveAssetUrl(authPreview.licenseUrl)"
                  target="_blank"
                  >查看已上传营业执照</a
                >
              </div>
            </el-form-item>
            <el-form-item label="企业 Logo">
              <el-upload
                action="#"
                :show-file-list="false"
                :http-request="uploadLogoRequest"
              >
                <el-button>上传 Logo</el-button>
              </el-upload>
              <div v-if="authPreview.logoUrl" class="upload-preview">
                <img :src="resolveAssetUrl(authPreview.logoUrl)" alt="logo" />
              </div>
            </el-form-item>
            <el-button type="success" @click="submitAuth">提交认证</el-button>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-title">
              <span>发布职位</span>
              <el-button
                type="primary"
                :disabled="enterprise.authStatus !== 'PASS'"
                @click="saveJob"
                >保存职位</el-button
              >
            </div>
          </template>
          <el-alert
            v-if="enterprise.authStatus !== 'PASS'"
            title="企业认证通过后才能发布职位"
            type="warning"
            :closable="false"
            class="auth-alert"
          />
          <el-form label-position="top" :model="jobForm">
            <el-row :gutter="12">
              <el-col :span="12">
                <el-form-item label="职位名称">
                  <el-input v-model="jobForm.jobName" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="职位分类">
                  <el-input v-model="jobForm.jobCategory" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="12">
              <el-col :span="8">
                <el-form-item label="薪资下限">
                  <el-input-number
                    v-model="jobForm.salaryMin"
                    :min="0"
                    :step="1000"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="薪资上限">
                  <el-input-number
                    v-model="jobForm.salaryMax"
                    :min="0"
                    :step="1000"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="招聘人数">
                  <el-input-number v-model="jobForm.headCount" :min="1" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="12">
              <el-col :span="8">
                <el-form-item label="工作地点">
                  <el-input v-model="jobForm.location" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="学历要求">
                  <el-input v-model="jobForm.education" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="经验要求">
                  <el-input v-model="jobForm.experience" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="岗位职责">
              <el-input
                v-model="jobForm.responsibility"
                type="textarea"
                :rows="3"
              />
            </el-form-item>
            <el-form-item label="任职要求">
              <el-input
                v-model="jobForm.requirement"
                type="textarea"
                :rows="3"
              />
            </el-form-item>
            <el-form-item label="福利标签">
              <el-select
                v-model="jobForm.welfare"
                multiple
                filterable
                allow-create
                default-first-option
              >
                <el-option
                  v-for="item in welfareOptions"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-row :gutter="12">
              <el-col :span="12">
                <el-form-item label="联系人">
                  <el-input v-model="jobForm.contactName" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话">
                  <el-input v-model="jobForm.contactMobile" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="section-gap">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>企业职位列表</template>
          <el-table :data="jobs" stripe>
            <el-table-column prop="jobName" label="职位" />
            <el-table-column prop="status" label="状态" width="120" />
            <el-table-column prop="applyCount" label="投递数" width="100" />
            <el-table-column prop="publishTime" label="发布时间" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>收到的简历</template>
          <el-table :data="applications" stripe>
            <el-table-column prop="candidateName" label="候选人" />
            <el-table-column prop="jobName" label="职位" />
            <el-table-column prop="status" label="状态" width="120" />
            <el-table-column label="操作" width="220">
              <template #default="{ row }">
                <el-button text type="primary" @click="openResume(row.resumeId)"
                  >查看</el-button
                >
                <el-button
                  text
                  type="success"
                  @click="handleStatus(row.applyId, 'SUITABLE')"
                  >通过</el-button
                >
                <el-button
                  text
                  type="danger"
                  @click="handleStatus(row.applyId, 'UNSUITABLE')"
                  >淘汰</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="resumeVisible" title="简历详情" width="760px">
      <template v-if="resumeDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="姓名">{{
            resumeDetail.basicInfo?.name
          }}</el-descriptions-item>
          <el-descriptions-item label="电话">{{
            resumeDetail.basicInfo?.mobile
          }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{
            resumeDetail.basicInfo?.email
          }}</el-descriptions-item>
          <el-descriptions-item label="期望职位">{{
            resumeDetail.jobIntention?.expectPosition
          }}</el-descriptions-item>
        </el-descriptions>
        <div class="detail-box">
          <h4>教育经历</h4>
          <pre>{{ resumeDetail.educationList }}</pre>
        </div>
        <div class="detail-box">
          <h4>自我评价</h4>
          <p>{{ resumeDetail.selfEvaluation }}</p>
        </div>
        <el-button type="primary" @click="downloadResumePdfFromEnterprise">
          导出简历 PDF
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage, UploadRequestOptions } from "element-plus";
import { useRouter } from "vue-router";
import {
  createEnterpriseJob,
  downloadGeneratedFile,
  exportEnterpriseResumePdf,
  exportStatistics,
  getEnterpriseAuthStatus,
  getEnterpriseInfo,
  getEnterpriseResumeDetail,
  getEnterpriseStatistics,
  listEnterpriseApplies,
  listEnterpriseJobs,
  submitEnterpriseAuth,
  uploadCommonFile,
  updateEnterpriseApplyStatus,
  updateEnterpriseInfo,
} from "@/api/recruit";
import { resolveAssetUrl } from "@/api/http";
import { clearAuth } from "@/utils/auth";

interface EnterpriseInfo {
  companyName?: string;
  industry?: string;
  scale?: string;
  address?: string;
  introduction?: string;
  website?: string;
  logo?: string;
  authStatus?: string;
  rejectReason?: string;
  creditCode?: string;
  legalPerson?: string;
  licenseImage?: string;
  logoFileUrl?: string;
}

interface EnterpriseJobRecord {
  jobId: number;
  jobName: string;
  status: string;
  applyCount: number;
  publishTime: string;
}

interface EnterpriseApplyRecord {
  applyId: number;
  resumeId: number;
  candidateName: string;
  jobName: string;
  status: string;
}

interface ResumeDetail {
  resumeId?: number;
  basicInfo?: Record<string, unknown>;
  jobIntention?: Record<string, unknown>;
  educationList?: unknown[];
  selfEvaluation?: string;
}

interface EnterpriseJobForm {
  jobName: string;
  jobCategory: string;
  responsibility: string;
  requirement: string;
  salaryMin: number;
  salaryMax: number;
  location: string;
  headCount: number;
  education: string;
  experience: string;
  welfare: string[];
  contactName: string;
  contactMobile: string;
}

const router = useRouter();
const welfareOptions = ["双休", "五险一金", "带薪年假", "年度体检", "餐补"];
const enterprise = reactive<EnterpriseInfo>({});
const authReady = computed(() => enterprise.authStatus === "PASS");
const enterpriseForm = reactive({
  industry: "互联网",
  scale: "20-99",
  address: "成都市郫都区",
  introduction: "聚焦校企人才服务与数字招聘。",
  website: "https://example.com",
});
const authForm = reactive({
  companyName: "成都校企科技有限公司",
  creditCode: "91510100MA6TEST001",
  legalPerson: "李四",
  licenseFileId: "",
  logoFileId: "",
});
const authPreview = reactive({
  licenseUrl: "",
  logoUrl: "",
});
const statistics = reactive({
  jobViewCount: 0,
  resumeReceivedCount: 0,
  interviewCount: 0,
  activeJobCount: 0,
});
const jobs = ref<EnterpriseJobRecord[]>([]);
const applications = ref<EnterpriseApplyRecord[]>([]);
const resumeVisible = ref(false);
const resumeDetail = ref<ResumeDetail | null>(null);
const jobForm = reactive<EnterpriseJobForm>({
  jobName: "Java开发工程师",
  jobCategory: "后端开发",
  responsibility: "负责招聘平台核心业务开发与维护。",
  requirement: "熟悉 Spring Boot、MySQL、Vue 基础协作流程。",
  salaryMin: 8000,
  salaryMax: 15000,
  location: "成都",
  headCount: 2,
  education: "本科",
  experience: "1-3年",
  welfare: ["双休", "五险一金"],
  contactName: "HR 李老师",
  contactMobile: "13900000000",
});

async function loadEnterpriseInfo() {
  const [data, authStatus] = await Promise.all([
    getEnterpriseInfo(),
    getEnterpriseAuthStatus(),
  ]);
  Object.assign(enterprise, data);
  Object.assign(enterprise, authStatus);
  enterpriseForm.industry = data.industry || "";
  enterpriseForm.scale = data.scale || "";
  enterpriseForm.address = data.address || "";
  enterpriseForm.introduction = data.introduction || "";
  enterpriseForm.website = data.website || "";
  authForm.companyName = data.companyName || authForm.companyName;
  authForm.creditCode = data.creditCode || authForm.creditCode;
  authForm.legalPerson = data.legalPerson || authForm.legalPerson;
  authForm.licenseFileId = data.licenseFileId || "";
  authForm.logoFileId = data.logoFileId || "";
  authPreview.licenseUrl = data.licenseImage || "";
  authPreview.logoUrl = data.logo || data.logoFileUrl || "";
}

async function saveEnterpriseInfo() {
  await updateEnterpriseInfo(enterpriseForm);
  ElMessage.success("企业信息已更新");
  await loadEnterpriseInfo();
}

async function loadStatistics() {
  const data = await getEnterpriseStatistics();
  statistics.jobViewCount = data.jobViewCount || 0;
  statistics.resumeReceivedCount = data.resumeReceivedCount || 0;
  statistics.interviewCount = data.interviewCount || 0;
  statistics.activeJobCount = data.activeJobCount || 0;
}

async function downloadEnterpriseStatistics() {
  const data = await exportStatistics({
    type: "ENTERPRISE",
    startDate: "2026-01-01",
    endDate: "2026-12-31",
    format: "xlsx",
  });
  await downloadGeneratedFile(data.fileId, data.fileName);
}

async function saveJob() {
  if (!authReady.value) {
    ElMessage.warning("企业认证通过后才能发布职位");
    return;
  }
  await createEnterpriseJob(jobForm);
  ElMessage.success("职位已发布");
  await loadJobs();
}

async function uploadLicenseRequest(option: UploadRequestOptions) {
  const data = await uploadCommonFile(option.file as File, "LICENSE");
  authForm.licenseFileId = data.fileId;
  authPreview.licenseUrl = data.fileUrl;
  ElMessage.success("营业执照上传成功");
}

async function uploadLogoRequest(option: UploadRequestOptions) {
  const data = await uploadCommonFile(option.file as File, "LOGO");
  authForm.logoFileId = data.fileId;
  authPreview.logoUrl = data.fileUrl;
  ElMessage.success("Logo 上传成功");
}

async function submitAuth() {
  if (!authForm.licenseFileId) {
    ElMessage.warning("请先上传营业执照");
    return;
  }
  const data = await submitEnterpriseAuth({
    companyName: authForm.companyName,
    creditCode: authForm.creditCode,
    legalPerson: authForm.legalPerson,
    licenseFileId: authForm.licenseFileId,
    industry: enterpriseForm.industry,
    scale: enterpriseForm.scale,
    address: enterpriseForm.address,
    introduction: enterpriseForm.introduction,
    logoFileId: authForm.logoFileId,
    website: enterpriseForm.website,
  });
  enterprise.authStatus = data.authStatus;
  ElMessage.success("认证资料已提交，请等待审核");
  await loadEnterpriseInfo();
}

async function loadJobs() {
  const data = await listEnterpriseJobs({ pageNum: 1, pageSize: 10 });
  jobs.value = data.list || [];
}

async function loadApplications() {
  const data = await listEnterpriseApplies({ pageNum: 1, pageSize: 10 });
  applications.value = data.list || [];
}

async function handleStatus(applyId: number, status: string) {
  await updateEnterpriseApplyStatus(applyId, {
    status,
    remark:
      status === "SUITABLE"
        ? "欢迎进入后续沟通环节"
        : "感谢投递，当前岗位暂不匹配",
  });
  ElMessage.success("投递状态已更新");
  await loadApplications();
}

async function openResume(resumeId: number) {
  resumeDetail.value = await getEnterpriseResumeDetail(resumeId);
  resumeVisible.value = true;
}

async function downloadResumePdfFromEnterprise() {
  const resumeId = resumeDetail.value?.resumeId as number | undefined;
  if (!resumeId) {
    ElMessage.warning("请先查看简历详情");
    return;
  }
  const data = await exportEnterpriseResumePdf(resumeId);
  await downloadGeneratedFile(data.fileId, data.fileName);
}

function logout() {
  clearAuth();
  router.push("/login");
}

onMounted(async () => {
  await Promise.all([
    loadEnterpriseInfo(),
    loadJobs(),
    loadApplications(),
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

.auth-alert {
  margin-bottom: 12px;
}

.section-gap {
  margin-top: 16px;
}

.card-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 96px;
  padding: 16px;
  border-radius: 16px;
  background: linear-gradient(135deg, #eff6ff, #ecfeff);
}

.stat-card strong {
  font-size: 28px;
  color: #0f766e;
}

.stat-card span {
  margin-top: 8px;
  color: #6b7280;
}

.detail-box {
  margin-top: 16px;
}

.detail-box h4 {
  margin: 0 0 8px;
}

.detail-box p,
.detail-box pre {
  margin: 0;
  line-height: 1.8;
  white-space: pre-wrap;
}

.upload-preview {
  margin-top: 8px;
}

.upload-preview img {
  width: 72px;
  height: 72px;
  border-radius: 12px;
  object-fit: cover;
  border: 1px solid #e5e7eb;
}
</style>
