<template>
  <div class="workbench-shell">
    <WorkbenchSidebar
      brand-label="企业招聘工作台"
      :tabs="tabItems"
      :active-key="activeTab"
      nav-panel-id="enterprise-nav-panel"
      aria-label="企业导航"
      @select="onWorkbenchSelect"
      @logout="logout"
    />

    <div class="workbench-main">
      <header class="workbench-main__top">
        <h1 class="workbench-main__hello">{{ enterpriseTitle }}</h1>
        <p class="workbench-main__sub">
          维护企业信息、发布岗位、查看投递并处理简历
        </p>
      </header>

      <div class="workbench-panels">
        <section
          v-show="activeTab === 'overview'"
          aria-labelledby="ent-panel-overview"
        >
          <h2 id="ent-panel-overview" class="sr-only">数据概览</h2>
          <div class="overview-hero">
            <div class="overview-card overview-card--blue">
              <div class="overview-card__body">
                <p class="overview-card__value">
                  {{ statistics.jobViewCount }}
                </p>
                <p class="overview-card__label">职位浏览量</p>
              </div>
              <div class="overview-card__badge overview-card__badge--blue" />
            </div>
            <div class="overview-card overview-card--amber">
              <div class="overview-card__body">
                <p class="overview-card__value">
                  {{ statistics.resumeReceivedCount }}
                </p>
                <p class="overview-card__label">收到简历数</p>
              </div>
              <div class="overview-card__badge overview-card__badge--amber" />
            </div>
          </div>
          <div class="overview-substats">
            <div class="overview-substat">
              <span class="overview-substat__value">{{
                statistics.interviewCount
              }}</span>
              <span class="overview-substat__label">面试邀约数</span>
            </div>
            <div class="overview-substat">
              <span class="overview-substat__value">{{
                statistics.activeJobCount
              }}</span>
              <span class="overview-substat__label">招聘中职位</span>
            </div>
          </div>
          <el-card shadow="never" class="workbench-card">
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
            <p class="overview-export-hint">
              上方为实时汇总指标，导出可下载指定周期的完整报表数据。
            </p>
          </el-card>
        </section>

        <section
          v-show="activeTab === 'profile'"
          aria-labelledby="ent-panel-profile"
          class="ent-profile-section"
        >
          <h2 id="ent-panel-profile" class="workbench-panel__title">
            企业资料
          </h2>
          <p class="workbench-panel__desc">
            维护工商信息与认证资料，认证通过后方可发布职位。
          </p>

          <div class="ent-profile-body">
            <h3 class="ent-profile-block-title">企业信息</h3>
            <el-form
              class="ent-profile-form"
              label-position="top"
              :model="enterpriseForm"
            >
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
                class="auth-alert ent-profile-alert"
              />
              <el-alert
                v-if="enterprise.rejectReason"
                :title="`驳回原因：${enterprise.rejectReason}`"
                type="error"
                :closable="false"
                class="auth-alert ent-profile-alert"
              />
              <el-row :gutter="20">
                <el-col :xs="24" :sm="8">
                  <el-form-item label="企业名称">
                    <el-input v-model="enterprise.companyName" disabled />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="8">
                  <el-form-item label="所属行业">
                    <el-input
                      v-model="enterpriseForm.industry"
                      placeholder="例如：互联网、制造业"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="8">
                  <el-form-item label="企业规模">
                    <el-input
                      v-model="enterpriseForm.scale"
                      placeholder="例如：20-99 人"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xs="24" :sm="12">
                  <el-form-item label="地址">
                    <el-input
                      v-model="enterpriseForm.address"
                      placeholder="企业办公地址"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12">
                  <el-form-item label="官网">
                    <el-input
                      v-model="enterpriseForm.website"
                      placeholder="https://example.com"
                      class="ent-profile-website-input"
                    >
                      <template #prefix>
                        <span
                          class="ent-profile-input-prefix-icon"
                          aria-hidden="true"
                        >
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            width="16"
                            height="16"
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="2"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          >
                            <path
                              d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"
                            />
                            <path
                              d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"
                            />
                          </svg>
                        </span>
                      </template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="企业简介">
                <el-input
                  v-model="enterpriseForm.introduction"
                  type="textarea"
                  :rows="6"
                  resize="vertical"
                  placeholder="请介绍企业定位、主营业务与亮点等"
                />
              </el-form-item>
              <div class="ent-profile-actions">
                <el-button
                  type="primary"
                  class="ent-profile-btn-primary"
                  @click="saveEnterpriseInfo"
                >
                  保存企业信息
                  <span class="ent-profile-btn-arrow" aria-hidden="true"
                    >→</span
                  >
                </el-button>
              </div>
            </el-form>

            <el-divider class="ent-profile-divider" content-position="left">
              企业认证资料
            </el-divider>

            <el-form
              class="ent-profile-form"
              label-position="top"
              :model="authForm"
            >
              <el-row :gutter="20">
                <el-col :xs="24" :sm="8">
                  <el-form-item label="企业全称">
                    <el-input
                      v-model="authForm.companyName"
                      placeholder="与营业执照一致"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="8">
                  <el-form-item label="统一社会信用代码">
                    <el-input
                      v-model="authForm.creditCode"
                      placeholder="18 位代码"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="8">
                  <el-form-item label="法人姓名">
                    <el-input v-model="authForm.legalPerson" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xs="24" :sm="12">
                  <el-form-item label="营业执照">
                    <el-upload
                      action="#"
                      :show-file-list="false"
                      :http-request="uploadLicenseRequest"
                      class="ent-profile-upload"
                    >
                      <el-button class="ent-profile-upload-btn"
                        >上传营业执照</el-button
                      >
                    </el-upload>
                    <div v-if="authPreview.licenseUrl" class="upload-preview">
                      <a
                        :href="resolveAssetUrl(authPreview.licenseUrl)"
                        target="_blank"
                        >查看已上传营业执照</a
                      >
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :xs="24" :sm="12">
                  <el-form-item label="企业 Logo">
                    <el-upload
                      action="#"
                      :show-file-list="false"
                      :http-request="uploadLogoRequest"
                      class="ent-profile-upload"
                    >
                      <el-button class="ent-profile-upload-btn"
                        >上传 Logo</el-button
                      >
                    </el-upload>
                    <div v-if="authPreview.logoUrl" class="upload-preview">
                      <img
                        :src="resolveAssetUrl(authPreview.logoUrl)"
                        alt="logo"
                      />
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <div class="ent-profile-actions">
                <el-button
                  type="success"
                  class="ent-profile-btn-secondary"
                  @click="submitAuth"
                >
                  提交认证
                </el-button>
              </div>
            </el-form>
          </div>
        </section>

        <section v-show="activeTab === 'jobs'" aria-labelledby="ent-panel-jobs">
          <div class="ent-jobs-toolbar">
            <h2
              id="ent-panel-jobs"
              class="workbench-panel__title ent-jobs-toolbar__title"
            >
              职位管理
            </h2>
            <p class="workbench-panel__desc ent-jobs-toolbar__desc">
              查看与管理已在招岗位；需要新增时点击「发布职位」填写表单。
            </p>
            <el-button
              type="primary"
              class="ent-jobs-toolbar__btn"
              @click="openJobDialog"
              >发布职位</el-button
            >
          </div>
          <div v-if="!jobs.length" class="ent-job-list-empty">
            暂无职位，点击「发布职位」创建。
          </div>
          <div
            v-else
            class="ent-job-list"
            role="table"
            aria-label="企业职位列表"
          >
            <div class="ent-job-list__head" role="row">
              <span
                class="ent-job-list__col ent-job-list__col--jobs"
                role="columnheader"
                >职位</span
              >
              <span
                class="ent-job-list__col ent-job-list__col--status"
                role="columnheader"
                >状态</span
              >
              <span
                class="ent-job-list__col ent-job-list__col--apps"
                role="columnheader"
                >投递</span
              >
              <span
                class="ent-job-list__col ent-job-list__col--actions"
                role="columnheader"
                >操作</span
              >
            </div>
            <div
              v-for="row in jobs"
              :key="row.jobId"
              class="ent-job-list__row"
              :class="{
                'ent-job-list__row--active': selectedJobId === row.jobId,
              }"
              role="row"
              tabindex="0"
              @click="onJobListRowClick(row)"
              @keydown.enter.prevent="onJobListRowClick(row)"
            >
              <div
                class="ent-job-list__col ent-job-list__col--jobs"
                role="cell"
              >
                <div class="ent-job-list__title">{{ row.jobName }}</div>
                <div class="ent-job-list__meta">
                  {{ jobRowSubline(row) }}
                </div>
              </div>
              <div
                class="ent-job-list__col ent-job-list__col--status"
                role="cell"
              >
                <span
                  class="ent-job-status"
                  :class="'ent-job-status--' + jobStatusMeta(row.status).kind"
                >
                  <span class="ent-job-status__icon" aria-hidden="true" />
                  {{ jobStatusMeta(row.status).label }}
                </span>
              </div>
              <div
                class="ent-job-list__col ent-job-list__col--apps"
                role="cell"
              >
                <span class="ent-job-apps">
                  <span class="ent-job-apps__icon" aria-hidden="true" />
                  <span class="ent-job-apps__text"
                    >{{ row.applyCount }} 份投递</span
                  >
                </span>
              </div>
              <div
                class="ent-job-list__col ent-job-list__col--actions"
                role="cell"
              >
                <div class="ent-job-list__actions-inner">
                  <el-button
                    :type="selectedJobId === row.jobId ? 'primary' : 'default'"
                    class="ent-job-list__cta"
                    @click.stop="onViewApplications(row)"
                    >查看投递</el-button
                  >
                  <el-dropdown
                    trigger="click"
                    @command="(c) => onJobRowCommand(c, row)"
                  >
                    <el-button
                      class="ent-job-list__more"
                      circle
                      plain
                      @click.stop
                    >
                      <span class="ent-job-list__more-dots" aria-hidden="true"
                        >⋮</span
                      >
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="promote">
                          推广职位（刷新曝光）
                        </el-dropdown-item>
                        <el-dropdown-item command="detail">
                          查看详情
                        </el-dropdown-item>
                        <el-dropdown-item command="share">
                          复制分享链接
                        </el-dropdown-item>
                        <el-dropdown-item command="expire" divided>
                          标记下架
                        </el-dropdown-item>
                        <el-dropdown-item command="remove">
                          删除职位
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section
          v-show="activeTab === 'applications'"
          aria-labelledby="ent-panel-applications"
        >
          <h2 id="ent-panel-applications" class="workbench-panel__title">
            收到简历
            <span class="workbench-panel__count"
              >（{{ applications.length }}）</span
            >
          </h2>
          <p class="workbench-panel__desc">
            处理候选人投递，筛选并发起面试邀约。
          </p>
          <el-card shadow="never" class="workbench-card">
            <template v-if="applications.length">
              <div
                class="workbench-data-list wb-list--ent-applications"
                role="table"
                aria-label="收到的简历"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div class="workbench-data-list__th" role="columnheader">
                    候选人
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    职位
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    状态
                  </div>
                  <div
                    class="workbench-data-list__th workbench-data-list__cell--action"
                    role="columnheader"
                  >
                    操作
                  </div>
                </div>
                <div
                  v-for="row in applications"
                  :key="row.applyId"
                  class="workbench-data-list__tr"
                  :class="{
                    'workbench-data-list__tr--active':
                      hoveredApplicationApplyId === row.applyId,
                  }"
                  role="row"
                  @mouseenter="hoveredApplicationApplyId = row.applyId"
                  @mouseleave="hoveredApplicationApplyId = null"
                >
                  <div class="workbench-data-list__td" role="cell">
                    <div class="workbench-data-list__primary-title">
                      {{ row.candidateName }}
                    </div>
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.jobName }}
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    <span
                      class="workbench-data-list__status"
                      :class="
                        'workbench-data-list__status--' +
                        enterpriseApplyStatusTone(row.status)
                      "
                    >
                      <span
                        class="workbench-data-list__status-ico"
                        aria-hidden="true"
                      />
                      {{ enterpriseApplyStatusLabel(row.status) }}
                    </span>
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--action"
                    role="cell"
                  >
                    <div class="workbench-data-list__actions">
                      <el-button
                        link
                        type="primary"
                        @click="openResume(row.resumeId)"
                        >查看</el-button
                      >
                      <el-button
                        link
                        type="success"
                        @click="handleStatus(row.applyId, 'SUITABLE')"
                        >通过</el-button
                      >
                      <el-button
                        link
                        type="primary"
                        @click="openInterviewDialog(row)"
                        >邀约</el-button
                      >
                      <el-button
                        link
                        type="danger"
                        @click="handleStatus(row.applyId, 'UNSUITABLE')"
                        >淘汰</el-button
                      >
                    </div>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无投递记录"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>

        <section
          v-show="activeTab === 'interviews'"
          aria-labelledby="ent-panel-interviews"
        >
          <h2 id="ent-panel-interviews" class="workbench-panel__title">
            面试邀约
            <span class="workbench-panel__count"
              >（{{ interviews.length }}）</span
            >
          </h2>
          <p class="workbench-panel__desc">查看已发出的面试安排与状态。</p>
          <el-card shadow="never" class="workbench-card">
            <template v-if="interviews.length">
              <div
                class="workbench-data-list wb-list--ent-interviews"
                role="table"
                aria-label="面试邀约记录"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div class="workbench-data-list__th" role="columnheader">
                    候选人
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    职位
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    面试时间
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    形式
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    状态
                  </div>
                </div>
                <div
                  v-for="row in interviews"
                  :key="row.interviewId"
                  class="workbench-data-list__tr"
                  :class="{
                    'workbench-data-list__tr--active':
                      hoveredInterviewId === row.interviewId,
                  }"
                  role="row"
                  @mouseenter="hoveredInterviewId = row.interviewId"
                  @mouseleave="hoveredInterviewId = null"
                >
                  <div class="workbench-data-list__td" role="cell">
                    <div class="workbench-data-list__primary-title">
                      {{ row.candidateName }}
                    </div>
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.jobName }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.interviewTime }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ interviewTypeLabel(row.interviewType) }}
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    <span
                      class="workbench-data-list__status"
                      :class="
                        'workbench-data-list__status--' +
                        enterpriseInterviewStatusTone(row.status)
                      "
                    >
                      <span
                        class="workbench-data-list__status-ico"
                        aria-hidden="true"
                      />
                      {{ enterpriseInterviewStatusLabel(row.status) }}
                    </span>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无面试邀约"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>

        <section
          v-show="activeTab === 'talents'"
          aria-labelledby="ent-panel-talents"
        >
          <h2 id="ent-panel-talents" class="workbench-panel__title">
            人才搜索
            <span class="workbench-panel__count">（{{ talents.length }}）</span>
          </h2>
          <p class="workbench-panel__desc">按条件检索平台人才库并发起沟通。</p>
          <el-card shadow="never" class="workbench-card">
            <div class="workbench-card-toolbar">
              <el-input
                v-model="talentFilters.keyword"
                placeholder="关键词/技能/自我评价"
              />
              <el-input v-model="talentFilters.major" placeholder="专业" />
              <el-input
                v-model="talentFilters.expectCity"
                placeholder="期望城市"
              />
              <el-button type="primary" @click="loadTalents"
                >搜索人才</el-button
              >
            </div>
            <template v-if="talents.length">
              <div
                class="workbench-data-list wb-list--ent-talents"
                role="table"
                aria-label="人才搜索结果"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div class="workbench-data-list__th" role="columnheader">
                    候选人
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    期望职位
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    期望城市
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    学历
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    学校
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    专业
                  </div>
                  <div
                    class="workbench-data-list__th workbench-data-list__cell--action"
                    role="columnheader"
                  >
                    操作
                  </div>
                </div>
                <div
                  v-for="row in talents"
                  :key="row.resumeId"
                  class="workbench-data-list__tr"
                  :class="{
                    'workbench-data-list__tr--active':
                      hoveredTalentResumeId === row.resumeId,
                  }"
                  role="row"
                  @mouseenter="hoveredTalentResumeId = row.resumeId"
                  @mouseleave="hoveredTalentResumeId = null"
                >
                  <div class="workbench-data-list__td" role="cell">
                    <div class="workbench-data-list__primary-title">
                      {{ row.candidateName }}
                    </div>
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.expectPosition }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.expectCity }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.education }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.school }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.major }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--action"
                    role="cell"
                  >
                    <div class="workbench-data-list__actions">
                      <el-button
                        link
                        type="primary"
                        @click="openTalent(row.resumeId)"
                        >查看</el-button
                      >
                      <el-button
                        link
                        type="success"
                        @click="openTalentContact(row.resumeId)"
                        >沟通</el-button
                      >
                      <el-button
                        link
                        @click="toggleFavoriteResume(row.resumeId)"
                      >
                        {{
                          isFavoriteResume(row.resumeId) ? "取消收藏" : "收藏"
                        }}
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无人才数据，请调整条件后搜索"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>

        <section
          v-show="activeTab === 'favorites'"
          aria-labelledby="ent-panel-favorites"
        >
          <h2 id="ent-panel-favorites" class="workbench-panel__title">
            简历收藏
            <span class="workbench-panel__count"
              >（{{ favoriteResumes.length }}）</span
            >
          </h2>
          <p class="workbench-panel__desc">
            管理已收藏简历，支持筛选与批量导出。
          </p>
          <el-card shadow="never" class="workbench-card">
            <div class="workbench-card-toolbar">
              <el-input
                v-model="favoriteResumeFilters.major"
                placeholder="专业"
              />
              <el-input
                v-model="favoriteResumeFilters.education"
                placeholder="学历"
              />
              <el-input
                v-model="favoriteResumeFilters.skillKeywords"
                placeholder="技能关键词"
              />
              <el-select
                v-model="favoriteResumeFilters.jobId"
                placeholder="关联职位"
                clearable
              >
                <el-option
                  v-for="job in jobs"
                  :key="job.jobId"
                  :label="job.jobName"
                  :value="job.jobId"
                />
              </el-select>
              <el-button type="primary" @click="searchFavoriteResumeLibrary">
                搜索收藏夹
              </el-button>
              <el-button @click="loadFavoriteResumes">重置</el-button>
              <el-button type="success" @click="exportSelectedResumes('PDF')">
                批量导出 PDF
              </el-button>
              <el-button @click="exportSelectedResumes('EXCEL')">
                批量导出 Excel
              </el-button>
            </div>
            <template v-if="favoriteResumes.length">
              <div
                class="workbench-data-list wb-list--ent-favorites"
                role="table"
                aria-label="简历收藏"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div
                    class="workbench-data-list__th workbench-data-list__cell--check"
                    role="columnheader"
                  >
                    <span class="sr-only">选择</span>
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    候选人
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    期望职位
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    学历
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    学校
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    专业
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    来源
                  </div>
                  <div
                    class="workbench-data-list__th workbench-data-list__cell--action"
                    role="columnheader"
                  >
                    操作
                  </div>
                </div>
                <div
                  v-for="row in favoriteResumes"
                  :key="row.resumeId"
                  class="workbench-data-list__tr"
                  :class="{
                    'workbench-data-list__tr--active':
                      hoveredFavoriteResumeId === row.resumeId,
                  }"
                  role="row"
                  @mouseenter="hoveredFavoriteResumeId = row.resumeId"
                  @mouseleave="hoveredFavoriteResumeId = null"
                >
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--check"
                    role="cell"
                  >
                    <el-checkbox
                      :model-value="
                        selectedFavoriteResumeIds.includes(row.resumeId)
                      "
                      @change="(v) => onFavoriteRowCheck(row, Boolean(v))"
                    />
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    <div class="workbench-data-list__primary-title">
                      {{ row.candidateName }}
                    </div>
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.expectPosition }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.education }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.school }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.major }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.applied ? "投递/已收藏" : "收藏" }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--action"
                    role="cell"
                  >
                    <div class="workbench-data-list__actions">
                      <el-button
                        link
                        type="primary"
                        @click="openResume(row.resumeId)"
                        >查看</el-button
                      >
                      <el-button
                        link
                        type="danger"
                        @click="toggleFavoriteResume(row.resumeId)"
                        >取消收藏</el-button
                      >
                    </div>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无收藏简历"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>
      </div>
    </div>

    <el-dialog
      v-model="jobDialogVisible"
      title="发布职位"
      width="720px"
      destroy-on-close
    >
      <el-alert
        title="职位发布后会先进入待审核，只有管理员审核通过并处于招聘中状态，首页才会显示。"
        type="info"
        :closable="false"
        class="auth-alert"
      />
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
          <el-input v-model="jobForm.requirement" type="textarea" :rows="3" />
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
      <template #footer>
        <el-button @click="jobDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          :disabled="enterprise.authStatus !== 'PASS'"
          @click="saveJob"
          >保存职位</el-button
        >
      </template>
    </el-dialog>

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
        <el-button
          @click="toggleFavoriteResume(resumeDetail.resumeId as number)"
        >
          {{
            isFavoriteResume(resumeDetail.resumeId) ? "取消收藏" : "收藏简历"
          }}
        </el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="interviewVisible" title="发送面试邀约" width="620px">
      <el-form label-position="top" :model="interviewForm">
        <el-form-item label="面试时间">
          <el-input
            v-model="interviewForm.interviewTime"
            placeholder="2026-04-20 14:00:00"
          />
        </el-form-item>
        <el-form-item label="面试形式">
          <el-select v-model="interviewForm.interviewType">
            <el-option label="线上" value="ONLINE" />
            <el-option label="线下" value="OFFLINE" />
          </el-select>
        </el-form-item>
        <el-form-item label="面试地点">
          <el-input v-model="interviewForm.interviewPlace" />
        </el-form-item>
        <el-form-item label="会议链接">
          <el-input v-model="interviewForm.interviewLink" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="interviewForm.contactName" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="interviewForm.contactMobile" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="interviewForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="interviewVisible = false">取消</el-button>
        <el-button type="primary" @click="submitInterview">发送邀约</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="talentVisible" title="人才详情" width="760px">
      <div v-if="talentDetail" class="detail-box">
        <h4>基础信息</h4>
        <pre>{{ JSON.stringify(talentDetail.basicInfo, null, 2) }}</pre>
        <h4>求职意向</h4>
        <pre>{{ JSON.stringify(talentDetail.jobIntention, null, 2) }}</pre>
        <h4>教育经历</h4>
        <pre>{{ JSON.stringify(talentDetail.educationList, null, 2) }}</pre>
        <h4>技能标签</h4>
        <pre>{{ JSON.stringify(talentDetail.skillList, null, 2) }}</pre>
        <el-button
          type="primary"
          @click="toggleFavoriteResume(talentDetail.resumeId as number)"
        >
          {{
            isFavoriteResume(talentDetail.resumeId as number)
              ? "取消收藏"
              : "收藏简历"
          }}
        </el-button>
      </div>
    </el-dialog>

    <el-dialog v-model="talentContactVisible" title="主动沟通" width="560px">
      <el-form label-position="top" :model="talentContactForm">
        <el-form-item label="推荐职位">
          <el-select v-model="talentContactForm.jobId">
            <el-option
              v-for="job in jobs"
              :key="job.jobId"
              :label="job.jobName"
              :value="job.jobId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="沟通内容">
          <el-input
            v-model="talentContactForm.message"
            type="textarea"
            :rows="4"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="talentContactVisible = false">取消</el-button>
        <el-button type="primary" @click="submitTalentContact"
          >发送沟通</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from "vue";
import { ElMessage, UploadRequestOptions } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import WorkbenchSidebar from "@/components/workbench/WorkbenchSidebar.vue";
import {
  batchExportEnterpriseResumes,
  contactEnterpriseTalent,
  createEnterpriseInterview,
  createEnterpriseJob,
  deleteEnterpriseJob,
  downloadGeneratedFile,
  exportEnterpriseResumePdf,
  exportStatistics,
  favoriteEnterpriseResume,
  listFavoriteEnterpriseResumes,
  listEnterpriseInterviews,
  previewEnterpriseJob,
  refreshEnterpriseJob,
  shareEnterpriseJob,
  getEnterpriseAuthStatus,
  getEnterpriseInfo,
  getEnterpriseResumeDetail,
  getEnterpriseStatistics,
  listEnterpriseApplies,
  listEnterpriseJobs,
  offlineEnterpriseJob,
  searchFavoriteEnterpriseResumes,
  searchEnterpriseTalents,
  getEnterpriseTalentDetail,
  submitEnterpriseAuth,
  unfavoriteEnterpriseResume,
  uploadCommonFile,
  updateEnterpriseApplyStatus,
  updateEnterpriseInfo,
} from "@/api/recruit";
import { resolveAssetUrl } from "@/api/http";
import { clearAuth } from "@/utils/auth";

type EnterpriseTab =
  | "overview"
  | "profile"
  | "jobs"
  | "applications"
  | "interviews"
  | "talents"
  | "favorites";

const tabItems: { key: EnterpriseTab; label: string }[] = [
  { key: "overview", label: "数据概览" },
  { key: "profile", label: "企业资料" },
  { key: "jobs", label: "职位管理" },
  { key: "applications", label: "收到简历" },
  { key: "interviews", label: "面试邀约" },
  { key: "talents", label: "人才搜索" },
  { key: "favorites", label: "简历收藏" },
];

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
  /** 职位类型，用于副标题，如「全职」 */
  jobCategory?: string;
  /** 招聘结束日 yyyy-MM-dd，用于副标题「剩余 n 天」 */
  recruitEndDate?: string;
}

interface EnterpriseApplyRecord {
  applyId: number;
  resumeId: number;
  candidateName: string;
  jobName: string;
  status: string;
}

interface InterviewRecord {
  interviewId: number;
  candidateName: string;
  jobName: string;
  interviewTime: string;
  interviewType: string;
  status: string;
}

interface TalentRecord {
  resumeId: number;
  candidateName: string;
  expectPosition: string;
  expectCity: string;
  education: string;
  school: string;
  major: string;
}

interface FavoriteResumeRecord {
  resumeId: number;
  title: string;
  candidateName: string;
  education: string;
  school: string;
  major: string;
  expectPosition: string;
  expectCity: string;
  favorited: boolean;
  applied: boolean;
}

interface ResumeDetail {
  resumeId?: number;
  basicInfo?: Record<string, unknown>;
  jobIntention?: Record<string, unknown>;
  educationList?: unknown[];
  skillList?: unknown[];
  selfEvaluation?: string;
  favorited?: boolean;
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
const route = useRoute();
const activeTab = ref<EnterpriseTab>("overview");
const welfareOptions = ["双休", "五险一金", "带薪年假", "年度体检", "餐补"];
const enterprise = reactive<EnterpriseInfo>({});
const enterpriseTitle = computed(
  () => enterprise.companyName?.trim() || "企业招聘工作台"
);
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
const selectedJobId = ref<number | null>(null);
const applications = ref<EnterpriseApplyRecord[]>([]);
const interviews = ref<InterviewRecord[]>([]);
const talents = ref<TalentRecord[]>([]);
const favoriteResumes = ref<FavoriteResumeRecord[]>([]);
const selectedFavoriteResumeIds = ref<number[]>([]);
const hoveredApplicationApplyId = ref<number | null>(null);
const hoveredInterviewId = ref<number | null>(null);
const hoveredTalentResumeId = ref<number | null>(null);
const hoveredFavoriteResumeId = ref<number | null>(null);
const resumeVisible = ref(false);
const interviewVisible = ref(false);
const talentVisible = ref(false);
const talentContactVisible = ref(false);
const currentApplyId = ref<number | null>(null);
const currentTalentResumeId = ref<number | null>(null);
const resumeDetail = ref<ResumeDetail | null>(null);
const talentDetail = ref<Record<string, unknown> | null>(null);
const jobDialogVisible = ref(false);

const JOB_FORM_DEFAULTS: EnterpriseJobForm = {
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
};

const jobForm = reactive<EnterpriseJobForm>({
  ...JOB_FORM_DEFAULTS,
  welfare: [...JOB_FORM_DEFAULTS.welfare],
});

function resetJobForm() {
  Object.assign(jobForm, JOB_FORM_DEFAULTS);
  jobForm.welfare = [...JOB_FORM_DEFAULTS.welfare];
}

function openJobDialog() {
  if (!authReady.value) {
    ElMessage.warning("企业认证通过后才能发布职位");
    return;
  }
  resetJobForm();
  jobDialogVisible.value = true;
}
const interviewForm = reactive({
  interviewTime: "2026-04-20 14:00:00",
  interviewType: "OFFLINE",
  interviewPlace: "成都市高新区天府软件园",
  interviewLink: "",
  contactName: "HR 李老师",
  contactMobile: "13900000000",
  remark: "请提前 10 分钟到场",
});
const talentFilters = reactive({
  keyword: "",
  major: "",
  expectCity: "",
});
const favoriteResumeFilters = reactive({
  major: "",
  education: "",
  skillKeywords: "",
  jobId: undefined as number | undefined,
});
const talentContactForm = reactive({
  jobId: 0,
  message: "您好，我们对您的背景很感兴趣，想进一步沟通岗位机会。",
});

type ListStatusTone = "success" | "muted" | "danger";

function enterpriseApplyStatusTone(status: string): ListStatusTone {
  const s = (status || "").toUpperCase();
  if (
    s === "SUITABLE" ||
    s === "INVITED" ||
    s === "ACCEPTED" ||
    s === "HIRED"
  ) {
    return "success";
  }
  if (s === "UNSUITABLE" || s === "REJECTED" || s === "DECLINED") {
    return "danger";
  }
  return "muted";
}

function enterpriseApplyStatusLabel(status: string): string {
  const m: Record<string, string> = {
    SUBMITTED: "待处理",
    SUITABLE: "已通过",
    UNSUITABLE: "已淘汰",
    INVITED: "已邀约",
    ACCEPTED: "已接受",
    REJECTED: "已拒绝",
  };
  return m[(status || "").toUpperCase()] || status || "—";
}

function enterpriseInterviewStatusTone(status: string): ListStatusTone {
  const s = (status || "").toUpperCase();
  if (s === "CONFIRMED" || s === "DONE" || s === "COMPLETED") {
    return "success";
  }
  if (s === "CANCELLED" || s === "REJECTED" || s === "NO_SHOW") {
    return "danger";
  }
  return "muted";
}

function enterpriseInterviewStatusLabel(status: string): string {
  const m: Record<string, string> = {
    PENDING: "待定",
    CONFIRMED: "已确认",
    CANCELLED: "已取消",
    DONE: "已完成",
  };
  return m[(status || "").toUpperCase()] || status || "—";
}

function interviewTypeLabel(t: string): string {
  if (t === "ONLINE") {
    return "线上";
  }
  if (t === "OFFLINE") {
    return "线下";
  }
  return t || "—";
}

function onFavoriteRowCheck(row: FavoriteResumeRecord, checked: boolean) {
  const id = row.resumeId;
  if (checked) {
    if (!selectedFavoriteResumeIds.value.includes(id)) {
      selectedFavoriteResumeIds.value = [
        ...selectedFavoriteResumeIds.value,
        id,
      ];
    }
  } else {
    selectedFavoriteResumeIds.value = selectedFavoriteResumeIds.value.filter(
      (x) => x !== id
    );
  }
}

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
  ElMessage.success("职位已发布，管理员审核通过后会显示在首页");
  await loadJobs();
  jobDialogVisible.value = false;
  resetJobForm();
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
  if (
    selectedJobId.value != null &&
    !jobs.value.some((j) => j.jobId === selectedJobId.value)
  ) {
    selectedJobId.value = null;
  }
}

type JobStatusKind = "active" | "pending" | "expired";

function jobStatusMeta(status: string): { kind: JobStatusKind; label: string } {
  const s = (status || "").toUpperCase();
  if (s === "RECRUITING" || s === "PUBLISHED" || s === "ACTIVE") {
    return { kind: "active", label: "招聘中" };
  }
  if (s === "PENDING" || s === "AUDITING") {
    return { kind: "pending", label: "待审核" };
  }
  return { kind: "expired", label: "已下架" };
}

function formatJobPublishSlice(publishTime: string): string {
  const t = (publishTime || "").trim();
  return t.length >= 10 ? t.slice(0, 10) : t || "—";
}

function jobRowSubline(row: EnterpriseJobRecord): string {
  const type = row.jobCategory?.trim() || "全职";
  const endRaw = row.recruitEndDate?.trim();
  if (endRaw) {
    const end = new Date(endRaw + (endRaw.length <= 10 ? "T00:00:00" : ""));
    if (!Number.isNaN(end.getTime())) {
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      const endDay = new Date(end);
      endDay.setHours(0, 0, 0, 0);
      const days = Math.round((endDay.getTime() - today.getTime()) / 86400000);
      if (days > 0) {
        return `${type} · 剩余 ${days} 天`;
      }
      if (days === 0) {
        return `${type} · 今日截止`;
      }
    }
    return `${type} · ${endRaw.slice(0, 10)}`;
  }
  return `${type} · 发布于 ${formatJobPublishSlice(row.publishTime)}`;
}

function onJobListRowClick(row: EnterpriseJobRecord) {
  selectedJobId.value = row.jobId;
}

function onViewApplications(row: EnterpriseJobRecord) {
  selectedJobId.value = row.jobId;
  selectTab("applications");
}

function onJobRowCommand(command: string, row: EnterpriseJobRecord) {
  if (command === "promote") {
    refreshJob(row.jobId);
  } else if (command === "detail") {
    previewJob(row.jobId);
  } else if (command === "share") {
    shareJob(row.jobId);
  } else if (command === "expire") {
    offlineJob(row.jobId);
  } else if (command === "remove") {
    removeJob(row.jobId);
  }
}

async function previewJob(jobId: number) {
  const data = await previewEnterpriseJob(jobId);
  ElMessage.info(`预览职位：${data.jobName}`);
}

async function refreshJob(jobId: number) {
  await refreshEnterpriseJob(jobId);
  ElMessage.success("职位已刷新");
  await loadJobs();
}

async function shareJob(jobId: number) {
  const data = await shareEnterpriseJob(jobId);
  await navigator.clipboard.writeText(window.location.origin + data.shareUrl);
  ElMessage.success("职位分享链接已复制");
}

async function offlineJob(jobId: number) {
  await offlineEnterpriseJob(jobId);
  ElMessage.success("职位已下架");
  await loadJobs();
}

async function removeJob(jobId: number) {
  await deleteEnterpriseJob(jobId);
  ElMessage.success("职位已删除");
  await loadJobs();
}

async function loadApplications() {
  const data = await listEnterpriseApplies({ pageNum: 1, pageSize: 10 });
  applications.value = data.list || [];
}

async function loadInterviews() {
  const data = await listEnterpriseInterviews({ pageNum: 1, pageSize: 10 });
  interviews.value = data.list || [];
}

async function loadTalents() {
  const data = await searchEnterpriseTalents({
    pageNum: 1,
    pageSize: 10,
    ...talentFilters,
  });
  talents.value = data.list || [];
}

async function loadFavoriteResumes() {
  const data = await listFavoriteEnterpriseResumes({
    pageNum: 1,
    pageSize: 20,
  });
  favoriteResumes.value = data.list || [];
}

async function searchFavoriteResumeLibrary() {
  const data = await searchFavoriteEnterpriseResumes({
    pageNum: 1,
    pageSize: 20,
    ...favoriteResumeFilters,
  });
  favoriteResumes.value = data.list || [];
}

function isFavoriteResume(resumeId?: number) {
  if (!resumeId) {
    return false;
  }
  return favoriteResumes.value.some((item) => item.resumeId === resumeId);
}

async function toggleFavoriteResume(resumeId: number) {
  if (isFavoriteResume(resumeId)) {
    await unfavoriteEnterpriseResume(resumeId);
    ElMessage.success("已取消收藏");
  } else {
    await favoriteEnterpriseResume(resumeId);
    ElMessage.success("已加入收藏夹");
  }
  if (resumeDetail.value?.resumeId === resumeId) {
    resumeDetail.value.favorited = !resumeDetail.value.favorited;
  }
  await loadFavoriteResumes();
}

async function exportSelectedResumes(format: "PDF" | "EXCEL") {
  if (selectedFavoriteResumeIds.value.length === 0) {
    ElMessage.warning("请先选择要导出的简历");
    return;
  }
  const data = await batchExportEnterpriseResumes({
    resumeIds: selectedFavoriteResumeIds.value,
    format,
  });
  await downloadGeneratedFile(data.fileId, data.fileName);
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

function openInterviewDialog(row: EnterpriseApplyRecord) {
  currentApplyId.value = row.applyId;
  interviewVisible.value = true;
}

async function submitInterview() {
  if (!currentApplyId.value) {
    ElMessage.warning("请先选择投递记录");
    return;
  }
  await createEnterpriseInterview({
    applyId: currentApplyId.value,
    ...interviewForm,
  });
  interviewVisible.value = false;
  currentApplyId.value = null;
  ElMessage.success("面试邀约已发送");
  await Promise.all([loadApplications(), loadInterviews(), loadStatistics()]);
}

async function openTalent(resumeId: number) {
  talentDetail.value = await getEnterpriseTalentDetail(resumeId);
  talentVisible.value = true;
}

function openTalentContact(resumeId: number) {
  currentTalentResumeId.value = resumeId;
  talentContactForm.jobId = jobs.value[0]?.jobId || 0;
  talentContactVisible.value = true;
}

async function submitTalentContact() {
  if (!currentTalentResumeId.value || !talentContactForm.jobId) {
    ElMessage.warning("请先选择人才和推荐职位");
    return;
  }
  await contactEnterpriseTalent({
    resumeId: currentTalentResumeId.value,
    jobId: talentContactForm.jobId,
    message: talentContactForm.message,
  });
  talentContactVisible.value = false;
  ElMessage.success("沟通消息已发送");
}

async function openResume(resumeId: number) {
  resumeDetail.value = await getEnterpriseResumeDetail(resumeId);
  resumeDetail.value.favorited = isFavoriteResume(resumeId);
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
  router.replace("/");
}

function isEnterpriseTab(s: string): s is EnterpriseTab {
  return tabItems.some((t) => t.key === s);
}

function selectTab(key: EnterpriseTab) {
  activeTab.value = key;
  router.replace({ path: "/enterprise", query: { tab: key } });
}

function onWorkbenchSelect(key: string) {
  if (isEnterpriseTab(key)) {
    selectTab(key);
  }
}

watch(
  () => route.query.tab,
  (q) => {
    if (typeof q === "string" && isEnterpriseTab(q)) {
      activeTab.value = q;
    }
  },
  { immediate: true }
);

onMounted(async () => {
  if (!route.query.tab) {
    router.replace({ path: "/enterprise", query: { tab: activeTab.value } });
  }
  try {
    await Promise.all([
      loadEnterpriseInfo(),
      loadJobs(),
      loadApplications(),
      loadInterviews(),
      loadTalents(),
      loadFavoriteResumes(),
      loadStatistics(),
    ]);
  } catch (error) {
    ElMessage.warning("企业工作台初始化失败，请确认登录状态和后端服务");
  }
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

.toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
  margin-bottom: 12px;
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

.overview-export-hint {
  margin: 0;
  font-size: 0.9375rem;
  line-height: 1.55;
  color: #64748b;
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

/* 企业资料：参考现代表单（栅格 + 顶置标签 + 圆角边框） */
.ent-profile-body {
  max-width: 920px;
  padding-top: 4px;
}

.ent-profile-block-title {
  margin: 0 0 20px;
  font-size: 1.0625rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.02em;
}

.ent-profile-form :deep(.el-form-item) {
  margin-bottom: 22px;
}

.ent-profile-form :deep(.el-form-item__label) {
  margin-bottom: 8px !important;
  padding: 0 !important;
  font-size: 0.875rem;
  font-weight: 500;
  color: #475569;
  line-height: 1.4;
}

.ent-profile-form :deep(.el-input__wrapper) {
  border-radius: 6px !important;
  box-shadow: none !important;
  border: 1px solid #e2e8f0 !important;
  padding: 4px 12px;
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
}

.ent-profile-form :deep(.el-input__wrapper:hover) {
  border-color: #cbd5e1 !important;
}

.ent-profile-form :deep(.el-input__wrapper.is-focus) {
  border-color: #146bce !important;
  box-shadow: 0 0 0 1px rgba(20, 107, 206, 0.2) !important;
}

.ent-profile-form :deep(.el-textarea__inner) {
  border-radius: 6px !important;
  border: 1px solid #e2e8f0 !important;
  box-shadow: none !important;
  padding: 12px 14px !important;
  font-size: 0.9375rem;
  line-height: 1.55;
  color: #0f172a;
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
}

.ent-profile-form :deep(.el-textarea__inner:hover) {
  border-color: #cbd5e1 !important;
}

.ent-profile-form :deep(.el-textarea__inner:focus) {
  border-color: #146bce !important;
  box-shadow: 0 0 0 1px rgba(20, 107, 206, 0.2) !important;
}

.ent-profile-form :deep(.el-input.is-disabled .el-input__wrapper) {
  background: #f8fafc !important;
  border-color: #e2e8f0 !important;
}

.ent-profile-input-prefix-icon {
  display: inline-flex;
  align-items: center;
  margin-right: 4px;
  color: #146bce;
}

.ent-profile-alert {
  border-radius: 6px;
}

.ent-profile-divider {
  margin: 32px 0 28px;
}

.ent-profile-divider :deep(.el-divider__text) {
  font-size: 0.9375rem;
  font-weight: 600;
  color: #64748b;
}

.ent-profile-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
  margin-top: 4px;
  margin-bottom: 4px;
}

.ent-profile-btn-primary {
  border-radius: 6px !important;
  padding: 10px 22px !important;
  font-weight: 600 !important;
}

.ent-profile-btn-arrow {
  margin-left: 8px;
  font-size: 1rem;
  opacity: 0.95;
}

.ent-profile-btn-secondary {
  border-radius: 6px !important;
  padding: 10px 22px !important;
  font-weight: 600 !important;
}

.ent-profile-upload :deep(.ent-profile-upload-btn) {
  border-radius: 6px !important;
  border: 1px solid #e2e8f0 !important;
  background: #f8fafc !important;
  color: #334155 !important;
  font-weight: 500;
}

.ent-profile-upload :deep(.ent-profile-upload-btn:hover) {
  border-color: #cbd5e1 !important;
  background: #f1f5f9 !important;
  color: #0f172a !important;
}

/* 职位管理：顶栏（标题 + 说明 + 发布） */
.ent-jobs-toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px 16px;
  margin-bottom: 20px;
}

.ent-jobs-toolbar__title {
  margin: 0 !important;
}

.ent-jobs-toolbar__desc {
  margin: 0 !important;
  flex: 1;
  min-width: 200px;
}

.ent-jobs-toolbar__btn {
  flex-shrink: 0;
  margin-left: auto;
}

/* 职位管理：参考卡片式列表（无外框） */
.ent-job-list-empty {
  padding: 28px 16px;
  text-align: center;
  font-size: 0.9375rem;
  color: #64748b;
}

.ent-job-list {
  border: none;
  border-radius: 0;
  overflow: visible;
  background: transparent;
}

.ent-job-list__head {
  display: grid;
  grid-template-columns: minmax(200px, 1fr) 120px 140px minmax(200px, auto);
  gap: 16px;
  align-items: center;
  padding: 14px 4px 14px 0;
  font-size: 0.6875rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  color: #94a3b8;
  text-transform: uppercase;
  background: transparent;
  border-bottom: 1px solid #e8ecf1;
}

.ent-job-list__row {
  display: grid;
  grid-template-columns: minmax(200px, 1fr) 120px 140px minmax(200px, auto);
  gap: 16px;
  align-items: center;
  padding: 20px 4px 20px 0;
  border-bottom: 1px solid #f1f5f9;
  cursor: pointer;
  transition: background 0.12s ease, box-shadow 0.12s ease,
    border-color 0.12s ease;
  outline: none;
}

.ent-job-list__row:last-child {
  border-bottom: none;
}

.ent-job-list__row:hover {
  background: #fafbfc;
}

.ent-job-list__row:focus-visible {
  box-shadow: inset 0 0 0 2px rgba(20, 107, 206, 0.35);
}

.ent-job-list__row--active {
  background: #fff;
  box-shadow: inset 0 0 0 1px #146bce;
  border-bottom-color: transparent;
}

.ent-job-list__title {
  font-size: 1.0625rem;
  font-weight: 700;
  color: #0f172a;
  letter-spacing: -0.02em;
  line-height: 1.3;
}

.ent-job-list__meta {
  margin-top: 6px;
  font-size: 0.8125rem;
  color: #64748b;
  line-height: 1.4;
}

.ent-job-status {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  font-weight: 600;
}

.ent-job-status__icon {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  flex-shrink: 0;
  position: relative;
}

.ent-job-status--active {
  color: #15803d;
}

.ent-job-status--active .ent-job-status__icon {
  background: #dcfce7;
  border: 1px solid #86efac;
}

.ent-job-status--active .ent-job-status__icon::after {
  content: "";
  position: absolute;
  left: 5px;
  top: 2px;
  width: 5px;
  height: 9px;
  border: solid #16a34a;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.ent-job-status--pending {
  color: #b45309;
}

.ent-job-status--pending .ent-job-status__icon {
  background: #ffedd5;
  border: 1px solid #fdba74;
}

.ent-job-status--pending .ent-job-status__icon::after {
  content: "";
  position: absolute;
  left: 50%;
  top: 50%;
  width: 2px;
  height: 8px;
  margin: -4px 0 0 -1px;
  background: #d97706;
  border-radius: 1px;
}

.ent-job-status--expired {
  color: #b91c1c;
}

.ent-job-status--expired .ent-job-status__icon {
  background: #fee2e2;
  border: 1px solid #fca5a5;
}

.ent-job-status--expired .ent-job-status__icon::before,
.ent-job-status--expired .ent-job-status__icon::after {
  content: "";
  position: absolute;
  left: 50%;
  top: 50%;
  width: 10px;
  height: 2px;
  margin: -1px 0 0 -5px;
  background: #dc2626;
  border-radius: 1px;
}

.ent-job-status--expired .ent-job-status__icon::before {
  transform: rotate(45deg);
}

.ent-job-status--expired .ent-job-status__icon::after {
  transform: rotate(-45deg);
}

.ent-job-apps {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 0.875rem;
  color: #64748b;
}

.ent-job-apps__icon {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%2394a3b8' stroke-width='2'%3E%3Cpath d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'/%3E%3Ccircle cx='9' cy='7' r='4'/%3E%3Cpath d='M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75'/%3E%3C/svg%3E")
    center / 16px 16px no-repeat;
}

.ent-job-list__actions-inner {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 10px;
}

.ent-job-list__cta {
  border-radius: var(--jb-radius-sm, 6px) !important;
  font-weight: 600;
}

.ent-job-list__cta.el-button--default {
  background: #f1f5f9 !important;
  color: #146bce !important;
  border-color: #e2e8f0 !important;
}

.ent-job-list__cta.el-button--default:hover {
  background: #e2e8f0 !important;
  border-color: #cbd5e1 !important;
  color: #0f4c9e !important;
}

.ent-job-list__more {
  width: 36px !important;
  height: 36px !important;
  padding: 0 !important;
  border-radius: 50% !important;
}

.ent-job-list__more-dots {
  display: block;
  font-size: 1.1rem;
  line-height: 1;
  color: #64748b;
  transform: translateY(-1px);
}

@media (max-width: 960px) {
  .ent-job-list__head,
  .ent-job-list__row {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .ent-job-list__head .ent-job-list__col--status,
  .ent-job-list__head .ent-job-list__col--apps,
  .ent-job-list__head .ent-job-list__col--actions {
    display: none;
  }

  .ent-job-list__row .ent-job-list__col--actions {
    justify-self: stretch;
  }

  .ent-job-list__actions-inner {
    justify-content: flex-start;
    flex-wrap: wrap;
  }
}
</style>
