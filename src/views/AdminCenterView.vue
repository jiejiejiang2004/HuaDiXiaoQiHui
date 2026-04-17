<template>
  <div class="admin-page">
    <div class="page-header">
      <div>
        <h1>管理员后台</h1>
        <p>二期聚焦用户管理、审核流和系统管理</p>
      </div>
      <div class="header-actions">
        <el-button @click="$router.push('/')">返回首页</el-button>
        <el-button type="danger" plain @click="logout">退出</el-button>
      </div>
    </div>

    <el-tabs v-model="activeTab" class="admin-tabs">
      <el-tab-pane label="求职者管理" name="candidate">
        <div class="toolbar">
          <el-input
            v-model="candidateQuery.keyword"
            placeholder="姓名/手机号"
            clearable
          />
          <el-select
            v-model="candidateQuery.status"
            placeholder="状态"
            clearable
          >
            <el-option label="ACTIVE" value="ACTIVE" />
            <el-option label="DISABLED" value="DISABLED" />
          </el-select>
          <el-button type="primary" @click="loadCandidates">查询</el-button>
        </div>
        <el-table :data="candidates" stripe>
          <el-table-column prop="realName" label="姓名" />
          <el-table-column prop="mobile" label="手机号" />
          <el-table-column prop="identityType" label="身份" />
          <el-table-column prop="status" label="状态" width="120" />
          <el-table-column label="操作" width="240">
            <template #default="{ row }">
              <el-button
                text
                type="primary"
                @click="openCandidateDetail(row.userId)"
                >详情</el-button
              >
              <el-button
                text
                type="warning"
                @click="changeCandidateStatus(row)"
              >
                {{ row.status === "ACTIVE" ? "禁用" : "启用" }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="企业管理" name="enterprise">
        <div class="toolbar">
          <el-input
            v-model="enterpriseQuery.keyword"
            placeholder="企业名称"
            clearable
          />
          <el-select
            v-model="enterpriseQuery.authStatus"
            placeholder="认证状态"
            clearable
          >
            <el-option label="PENDING" value="PENDING" />
            <el-option label="PASS" value="PASS" />
            <el-option label="REJECT" value="REJECT" />
          </el-select>
          <el-button type="primary" @click="loadEnterprises">查询</el-button>
        </div>
        <el-table :data="enterprises" stripe>
          <el-table-column prop="companyName" label="企业名称" />
          <el-table-column prop="industry" label="行业" />
          <el-table-column prop="authStatus" label="认证状态" width="120" />
          <el-table-column prop="status" label="账号状态" width="120" />
          <el-table-column label="操作" width="320">
            <template #default="{ row }">
              <el-button
                text
                type="success"
                @click="handleEnterpriseAudit(row.enterpriseId, 'PASS')"
                >通过</el-button
              >
              <el-button
                text
                type="danger"
                @click="handleEnterpriseAudit(row.enterpriseId, 'REJECT')"
                >驳回</el-button
              >
              <el-button
                text
                type="warning"
                @click="changeEnterpriseStatus(row)"
              >
                {{ row.status === "ACTIVE" ? "禁用" : "启用" }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="职位审核" name="job-audit">
        <div class="toolbar">
          <el-select
            v-model="jobAuditQuery.status"
            placeholder="审核状态"
            clearable
          >
            <el-option label="PENDING" value="PENDING" />
            <el-option label="PASSED" value="PASSED" />
            <el-option label="REJECTED" value="REJECTED" />
          </el-select>
          <el-input
            v-model="jobAuditQuery.companyName"
            placeholder="企业名称"
            clearable
          />
          <el-button type="primary" @click="loadAuditJobs">查询</el-button>
        </div>
        <el-table :data="auditJobs" stripe>
          <el-table-column prop="jobName" label="职位" />
          <el-table-column prop="companyName" label="企业" />
          <el-table-column prop="status" label="状态" width="120" />
          <el-table-column prop="auditRemark" label="审核备注" />
          <el-table-column label="操作" width="220">
            <template #default="{ row }">
              <el-button
                text
                type="success"
                @click="handleJobAudit(row.jobId, 'PASS')"
                >通过</el-button
              >
              <el-button
                text
                type="danger"
                @click="handleJobAudit(row.jobId, 'REJECT')"
                >驳回</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="公告管理" name="notice">
        <el-row :gutter="16">
          <el-col :span="10">
            <el-card shadow="hover">
              <template #header>发布公告</template>
              <el-form label-position="top" :model="noticeForm">
                <el-form-item label="标题">
                  <el-input v-model="noticeForm.title" />
                </el-form-item>
                <el-form-item label="类型">
                  <el-select v-model="noticeForm.type">
                    <el-option label="JOB_FAIR" value="JOB_FAIR" />
                    <el-option label="POLICY" value="POLICY" />
                    <el-option label="COOPERATION" value="COOPERATION" />
                  </el-select>
                </el-form-item>
                <el-form-item label="状态">
                  <el-select v-model="noticeForm.status">
                    <el-option label="PENDING" value="PENDING" />
                    <el-option label="ONLINE" value="ONLINE" />
                  </el-select>
                </el-form-item>
                <el-form-item label="内容">
                  <el-input
                    v-model="noticeForm.content"
                    type="textarea"
                    :rows="5"
                  />
                </el-form-item>
                <el-button type="primary" @click="saveNotice"
                  >保存公告</el-button
                >
              </el-form>
            </el-card>
          </el-col>
          <el-col :span="14">
            <el-card shadow="hover">
              <template #header>公告审核 / 列表</template>
              <el-table :data="notices" stripe>
                <el-table-column prop="title" label="标题" />
                <el-table-column prop="type" label="类型" width="140" />
                <el-table-column prop="status" label="状态" width="120" />
                <el-table-column label="操作" width="260">
                  <template #default="{ row }">
                    <el-button
                      text
                      type="success"
                      @click="handleNoticeAudit(row.noticeId, 'PASS')"
                      >通过</el-button
                    >
                    <el-button
                      text
                      type="danger"
                      @click="handleNoticeAudit(row.noticeId, 'REJECT')"
                      >驳回</el-button
                    >
                    <el-button
                      text
                      type="danger"
                      @click="removeNotice(row.noticeId)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="分类管理" name="category">
        <div class="toolbar">
          <el-input v-model="categoryForm.name" placeholder="分类名称" />
          <el-input-number v-model="categoryForm.sort" :min="0" />
          <el-button type="primary" @click="saveCategory">新增分类</el-button>
        </div>
        <el-table :data="categories" stripe>
          <el-table-column prop="categoryId" label="ID" width="100" />
          <el-table-column prop="name" label="名称" />
          <el-table-column prop="sort" label="排序" width="120" />
          <el-table-column label="操作" width="140">
            <template #default="{ row }">
              <el-button
                text
                type="danger"
                @click="removeCategory(row.categoryId)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="审核日志" name="audit-log">
        <el-table :data="auditLogs" stripe>
          <el-table-column prop="bizType" label="业务类型" width="120" />
          <el-table-column prop="bizId" label="业务ID" width="100" />
          <el-table-column prop="auditResult" label="结果" width="100" />
          <el-table-column prop="auditorName" label="审核人" width="120" />
          <el-table-column prop="auditRemark" label="备注" />
          <el-table-column prop="createTime" label="时间" width="180" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-dialog
      v-model="candidateDetailVisible"
      title="求职者详情"
      width="720px"
    >
      <template v-if="candidateDetail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="姓名">{{
            candidateDetail.name
          }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{
            candidateDetail.mobile
          }}</el-descriptions-item>
          <el-descriptions-item label="学校">{{
            candidateDetail.school
          }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{
            candidateDetail.major
          }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{
            candidateDetail.status
          }}</el-descriptions-item>
          <el-descriptions-item label="简历数">{{
            candidateDetail.resumeCount
          }}</el-descriptions-item>
        </el-descriptions>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import {
  auditEnterprise,
  auditJob,
  auditNotice,
  createCategoryAdmin,
  createSystemNotice,
  deleteCategoryAdmin,
  deleteSystemNotice,
  getCandidateDetail,
  listAuditJobs,
  listAuditLogs,
  listAuditNotices,
  listCandidates,
  listCategoriesAdmin,
  listEnterprisesAdmin,
  updateCandidateStatus,
  updateEnterpriseStatusAdmin,
} from "@/api/recruit";
import { clearAuth } from "@/utils/auth";

interface CandidateRecord {
  userId: number;
  realName: string;
  mobile: string;
  identityType: string;
  status: string;
}

interface EnterpriseRecord {
  enterpriseId: number;
  companyName: string;
  industry: string;
  authStatus: string;
  status: string;
}

interface JobAuditRecord {
  jobId: number;
  jobName: string;
  companyName: string;
  status: string;
  auditRemark?: string;
}

interface NoticeRecord {
  noticeId: number;
  title: string;
  type: string;
  status: string;
}

interface CategoryRecord {
  categoryId: number;
  name: string;
  sort: number;
}

interface AuditLogRecord {
  auditId: number;
  bizType: string;
  bizId: number;
  auditResult: string;
  auditorName: string;
  auditRemark: string;
  createTime: string;
}

const router = useRouter();
const activeTab = ref("candidate");
const candidates = ref<CandidateRecord[]>([]);
const enterprises = ref<EnterpriseRecord[]>([]);
const auditJobs = ref<JobAuditRecord[]>([]);
const notices = ref<NoticeRecord[]>([]);
const categories = ref<CategoryRecord[]>([]);
const auditLogs = ref<AuditLogRecord[]>([]);
const candidateDetailVisible = ref(false);
const candidateDetail = ref<Record<string, unknown> | null>(null);

const candidateQuery = reactive({
  keyword: "",
  status: "",
});

const enterpriseQuery = reactive({
  keyword: "",
  authStatus: "",
});

const jobAuditQuery = reactive({
  status: "PENDING",
  companyName: "",
});

const noticeForm = reactive({
  title: "平台审核公告",
  type: "JOB_FAIR",
  status: "PENDING",
  content: "请及时处理待审核的职位与企业认证信息。",
});

const categoryForm = reactive({
  name: "",
  sort: 0,
});

async function loadCandidates() {
  const data = await listCandidates({
    ...candidateQuery,
    pageNum: 1,
    pageSize: 20,
  });
  candidates.value = data.list || [];
}

async function openCandidateDetail(userId: number) {
  candidateDetail.value = await getCandidateDetail(userId);
  candidateDetailVisible.value = true;
}

async function changeCandidateStatus(row: CandidateRecord) {
  const nextStatus = row.status === "ACTIVE" ? "DISABLED" : "ACTIVE";
  await updateCandidateStatus(row.userId, {
    status: nextStatus,
    reason: "管理员手动调整",
  });
  ElMessage.success("求职者状态已更新");
  await loadCandidates();
}

async function loadEnterprises() {
  const data = await listEnterprisesAdmin({
    ...enterpriseQuery,
    pageNum: 1,
    pageSize: 20,
  });
  enterprises.value = data.list || [];
}

async function handleEnterpriseAudit(enterpriseId: number, result: string) {
  await auditEnterprise(enterpriseId, {
    result,
    reason: result === "PASS" ? "资料审核通过" : "资料不完整，请补充后重新提交",
  });
  ElMessage.success("企业审核已完成");
  await loadEnterprises();
}

async function changeEnterpriseStatus(row: EnterpriseRecord) {
  const nextStatus = row.status === "ACTIVE" ? "DISABLED" : "ACTIVE";
  await updateEnterpriseStatusAdmin(row.enterpriseId, {
    status: nextStatus,
    reason: "管理员手动调整",
  });
  ElMessage.success("企业账号状态已更新");
  await loadEnterprises();
}

async function loadAuditJobs() {
  const data = await listAuditJobs({
    ...jobAuditQuery,
    pageNum: 1,
    pageSize: 20,
  });
  auditJobs.value = data.list || [];
}

async function handleJobAudit(jobId: number, result: string) {
  await auditJob(jobId, {
    result,
    reason:
      result === "PASS" ? "职位内容审核通过" : "职位描述不规范，请修改后重提",
  });
  ElMessage.success("职位审核已完成");
  await loadAuditJobs();
}

async function loadNotices() {
  const data = await listAuditNotices({ pageNum: 1, pageSize: 20 });
  notices.value = data.list || [];
}

async function saveNotice() {
  await createSystemNotice(noticeForm);
  ElMessage.success("公告已保存");
  await loadNotices();
}

async function handleNoticeAudit(noticeId: number, result: string) {
  await auditNotice(noticeId, {
    result,
    reason: result === "PASS" ? "公告审核通过" : "公告内容需调整",
  });
  ElMessage.success("公告审核已完成");
  await loadNotices();
}

async function removeNotice(noticeId: number) {
  await deleteSystemNotice(noticeId);
  ElMessage.success("公告已删除");
  await loadNotices();
}

async function loadCategories() {
  const data = await listCategoriesAdmin();
  categories.value = data.list || [];
}

async function saveCategory() {
  if (!categoryForm.name) {
    ElMessage.warning("请输入分类名称");
    return;
  }
  await createCategoryAdmin(categoryForm);
  categoryForm.name = "";
  categoryForm.sort = 0;
  ElMessage.success("分类已新增");
  await loadCategories();
}

async function removeCategory(categoryId: number) {
  await deleteCategoryAdmin(categoryId);
  ElMessage.success("分类已删除");
  await loadCategories();
}

async function loadAuditLogList() {
  const data = await listAuditLogs({ pageNum: 1, pageSize: 20 });
  auditLogs.value = data.list || [];
}

function logout() {
  clearAuth();
  router.push("/login");
}

watch(
  activeTab,
  async (tab) => {
    if (tab === "candidate") await loadCandidates();
    if (tab === "enterprise") await loadEnterprises();
    if (tab === "job-audit") await loadAuditJobs();
    if (tab === "notice") await loadNotices();
    if (tab === "category") await loadCategories();
    if (tab === "audit-log") await loadAuditLogList();
  },
  { immediate: false }
);

onMounted(async () => {
  try {
    await Promise.all([
      loadCandidates(),
      loadEnterprises(),
      loadAuditJobs(),
      loadNotices(),
      loadCategories(),
      loadAuditLogList(),
    ]);
  } catch (error) {
    ElMessageBox.alert("请确认管理员账号和后端服务已正常启动。", "初始化失败");
  }
});
</script>

<style scoped>
.admin-page {
  max-width: 1320px;
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

.admin-tabs {
  background: #fff;
  padding: 16px;
  border-radius: 16px;
}

.toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.toolbar > * {
  max-width: 240px;
}
</style>
