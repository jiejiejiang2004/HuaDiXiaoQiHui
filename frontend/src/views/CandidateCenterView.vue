<template>
  <div class="workbench-shell">
    <WorkbenchSidebar
      brand-label="求职者工作台"
      :tabs="tabItems"
      :active-key="activeTab"
      nav-panel-id="candidate-nav-panel"
      aria-label="求职者导航"
      @select="onWorkbenchSelect"
      @logout="logout"
    />

    <div class="workbench-main">
      <header class="workbench-main__top">
        <h1 class="workbench-main__hello">你好，{{ displayName }}</h1>
        <p class="workbench-main__sub">在这里管理个人资料、简历、投递与消息</p>
      </header>

      <div class="workbench-panels">
        <!-- 概览 -->
        <section
          v-show="activeTab === 'overview'"
          aria-labelledby="panel-overview"
        >
          <h2 id="panel-overview" class="sr-only">概览</h2>
          <div class="overview-hero">
            <div class="overview-card overview-card--blue">
              <div class="overview-card__body">
                <p class="overview-card__value">
                  {{ statistics.applyCount }}
                </p>
                <p class="overview-card__label">累计投递</p>
              </div>
              <div class="overview-card__badge overview-card__badge--blue" />
            </div>
            <div class="overview-card overview-card--amber">
              <div class="overview-card__body">
                <p class="overview-card__value">
                  {{ statistics.favoriteCount }}
                </p>
                <p class="overview-card__label">收藏职位</p>
              </div>
              <div class="overview-card__badge overview-card__badge--amber" />
            </div>
          </div>
          <div class="overview-substats">
            <div class="overview-substat">
              <span class="overview-substat__value">{{
                statistics.interviewCount
              }}</span>
              <span class="overview-substat__label">面试次数</span>
            </div>
            <div class="overview-substat">
              <span class="overview-substat__value">{{
                statistics.viewedCount
              }}</span>
              <span class="overview-substat__label">简历被查看</span>
            </div>
          </div>
          <el-card shadow="never" class="workbench-card">
            <template #header>
              <div class="card-title">
                <span>投递趋势</span>
                <el-button text type="primary" @click="downloadMyStatistics"
                  >导出统计</el-button
                >
              </div>
            </template>
            <div
              class="apply-heatmap"
              role="img"
              :aria-label="`最近 ${heatmapWeekCount} 周的投递热力图，颜色越深投递越多`"
            >
              <div class="apply-heatmap__layout">
                <div class="apply-heatmap__dow" aria-hidden="true">
                  <span
                    v-for="(d, i) in heatmapDowLabels"
                    :key="i"
                    class="apply-heatmap__dow-label"
                    >{{ d }}</span
                  >
                </div>
                <div ref="heatmapScrollRef" class="apply-heatmap__scroll">
                  <div class="apply-heatmap__months">
                    <span
                      v-for="(ml, wi) in applyHeatmap.monthLabels"
                      :key="'m' + wi"
                      class="apply-heatmap__month"
                      >{{ ml }}</span
                    >
                  </div>
                  <div class="apply-heatmap__grid">
                    <div
                      v-for="(col, wi) in applyHeatmap.columns"
                      :key="'w' + wi"
                      class="apply-heatmap__col"
                    >
                      <div
                        v-for="cell in col"
                        :key="cell.dateKey"
                        class="apply-heatmap__cell"
                        :class="heatmapCellClass(cell)"
                        :title="heatmapTooltip(cell)"
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div class="apply-heatmap__legend">
                <span class="apply-heatmap__legend-text">更少</span>
                <span
                  class="apply-heatmap__cell apply-heatmap__cell--lvl-0 apply-heatmap__legend-swatch"
                />
                <span
                  class="apply-heatmap__cell apply-heatmap__cell--lvl-1 apply-heatmap__legend-swatch"
                />
                <span
                  class="apply-heatmap__cell apply-heatmap__cell--lvl-2 apply-heatmap__legend-swatch"
                />
                <span
                  class="apply-heatmap__cell apply-heatmap__cell--lvl-3 apply-heatmap__legend-swatch"
                />
                <span
                  class="apply-heatmap__cell apply-heatmap__cell--lvl-4 apply-heatmap__legend-swatch"
                />
                <span class="apply-heatmap__legend-text">更多</span>
              </div>
            </div>
          </el-card>
        </section>

        <!-- 个人资料 -->
        <section
          v-show="activeTab === 'profile'"
          aria-labelledby="panel-profile"
        >
          <h2 id="panel-profile" class="workbench-panel__title">个人资料</h2>
          <p class="workbench-panel__desc">
            完善学业与联络信息，便于企业联系；姓名与手机与账号一致，仅可查看。
          </p>
          <el-card shadow="never" class="workbench-card">
            <el-form label-position="top" :model="profileForm">
              <div class="candidate-form-stack">
                <div class="candidate-form-section">
                  <h3 class="candidate-form-section__title">账号信息</h3>
                  <p class="candidate-form-section__hint">
                    与注册账号绑定，如需修改请通过安全流程（若有）。
                  </p>
                  <el-row :gutter="12">
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="姓名">
                        <el-input :model-value="profile.name || '—'" disabled />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="手机">
                        <el-input
                          :model-value="profile.mobile || '—'"
                          disabled
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div class="candidate-form-section">
                  <h3 class="candidate-form-section__title">学业与联络</h3>
                  <p class="candidate-form-section__hint">
                    保存后写入个人档案；邮箱建议与简历保持一致。
                  </p>
                  <el-row :gutter="12">
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="邮箱">
                        <el-input
                          v-model="profileForm.email"
                          placeholder="用于接收通知"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="现居城市">
                        <el-input
                          v-model="profileForm.currentCity"
                          placeholder="例如：成都"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="学校">
                        <el-input
                          v-model="profileForm.school"
                          placeholder="在读或毕业院校"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="专业">
                        <el-input
                          v-model="profileForm.major"
                          placeholder="专业名称"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <div class="candidate-form-actions">
                    <el-button type="primary" @click="saveProfile">
                      保存资料
                    </el-button>
                  </div>
                </div>
              </div>
            </el-form>
          </el-card>
        </section>

        <!-- 我的简历 -->
        <section v-show="activeTab === 'resume'" aria-labelledby="panel-resume">
          <h2 id="panel-resume" class="workbench-panel__title">我的简历</h2>
          <p class="workbench-panel__desc">
            在线编辑简历内容；样式与投递记录、职位卡片一致，使用圆角与主色变量。
          </p>
          <el-card shadow="never" class="workbench-card">
            <template #header>
              <div class="card-title">
                <div class="card-title__text">
                  <span class="card-title__label">在线简历</span>
                  <span v-if="resumeId" class="card-title__meta"
                    >简历 ID {{ resumeId }}</span
                  >
                </div>
                <div class="header-actions-inline">
                  <el-button size="small" @click="saveAsDefaultResume"
                    >设为默认</el-button
                  >
                  <el-button size="small" @click="downloadResumePdf"
                    >导出 PDF</el-button
                  >
                  <el-button
                    size="small"
                    type="danger"
                    plain
                    @click="removeResume"
                    >删除</el-button
                  >
                  <el-button size="small" type="primary" @click="saveResume"
                    >保存简历</el-button
                  >
                </div>
              </div>
            </template>
            <el-form label-position="top" :model="resumeForm">
              <div class="candidate-form-stack">
                <div class="candidate-form-section">
                  <h3 class="candidate-form-section__title">简历标题</h3>
                  <p class="candidate-form-section__hint">
                    展示在列表与预览顶部，建议写清方向与年限。
                  </p>
                  <el-form-item label="标题">
                    <el-input
                      v-model="resumeForm.title"
                      placeholder="例如：Java 后端 · 应届 / 3 年经验"
                    />
                  </el-form-item>
                </div>
                <div class="candidate-form-section">
                  <h3 class="candidate-form-section__title">基本信息</h3>
                  <el-row :gutter="12">
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="姓名">
                        <el-input v-model="resumeForm.basicInfo.name" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="电话">
                        <el-input v-model="resumeForm.basicInfo.mobile" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="邮箱">
                        <el-input v-model="resumeForm.basicInfo.email" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="现居城市">
                        <el-input v-model="resumeForm.basicInfo.currentCity" />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="性别">
                        <el-select
                          v-model="resumeForm.basicInfo.gender"
                          placeholder="请选择"
                          style="width: 100%"
                        >
                          <el-option label="男" value="MALE" />
                          <el-option label="女" value="FEMALE" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="出生日期">
                        <el-date-picker
                          v-model="resumeForm.basicInfo.birthday"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="选择日期"
                          style="width: 100%"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div class="candidate-form-section">
                  <h3 class="candidate-form-section__title">求职意向</h3>
                  <el-row :gutter="12">
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="期望职位">
                        <el-input
                          v-model="resumeForm.jobIntention.expectPosition"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="期望行业">
                        <el-input
                          v-model="resumeForm.jobIntention.expectIndustry"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="期望城市">
                        <el-input
                          v-model="resumeForm.jobIntention.expectCity"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="8">
                      <el-form-item label="期望月薪下限">
                        <el-input-number
                          v-model="resumeForm.jobIntention.expectSalaryMin"
                          :min="0"
                          :step="500"
                          controls-position="right"
                          style="width: 100%"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="8">
                      <el-form-item label="期望月薪上限">
                        <el-input-number
                          v-model="resumeForm.jobIntention.expectSalaryMax"
                          :min="0"
                          :step="500"
                          controls-position="right"
                          style="width: 100%"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div class="candidate-form-section">
                  <h3 class="candidate-form-section__title">教育经历</h3>
                  <el-row :gutter="12">
                    <el-col :xs="24" :sm="8">
                      <el-form-item label="学校">
                        <el-input
                          v-model="resumeForm.educationList[0].school"
                          placeholder="学校名称"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="8">
                      <el-form-item label="专业">
                        <el-input
                          v-model="resumeForm.educationList[0].major"
                          placeholder="专业"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="8">
                      <el-form-item label="学历">
                        <el-input
                          v-model="resumeForm.educationList[0].degree"
                          placeholder="如：本科"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="入学时间">
                        <el-input
                          v-model="resumeForm.educationList[0].startDate"
                          placeholder="如：2019-09"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="毕业时间">
                        <el-input
                          v-model="resumeForm.educationList[0].endDate"
                          placeholder="如：2023-06"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div class="candidate-form-section">
                  <h3 class="candidate-form-section__title">隐私与附件</h3>
                  <p class="candidate-form-section__hint">
                    隐私变更会立即提交（与原先逻辑一致）。
                  </p>
                  <el-row :gutter="12">
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="隐私设置">
                        <el-select
                          v-model="resumeForm.privacy"
                          style="width: 100%"
                          @change="saveResumePrivacy"
                        >
                          <el-option label="公开" value="PUBLIC" />
                          <el-option label="企业可见" value="ENTERPRISE_ONLY" />
                          <el-option label="隐藏" value="HIDDEN" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12">
                      <el-form-item label="简历附件">
                        <div class="candidate-upload-row">
                          <el-upload
                            action="#"
                            :show-file-list="false"
                            :http-request="uploadResumeAttachmentRequest"
                          >
                            <el-button type="primary" plain>上传附件</el-button>
                          </el-upload>
                          <span class="candidate-upload-row__tip"
                            >支持常见文档格式</span
                          >
                        </div>
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <div v-if="attachmentList.length" class="attachment-list">
                    <div
                      v-for="attachment in attachmentList"
                      :key="attachment.attachmentId"
                      class="attachment-item candidate-attachment-chip"
                    >
                      <span class="candidate-attachment-chip__name">{{
                        attachment.fileName
                      }}</span>
                      <div class="attachment-actions">
                        <el-button
                          text
                          type="primary"
                          @click="
                            downloadGeneratedFile(
                              attachment.fileId,
                              attachment.fileName
                            )
                          "
                        >
                          下载
                        </el-button>
                        <el-button
                          text
                          type="danger"
                          @click="removeAttachment(attachment.attachmentId)"
                        >
                          删除
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="candidate-form-section">
                  <h3 class="candidate-form-section__title">自我评价</h3>
                  <el-form-item label="简介">
                    <el-input
                      v-model="resumeForm.selfEvaluation"
                      type="textarea"
                      :rows="5"
                      placeholder="简要说明技能栈、项目经验与求职意向"
                    />
                  </el-form-item>
                </div>
              </div>
            </el-form>
          </el-card>
        </section>

        <!-- 投递记录 -->
        <section
          v-show="activeTab === 'applications'"
          aria-labelledby="panel-applications"
        >
          <h2 id="panel-applications" class="workbench-panel__title">
            投递记录
            <span class="workbench-panel__count"
              >（{{ applyList.length }}）</span
            >
          </h2>
          <el-card shadow="never" class="workbench-card">
            <template v-if="applyList.length">
              <div
                class="candidate-job-list"
                role="table"
                aria-label="投递记录"
              >
                <div class="candidate-job-list__thead" role="rowgroup">
                  <div
                    class="candidate-job-list__tr candidate-job-list__tr--head"
                    role="row"
                  >
                    <div
                      class="candidate-job-list__th candidate-job-list__cell--job"
                      role="columnheader"
                    >
                      职位
                    </div>
                    <div
                      class="candidate-job-list__th candidate-job-list__cell--date"
                      role="columnheader"
                    >
                      投递时间
                    </div>
                    <div
                      class="candidate-job-list__th candidate-job-list__cell--status"
                      role="columnheader"
                    >
                      状态
                    </div>
                    <div
                      class="candidate-job-list__th candidate-job-list__cell--action"
                      role="columnheader"
                    >
                      操作
                    </div>
                  </div>
                </div>
                <div class="candidate-job-list__tbody" role="rowgroup">
                  <div
                    v-for="row in applyList"
                    :key="row.applyId"
                    class="candidate-job-list__tr"
                    :class="{
                      'candidate-job-list__tr--active':
                        hoveredApplyId === row.applyId,
                    }"
                    role="row"
                    @mouseenter="hoveredApplyId = row.applyId"
                    @mouseleave="hoveredApplyId = null"
                  >
                    <div
                      class="candidate-job-list__td candidate-job-list__cell--job"
                      role="cell"
                    >
                      <div class="candidate-job-list__job">
                        <div
                          class="candidate-job-list__logo"
                          aria-hidden="true"
                        >
                          {{ listCompanyInitial(row.companyName) }}
                        </div>
                        <div class="candidate-job-list__job-main">
                          <div class="candidate-job-list__title-row">
                            <span class="candidate-job-list__job-title">{{
                              row.jobName
                            }}</span>
                            <span
                              v-if="row.jobType"
                              class="candidate-job-list__tag"
                              >{{ row.jobType }}</span
                            >
                          </div>
                          <p class="candidate-job-list__company">
                            {{ row.companyName }}
                          </p>
                          <div class="candidate-job-list__meta">
                            <span
                              v-if="row.location"
                              class="candidate-job-list__meta-item candidate-job-list__meta-item--loc"
                            >
                              {{ row.location }}
                            </span>
                            <span
                              class="candidate-job-list__meta-item candidate-job-list__meta-item--pay"
                            >
                              {{
                                listSalaryLabel(row.salaryMin, row.salaryMax)
                              }}
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div
                      class="candidate-job-list__td candidate-job-list__cell--date"
                      role="cell"
                    >
                      {{ formatCandidateListDateTime(row.applyTime) }}
                    </div>
                    <div
                      class="candidate-job-list__td candidate-job-list__cell--status"
                      role="cell"
                    >
                      <span
                        class="candidate-job-list__status"
                        :class="
                          'candidate-job-list__status--' +
                          applyStatusMeta(row.status).tone
                        "
                      >
                        <span
                          class="candidate-job-list__status-ico"
                          aria-hidden="true"
                        />
                        {{ applyStatusMeta(row.status).label }}
                      </span>
                    </div>
                    <div
                      class="candidate-job-list__td candidate-job-list__cell--action"
                      role="cell"
                    >
                      <el-button
                        :type="
                          hoveredApplyId === row.applyId ? 'primary' : 'default'
                        "
                        size="small"
                        @click="openJobDetail(row.jobId)"
                      >
                        查看详情
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无投递记录"
              class="candidate-job-list__empty"
            />
          </el-card>
        </section>

        <!-- 消息中心 -->
        <section
          v-show="activeTab === 'messages'"
          aria-labelledby="panel-messages"
        >
          <h2 id="panel-messages" class="workbench-panel__title">消息中心</h2>
          <el-card shadow="never" class="workbench-card">
            <template #header>
              <div class="card-title">
                <span>站内消息</span>
                <div class="header-actions-inline">
                  <span class="badge-text">未读 {{ unreadCount }}</span>
                  <el-button text type="primary" @click="readAllMessages"
                    >全部已读</el-button
                  >
                </div>
              </div>
            </template>
            <template v-if="messages.length">
              <div class="candidate-msg-list">
                <article
                  v-for="message in messages"
                  :key="message.messageId"
                  class="candidate-msg-card"
                  :class="{
                    'candidate-msg-card--unread':
                      message.readStatus === 'UNREAD',
                  }"
                >
                  <header class="candidate-msg-card__head">
                    <span
                      class="candidate-msg-card__kind"
                      :class="
                        'candidate-msg-card__kind--' +
                        messageTypeKind(message.type)
                      "
                    >
                      {{ messageTypeLabel(message.type) }}
                    </span>
                    <time
                      class="candidate-msg-card__time"
                      :datetime="message.createTime"
                    >
                      {{ formatCandidateListDateTime(message.createTime) }}
                    </time>
                  </header>
                  <div
                    v-if="message.jobName || message.jobId"
                    class="candidate-msg-card__job"
                  >
                    <div class="candidate-msg-card__logo" aria-hidden="true">
                      {{ listCompanyInitial(message.companyName) }}
                    </div>
                    <div class="candidate-msg-card__job-main">
                      <div class="candidate-msg-card__title-row">
                        <span class="candidate-msg-card__job-title">{{
                          message.jobName || "关联职位"
                        }}</span>
                        <span
                          v-if="message.jobType"
                          class="candidate-job-list__tag"
                          >{{ message.jobType }}</span
                        >
                      </div>
                      <p
                        v-if="message.companyName"
                        class="candidate-msg-card__company"
                      >
                        {{ message.companyName }}
                      </p>
                      <div class="candidate-msg-card__meta">
                        <span
                          v-if="message.location"
                          class="candidate-job-list__meta-item candidate-job-list__meta-item--loc"
                        >
                          {{ message.location }}
                        </span>
                        <span
                          class="candidate-job-list__meta-item candidate-job-list__meta-item--pay"
                        >
                          {{
                            listSalaryLabel(
                              message.salaryMin,
                              message.salaryMax
                            )
                          }}
                        </span>
                      </div>
                    </div>
                  </div>
                  <div class="candidate-msg-card__body">
                    <p class="candidate-msg-card__subject">
                      {{ message.title }}
                    </p>
                    <p class="candidate-msg-card__content">
                      {{ message.content }}
                    </p>
                  </div>
                  <footer class="candidate-msg-card__actions">
                    <el-button
                      v-if="message.jobId"
                      size="small"
                      @click="openJobDetail(message.jobId)"
                    >
                      查看职位
                    </el-button>
                    <el-button
                      size="small"
                      type="primary"
                      @click="openMessage(message.messageId)"
                    >
                      消息详情
                    </el-button>
                    <el-button
                      size="small"
                      text
                      type="danger"
                      @click="removeMessage(message.messageId)"
                    >
                      删除
                    </el-button>
                  </footer>
                </article>
              </div>
            </template>
            <el-empty v-else description="暂无消息" />
          </el-card>
        </section>

        <!-- 收藏职位 -->
        <section
          v-show="activeTab === 'favorites'"
          aria-labelledby="panel-favorites"
        >
          <h2 id="panel-favorites" class="workbench-panel__title">
            收藏职位
            <span class="workbench-panel__count"
              >（{{ favoriteJobs.length }}）</span
            >
          </h2>
          <el-card shadow="never" class="workbench-card">
            <template v-if="favoriteJobs.length">
              <div
                class="candidate-job-list"
                role="table"
                aria-label="收藏职位"
              >
                <div class="candidate-job-list__thead" role="rowgroup">
                  <div
                    class="candidate-job-list__tr candidate-job-list__tr--head"
                    role="row"
                  >
                    <div
                      class="candidate-job-list__th candidate-job-list__cell--job"
                      role="columnheader"
                    >
                      职位
                    </div>
                    <div
                      class="candidate-job-list__th candidate-job-list__cell--date"
                      role="columnheader"
                    >
                      收藏时间
                    </div>
                    <div
                      class="candidate-job-list__th candidate-job-list__cell--status"
                      role="columnheader"
                    >
                      状态
                    </div>
                    <div
                      class="candidate-job-list__th candidate-job-list__cell--action"
                      role="columnheader"
                    >
                      操作
                    </div>
                  </div>
                </div>
                <div class="candidate-job-list__tbody" role="rowgroup">
                  <div
                    v-for="row in favoriteJobs"
                    :key="row.jobId"
                    class="candidate-job-list__tr"
                    :class="{
                      'candidate-job-list__tr--active':
                        hoveredFavoriteId === row.jobId,
                    }"
                    role="row"
                    @mouseenter="hoveredFavoriteId = row.jobId"
                    @mouseleave="hoveredFavoriteId = null"
                  >
                    <div
                      class="candidate-job-list__td candidate-job-list__cell--job"
                      role="cell"
                    >
                      <div class="candidate-job-list__job">
                        <div
                          class="candidate-job-list__logo"
                          aria-hidden="true"
                        >
                          {{ listCompanyInitial(row.companyName) }}
                        </div>
                        <div class="candidate-job-list__job-main">
                          <div class="candidate-job-list__title-row">
                            <span class="candidate-job-list__job-title">{{
                              row.jobName
                            }}</span>
                            <span
                              v-if="row.jobType"
                              class="candidate-job-list__tag"
                              >{{ row.jobType }}</span
                            >
                          </div>
                          <p class="candidate-job-list__company">
                            {{ row.companyName }}
                          </p>
                          <div class="candidate-job-list__meta">
                            <span
                              v-if="row.location"
                              class="candidate-job-list__meta-item candidate-job-list__meta-item--loc"
                            >
                              {{ row.location }}
                            </span>
                            <span
                              class="candidate-job-list__meta-item candidate-job-list__meta-item--pay"
                            >
                              {{
                                listSalaryLabel(row.salaryMin, row.salaryMax)
                              }}
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div
                      class="candidate-job-list__td candidate-job-list__cell--date"
                      role="cell"
                    >
                      {{
                        row.collectedAt
                          ? formatCandidateListDateTime(row.collectedAt)
                          : "—"
                      }}
                    </div>
                    <div
                      class="candidate-job-list__td candidate-job-list__cell--status"
                      role="cell"
                    >
                      <span
                        class="candidate-job-list__status candidate-job-list__status--success"
                      >
                        <span
                          class="candidate-job-list__status-ico"
                          aria-hidden="true"
                        />
                        已收藏
                      </span>
                    </div>
                    <div
                      class="candidate-job-list__td candidate-job-list__cell--action"
                      role="cell"
                    >
                      <el-button
                        :type="
                          hoveredFavoriteId === row.jobId
                            ? 'primary'
                            : 'default'
                        "
                        size="small"
                        @click="openJobDetail(row.jobId)"
                      >
                        查看详情
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无收藏职位"
              class="candidate-job-list__empty"
            />
          </el-card>
        </section>
      </div>
    </div>

    <el-dialog v-model="messageVisible" title="消息详情" width="560px">
      <div v-if="currentMessage" class="message-detail">
        <div class="message-detail__meta">
          <span
            class="candidate-msg-card__kind"
            :class="
              'candidate-msg-card__kind--' +
              messageTypeKind(currentMessage.type)
            "
          >
            {{ messageTypeLabel(currentMessage.type) }}
          </span>
          <span class="message-detail__time">{{
            formatCandidateListDateTime(currentMessage.createTime)
          }}</span>
        </div>
        <div
          v-if="currentMessage.jobName || currentMessage.jobId"
          class="candidate-msg-card__job message-detail__job"
        >
          <div class="candidate-msg-card__logo" aria-hidden="true">
            {{ listCompanyInitial(currentMessage.companyName) }}
          </div>
          <div class="candidate-msg-card__job-main">
            <div class="candidate-msg-card__title-row">
              <span class="candidate-msg-card__job-title">{{
                currentMessage.jobName || "关联职位"
              }}</span>
              <span
                v-if="currentMessage.jobType"
                class="candidate-job-list__tag"
                >{{ currentMessage.jobType }}</span
              >
            </div>
            <p
              v-if="currentMessage.companyName"
              class="candidate-msg-card__company"
            >
              {{ currentMessage.companyName }}
            </p>
            <div class="candidate-msg-card__meta">
              <span
                v-if="currentMessage.location"
                class="candidate-job-list__meta-item candidate-job-list__meta-item--loc"
              >
                {{ currentMessage.location }}
              </span>
              <span
                class="candidate-job-list__meta-item candidate-job-list__meta-item--pay"
              >
                {{
                  listSalaryLabel(
                    currentMessage.salaryMin,
                    currentMessage.salaryMax
                  )
                }}
              </span>
            </div>
          </div>
        </div>
        <h3 class="message-detail__title">{{ currentMessage.title }}</h3>
        <p class="message-detail__content">{{ currentMessage.content }}</p>
        <div v-if="currentMessage.jobId" class="message-detail__foot">
          <el-button
            type="primary"
            @click="
              messageVisible = false;
              openJobDetail(currentMessage!.jobId);
            "
          >
            查看职位
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {
  computed,
  nextTick,
  onMounted,
  onUnmounted,
  reactive,
  ref,
  watch,
} from "vue";
import { ElMessage, UploadRequestOptions } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import {
  addResumeAttachment,
  createResume,
  deleteMessages,
  deleteResume,
  deleteResumeAttachment,
  downloadGeneratedFile,
  exportResumePdf,
  exportStatistics,
  getCandidateStatistics,
  type CandidateApplyTrendDay,
  getMessageDetail,
  getProfile,
  getResumeDetail,
  getUnreadMessageCount,
  listCollectedJobs,
  listMessages,
  listMyApplies,
  listResumes,
  markMessagesRead,
  setDefaultResume,
  updateResumePrivacy,
  updateProfile,
  updateResume,
  uploadCommonFile,
} from "@/api/recruit";
import { clearAuth, getUserName } from "@/utils/auth";
import WorkbenchSidebar from "@/components/workbench/WorkbenchSidebar.vue";

/** 与 .apply-heatmap 样式一致：格子宽 + 列间距 */
const HEATMAP_CELL_PX = 11;
const HEATMAP_COL_GAP_PX = 3;
const HEATMAP_WEEKS_MIN = 4;
/** 不限制周数列数上限（Infinity 表示仅受容器宽度约束） */
const HEATMAP_WEEKS_MAX = Number.POSITIVE_INFINITY;

interface HeatmapCell {
  dateKey: string;
  count: number;
  level: number;
  isFuture: boolean;
}

function startOfMonday(d: Date): Date {
  const t = new Date(d);
  t.setHours(0, 0, 0, 0);
  const day = t.getDay();
  const offset = day === 0 ? -6 : 1 - day;
  t.setDate(t.getDate() + offset);
  return t;
}

function formatYmd(d: Date): string {
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${y}-${m}-${day}`;
}

function heatLevel(count: number, max: number): number {
  if (count <= 0 || max <= 0) {
    return 0;
  }
  const step = Math.max(1, max / 4);
  return Math.min(4, Math.ceil(count / step));
}

const heatmapDowLabels = ["一", "二", "三", "四", "五", "六", "日"];

const heatmapScrollRef = ref<HTMLElement | null>(null);
const heatmapWeekCount = ref(20);

let heatmapResizeObserver: ResizeObserver | null = null;

function computeHeatmapWeeksFromWidth(widthPx: number) {
  if (widthPx <= 0) {
    return;
  }
  const unit = HEATMAP_CELL_PX + HEATMAP_COL_GAP_PX;
  const n = Math.floor((widthPx + HEATMAP_COL_GAP_PX) / unit);
  heatmapWeekCount.value = Math.max(
    HEATMAP_WEEKS_MIN,
    Math.min(HEATMAP_WEEKS_MAX, n)
  );
}

function bindHeatmapResize() {
  const el = heatmapScrollRef.value;
  if (!el) {
    return;
  }
  const measure = () => {
    computeHeatmapWeeksFromWidth(el.clientWidth);
  };
  measure();
  heatmapResizeObserver?.disconnect();
  heatmapResizeObserver = new ResizeObserver(() => {
    measure();
  });
  heatmapResizeObserver.observe(el);
}

type CandidateTab =
  | "overview"
  | "profile"
  | "resume"
  | "applications"
  | "messages"
  | "favorites";

const tabItems: { key: CandidateTab; label: string }[] = [
  { key: "overview", label: "概览" },
  { key: "profile", label: "个人资料" },
  { key: "resume", label: "我的简历" },
  { key: "applications", label: "投递记录" },
  { key: "messages", label: "消息中心" },
  { key: "favorites", label: "收藏职位" },
];

const route = useRoute();
const router = useRouter();
const activeTab = ref<CandidateTab>("overview");

function isCandidateTab(s: string): s is CandidateTab {
  return tabItems.some((t) => t.key === s);
}

function selectTab(key: CandidateTab) {
  activeTab.value = key;
  router.replace({ path: "/candidate", query: { tab: key } });
}

function onWorkbenchSelect(key: string) {
  if (isCandidateTab(key)) {
    selectTab(key);
  }
}

watch(
  () => route.query.tab,
  (q) => {
    if (typeof q === "string" && isCandidateTab(q)) {
      activeTab.value = q;
    }
  },
  { immediate: true }
);

const displayName = computed(
  () => profile.name?.trim() || getUserName() || "求职者"
);

/** 热力图数据与原先表格相同：均来自 GET /statistics/candidate → applyTrend */
const applyHeatmap = computed(() => {
  const weeks = heatmapWeekCount.value;
  const trend = statistics.applyTrend;
  const byDay = new Map<string, number>();
  let max = 0;
  for (const row of trend) {
    const k = String(row.day).slice(0, 10);
    const c = Number(row.count) || 0;
    byDay.set(k, c);
    max = Math.max(max, c);
  }

  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const thisMonday = startOfMonday(today);
  const gridStart = new Date(thisMonday);
  gridStart.setDate(gridStart.getDate() - (weeks - 1) * 7);

  const columns: HeatmapCell[][] = [];
  const monthLabels: string[] = [];
  let prevMonth = -1;

  for (let w = 0; w < weeks; w++) {
    const weekMonday = new Date(gridStart);
    weekMonday.setDate(weekMonday.getDate() + w * 7);
    const m = weekMonday.getMonth();
    monthLabels.push(m !== prevMonth ? `${m + 1}月` : "");
    prevMonth = m;

    const col: HeatmapCell[] = [];
    for (let r = 0; r < 7; r++) {
      const cellDate = new Date(weekMonday);
      cellDate.setDate(cellDate.getDate() + r);
      const dateKey = formatYmd(cellDate);
      const isFuture = cellDate > today;
      const count = isFuture ? 0 : byDay.get(dateKey) ?? 0;
      const level = isFuture ? -1 : heatLevel(count, max);
      col.push({ dateKey, count, level, isFuture });
    }
    columns.push(col);
  }

  return { columns, monthLabels, max };
});

function heatmapCellClass(cell: HeatmapCell): string {
  if (cell.isFuture) {
    return "apply-heatmap__cell--future";
  }
  return `apply-heatmap__cell--lvl-${cell.level}`;
}

function heatmapTooltip(cell: HeatmapCell): string {
  if (cell.isFuture) {
    return "";
  }
  if (cell.count <= 0) {
    return `${cell.dateKey} · 无投递`;
  }
  return `${cell.dateKey} · 投递 ${cell.count} 次`;
}

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
  jobId?: number;
  jobName: string;
  companyName: string;
  status: string;
  applyTime: string;
  location?: string;
  salaryMin?: number;
  salaryMax?: number;
  jobType?: string;
}

/** 与 GET /messages、GET /messages/:id 一致；job* 为可选（后端可扩展，Mock 已带） */
interface MessageRecord {
  messageId: number;
  type: string;
  title: string;
  content: string;
  createTime: string;
  readStatus: string;
  bizId?: number;
  readTime?: string;
  jobId?: number;
  jobName?: string;
  companyName?: string;
  location?: string;
  salaryMin?: number;
  salaryMax?: number;
  jobType?: string;
}

interface AttachmentRecord {
  attachmentId: number;
  fileId: string;
  fileName: string;
}

interface FavoriteJobRecord {
  jobId: number;
  jobName: string;
  companyName: string;
  location?: string;
  salaryMin?: number;
  salaryMax?: number;
  jobType?: string;
  collectedAt?: string;
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
const unreadCount = ref(0);
const messageVisible = ref(false);
const currentMessage = ref<MessageRecord | null>(null);
const attachmentList = ref<AttachmentRecord[]>([]);
const favoriteJobs = ref<FavoriteJobRecord[]>([]);
const hoveredApplyId = ref<number | null>(null);
const hoveredFavoriteId = ref<number | null>(null);

type ApplyStatusTone = "success" | "muted" | "danger";

const APPLY_STATUS_META: Record<
  string,
  { label: string; tone: ApplyStatusTone }
> = {
  SUBMITTED: { label: "已投递", tone: "muted" },
  INVITED: { label: "待面试", tone: "success" },
  PENDING: { label: "处理中", tone: "muted" },
  REVIEWING: { label: "审核中", tone: "muted" },
  REJECTED: { label: "未通过", tone: "danger" },
  WITHDRAWN: { label: "已撤回", tone: "muted" },
  OFFERED: { label: "已录用", tone: "success" },
  HIRED: { label: "已入职", tone: "success" },
};

function normalizeApplyRecord(raw: Record<string, unknown>): ApplyRecord {
  const salaryMin = raw.salaryMin != null ? Number(raw.salaryMin) : undefined;
  const salaryMax = raw.salaryMax != null ? Number(raw.salaryMax) : undefined;
  return {
    applyId: Number(raw.applyId),
    jobId: raw.jobId != null ? Number(raw.jobId) : undefined,
    jobName: String(raw.jobName ?? ""),
    companyName: String(raw.companyName ?? ""),
    status: String(raw.status ?? "SUBMITTED"),
    applyTime: String(raw.applyTime ?? raw.createTime ?? ""),
    location: raw.location != null ? String(raw.location) : undefined,
    salaryMin:
      salaryMin != null && Number.isFinite(salaryMin) ? salaryMin : undefined,
    salaryMax:
      salaryMax != null && Number.isFinite(salaryMax) ? salaryMax : undefined,
    jobType: raw.jobType != null ? String(raw.jobType) : undefined,
  };
}

function normalizeMessageRecord(raw: Record<string, unknown>): MessageRecord {
  const salaryMin = raw.salaryMin != null ? Number(raw.salaryMin) : undefined;
  const salaryMax = raw.salaryMax != null ? Number(raw.salaryMax) : undefined;
  return {
    messageId: Number(raw.messageId),
    type: raw.type != null ? String(raw.type) : "",
    title: String(raw.title ?? ""),
    content: String(raw.content ?? ""),
    createTime: String(raw.createTime ?? ""),
    readStatus: String(raw.readStatus ?? ""),
    bizId: raw.bizId != null ? Number(raw.bizId) : undefined,
    readTime: raw.readTime != null ? String(raw.readTime) : undefined,
    jobId: raw.jobId != null ? Number(raw.jobId) : undefined,
    jobName: raw.jobName != null ? String(raw.jobName) : undefined,
    companyName: raw.companyName != null ? String(raw.companyName) : undefined,
    location: raw.location != null ? String(raw.location) : undefined,
    salaryMin:
      salaryMin != null && Number.isFinite(salaryMin) ? salaryMin : undefined,
    salaryMax:
      salaryMax != null && Number.isFinite(salaryMax) ? salaryMax : undefined,
    jobType: raw.jobType != null ? String(raw.jobType) : undefined,
  };
}

function messageTypeKind(type: string): string {
  const t = (type || "").toUpperCase();
  if (t === "INTERVIEW") {
    return "interview";
  }
  if (t === "REPLY") {
    return "reply";
  }
  if (t === "SYSTEM") {
    return "system";
  }
  if (t === "APPLY") {
    return "apply";
  }
  return "default";
}

function messageTypeLabel(type: string): string {
  const t = (type || "").toUpperCase();
  if (t === "INTERVIEW") {
    return "面试邀约";
  }
  if (t === "REPLY") {
    return "投递通知";
  }
  if (t === "SYSTEM") {
    return "系统通知";
  }
  if (t === "APPLY") {
    return "投递动态";
  }
  return "通知";
}

function normalizeFavoriteJobRecord(
  raw: Record<string, unknown>
): FavoriteJobRecord {
  const salaryMin = raw.salaryMin != null ? Number(raw.salaryMin) : undefined;
  const salaryMax = raw.salaryMax != null ? Number(raw.salaryMax) : undefined;
  return {
    jobId: Number(raw.jobId),
    jobName: String(raw.jobName ?? ""),
    companyName: String(raw.companyName ?? ""),
    location: raw.location != null ? String(raw.location) : undefined,
    salaryMin:
      salaryMin != null && Number.isFinite(salaryMin) ? salaryMin : undefined,
    salaryMax:
      salaryMax != null && Number.isFinite(salaryMax) ? salaryMax : undefined,
    jobType: raw.jobType != null ? String(raw.jobType) : undefined,
    collectedAt:
      raw.collectedAt != null
        ? String(raw.collectedAt)
        : raw.collectTime != null
        ? String(raw.collectTime)
        : undefined,
  };
}

function listCompanyInitial(name?: string): string {
  const s = (name || "").trim();
  if (!s) {
    return "?";
  }
  return s.slice(0, 1);
}

function listSalaryLabel(min?: number, max?: number): string {
  if (
    min == null ||
    max == null ||
    !Number.isFinite(min) ||
    !Number.isFinite(max)
  ) {
    return "面议";
  }
  if (min >= 1000 || max >= 1000) {
    const a = Math.round(min / 1000);
    const b = Math.round(max / 1000);
    return `¥${a}k-${b}k/月`;
  }
  return `¥${min}-${max}/月`;
}

function formatCandidateListDateTime(s: string): string {
  if (!s?.trim()) {
    return "—";
  }
  const normalized = s.includes("T") ? s : s.replace(" ", "T");
  const t = Date.parse(normalized);
  if (Number.isNaN(t)) {
    return s;
  }
  return new Intl.DateTimeFormat("zh-CN", {
    year: "numeric",
    month: "short",
    day: "numeric",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  }).format(t);
}

function applyStatusMeta(status: string): {
  label: string;
  tone: ApplyStatusTone;
} {
  return (
    APPLY_STATUS_META[status] ?? {
      label: status.trim() || "—",
      tone: "muted",
    }
  );
}

function openJobDetail(jobId?: number) {
  if (jobId == null || !Number.isFinite(jobId)) {
    ElMessage.warning("暂无法打开职位详情");
    return;
  }
  router.push({ name: "job-detail", params: { jobId: String(jobId) } });
}

const statistics = reactive({
  applyCount: 0,
  interviewCount: 0,
  viewedCount: 0,
  favoriteCount: 0,
  applyTrend: [] as CandidateApplyTrendDay[],
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
  const detail = await getResumeDetail(firstResume.resumeId);
  resumeForm.title = detail.title || resumeForm.title;
  resumeForm.basicInfo = detail.basicInfo || resumeForm.basicInfo;
  resumeForm.jobIntention = detail.jobIntention || resumeForm.jobIntention;
  resumeForm.educationList = detail.educationList?.length
    ? detail.educationList
    : resumeForm.educationList;
  resumeForm.workList = detail.workList || [];
  resumeForm.skillList = detail.skillList || [];
  resumeForm.selfEvaluation = detail.selfEvaluation || "";
  resumeForm.privacy = detail.privacy || "ENTERPRISE_ONLY";
  attachmentList.value = detail.attachmentList || [];
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
  applyList.value = (data.list || []).map((row) =>
    normalizeApplyRecord(row as Record<string, unknown>)
  );
}

async function loadMessages() {
  const data = await listMessages({ pageNum: 1, pageSize: 10 });
  messages.value = (data.list || []).map((row) =>
    normalizeMessageRecord(row as Record<string, unknown>)
  );
  unreadCount.value = data.unreadCount ?? 0;
}

async function loadFavoriteJobs() {
  const data = await listCollectedJobs({ pageNum: 1, pageSize: 20 });
  favoriteJobs.value = (data.list || []).map((row) =>
    normalizeFavoriteJobRecord(row as Record<string, unknown>)
  );
}

/** 与 GET /statistics/candidate 的 applyTrend 一致；兼容 date、applyCount 等别名 */
function normalizeApplyTrendFromApi(raw: unknown): CandidateApplyTrendDay[] {
  if (!Array.isArray(raw)) {
    return [];
  }
  const out: CandidateApplyTrendDay[] = [];
  for (const row of raw) {
    if (!row || typeof row !== "object") {
      continue;
    }
    const o = row as Record<string, unknown>;
    const dayRaw = o.day ?? o.date ?? "";
    const day = String(dayRaw).trim().slice(0, 10);
    if (!/^\d{4}-\d{2}-\d{2}$/.test(day)) {
      continue;
    }
    const c = Number(o.count ?? o.applyCount ?? 0);
    const count = Number.isFinite(c) ? Math.max(0, Math.floor(c)) : 0;
    out.push({ day, count });
  }
  return out;
}

async function loadStatistics() {
  const data = await getCandidateStatistics();
  statistics.applyCount = data.applyCount || 0;
  statistics.interviewCount = data.interviewCount || 0;
  statistics.viewedCount = data.viewedCount || 0;
  statistics.favoriteCount = data.favoriteCount || 0;
  statistics.applyTrend = normalizeApplyTrendFromApi(data.applyTrend);
}

watch(
  () => activeTab.value,
  async (t, prev) => {
    if (t !== "overview") {
      return;
    }
    if (prev && prev !== "overview") {
      try {
        await loadStatistics();
      } catch {
        /* 拦截器已提示 */
      }
    }
    await nextTick();
    bindHeatmapResize();
  },
  { immediate: true }
);

async function downloadMyStatistics() {
  const data = await exportStatistics({
    type: "CANDIDATE",
    startDate: "2026-01-01",
    endDate: "2026-12-31",
    format: "xlsx",
  });
  await downloadGeneratedFile(data.fileId, data.fileName);
}

async function downloadResumePdf() {
  if (!resumeId.value) {
    ElMessage.warning("请先保存简历");
    return;
  }
  const data = await exportResumePdf(resumeId.value);
  await downloadGeneratedFile(data.fileId, data.fileName);
}

async function saveAsDefaultResume() {
  if (!resumeId.value) {
    ElMessage.warning("请先保存简历");
    return;
  }
  await setDefaultResume(resumeId.value);
  ElMessage.success("已设为默认简历");
}

async function saveResumePrivacy() {
  if (!resumeId.value) {
    return;
  }
  await updateResumePrivacy(resumeId.value, { privacy: resumeForm.privacy });
  ElMessage.success("隐私设置已更新");
}

async function removeResume() {
  if (!resumeId.value) {
    ElMessage.warning("暂无可删除简历");
    return;
  }
  await deleteResume(resumeId.value);
  resumeId.value = null;
  attachmentList.value = [];
  ElMessage.success("简历已删除");
}

async function uploadResumeAttachmentRequest(option: UploadRequestOptions) {
  if (!resumeId.value) {
    ElMessage.warning("请先保存简历后再上传附件");
    return;
  }
  const upload = await uploadCommonFile(
    option.file as File,
    "RESUME_ATTACHMENT"
  );
  await addResumeAttachment(resumeId.value, {
    fileId: upload.fileId,
    fileName: (option.file as File).name,
  });
  ElMessage.success("附件上传成功");
  await loadResume();
}

async function removeAttachment(attachmentId: number) {
  if (!resumeId.value) {
    return;
  }
  await deleteResumeAttachment(resumeId.value, attachmentId);
  ElMessage.success("附件已删除");
  await loadResume();
}

async function readAllMessages() {
  await markMessagesRead();
  ElMessage.success("消息已全部标记已读");
  await loadMessages();
}

async function openMessage(messageId: number) {
  const data = await getMessageDetail(messageId);
  currentMessage.value = normalizeMessageRecord(
    data as Record<string, unknown>
  );
  messageVisible.value = true;
  await refreshUnreadCount();
  await loadMessages();
}

async function removeMessage(messageId: number) {
  await deleteMessages({ messageIds: [messageId] });
  ElMessage.success("消息已删除");
  await loadMessages();
  await refreshUnreadCount();
}

async function refreshUnreadCount() {
  const data = await getUnreadMessageCount();
  unreadCount.value = data.total || 0;
}

function logout() {
  clearAuth();
  router.replace("/");
}

onMounted(async () => {
  if (!route.query.tab) {
    router.replace({ path: "/candidate", query: { tab: activeTab.value } });
  }
  try {
    await Promise.all([
      loadProfile(),
      loadResume(),
      loadApplies(),
      loadMessages(),
      refreshUnreadCount(),
      loadFavoriteJobs(),
      loadStatistics(),
    ]);
  } catch {
    ElMessage.warning("个人中心初始化失败，请确认登录状态和后端服务");
  }
  await nextTick();
  if (activeTab.value === "overview") {
    bindHeatmapResize();
  }
});

onUnmounted(() => {
  heatmapResizeObserver?.disconnect();
  heatmapResizeObserver = null;
});
</script>

<style scoped>
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}

.candidate-form-stack {
  display: flex;
  flex-direction: column;
}

.candidate-form-section {
  padding: 22px 0;
  border-top: 1px solid #f1f5f9;
}

.candidate-form-section:first-child {
  padding-top: 2px;
  border-top: none;
}

.candidate-form-section__title {
  margin: 0 0 6px;
  font-size: 0.9375rem;
  font-weight: 800;
  letter-spacing: -0.01em;
  color: #0f172a;
}

.candidate-form-section__hint {
  margin: 0 0 16px;
  font-size: 0.8125rem;
  line-height: 1.45;
  color: #64748b;
}

.candidate-form-actions {
  margin-top: 4px;
  padding-top: 18px;
  border-top: 1px solid #f1f5f9;
}

.card-title__text {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 2px;
  min-width: 0;
}

.card-title__label {
  font-weight: 800;
  color: #0f172a;
  font-size: 0.9375rem;
}

.card-title__meta {
  font-size: 0.75rem;
  font-weight: 600;
  color: #94a3b8;
}

.candidate-upload-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
}

.candidate-upload-row__tip {
  font-size: 12px;
  color: #94a3b8;
}

.candidate-attachment-chip {
  padding: 10px 12px;
  border-radius: var(--jb-radius-sm, 4px);
  background: #f8fafc;
  border: 1px solid #eef0f3;
}

.candidate-attachment-chip__name {
  font-size: 13px;
  color: #334155;
  font-weight: 600;
}

.candidate-job-list {
  font-size: 14px;
  color: #0f172a;
  overflow-x: auto;
}

.candidate-job-list__tr {
  display: grid;
  grid-template-columns: minmax(220px, 1fr) 156px 120px 108px;
  gap: 12px;
  align-items: center;
  min-width: 600px;
  padding: 18px 20px;
  border-bottom: 1px solid #eef0f3;
  transition: background 0.15s ease, box-shadow 0.15s ease;
}

.candidate-job-list__tr:last-child {
  border-bottom: none;
}

.candidate-job-list__tr--head {
  background: #f3f4f6;
  padding: 12px 20px;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.03em;
  color: #64748b;
  border-bottom: 1px solid #e5e7eb;
}

.candidate-job-list__tr--active {
  background: #f0f7ff;
  box-shadow: inset 0 0 0 1px #146bce;
}

.candidate-job-list__td,
.candidate-job-list__th {
  min-width: 0;
}

.candidate-job-list__cell--date,
.candidate-job-list__cell--status {
  font-size: 13px;
  color: #475569;
}

.candidate-job-list__cell--action {
  text-align: right;
}

.candidate-job-list__cell--action :deep(.el-button) {
  font-weight: 600;
}

.candidate-job-list__job {
  display: flex;
  gap: 14px;
  align-items: flex-start;
  min-width: 0;
}

.candidate-job-list__logo {
  width: 44px;
  height: 44px;
  border-radius: 8px;
  background: linear-gradient(145deg, #e8effc, #dbeafe);
  color: #146bce;
  font-weight: 800;
  font-size: 16px;
  line-height: 44px;
  text-align: center;
  flex-shrink: 0;
  border: 1px solid rgba(20, 107, 206, 0.18);
}

.candidate-job-list__job-main {
  min-width: 0;
}

.candidate-job-list__title-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.candidate-job-list__job-title {
  font-weight: 800;
  font-size: 15px;
  line-height: 1.35;
  color: #0f172a;
}

.candidate-job-list__tag {
  font-size: 12px;
  padding: 2px 10px;
  border-radius: 999px;
  background: #e8f1fc;
  color: #146bce;
  font-weight: 600;
  white-space: nowrap;
}

.candidate-job-list__company {
  margin: 4px 0 6px;
  font-size: 13px;
  color: #64748b;
  line-height: 1.4;
}

.candidate-job-list__meta {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  font-size: 12px;
  color: #64748b;
}

.candidate-job-list__meta-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.candidate-job-list__meta-item--loc::before {
  content: "";
  width: 13px;
  height: 13px;
  flex-shrink: 0;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%2394a3b8' stroke-width='2'%3E%3Cpath d='M12 21s-6-5.2-6-10a6 6 0 1 1 12 0c0 4.8-6 10-6 10z'/%3E%3Ccircle cx='12' cy='11' r='2.5'/%3E%3C/svg%3E")
    center / contain no-repeat;
}

.candidate-job-list__meta-item--pay::before {
  content: "";
  width: 13px;
  height: 13px;
  flex-shrink: 0;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%2394a3b8' stroke-width='2'%3E%3Cpath d='M12 2v20M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6'/%3E%3C/svg%3E")
    center / contain no-repeat;
}

.candidate-job-list__status {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
}

.candidate-job-list__status--success {
  color: #15803d;
}

.candidate-job-list__status--muted {
  color: #64748b;
}

.candidate-job-list__status--danger {
  color: #dc2626;
}

.candidate-job-list__status-ico {
  width: 15px;
  height: 15px;
  flex-shrink: 0;
  background-repeat: no-repeat;
  background-position: center;
  background-size: contain;
}

.candidate-job-list__status--success .candidate-job-list__status-ico {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%2322c55e'%3E%3Cpath d='M9 16.2 4.8 12l-1.4 1.4L9 19 21 7l-1.4-1.4L9 16.2z'/%3E%3C/svg%3E");
}

.candidate-job-list__status--muted .candidate-job-list__status-ico {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none'%3E%3Ccircle cx='12' cy='12' r='9' stroke='%2394a3b8' stroke-width='2'/%3E%3Cpath stroke='%2394a3b8' stroke-width='2' stroke-linecap='round' d='M12 8v4.5'/%3E%3Ccircle cx='12' cy='16.5' r='1' fill='%2394a3b8'/%3E%3C/svg%3E");
}

.candidate-job-list__status--danger .candidate-job-list__status-ico {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%23ef4444' stroke-width='2.2' stroke-linecap='round'%3E%3Cpath d='M18 6L6 18M6 6l12 12'/%3E%3C/svg%3E");
}

.candidate-job-list__empty {
  padding: 12px 0 8px;
}

.candidate-msg-list {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.candidate-msg-card {
  padding: 16px 0;
  border: none;
  border-radius: 0;
  background: transparent;
  box-shadow: none;
  border-bottom: 1px solid #eef0f3;
}

.candidate-msg-list .candidate-msg-card:last-child {
  border-bottom: none;
}

.candidate-msg-card--unread {
  margin: 12px 0;
  padding: 16px 18px;
  border: 1px solid rgba(20, 107, 206, 0.35);
  border-radius: var(--jb-radius-md, 4px);
  background: rgba(240, 247, 255, 0.55);
  box-shadow: var(--jb-shadow-sm, 0 1px 3px rgba(15, 23, 42, 0.06)),
    inset 3px 0 0 #146bce;
  border-bottom: none;
}

.candidate-msg-card__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
}

.candidate-msg-card__kind {
  display: inline-block;
  padding: 3px 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 700;
}

.candidate-msg-card__kind--interview {
  background: #e8f1fc;
  color: #146bce;
}

.candidate-msg-card__kind--reply {
  background: #ecfdf5;
  color: #047857;
}

.candidate-msg-card__kind--system {
  background: #f1f5f9;
  color: #475569;
}

.candidate-msg-card__kind--apply {
  background: #fffbeb;
  color: #b45309;
}

.candidate-msg-card__kind--default {
  background: #f3f4f6;
  color: #4b5563;
}

.candidate-msg-card__time {
  font-size: 12px;
  color: #94a3b8;
  flex-shrink: 0;
}

.candidate-msg-card__job {
  display: flex;
  gap: 12px;
  align-items: flex-start;
  padding: 12px 14px;
  margin-bottom: 12px;
  background: #f8fafc;
  border-radius: var(--jb-radius-sm, 4px);
}

.candidate-msg-card__logo {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: linear-gradient(145deg, #e8effc, #dbeafe);
  color: #146bce;
  font-weight: 800;
  font-size: 15px;
  line-height: 40px;
  text-align: center;
  flex-shrink: 0;
  border: 1px solid rgba(20, 107, 206, 0.18);
}

.candidate-msg-card__job-main {
  min-width: 0;
  flex: 1;
}

.candidate-msg-card__title-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.candidate-msg-card__job-title {
  font-weight: 800;
  font-size: 14px;
  color: #0f172a;
}

.candidate-msg-card__company {
  margin: 4px 0 6px;
  font-size: 12px;
  color: #64748b;
}

.candidate-msg-card__meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  font-size: 12px;
  color: #64748b;
}

.candidate-msg-card__body {
  margin-bottom: 12px;
}

.candidate-msg-card__subject {
  margin: 0 0 6px;
  font-size: 14px;
  font-weight: 700;
  color: #0f172a;
}

.candidate-msg-card__content {
  margin: 0;
  font-size: 13px;
  line-height: 1.55;
  color: #475569;
  white-space: pre-wrap;
}

.candidate-msg-card__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
  padding-top: 4px;
  border-top: 1px solid #f1f5f9;
}

.card-title {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
}

.header-actions-inline {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.inline-input {
  margin-right: 12px;
  width: calc(33.3% - 8px);
}

.apply-heatmap {
  padding: 4px 0 2px;
}

.apply-heatmap__layout {
  display: flex;
  align-items: stretch;
  gap: 6px;
}

.apply-heatmap__dow {
  display: flex;
  flex-direction: column;
  gap: 3px;
  padding-top: 16px;
  flex-shrink: 0;
}

.apply-heatmap__dow-label {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 11px;
  font-size: 10px;
  line-height: 1;
  color: #94a3b8;
  width: 1em;
}

.apply-heatmap__scroll {
  flex: 1;
  min-width: 0;
  overflow-x: hidden;
  padding-bottom: 4px;
}

.apply-heatmap__months {
  display: flex;
  gap: 3px;
  margin-bottom: 6px;
  min-height: 14px;
}

.apply-heatmap__month {
  flex: 0 0 11px;
  width: 11px;
  font-size: 10px;
  line-height: 1.2;
  color: #64748b;
  white-space: nowrap;
  overflow: visible;
  text-align: center;
}

.apply-heatmap__grid {
  display: flex;
  flex-direction: row;
  gap: 3px;
}

.apply-heatmap__col {
  display: flex;
  flex-direction: column;
  gap: 3px;
  flex: 0 0 auto;
}

.apply-heatmap__cell {
  width: 11px;
  height: 11px;
  border-radius: 2px;
  flex-shrink: 0;
  box-sizing: border-box;
}

.apply-heatmap__cell--lvl-0 {
  background: #ebedf0;
  outline: 1px solid rgba(27, 31, 36, 0.06);
  outline-offset: -1px;
}

.apply-heatmap__cell--lvl-1 {
  background: #bfd4f1;
}

.apply-heatmap__cell--lvl-2 {
  background: #7eb0e6;
}

.apply-heatmap__cell--lvl-3 {
  background: #3d8adb;
}

.apply-heatmap__cell--lvl-4 {
  background: #146bce;
}

.apply-heatmap__cell--future {
  background: #f6f8fa;
  outline: 1px solid rgba(27, 31, 36, 0.04);
  outline-offset: -1px;
}

.apply-heatmap__legend {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 4px;
  margin-top: 12px;
  flex-wrap: wrap;
}

.apply-heatmap__legend-text {
  font-size: 11px;
  color: #64748b;
}

.apply-heatmap__legend-swatch {
  width: 11px;
  height: 11px;
}

.attachment-list {
  margin-top: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.attachment-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.attachment-actions {
  display: flex;
  gap: 8px;
}

.badge-text {
  color: #6b7280;
  font-size: 13px;
}

.message-detail__meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 14px;
}

.message-detail__time {
  font-size: 12px;
  color: #94a3b8;
}

.message-detail__job {
  margin-bottom: 16px;
}

.message-detail__title {
  margin: 0 0 10px;
  font-size: 1.05rem;
  font-weight: 800;
  color: #0f172a;
}

.message-detail__content {
  margin: 0;
  font-size: 14px;
  line-height: 1.6;
  color: #475569;
  white-space: pre-wrap;
}

.message-detail__foot {
  margin-top: 18px;
  padding-top: 14px;
  border-top: 1px solid #f1f5f9;
}

@media (max-width: 520px) {
  .inline-input {
    width: 100%;
    margin-right: 0;
    margin-bottom: 8px;
  }
}
</style>
