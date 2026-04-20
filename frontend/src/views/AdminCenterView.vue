<template>
  <div class="workbench-shell">
    <WorkbenchSidebar
      brand-label="管理员后台"
      :tabs="tabItems"
      :active-key="activeTab"
      nav-panel-id="admin-nav-panel"
      aria-label="管理员导航"
      @select="onWorkbenchSelect"
      @logout="logout"
    />
    <div class="workbench-main">
      <header class="workbench-main__top">
        <h1 class="workbench-main__hello">管理员工作台</h1>
        <p class="workbench-main__sub">用户管理、审核流与系统配置</p>
      </header>
      <div class="workbench-panels">
        <section v-show="activeTab === 'candidate'">
          <h2 class="workbench-panel__title">
            求职者管理
            <span class="workbench-panel__count"
              >（{{ candidates.length }}）</span
            >
          </h2>
          <p class="workbench-panel__desc">
            查询求职者账号，查看详情并调整启用状态。
          </p>
          <el-card shadow="never" class="workbench-card">
            <div class="workbench-card-toolbar">
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
            <template v-if="candidates.length">
              <div
                class="workbench-data-list wb-list--admin-candidates"
                role="table"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div class="workbench-data-list__th" role="columnheader">
                    姓名
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    手机号
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    身份
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
                  v-for="row in candidates"
                  :key="row.userId"
                  class="workbench-data-list__tr"
                  role="row"
                >
                  <div class="workbench-data-list__td" role="cell">
                    <span class="workbench-data-list__primary-title">{{
                      row.realName
                    }}</span>
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.mobile }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.identityType }}
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    {{ row.status }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--action"
                    role="cell"
                  >
                    <div class="workbench-data-list__actions">
                      <el-button
                        link
                        type="primary"
                        @click="openCandidateDetail(row.userId)"
                        >详情</el-button
                      >
                      <el-button
                        link
                        type="warning"
                        @click="changeCandidateStatus(row)"
                      >
                        {{ row.status === "ACTIVE" ? "禁用" : "启用" }}
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无数据"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>

        <section v-show="activeTab === 'enterprise'">
          <h2 class="workbench-panel__title">
            企业管理
            <span class="workbench-panel__count"
              >（{{ enterprises.length }}）</span
            >
          </h2>
          <p class="workbench-panel__desc">企业认证与账号状态管理。</p>
          <el-card shadow="never" class="workbench-card">
            <div class="workbench-card-toolbar">
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
              <el-button type="primary" @click="loadEnterprises"
                >查询</el-button
              >
            </div>
            <template v-if="enterprises.length">
              <div
                class="workbench-data-list wb-list--admin-enterprises"
                role="table"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div class="workbench-data-list__th" role="columnheader">
                    企业名称
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    行业
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    认证状态
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    账号状态
                  </div>
                  <div
                    class="workbench-data-list__th workbench-data-list__cell--action"
                    role="columnheader"
                  >
                    操作
                  </div>
                </div>
                <div
                  v-for="row in enterprises"
                  :key="row.enterpriseId"
                  class="workbench-data-list__tr"
                  role="row"
                >
                  <div class="workbench-data-list__td" role="cell">
                    <span class="workbench-data-list__primary-title">{{
                      row.companyName
                    }}</span>
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.industry }}
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    {{ row.authStatus }}
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    {{ row.status }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--action"
                    role="cell"
                  >
                    <div class="workbench-data-list__actions">
                      <el-button
                        link
                        type="success"
                        @click="handleEnterpriseAudit(row.enterpriseId, 'PASS')"
                        >通过</el-button
                      >
                      <el-button
                        link
                        type="danger"
                        @click="
                          handleEnterpriseAudit(row.enterpriseId, 'REJECT')
                        "
                        >驳回</el-button
                      >
                      <el-button
                        link
                        type="warning"
                        @click="changeEnterpriseStatus(row)"
                      >
                        {{ row.status === "ACTIVE" ? "禁用" : "启用" }}
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无数据"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>

        <section v-show="activeTab === 'job-audit'">
          <h2 class="workbench-panel__title">
            职位审核
            <span class="workbench-panel__count"
              >（{{ auditJobs.length }}）</span
            >
          </h2>
          <p class="workbench-panel__desc">待审职位与审核结果记录。</p>
          <el-card shadow="never" class="workbench-card">
            <div class="workbench-card-toolbar">
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
            <template v-if="auditJobs.length">
              <div
                class="workbench-data-list wb-list--admin-job-audit"
                role="table"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div class="workbench-data-list__th" role="columnheader">
                    职位
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    企业
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    状态
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    审核备注
                  </div>
                  <div
                    class="workbench-data-list__th workbench-data-list__cell--action"
                    role="columnheader"
                  >
                    操作
                  </div>
                </div>
                <div
                  v-for="row in auditJobs"
                  :key="row.jobId"
                  class="workbench-data-list__tr"
                  role="row"
                >
                  <div class="workbench-data-list__td" role="cell">
                    <span class="workbench-data-list__primary-title">{{
                      row.jobName
                    }}</span>
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.companyName }}
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    {{ row.status }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.auditRemark || "—" }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--action"
                    role="cell"
                  >
                    <div class="workbench-data-list__actions">
                      <el-button
                        link
                        type="success"
                        @click="
                          runSafely(() => handleJobAudit(row.jobId, 'PASS'))
                        "
                        >通过</el-button
                      >
                      <el-button
                        link
                        type="danger"
                        @click="
                          runSafely(() => handleJobAudit(row.jobId, 'REJECT'))
                        "
                        >驳回</el-button
                      >
                    </div>
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无数据"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>

        <section v-show="activeTab === 'notice'">
          <h2 class="workbench-panel__title">
            公告管理
            <span class="workbench-panel__count">（{{ notices.length }}）</span>
          </h2>
          <p class="workbench-panel__desc">发布公告并审核平台通知。</p>
          <el-row :gutter="16">
            <el-col :span="10">
              <el-card shadow="never" class="workbench-card">
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
                  <el-button type="primary" @click="runSafely(saveNotice)"
                    >保存公告</el-button
                  >
                </el-form>
              </el-card>
            </el-col>
            <el-col :span="14">
              <el-card shadow="never" class="workbench-card">
                <template #header>公告审核 / 列表</template>
                <template v-if="notices.length">
                  <div
                    class="workbench-data-list wb-list--admin-notices"
                    role="table"
                  >
                    <div
                      class="workbench-data-list__tr workbench-data-list__tr--head"
                      role="row"
                    >
                      <div class="workbench-data-list__th" role="columnheader">
                        标题
                      </div>
                      <div class="workbench-data-list__th" role="columnheader">
                        类型
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
                      v-for="row in notices"
                      :key="row.noticeId"
                      class="workbench-data-list__tr"
                      role="row"
                    >
                      <div class="workbench-data-list__td" role="cell">
                        <span class="workbench-data-list__primary-title">{{
                          row.title
                        }}</span>
                      </div>
                      <div
                        class="workbench-data-list__td workbench-data-list__cell--muted"
                        role="cell"
                      >
                        {{ row.type }}
                      </div>
                      <div class="workbench-data-list__td" role="cell">
                        {{ row.status }}
                      </div>
                      <div
                        class="workbench-data-list__td workbench-data-list__cell--action"
                        role="cell"
                      >
                        <div class="workbench-data-list__actions">
                          <el-button
                            link
                            type="success"
                            @click="
                              runSafely(() =>
                                handleNoticeAudit(row.noticeId, 'PASS')
                              )
                            "
                            >通过</el-button
                          >
                          <el-button
                            link
                            type="danger"
                            @click="
                              runSafely(() =>
                                handleNoticeAudit(row.noticeId, 'REJECT')
                              )
                            "
                            >驳回</el-button
                          >
                          <el-button
                            link
                            type="danger"
                            @click="removeNotice(row.noticeId)"
                            >删除</el-button
                          >
                        </div>
                      </div>
                    </div>
                  </div>
                </template>
                <el-empty
                  v-else
                  description="暂无数据"
                  class="workbench-data-list__empty"
                />
              </el-card>
            </el-col>
          </el-row>
        </section>

        <section v-show="activeTab === 'category'">
          <h2 class="workbench-panel__title">
            分类管理
            <span class="workbench-panel__count"
              >（{{ categories.length }}）</span
            >
          </h2>
          <p class="workbench-panel__desc">维护职位分类与展示顺序。</p>
          <el-card shadow="never" class="workbench-card">
            <div class="workbench-card-toolbar">
              <el-input v-model="categoryForm.name" placeholder="分类名称" />
              <el-input-number v-model="categoryForm.sort" :min="0" />
              <el-button type="primary" @click="runSafely(saveCategory)"
                >新增分类</el-button
              >
            </div>
            <template v-if="categories.length">
              <div
                class="workbench-data-list wb-list--admin-categories"
                role="table"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div class="workbench-data-list__th" role="columnheader">
                    ID
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    名称
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    排序
                  </div>
                  <div
                    class="workbench-data-list__th workbench-data-list__cell--action"
                    role="columnheader"
                  >
                    操作
                  </div>
                </div>
                <div
                  v-for="row in categories"
                  :key="row.categoryId"
                  class="workbench-data-list__tr"
                  role="row"
                >
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.categoryId }}
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    <span class="workbench-data-list__primary-title">{{
                      row.name
                    }}</span>
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    {{ row.sort }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--action"
                    role="cell"
                  >
                    <el-button
                      link
                      type="danger"
                      @click="removeCategory(row.categoryId)"
                      >删除</el-button
                    >
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无数据"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>

        <section v-show="activeTab === 'banner'">
          <h2 class="workbench-panel__title">
            轮播图管理
            <span class="workbench-panel__count">（{{ banners.length }}）</span>
          </h2>
          <p class="workbench-panel__desc">配置首页轮播素材与排序。</p>
          <el-row :gutter="16">
            <el-col :span="10">
              <el-card shadow="never" class="workbench-card">
                <template #header>新增轮播图</template>
                <el-form label-position="top" :model="bannerForm">
                  <el-form-item label="标题">
                    <el-input v-model="bannerForm.title" />
                  </el-form-item>
                  <el-form-item label="跳转链接">
                    <el-input
                      v-model="bannerForm.linkUrl"
                      placeholder="https://..."
                    />
                  </el-form-item>
                  <el-form-item label="排序">
                    <el-input-number v-model="bannerForm.sort" :min="0" />
                  </el-form-item>
                  <el-form-item label="状态">
                    <el-select v-model="bannerForm.status">
                      <el-option label="ONLINE" value="ONLINE" />
                      <el-option label="OFFLINE" value="OFFLINE" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="轮播图图片">
                    <input
                      type="file"
                      accept=".jpg,.jpeg,.png"
                      @change="onBannerFileChange"
                    />
                    <p v-if="bannerPreview" class="preview-text">
                      {{ bannerPreview }}
                    </p>
                  </el-form-item>
                  <el-button type="primary" @click="runSafely(saveBanner)"
                    >保存轮播图</el-button
                  >
                </el-form>
              </el-card>
            </el-col>
            <el-col :span="14">
              <el-card shadow="never" class="workbench-card">
                <template #header>轮播图列表</template>
                <template v-if="banners.length">
                  <div
                    class="workbench-data-list wb-list--admin-banners"
                    role="table"
                  >
                    <div
                      class="workbench-data-list__tr workbench-data-list__tr--head"
                      role="row"
                    >
                      <div class="workbench-data-list__th" role="columnheader">
                        标题
                      </div>
                      <div class="workbench-data-list__th" role="columnheader">
                        状态
                      </div>
                      <div class="workbench-data-list__th" role="columnheader">
                        排序
                      </div>
                      <div class="workbench-data-list__th" role="columnheader">
                        图片地址
                      </div>
                      <div
                        class="workbench-data-list__th workbench-data-list__cell--action"
                        role="columnheader"
                      >
                        操作
                      </div>
                    </div>
                    <div
                      v-for="row in banners"
                      :key="row.bannerId"
                      class="workbench-data-list__tr"
                      role="row"
                    >
                      <div class="workbench-data-list__td" role="cell">
                        <span class="workbench-data-list__primary-title">{{
                          row.title
                        }}</span>
                      </div>
                      <div class="workbench-data-list__td" role="cell">
                        {{ row.status }}
                      </div>
                      <div class="workbench-data-list__td" role="cell">
                        {{ row.sort }}
                      </div>
                      <div
                        class="workbench-data-list__td workbench-data-list__cell--muted admin-banner-url"
                        role="cell"
                        :title="String(row.imageUrl || '')"
                      >
                        {{ row.imageUrl }}
                      </div>
                      <div
                        class="workbench-data-list__td workbench-data-list__cell--action"
                        role="cell"
                      >
                        <el-button
                          link
                          type="danger"
                          @click="removeBanner(row.bannerId)"
                          >删除</el-button
                        >
                      </div>
                    </div>
                  </div>
                </template>
                <el-empty
                  v-else
                  description="暂无数据"
                  class="workbench-data-list__empty"
                />
              </el-card>
            </el-col>
          </el-row>
        </section>

        <section v-show="activeTab === 'role'">
          <h2 class="workbench-panel__title">
            角色权限
            <span class="workbench-panel__count">（{{ roles.length }}）</span>
          </h2>
          <p class="workbench-panel__desc">创建角色并绑定权限点。</p>
          <el-row :gutter="16">
            <el-col :span="10">
              <el-card shadow="never" class="workbench-card">
                <template #header>新增角色</template>
                <el-form label-position="top" :model="roleForm">
                  <el-form-item label="角色名称">
                    <el-input v-model="roleForm.roleName" />
                  </el-form-item>
                  <el-form-item label="角色编码">
                    <el-input v-model="roleForm.roleCode" />
                  </el-form-item>
                  <el-form-item label="权限">
                    <el-select
                      v-model="roleForm.permissionIds"
                      multiple
                      collapse-tags
                      collapse-tags-tooltip
                    >
                      <el-option
                        v-for="permission in permissions"
                        :key="permission.permissionId"
                        :label="permission.permissionName"
                        :value="permission.permissionId"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="备注">
                    <el-input
                      v-model="roleForm.remark"
                      type="textarea"
                      :rows="3"
                    />
                  </el-form-item>
                  <el-button type="primary" @click="runSafely(saveRole)"
                    >保存角色</el-button
                  >
                </el-form>
              </el-card>
            </el-col>
            <el-col :span="14">
              <el-card shadow="never" class="workbench-card">
                <template #header>角色列表</template>
                <template v-if="roles.length">
                  <div
                    class="workbench-data-list wb-list--admin-roles"
                    role="table"
                  >
                    <div
                      class="workbench-data-list__tr workbench-data-list__tr--head"
                      role="row"
                    >
                      <div class="workbench-data-list__th" role="columnheader">
                        角色名称
                      </div>
                      <div class="workbench-data-list__th" role="columnheader">
                        角色编码
                      </div>
                      <div class="workbench-data-list__th" role="columnheader">
                        权限
                      </div>
                      <div
                        class="workbench-data-list__th workbench-data-list__cell--action"
                        role="columnheader"
                      >
                        操作
                      </div>
                    </div>
                    <div
                      v-for="row in roles"
                      :key="row.roleId"
                      class="workbench-data-list__tr"
                      role="row"
                    >
                      <div class="workbench-data-list__td" role="cell">
                        <span class="workbench-data-list__primary-title">{{
                          row.roleName
                        }}</span>
                      </div>
                      <div
                        class="workbench-data-list__td workbench-data-list__cell--muted"
                        role="cell"
                      >
                        {{ row.roleCode }}
                      </div>
                      <div
                        class="workbench-data-list__td workbench-data-list__cell--muted"
                        role="cell"
                      >
                        {{ formatPermissionNames(row.permissionIds) }}
                      </div>
                      <div
                        class="workbench-data-list__td workbench-data-list__cell--action"
                        role="cell"
                      >
                        <el-button
                          link
                          type="danger"
                          @click="removeRole(row.roleId)"
                          >删除</el-button
                        >
                      </div>
                    </div>
                  </div>
                </template>
                <el-empty
                  v-else
                  description="暂无数据"
                  class="workbench-data-list__empty"
                />
              </el-card>
            </el-col>
          </el-row>
        </section>

        <section v-show="activeTab === 'message-template'">
          <h2 class="workbench-panel__title">消息模板</h2>
          <p class="workbench-panel__desc">
            维护站内信与邮件等通道的模板文案。
          </p>
          <div class="template-grid">
            <el-card
              v-for="template in messageTemplates"
              :key="template.templateId"
              shadow="never"
              class="workbench-card"
            >
              <template #header>{{ template.type }}</template>
              <el-form label-position="top">
                <el-form-item label="标题模板">
                  <el-input v-model="template.titleTemplate" />
                </el-form-item>
                <el-form-item label="内容模板">
                  <el-input
                    v-model="template.contentTemplate"
                    type="textarea"
                    :rows="4"
                  />
                </el-form-item>
                <el-form-item label="通道">
                  <el-select
                    v-model="template.channelList"
                    multiple
                    collapse-tags
                    collapse-tags-tooltip
                  >
                    <el-option label="INSITE" value="INSITE" />
                    <el-option label="EMAIL" value="EMAIL" />
                  </el-select>
                </el-form-item>
                <el-form-item label="状态">
                  <el-select v-model="template.enabled">
                    <el-option label="ACTIVE" value="ACTIVE" />
                    <el-option label="DISABLED" value="DISABLED" />
                  </el-select>
                </el-form-item>
                <el-button
                  type="primary"
                  @click="runSafely(() => saveMessageTemplate(template))"
                >
                  保存模板
                </el-button>
              </el-form>
            </el-card>
          </div>
        </section>

        <section v-show="activeTab === 'audit-log'">
          <h2 class="workbench-panel__title">
            审核日志
            <span class="workbench-panel__count"
              >（{{ auditLogs.length }}）</span
            >
          </h2>
          <p class="workbench-panel__desc">审核操作留痕，便于追溯。</p>
          <el-card shadow="never" class="workbench-card">
            <template v-if="auditLogs.length">
              <div
                class="workbench-data-list wb-list--admin-audit-log"
                role="table"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div class="workbench-data-list__th" role="columnheader">
                    业务类型
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    业务ID
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    结果
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    审核人
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    备注
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    时间
                  </div>
                </div>
                <div
                  v-for="row in auditLogs"
                  :key="row.auditId"
                  class="workbench-data-list__tr"
                  role="row"
                >
                  <div class="workbench-data-list__td" role="cell">
                    {{ row.bizType }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.bizId }}
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    {{ row.auditResult }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.auditorName }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.auditRemark }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.createTime }}
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无数据"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>

        <section v-show="activeTab === 'operation-log'">
          <h2 class="workbench-panel__title">
            操作日志
            <span class="workbench-panel__count"
              >（{{ operationLogs.length }}）</span
            >
          </h2>
          <p class="workbench-panel__desc">按操作人与动作筛选后台行为记录。</p>
          <el-card shadow="never" class="workbench-card">
            <div class="workbench-card-toolbar">
              <el-input
                v-model="operationLogQuery.userId"
                placeholder="操作人ID"
                clearable
              />
              <el-input
                v-model="operationLogQuery.action"
                placeholder="动作类型"
                clearable
              />
              <el-button type="primary" @click="loadOperationLogList"
                >查询</el-button
              >
            </div>
            <template v-if="operationLogs.length">
              <div
                class="workbench-data-list wb-list--admin-operation-log"
                role="table"
              >
                <div
                  class="workbench-data-list__tr workbench-data-list__tr--head"
                  role="row"
                >
                  <div class="workbench-data-list__th" role="columnheader">
                    用户ID
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    操作人
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    动作
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    资源
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    说明
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    IP
                  </div>
                  <div class="workbench-data-list__th" role="columnheader">
                    时间
                  </div>
                </div>
                <div
                  v-for="row in operationLogs"
                  :key="row.logId"
                  class="workbench-data-list__tr"
                  role="row"
                >
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.userId }}
                  </div>
                  <div class="workbench-data-list__td" role="cell">
                    {{ row.userName }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.action }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.resource }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.detail || "—" }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.ip || "—" }}
                  </div>
                  <div
                    class="workbench-data-list__td workbench-data-list__cell--muted"
                    role="cell"
                  >
                    {{ row.createTime }}
                  </div>
                </div>
              </div>
            </template>
            <el-empty
              v-else
              description="暂无数据"
              class="workbench-data-list__empty"
            />
          </el-card>
        </section>

        <section v-show="activeTab === 'statistics'">
          <h2 class="workbench-panel__title">统计分析</h2>
          <p class="workbench-panel__desc">
            平台核心指标与分布数据，支持导出报表。
          </p>
          <el-card shadow="never" class="workbench-card">
            <div class="workbench-card-toolbar">
              <el-button type="primary" @click="loadPlatformStats"
                >刷新统计</el-button
              >
              <el-button @click="downloadPlatformStatistics"
                >导出报表</el-button
              >
            </div>
            <div class="overview-hero">
              <div class="overview-card overview-card--blue">
                <div class="overview-card__body">
                  <p class="overview-card__value">
                    {{ platformStats.candidateCount }}
                  </p>
                  <p class="overview-card__label">个人用户数</p>
                </div>
                <div class="overview-card__badge overview-card__badge--blue" />
              </div>
              <div class="overview-card overview-card--amber">
                <div class="overview-card__body">
                  <p class="overview-card__value">
                    {{ platformStats.enterpriseCount }}
                  </p>
                  <p class="overview-card__label">企业用户数</p>
                </div>
                <div class="overview-card__badge overview-card__badge--amber" />
              </div>
            </div>
            <div class="overview-hero">
              <div class="overview-card overview-card--blue">
                <div class="overview-card__body">
                  <p class="overview-card__value">
                    {{ platformStats.jobCount }}
                  </p>
                  <p class="overview-card__label">职位总数</p>
                </div>
                <div class="overview-card__badge overview-card__badge--blue" />
              </div>
              <div class="overview-card overview-card--amber">
                <div class="overview-card__body">
                  <p class="overview-card__value">
                    {{ platformStats.applyCount }}
                  </p>
                  <p class="overview-card__label">投递总数</p>
                </div>
                <div class="overview-card__badge overview-card__badge--amber" />
              </div>
            </div>
            <div class="overview-substats">
              <div class="overview-substat">
                <span class="overview-substat__value">{{
                  platformStats.interviewCount
                }}</span>
                <span class="overview-substat__label">面试邀约数</span>
              </div>
            </div>
            <el-row :gutter="16" class="section-gap">
              <el-col :span="12">
                <el-card shadow="never" class="workbench-card">
                  <template #header>区域分布</template>
                  <template
                    v-if="(platformStats.employmentByArea || []).length"
                  >
                    <div
                      class="workbench-data-list wb-list--admin-stats-2"
                      role="table"
                    >
                      <div
                        class="workbench-data-list__tr workbench-data-list__tr--head"
                        role="row"
                      >
                        <div
                          class="workbench-data-list__th"
                          role="columnheader"
                        >
                          区域
                        </div>
                        <div
                          class="workbench-data-list__th"
                          role="columnheader"
                        >
                          人数
                        </div>
                      </div>
                      <div
                        v-for="(r, idx) in platformStats.employmentByArea || []"
                        :key="idx"
                        class="workbench-data-list__tr"
                        role="row"
                      >
                        <div class="workbench-data-list__td" role="cell">
                          {{ r.area }}
                        </div>
                        <div class="workbench-data-list__td" role="cell">
                          {{ r.count }}
                        </div>
                      </div>
                    </div>
                  </template>
                  <el-empty
                    v-else
                    description="暂无数据"
                    class="workbench-data-list__empty"
                  />
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card shadow="never" class="workbench-card">
                  <template #header>行业分布</template>
                  <template
                    v-if="(platformStats.industryDistribution || []).length"
                  >
                    <div
                      class="workbench-data-list wb-list--admin-stats-3"
                      role="table"
                    >
                      <div
                        class="workbench-data-list__tr workbench-data-list__tr--head"
                        role="row"
                      >
                        <div
                          class="workbench-data-list__th"
                          role="columnheader"
                        >
                          行业
                        </div>
                        <div
                          class="workbench-data-list__th"
                          role="columnheader"
                        >
                          职位数
                        </div>
                        <div
                          class="workbench-data-list__th"
                          role="columnheader"
                        >
                          投递数
                        </div>
                      </div>
                      <div
                        v-for="(r, idx) in platformStats.industryDistribution ||
                        []"
                        :key="idx"
                        class="workbench-data-list__tr"
                        role="row"
                      >
                        <div class="workbench-data-list__td" role="cell">
                          {{ r.industry }}
                        </div>
                        <div class="workbench-data-list__td" role="cell">
                          {{ r.jobCount }}
                        </div>
                        <div class="workbench-data-list__td" role="cell">
                          {{ r.applyCount }}
                        </div>
                      </div>
                    </div>
                  </template>
                  <el-empty
                    v-else
                    description="暂无数据"
                    class="workbench-data-list__empty"
                  />
                </el-card>
              </el-col>
            </el-row>
          </el-card>
        </section>
      </div>
    </div>

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
        <el-divider>简历治理</el-divider>
        <template v-if="(candidateDetail.resumes || []).length">
          <div
            class="workbench-data-list wb-list--admin-resume-gov"
            role="table"
          >
            <div
              class="workbench-data-list__tr workbench-data-list__tr--head"
              role="row"
            >
              <div class="workbench-data-list__th" role="columnheader">
                简历ID
              </div>
              <div class="workbench-data-list__th" role="columnheader">
                标题
              </div>
              <div class="workbench-data-list__th" role="columnheader">
                隐私
              </div>
              <div class="workbench-data-list__th" role="columnheader">
                违规检测
              </div>
              <div
                class="workbench-data-list__th workbench-data-list__cell--action"
                role="columnheader"
              >
                操作
              </div>
            </div>
            <div
              v-for="row in candidateDetail.resumes || []"
              :key="row.resumeId"
              class="workbench-data-list__tr"
              role="row"
            >
              <div
                class="workbench-data-list__td workbench-data-list__cell--muted"
                role="cell"
              >
                {{ row.resumeId }}
              </div>
              <div class="workbench-data-list__td" role="cell">
                <span class="workbench-data-list__primary-title">{{
                  row.title
                }}</span>
              </div>
              <div class="workbench-data-list__td" role="cell">
                {{ row.privacy }}
              </div>
              <div class="workbench-data-list__td" role="cell">
                <span v-if="row.violationDetected" class="danger-text">
                  命中:
                  {{ (row.violationKeywords || []).join(" / ") }}
                </span>
                <span v-else class="workbench-data-list__cell--muted"
                  >未发现</span
                >
              </div>
              <div
                class="workbench-data-list__td workbench-data-list__cell--action"
                role="cell"
              >
                <div class="workbench-data-list__actions">
                  <el-button
                    link
                    type="primary"
                    @click="
                      runSafely(() => detectResumeViolation(row.resumeId))
                    "
                  >
                    检测
                  </el-button>
                  <el-button
                    link
                    type="danger"
                    @click="runSafely(() => cleanResumeViolation(row.resumeId))"
                  >
                    清理
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </template>
        <el-empty v-else description="暂无简历" />
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import WorkbenchSidebar from "@/components/workbench/WorkbenchSidebar.vue";
import {
  auditEnterprise,
  auditJob,
  auditNotice,
  createBannerAdmin,
  createCategoryAdmin,
  createRoleAdmin,
  createSystemNotice,
  downloadGeneratedFile,
  deleteBannerAdmin,
  deleteCategoryAdmin,
  deleteRoleAdmin,
  deleteSystemNotice,
  exportStatistics,
  getPlatformOverviewStatistics,
  getCandidateDetail,
  listBannersAdmin,
  listAuditJobs,
  listAuditLogs,
  listAuditNotices,
  listCandidates,
  listCategoriesAdmin,
  listEnterprisesAdmin,
  listMessageTemplatesAdmin,
  listOperationLogs,
  listPermissionsAdmin,
  listRolesAdmin,
  moderateResumeAdmin,
  updateMessageTemplateAdmin,
  updateCandidateStatus,
  updateEnterpriseStatusAdmin,
  uploadCommonFile,
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

interface BannerRecord {
  bannerId: number;
  title: string;
  imageUrl: string;
  status: string;
  sort: number;
}

interface PermissionRecord {
  permissionId: number;
  permissionName: string;
}

interface RoleRecord {
  roleId: number;
  roleName: string;
  roleCode: string;
  permissionIds: number[];
}

interface MessageTemplateRecord {
  templateId: number;
  type: string;
  titleTemplate: string;
  contentTemplate: string;
  channels: string;
  channelList: string[];
  enabled: string;
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

interface OperationLogRecord {
  logId: number;
  userId: number;
  userName: string;
  action: string;
  resource: string;
  detail?: string;
  ip?: string;
  createTime: string;
}

type AdminTab =
  | "candidate"
  | "enterprise"
  | "job-audit"
  | "notice"
  | "category"
  | "banner"
  | "role"
  | "message-template"
  | "audit-log"
  | "operation-log"
  | "statistics";

const tabItems: { key: AdminTab; label: string }[] = [
  { key: "candidate", label: "求职者管理" },
  { key: "enterprise", label: "企业管理" },
  { key: "job-audit", label: "职位审核" },
  { key: "notice", label: "公告管理" },
  { key: "category", label: "分类管理" },
  { key: "banner", label: "轮播图管理" },
  { key: "role", label: "角色权限" },
  { key: "message-template", label: "消息模板" },
  { key: "audit-log", label: "审核日志" },
  { key: "operation-log", label: "操作日志" },
  { key: "statistics", label: "统计分析" },
];

const router = useRouter();
const route = useRoute();
const activeTab = ref<AdminTab>("candidate");

function isAdminTab(s: string): s is AdminTab {
  return tabItems.some((t) => t.key === s);
}

function selectTab(key: AdminTab) {
  activeTab.value = key;
  router.replace({ path: "/admin", query: { tab: key } });
}

function onWorkbenchSelect(key: string) {
  if (isAdminTab(key)) {
    selectTab(key);
  }
}

watch(
  () => route.query.tab,
  (q) => {
    if (typeof q === "string" && isAdminTab(q)) {
      activeTab.value = q;
    }
  },
  { immediate: true }
);
const candidates = ref<CandidateRecord[]>([]);
const enterprises = ref<EnterpriseRecord[]>([]);
const auditJobs = ref<JobAuditRecord[]>([]);
const notices = ref<NoticeRecord[]>([]);
const categories = ref<CategoryRecord[]>([]);
const banners = ref<BannerRecord[]>([]);
const permissions = ref<PermissionRecord[]>([]);
const roles = ref<RoleRecord[]>([]);
const messageTemplates = ref<MessageTemplateRecord[]>([]);
const auditLogs = ref<AuditLogRecord[]>([]);
const operationLogs = ref<OperationLogRecord[]>([]);
const candidateDetailVisible = ref(false);
const candidateDetail = ref<Record<string, unknown> | null>(null);
const bannerPreview = ref("");
const platformStats = reactive({
  candidateCount: 0,
  enterpriseCount: 0,
  jobCount: 0,
  applyCount: 0,
  interviewCount: 0,
  employmentByArea: [] as Array<Record<string, unknown>>,
  industryDistribution: [] as Array<Record<string, unknown>>,
});

async function runSafely(task: () => Promise<void>) {
  try {
    await task();
  } catch (error) {
    ElMessage.error((error as { message?: string })?.message || "操作失败");
  }
}

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

const bannerForm = reactive({
  title: "",
  imageFileId: "",
  linkUrl: "",
  sort: 0,
  status: "ONLINE",
});

const roleForm = reactive({
  roleName: "",
  roleCode: "",
  permissionIds: [] as number[],
  remark: "",
  status: "ACTIVE",
});
const operationLogQuery = reactive({
  userId: "",
  action: "",
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

async function loadBanners() {
  const data = await listBannersAdmin();
  banners.value = data.list || [];
}

async function onBannerFileChange(event: Event) {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) {
    return;
  }
  const data = await uploadCommonFile(file, "BANNER");
  bannerForm.imageFileId = data.fileId;
  bannerPreview.value = data.fileUrl;
  ElMessage.success("轮播图图片上传成功");
}

async function saveBanner() {
  if (!bannerForm.title || !bannerForm.imageFileId) {
    ElMessage.warning("请填写标题并上传轮播图图片");
    return;
  }
  await createBannerAdmin({
    ...bannerForm,
    startTime: "2026-01-01 00:00:00",
    endTime: "2027-01-01 00:00:00",
  });
  bannerForm.title = "";
  bannerForm.imageFileId = "";
  bannerForm.linkUrl = "";
  bannerForm.sort = 0;
  bannerForm.status = "ONLINE";
  bannerPreview.value = "";
  ElMessage.success("轮播图已保存");
  await loadBanners();
}

async function removeBanner(bannerId: number) {
  await deleteBannerAdmin(bannerId);
  ElMessage.success("轮播图已删除");
  await loadBanners();
}

async function loadPermissions() {
  const data = await listPermissionsAdmin();
  permissions.value = data.list || [];
}

async function loadRoles() {
  const data = await listRolesAdmin();
  roles.value = data.list || [];
}

function formatPermissionNames(permissionIds: number[] = []) {
  return permissions.value
    .filter((item) => permissionIds.includes(item.permissionId))
    .map((item) => item.permissionName)
    .join(" / ");
}

async function saveRole() {
  if (
    !roleForm.roleName ||
    !roleForm.roleCode ||
    roleForm.permissionIds.length === 0
  ) {
    ElMessage.warning("请填写角色名称、编码并选择权限");
    return;
  }
  await createRoleAdmin(roleForm);
  roleForm.roleName = "";
  roleForm.roleCode = "";
  roleForm.permissionIds = [];
  roleForm.remark = "";
  roleForm.status = "ACTIVE";
  ElMessage.success("角色已保存");
  await loadRoles();
}

async function removeRole(roleId: number) {
  await deleteRoleAdmin(roleId);
  ElMessage.success("角色已删除");
  await loadRoles();
}

async function loadMessageTemplates() {
  const data = await listMessageTemplatesAdmin();
  messageTemplates.value = (data.list || []).map(
    (item: MessageTemplateRecord) => ({
      ...item,
      channelList: item.channels ? item.channels.split(",") : [],
    })
  );
}

async function saveMessageTemplate(template: MessageTemplateRecord) {
  await updateMessageTemplateAdmin(template.templateId, {
    titleTemplate: template.titleTemplate,
    contentTemplate: template.contentTemplate,
    channels: template.channelList,
    enabled: template.enabled,
  });
  ElMessage.success("消息模板已保存");
  await loadMessageTemplates();
}

async function loadAuditLogList() {
  const data = await listAuditLogs({ pageNum: 1, pageSize: 20 });
  auditLogs.value = data.list || [];
}

async function loadOperationLogList() {
  const data = await listOperationLogs({
    userId: operationLogQuery.userId || undefined,
    action: operationLogQuery.action || undefined,
    pageNum: 1,
    pageSize: 20,
  });
  operationLogs.value = data.list || [];
}

async function detectResumeViolation(resumeId: number) {
  const data = await moderateResumeAdmin(resumeId, {
    action: "DETECT",
    reason: "管理员手动检测",
  });
  ElMessage.info(
    data.violationDetected
      ? `检测到违规关键词: ${(data.keywords || []).join(" / ")}`
      : "未发现违规内容"
  );
  if (candidateDetail.value?.userId) {
    await openCandidateDetail(candidateDetail.value.userId as number);
  }
}

async function cleanResumeViolation(resumeId: number) {
  const data = await moderateResumeAdmin(resumeId, {
    action: "CLEAN",
    reason: "管理员手动清理",
  });
  ElMessage.success(
    data.violationDetected
      ? "违规简历已清理并转为私密"
      : "简历已复核，未发现违规"
  );
  if (candidateDetail.value?.userId) {
    await openCandidateDetail(candidateDetail.value.userId as number);
  }
  await loadOperationLogList();
  await loadAuditLogList();
}

async function loadPlatformStats() {
  const data = await getPlatformOverviewStatistics();
  platformStats.candidateCount = data.candidateCount || 0;
  platformStats.enterpriseCount = data.enterpriseCount || 0;
  platformStats.jobCount = data.jobCount || 0;
  platformStats.applyCount = data.applyCount || 0;
  platformStats.interviewCount = data.interviewCount || 0;
  platformStats.employmentByArea = data.employmentByArea || [];
  platformStats.industryDistribution = data.industryDistribution || [];
}

async function downloadPlatformStatistics() {
  const data = await exportStatistics({
    type: "PLATFORM",
    startDate: "2026-01-01",
    endDate: "2026-12-31",
    format: "xlsx",
  });
  await downloadGeneratedFile(data.fileId, data.fileName);
}

function logout() {
  clearAuth();
  router.replace("/");
}

watch(
  activeTab,
  async (tab) => {
    if (tab === "candidate") await loadCandidates();
    if (tab === "enterprise") await loadEnterprises();
    if (tab === "job-audit") await loadAuditJobs();
    if (tab === "notice") await loadNotices();
    if (tab === "category") await loadCategories();
    if (tab === "banner") await loadBanners();
    if (tab === "role") {
      await Promise.all([loadPermissions(), loadRoles()]);
    }
    if (tab === "message-template") await loadMessageTemplates();
    if (tab === "audit-log") await loadAuditLogList();
    if (tab === "operation-log") await loadOperationLogList();
    if (tab === "statistics") await loadPlatformStats();
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
      loadBanners(),
      loadPermissions(),
      loadRoles(),
      loadMessageTemplates(),
      loadAuditLogList(),
      loadOperationLogList(),
      loadPlatformStats(),
    ]);
  } catch (error) {
    ElMessageBox.alert("请确认管理员账号和后端服务已正常启动。", "初始化失败");
  }
});
</script>

<style scoped>
.section-gap {
  margin-top: 16px;
}

.admin-banner-url {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.template-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.preview-text {
  margin: 8px 0 0;
  color: #6b7280;
  word-break: break-all;
}

.danger-text {
  color: #dc2626;
}
</style>
