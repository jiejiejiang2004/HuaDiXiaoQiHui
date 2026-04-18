<template>
  <el-dialog
    v-model="visible"
    width="480px"
    align-center
    append-to-body
    class="auth-el-dialog"
    :show-close="true"
    @closed="onDialogClosed"
  >
    <div class="auth-modal">
      <h2 class="auth-modal__title">校企慧</h2>
      <p class="auth-modal__subtitle">登录你的账号</p>

      <div class="auth-modal__rolebar" role="tablist">
        <button
          type="button"
          class="auth-modal__rolebar-item"
          :class="{
            'auth-modal__rolebar-item--active': activeTab === 'candidate',
          }"
          role="tab"
          :aria-selected="activeTab === 'candidate'"
          @click="activeTab = 'candidate'"
        >
          求职者
        </button>
        <button
          type="button"
          class="auth-modal__rolebar-item"
          :class="{
            'auth-modal__rolebar-item--active': activeTab === 'enterprise',
          }"
          role="tab"
          :aria-selected="activeTab === 'enterprise'"
          @click="activeTab = 'enterprise'"
        >
          企业
        </button>
        <button
          type="button"
          class="auth-modal__rolebar-item"
          :class="{ 'auth-modal__rolebar-item--active': activeTab === 'admin' }"
          role="tab"
          :aria-selected="activeTab === 'admin'"
          @click="activeTab = 'admin'"
        >
          管理员
        </button>
      </div>
      <div class="auth-modal__role-underline" aria-hidden="true">
        <span
          class="auth-modal__role-seg"
          :class="{ 'auth-modal__role-seg--active': activeTab === 'candidate' }"
        />
        <span
          class="auth-modal__role-seg"
          :class="{
            'auth-modal__role-seg--active': activeTab === 'enterprise',
          }"
        />
        <span
          class="auth-modal__role-seg"
          :class="{ 'auth-modal__role-seg--active': activeTab === 'admin' }"
        />
      </div>

      <form class="form" @submit.prevent>
        <!-- 求职者 登录 -->
        <template v-if="activeTab === 'candidate'">
          <div class="flex-column">
            <label>手机号</label>
          </div>
          <div class="inputForm">
            <svg
              class="auth-modal__ico"
              height="20"
              viewBox="0 0 24 24"
              width="20"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
            >
              <path
                fill="currentColor"
                d="M6.62 10.79a15.05 15.05 0 006.59 6.59l2.2-2.2a1 1 0 011.01-.24c1.12.37 2.33.57 3.58.57a1 1 0 011 1V20a1 1 0 01-1 1C7.61 21 3 16.39 3 10.5a1 1 0 011-1h3.5a1 1 0 011 1c0 1.25.2 2.46.57 3.58a1 1 0 01-.24 1.01l-2.2 2.2z"
              />
            </svg>
            <input
              v-model="candidateForm.mobile"
              type="text"
              class="input"
              placeholder="请输入手机号"
              autocomplete="tel"
            />
          </div>

          <div class="flex-column">
            <label>邮箱</label>
          </div>
          <div class="inputForm">
            <svg
              height="20"
              viewBox="0 0 32 32"
              width="20"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
            >
              <path
                fill="currentColor"
                d="m30.853 13.87a15 15 0 0 0 -29.729 4.082 15.1 15.1 0 0 0 12.876 12.918 15.6 15.6 0 0 0 2.016.13 14.85 14.85 0 0 0 7.715-2.145 1 1 0 1 0 -1.031-1.711 13.007 13.007 0 1 1 5.458-6.529 2.149 2.149 0 0 1 -4.158-.759v-10.856a1 1 0 0 0 -2 0v1.726a8 8 0 1 0 .2 10.325 4.135 4.135 0 0 0 7.83.274 15.2 15.2 0 0 0 .823-7.455zm-14.853 8.13a6 6 0 1 1 6-6 6.006 6.006 0 0 1 -6 6z"
              />
            </svg>
            <input
              v-model="candidateForm.email"
              type="email"
              class="input"
              placeholder="请输入邮箱"
              autocomplete="email"
            />
          </div>

          <template v-if="candidateLoginMode === 'password'">
            <div class="flex-column">
              <label>密码</label>
            </div>
            <div class="inputForm">
              <svg
                height="20"
                viewBox="-64 0 512 512"
                width="20"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  fill="currentColor"
                  d="m336 512h-288c-26.453125 0-48-21.523438-48-48v-224c0-26.476562 21.546875-48 48-48h288c26.453125 0 48 21.523438 48 48v224c0 26.476562-21.546875 48-48 48zm-288-288c-8.8125 0-16 7.167969-16 16v224c0 8.832031 7.1875 16 16 16h288c8.8125 0 16-7.167969 16-16v-224c0-8.832031-7.1875-16-16-16zm0 0"
                />
                <path
                  fill="currentColor"
                  d="m304 224c-8.832031 0-16-7.167969-16-16v-80c0-52.929688-43.070312-96-96-96s-96 43.070312-96 96v80c0 8.832031-7.167969 16-16 16s-16-7.167969-16-16v-80c0-70.59375 57.40625-128 128-128s128 57.40625 128 128v80c0 8.832031-7.167969 16-16 16zm0 0"
                />
              </svg>
              <input
                v-model="candidateForm.password"
                :type="showCandidatePassword ? 'text' : 'password'"
                class="input"
                placeholder="请输入密码"
                autocomplete="current-password"
              />
              <button
                type="button"
                class="auth-modal__eye"
                tabindex="-1"
                aria-label="显示或隐藏密码"
                @click="showCandidatePassword = !showCandidatePassword"
              >
                <svg
                  viewBox="0 0 576 512"
                  height="1em"
                  xmlns="http://www.w3.org/2000/svg"
                  aria-hidden="true"
                >
                  <path
                    fill="currentColor"
                    d="M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z"
                  />
                </svg>
              </button>
            </div>
            <div class="auth-modal__field-foot auth-modal__field-foot--end">
              <button
                type="button"
                class="auth-modal__text-btn"
                @click="candidateLoginMode = 'email'"
              >
                忘记密码
              </button>
            </div>
          </template>

          <template v-else>
            <div class="flex-column">
              <label>登录验证码</label>
            </div>
            <div class="inputForm inputForm--with-action">
              <input
                v-model="candidateForm.emailCode"
                type="text"
                class="input input--grow"
                placeholder="邮箱验证码"
              />
              <button
                type="button"
                class="auth-modal__mini"
                @click="runSafely(() => sendCode(candidateForm.email, 'LOGIN'))"
              >
                获取验证码
              </button>
            </div>
            <div class="auth-modal__field-foot auth-modal__field-foot--split">
              <button
                type="button"
                class="auth-modal__text-btn"
                @click="candidateLoginMode = 'password'"
              >
                使用密码登录
              </button>
              <button
                type="button"
                class="auth-modal__text-btn"
                @click="openResetDialog('candidate')"
              >
                找回密码
              </button>
            </div>
          </template>

          <button
            type="button"
            class="button-submit"
            :disabled="candidateLoading"
            @click="runSafely(onCandidateLoginSubmit)"
          >
            {{ candidateLoginMode === "password" ? "登录" : "验证码登录" }}
          </button>

          <p class="auth-modal__register-hint">
            还未注册？
            <button
              type="button"
              class="auth-modal__text-btn auth-modal__text-btn--inline"
              @click="openRegisterDialog"
            >
              现在注册
            </button>
          </p>
        </template>

        <!-- 企业 登录 -->
        <template v-else-if="activeTab === 'enterprise'">
          <div class="flex-column">
            <label>手机号</label>
          </div>
          <div class="inputForm">
            <svg
              class="auth-modal__ico"
              height="20"
              viewBox="0 0 24 24"
              width="20"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
            >
              <path
                fill="currentColor"
                d="M6.62 10.79a15.05 15.05 0 006.59 6.59l2.2-2.2a1 1 0 011.01-.24c1.12.37 2.33.57 3.58.57a1 1 0 011 1V20a1 1 0 01-1 1C7.61 21 3 16.39 3 10.5a1 1 0 011-1h3.5a1 1 0 011 1c0 1.25.2 2.46.57 3.58a1 1 0 01-.24 1.01l-2.2 2.2z"
              />
            </svg>
            <input
              v-model="enterpriseForm.mobile"
              type="text"
              class="input"
              placeholder="联系人手机号"
              autocomplete="tel"
            />
          </div>
          <div class="flex-column">
            <label>邮箱</label>
          </div>
          <div class="inputForm">
            <svg
              height="20"
              viewBox="0 0 32 32"
              width="20"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
            >
              <path
                fill="currentColor"
                d="m30.853 13.87a15 15 0 0 0 -29.729 4.082 15.1 15.1 0 0 0 12.876 12.918 15.6 15.6 0 0 0 2.016.13 14.85 14.85 0 0 0 7.715-2.145 1 1 0 1 0 -1.031-1.711 13.007 13.007 0 1 1 5.458-6.529 2.149 2.149 0 0 1 -4.158-.759v-10.856a1 1 0 0 0 -2 0v1.726a8 8 0 1 0 .2 10.325 4.135 4.135 0 0 0 7.83.274 15.2 15.2 0 0 0 .823-7.455zm-14.853 8.13a6 6 0 1 1 6-6 6.006 6.006 0 0 1 -6 6z"
              />
            </svg>
            <input
              v-model="enterpriseForm.email"
              type="email"
              class="input"
              placeholder="企业邮箱"
              autocomplete="email"
            />
          </div>

          <template v-if="enterpriseLoginMode === 'password'">
            <div class="flex-column">
              <label>密码</label>
            </div>
            <div class="inputForm">
              <svg
                height="20"
                viewBox="-64 0 512 512"
                width="20"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  fill="currentColor"
                  d="m336 512h-288c-26.453125 0-48-21.523438-48-48v-224c0-26.476562 21.546875-48 48-48h288c26.453125 0 48 21.523438 48 48v224c0 26.476562-21.546875 48-48 48zm-288-288c-8.8125 0-16 7.167969-16 16v224c0 8.832031 7.1875 16 16 16h288c8.8125 0 16-7.167969 16-16v-224c0-8.832031-7.1875-16-16-16zm0 0"
                />
                <path
                  fill="currentColor"
                  d="m304 224c-8.832031 0-16-7.167969-16-16v-80c0-52.929688-43.070312-96-96-96s-96 43.070312-96 96v80c0 8.832031-7.167969 16-16 16s-16-7.167969-16-16v-80c0-70.59375 57.40625-128 128-128s128 57.40625 128 128v80c0 8.832031-7.167969 16-16 16zm0 0"
                />
              </svg>
              <input
                v-model="enterpriseForm.password"
                :type="showEnterprisePassword ? 'text' : 'password'"
                class="input"
                placeholder="密码"
                autocomplete="current-password"
              />
              <button
                type="button"
                class="auth-modal__eye"
                tabindex="-1"
                aria-label="显示或隐藏密码"
                @click="showEnterprisePassword = !showEnterprisePassword"
              >
                <svg
                  viewBox="0 0 576 512"
                  height="1em"
                  xmlns="http://www.w3.org/2000/svg"
                  aria-hidden="true"
                >
                  <path
                    fill="currentColor"
                    d="M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z"
                  />
                </svg>
              </button>
            </div>
            <div class="auth-modal__field-foot auth-modal__field-foot--end">
              <button
                type="button"
                class="auth-modal__text-btn"
                @click="enterpriseLoginMode = 'email'"
              >
                忘记密码
              </button>
            </div>
          </template>

          <template v-else>
            <div class="flex-column">
              <label>登录验证码</label>
            </div>
            <div class="inputForm inputForm--with-action">
              <input
                v-model="enterpriseForm.emailCode"
                type="text"
                class="input input--grow"
                placeholder="邮箱验证码"
              />
              <button
                type="button"
                class="auth-modal__mini"
                @click="
                  runSafely(() => sendCode(enterpriseForm.email, 'LOGIN'))
                "
              >
                获取验证码
              </button>
            </div>
            <div class="auth-modal__field-foot auth-modal__field-foot--split">
              <button
                type="button"
                class="auth-modal__text-btn"
                @click="enterpriseLoginMode = 'password'"
              >
                使用密码登录
              </button>
              <button
                type="button"
                class="auth-modal__text-btn"
                @click="openResetDialog('enterprise')"
              >
                找回密码
              </button>
            </div>
          </template>

          <button
            type="button"
            class="button-submit"
            :disabled="enterpriseLoading"
            @click="runSafely(onEnterpriseLoginSubmit)"
          >
            {{ enterpriseLoginMode === "password" ? "登录" : "验证码登录" }}
          </button>

          <p class="auth-modal__register-hint">
            还未注册？
            <button
              type="button"
              class="auth-modal__text-btn auth-modal__text-btn--inline"
              @click="openRegisterDialog"
            >
              现在注册
            </button>
          </p>
        </template>

        <!-- 管理员 -->
        <template v-else>
          <div class="flex-column">
            <label>手机号</label>
          </div>
          <div class="inputForm">
            <svg
              class="auth-modal__ico"
              height="20"
              viewBox="0 0 24 24"
              width="20"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
            >
              <path
                fill="currentColor"
                d="M6.62 10.79a15.05 15.05 0 006.59 6.59l2.2-2.2a1 1 0 011.01-.24c1.12.37 2.33.57 3.58.57a1 1 0 011 1V20a1 1 0 01-1 1C7.61 21 3 16.39 3 10.5a1 1 0 011-1h3.5a1 1 0 011 1c0 1.25.2 2.46.57 3.58a1 1 0 01-.24 1.01l-2.2 2.2z"
              />
            </svg>
            <input
              v-model="adminForm.mobile"
              type="text"
              class="input"
              placeholder="管理员手机号"
              autocomplete="tel"
            />
          </div>
          <div class="flex-column">
            <label>密码</label>
          </div>
          <div class="inputForm">
            <svg
              height="20"
              viewBox="-64 0 512 512"
              width="20"
              xmlns="http://www.w3.org/2000/svg"
              aria-hidden="true"
            >
              <path
                fill="currentColor"
                d="m336 512h-288c-26.453125 0-48-21.523438-48-48v-224c0-26.476562 21.546875-48 48-48h288c26.453125 0 48 21.523438 48 48v224c0 26.476562-21.546875 48-48 48zm-288-288c-8.8125 0-16 7.167969-16 16v224c0 8.832031 7.1875 16 16 16h288c8.8125 0 16-7.167969 16-16v-224c0-8.832031-7.1875-16-16-16zm0 0"
              />
              <path
                fill="currentColor"
                d="m304 224c-8.832031 0-16-7.167969-16-16v-80c0-52.929688-43.070312-96-96-96s-96 43.070312-96 96v80c0 8.832031-7.167969 16-16 16s-16-7.167969-16-16v-80c0-70.59375 57.40625-128 128-128s128 57.40625 128 128v80c0 8.832031-7.167969 16-16 16zm0 0"
              />
            </svg>
            <input
              v-model="adminForm.password"
              :type="showAdminPassword ? 'text' : 'password'"
              class="input"
              placeholder="密码"
              autocomplete="current-password"
            />
            <button
              type="button"
              class="auth-modal__eye"
              tabindex="-1"
              aria-label="显示或隐藏密码"
              @click="showAdminPassword = !showAdminPassword"
            >
              <svg
                viewBox="0 0 576 512"
                height="1em"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  fill="currentColor"
                  d="M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z"
                />
              </svg>
            </button>
          </div>
          <button
            type="button"
            class="button-submit"
            :disabled="adminLoading"
            @click="runSafely(handleAdminLogin)"
          >
            进入后台
          </button>
        </template>

        <div v-if="lastDebugCode" class="auth-modal__debug" role="status">
          调试验证码：{{ lastDebugCode }}
        </div>
      </form>
    </div>

    <!-- 注册弹窗 -->
    <el-dialog
      v-model="registerVisible"
      width="480px"
      align-center
      append-to-body
      class="auth-el-dialog auth-el-dialog--nested"
      :show-close="true"
      @closed="onRegisterDialogClosed"
    >
      <template #header>
        <div class="auth-modal__reg-head">
          <span class="auth-modal__reg-title">注册账号</span>
          <button
            type="button"
            class="auth-modal__text-btn"
            @click="registerVisible = false"
          >
            返回登录
          </button>
        </div>
      </template>
      <div class="auth-modal">
        <form class="form" @submit.prevent>
          <template v-if="registerTab === 'candidate'">
            <div class="flex-column">
              <label>手机号</label>
            </div>
            <div class="inputForm">
              <svg
                class="auth-modal__ico"
                height="20"
                viewBox="0 0 24 24"
                width="20"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  fill="currentColor"
                  d="M6.62 10.79a15.05 15.05 0 006.59 6.59l2.2-2.2a1 1 0 011.01-.24c1.12.37 2.33.57 3.58.57a1 1 0 011 1V20a1 1 0 01-1 1C7.61 21 3 16.39 3 10.5a1 1 0 011-1h3.5a1 1 0 011 1c0 1.25.2 2.46.57 3.58a1 1 0 01-.24 1.01l-2.2 2.2z"
                />
              </svg>
              <input
                v-model="candidateForm.mobile"
                type="text"
                class="input"
                placeholder="请输入手机号"
                autocomplete="tel"
              />
            </div>
            <div class="flex-column">
              <label>邮箱</label>
            </div>
            <div class="inputForm">
              <svg
                height="20"
                viewBox="0 0 32 32"
                width="20"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  fill="currentColor"
                  d="m30.853 13.87a15 15 0 0 0 -29.729 4.082 15.1 15.1 0 0 0 12.876 12.918 15.6 15.6 0 0 0 2.016.13 14.85 14.85 0 0 0 7.715-2.145 1 1 0 1 0 -1.031-1.711 13.007 13.007 0 1 1 5.458-6.529 2.149 2.149 0 0 1 -4.158-.759v-10.856a1 1 0 0 0 -2 0v1.726a8 8 0 1 0 .2 10.325 4.135 4.135 0 0 0 7.83.274 15.2 15.2 0 0 0 .823-7.455zm-14.853 8.13a6 6 0 1 1 6-6 6.006 6.006 0 0 1 -6 6z"
                />
              </svg>
              <input
                v-model="candidateForm.email"
                type="email"
                class="input"
                placeholder="请输入邮箱"
                autocomplete="email"
              />
            </div>
            <div class="flex-column">
              <label>密码</label>
            </div>
            <div class="inputForm">
              <svg
                height="20"
                viewBox="-64 0 512 512"
                width="20"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  fill="currentColor"
                  d="m336 512h-288c-26.453125 0-48-21.523438-48-48v-224c0-26.476562 21.546875-48 48-48h288c26.453125 0 48 21.523438 48 48v224c0 26.476562-21.546875 48-48 48zm-288-288c-8.8125 0-16 7.167969-16 16v224c0 8.832031 7.1875 16 16 16h288c8.8125 0 16-7.167969 16-16v-224c0-8.832031-7.1875-16-16-16zm0 0"
                />
                <path
                  fill="currentColor"
                  d="m304 224c-8.832031 0-16-7.167969-16-16v-80c0-52.929688-43.070312-96-96-96s-96 43.070312-96 96v80c0 8.832031-7.167969 16-16 16s-16-7.167969-16-16v-80c0-70.59375 57.40625-128 128-128s128 57.40625 128 128v80c0 8.832031-7.167969 16-16 16zm0 0"
                />
              </svg>
              <input
                v-model="candidateForm.password"
                :type="showCandidatePassword ? 'text' : 'password'"
                class="input"
                placeholder="设置密码"
                autocomplete="new-password"
              />
              <button
                type="button"
                class="auth-modal__eye"
                tabindex="-1"
                aria-label="显示或隐藏密码"
                @click="showCandidatePassword = !showCandidatePassword"
              >
                <svg
                  viewBox="0 0 576 512"
                  height="1em"
                  xmlns="http://www.w3.org/2000/svg"
                  aria-hidden="true"
                >
                  <path
                    fill="currentColor"
                    d="M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z"
                  />
                </svg>
              </button>
            </div>
            <div class="flex-column">
              <label>注册邮箱验证码</label>
            </div>
            <div class="inputForm inputForm--with-action">
              <input
                v-model="candidateForm.registerEmailCode"
                type="text"
                class="input input--grow"
                placeholder="注册验证码"
              />
              <button
                type="button"
                class="auth-modal__mini"
                @click="
                  runSafely(() => sendCode(candidateForm.email, 'REGISTER'))
                "
              >
                发送验证码
              </button>
            </div>
            <button
              type="button"
              class="button-submit"
              :disabled="candidateLoading"
              @click="runSafely(handleCandidateRegister)"
            >
              注册
            </button>
          </template>

          <template v-else>
            <div class="flex-column">
              <label>手机号</label>
            </div>
            <div class="inputForm">
              <svg
                class="auth-modal__ico"
                height="20"
                viewBox="0 0 24 24"
                width="20"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  fill="currentColor"
                  d="M6.62 10.79a15.05 15.05 0 006.59 6.59l2.2-2.2a1 1 0 011.01-.24c1.12.37 2.33.57 3.58.57a1 1 0 011 1V20a1 1 0 01-1 1C7.61 21 3 16.39 3 10.5a1 1 0 011-1h3.5a1 1 0 011 1c0 1.25.2 2.46.57 3.58a1 1 0 01-.24 1.01l-2.2 2.2z"
                />
              </svg>
              <input
                v-model="enterpriseForm.mobile"
                type="text"
                class="input"
                placeholder="联系人手机号"
                autocomplete="tel"
              />
            </div>
            <div class="flex-column">
              <label>邮箱</label>
            </div>
            <div class="inputForm">
              <svg
                height="20"
                viewBox="0 0 32 32"
                width="20"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  fill="currentColor"
                  d="m30.853 13.87a15 15 0 0 0 -29.729 4.082 15.1 15.1 0 0 0 12.876 12.918 15.6 15.6 0 0 0 2.016.13 14.85 14.85 0 0 0 7.715-2.145 1 1 0 1 0 -1.031-1.711 13.007 13.007 0 1 1 5.458-6.529 2.149 2.149 0 0 1 -4.158-.759v-10.856a1 1 0 0 0 -2 0v1.726a8 8 0 1 0 .2 10.325 4.135 4.135 0 0 0 7.83.274 15.2 15.2 0 0 0 .823-7.455zm-14.853 8.13a6 6 0 1 1 6-6 6.006 6.006 0 0 1 -6 6z"
                />
              </svg>
              <input
                v-model="enterpriseForm.email"
                type="email"
                class="input"
                placeholder="企业邮箱"
                autocomplete="email"
              />
            </div>
            <div class="flex-column">
              <label>密码</label>
            </div>
            <div class="inputForm">
              <svg
                height="20"
                viewBox="-64 0 512 512"
                width="20"
                xmlns="http://www.w3.org/2000/svg"
                aria-hidden="true"
              >
                <path
                  fill="currentColor"
                  d="m336 512h-288c-26.453125 0-48-21.523438-48-48v-224c0-26.476562 21.546875-48 48-48h288c26.453125 0 48 21.523438 48 48v224c0 26.476562-21.546875 48-48 48zm-288-288c-8.8125 0-16 7.167969-16 16v224c0 8.832031 7.1875 16 16 16h288c8.8125 0 16-7.167969 16-16v-224c0-8.832031-7.1875-16-16-16zm0 0"
                />
                <path
                  fill="currentColor"
                  d="m304 224c-8.832031 0-16-7.167969-16-16v-80c0-52.929688-43.070312-96-96-96s-96 43.070312-96 96v80c0 8.832031-7.167969 16-16 16s-16-7.167969-16-16v-80c0-70.59375 57.40625-128 128-128s128 57.40625 128 128v80c0 8.832031-7.167969 16-16 16zm0 0"
                />
              </svg>
              <input
                v-model="enterpriseForm.password"
                :type="showEnterprisePassword ? 'text' : 'password'"
                class="input"
                placeholder="设置密码"
                autocomplete="new-password"
              />
              <button
                type="button"
                class="auth-modal__eye"
                tabindex="-1"
                aria-label="显示或隐藏密码"
                @click="showEnterprisePassword = !showEnterprisePassword"
              >
                <svg
                  viewBox="0 0 576 512"
                  height="1em"
                  xmlns="http://www.w3.org/2000/svg"
                  aria-hidden="true"
                >
                  <path
                    fill="currentColor"
                    d="M288 32c-80.8 0-145.5 36.8-192.6 80.6C48.6 156 17.3 208 2.5 243.7c-3.3 7.9-3.3 16.7 0 24.6C17.3 304 48.6 356 95.4 399.4C142.5 443.2 207.2 480 288 480s145.5-36.8 192.6-80.6c46.8-43.5 78.1-95.4 93-131.1c3.3-7.9 3.3-16.7 0-24.6c-14.9-35.7-46.2-87.7-93-131.1C433.5 68.8 368.8 32 288 32zM144 256a144 144 0 1 1 288 0 144 144 0 1 1 -288 0zm144-64c0 35.3-28.7 64-64 64c-7.1 0-13.9-1.2-20.3-3.3c-5.5-1.8-11.9 1.6-11.7 7.4c.3 6.9 1.3 13.8 3.2 20.7c13.7 51.2 66.4 81.6 117.6 67.9s81.6-66.4 67.9-117.6c-11.1-41.5-47.8-69.4-88.6-71.1c-5.8-.2-9.2 6.1-7.4 11.7c2.1 6.4 3.3 13.2 3.3 20.3z"
                  />
                </svg>
              </button>
            </div>
            <div class="flex-column">
              <label>邮箱验证码</label>
            </div>
            <div class="inputForm inputForm--with-action">
              <input
                v-model="enterpriseForm.emailCode"
                type="text"
                class="input input--grow"
                placeholder="注册验证码"
              />
              <button
                type="button"
                class="auth-modal__mini"
                @click="
                  runSafely(() => sendCode(enterpriseForm.email, 'REGISTER'))
                "
              >
                获取验证码
              </button>
            </div>
            <div class="flex-column">
              <label>企业名称</label>
            </div>
            <div class="inputForm">
              <input
                v-model="enterpriseForm.companyName"
                type="text"
                class="input input--solo"
                placeholder="企业全称"
              />
            </div>
            <button
              type="button"
              class="button-submit"
              :disabled="enterpriseLoading"
              @click="runSafely(handleEnterpriseRegister)"
            >
              注册企业
            </button>
          </template>
        </form>
      </div>
    </el-dialog>

    <el-dialog
      v-model="resetDialogVisible"
      title="找回密码"
      width="420px"
      append-to-body
    >
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
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from "vue";
import { useRouter } from "vue-router";
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
import { closeAuthModal, useAuthModal } from "@/composables/useAuthModal";

const router = useRouter();
const { visible, initialTab, pendingRedirect } = useAuthModal();

const activeTab = ref<"candidate" | "enterprise" | "admin">("candidate");
const registerTab = ref<"candidate" | "enterprise">("candidate");
const registerVisible = ref(false);
const candidateLoginMode = ref<"password" | "email">("password");
const enterpriseLoginMode = ref<"password" | "email">("password");
const candidateLoading = ref(false);
const enterpriseLoading = ref(false);
const adminLoading = ref(false);
const resetDialogVisible = ref(false);
const resetTarget = ref<"candidate" | "enterprise">("candidate");
const lastDebugCode = ref("");
const showCandidatePassword = ref(false);
const showEnterprisePassword = ref(false);
const showAdminPassword = ref(false);

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

watch(visible, (v) => {
  if (v) {
    activeTab.value = initialTab.value;
    candidateLoginMode.value = "password";
    enterpriseLoginMode.value = "password";
    registerVisible.value = false;
  } else {
    registerVisible.value = false;
  }
});

watch(activeTab, (t) => {
  if (t === "admin") {
    candidateLoginMode.value = "password";
    enterpriseLoginMode.value = "password";
  }
});

function onDialogClosed() {
  closeAuthModal();
}

function onRegisterDialogClosed() {
  registerVisible.value = false;
}

function openRegisterDialog() {
  registerTab.value =
    activeTab.value === "enterprise" ? "enterprise" : "candidate";
  registerVisible.value = true;
}

function saveLoginState(
  userType: "CANDIDATE" | "ENTERPRISE" | "ADMIN",
  data: Record<string, unknown>,
  userName: string,
  fallbackPath: string
) {
  const accessToken = String(data.accessToken || "");
  const refreshToken = String(data.refreshToken || "");
  setAccessToken(accessToken);
  if (refreshToken) {
    setRefreshToken(refreshToken);
  }
  setUserType(userType);
  setUserName(userName);
  const target = pendingRedirect.value || fallbackPath;
  registerVisible.value = false;
  closeAuthModal();
  router.push(target);
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
    // 拦截器已统一提示
  }
}

async function handleCandidateLogin() {
  candidateLoading.value = true;
  try {
    const data = await candidateLogin({
      mobile: candidateForm.mobile,
      password: candidateForm.password,
    });
    saveLoginState(
      "CANDIDATE",
      data,
      String(data.userName || "求职者"),
      "/candidate"
    );
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

async function onCandidateLoginSubmit() {
  if (candidateLoginMode.value === "password") {
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

/** 企业邮箱验证码登录（Mock 可通；后端若未实现需对接专用接口） */
async function handleEnterpriseEmailLogin() {
  enterpriseLoading.value = true;
  try {
    const data = await enterpriseLogin({
      email: enterpriseForm.email,
      emailCode: enterpriseForm.emailCode,
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

async function onEnterpriseLoginSubmit() {
  if (enterpriseLoginMode.value === "password") {
    await handleEnterpriseLogin();
    return;
  }
  await handleEnterpriseEmailLogin();
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
    saveLoginState(
      "ADMIN",
      data,
      String(data.userName || "平台管理员"),
      "/admin"
    );
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
    // 拦截器提示
  }
}
</script>

<style scoped>
.auth-modal {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}

.auth-modal__title {
  margin: 0 0 4px;
  font-size: 1.35rem;
  font-weight: 800;
  color: #151717;
  text-align: center;
}

.auth-modal__subtitle {
  margin: 0 0 16px;
  font-size: 0.875rem;
  color: #64748b;
  text-align: center;
}

.auth-modal__rolebar {
  display: flex;
  width: 100%;
  margin: 0;
  padding: 0;
}

.auth-modal__rolebar-item {
  flex: 1 1 0;
  margin: 0;
  padding: 10px 4px 12px;
  border: none;
  background: none;
  font-size: 14px;
  font-weight: 500;
  color: #9ca3af;
  cursor: pointer;
  transition: color 0.15s ease;
}

.auth-modal__rolebar-item:hover {
  color: #6b7280;
}

.auth-modal__rolebar-item--active {
  color: #111827;
  font-weight: 600;
}

.auth-modal__role-underline {
  display: flex;
  width: 100%;
  height: 2px;
  margin: 0 0 20px;
  gap: 0;
  overflow: hidden;
  border-radius: 1px;
}

.auth-modal__role-seg {
  flex: 1 1 0;
  height: 100%;
  background: #e5e7eb;
  transition: background 0.15s ease;
}

.auth-modal__role-seg--active {
  background: #111827;
}

.auth-modal__field-foot {
  display: flex;
  margin-top: 4px;
  min-height: 22px;
  align-items: center;
}

.auth-modal__field-foot--end {
  justify-content: flex-end;
}

.auth-modal__field-foot--start {
  justify-content: flex-start;
}

.auth-modal__field-foot--split {
  justify-content: space-between;
  gap: 12px;
}

.auth-modal__text-btn {
  margin: 0;
  padding: 0;
  border: none;
  background: none;
  font-size: 13px;
  font-weight: 500;
  color: #2d79f3;
  cursor: pointer;
  font-family: inherit;
}

.auth-modal__text-btn:hover {
  text-decoration: underline;
}

.auth-modal__text-btn--inline {
  font-size: 14px;
}

.auth-modal__register-hint {
  text-align: center;
  font-size: 14px;
  color: #6b7280;
  margin: 4px 0 0;
}

.auth-modal__reg-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  gap: 12px;
  padding-right: 8px;
}

.auth-modal__reg-title {
  font-size: 1.05rem;
  font-weight: 700;
  color: #151717;
}

.auth-modal__hint {
  font-size: 13px;
  color: #64748b;
  font-weight: 400;
}

.auth-modal__ico {
  flex-shrink: 0;
  color: #64748b;
}

.auth-modal__eye {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8px;
  margin: 0;
  border: none;
  background: none;
  cursor: pointer;
  color: #94a3b8;
}

.auth-modal__eye:hover {
  color: #2d79f3;
}

.auth-modal__mini {
  flex-shrink: 0;
  margin-right: 6px;
  padding: 0 10px;
  height: 34px;
  border-radius: 8px;
  border: 1px solid #ecedec;
  background: #f8fafc;
  font-size: 12px;
  font-weight: 600;
  color: #2d79f3;
  cursor: pointer;
}

.auth-modal__mini:hover {
  border-color: #2d79f3;
  background: #eff6ff;
}

.auth-modal__debug {
  margin-top: 12px;
  padding: 10px;
  font-size: 12px;
  color: #15803d;
  background: #f0fdf4;
  border-radius: 10px;
  border: 1px solid #bbf7d0;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 10px;
  background-color: #ffffff;
  padding: 8px 4px 4px;
  width: 100%;
  border-radius: 20px;
  font-family: inherit;
}

.form .input::placeholder {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
}

.flex-column > label {
  color: #151717;
  font-weight: 600;
  font-size: 14px;
}

.inputForm {
  border: 1.5px solid #ecedec;
  border-radius: 10px;
  height: 50px;
  display: flex;
  align-items: center;
  padding-left: 10px;
  transition: 0.2s ease-in-out;
}

.inputForm--with-action {
  padding-right: 6px;
}

.inputForm:focus-within {
  border: 1.5px solid #2d79f3;
}

.input {
  margin-left: 10px;
  border-radius: 10px;
  border: none;
  width: 85%;
  height: 100%;
  font-size: 15px;
  background: transparent;
}

.input--grow {
  flex: 1 1 auto;
  width: auto;
  min-width: 0;
}

.input--solo {
  margin-left: 10px;
  width: calc(100% - 20px);
}

.input:focus {
  outline: none;
}

.flex-row {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 10px;
  justify-content: space-between;
}

.button-submit {
  margin: 20px 0 10px 0;
  background-color: #151717;
  border: none;
  color: white;
  font-size: 15px;
  font-weight: 500;
  border-radius: 10px;
  height: 50px;
  width: 100%;
  cursor: pointer;
  transition: background 0.15s ease;
}

.button-submit:hover:not(:disabled) {
  background-color: #252727;
}

.button-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.verify-row {
  display: flex;
  width: 100%;
  gap: 12px;
}
</style>

<style>
.auth-el-dialog.el-dialog {
  border-radius: 20px;
  padding: 0;
  overflow: hidden;
}

.auth-el-dialog .el-dialog__header {
  padding: 16px 20px 0;
}

.auth-el-dialog .el-dialog__body {
  padding: 8px 28px 28px;
}

.auth-el-dialog--nested .el-dialog__header {
  padding: 12px 20px 0;
}

.auth-el-dialog--nested .el-dialog__body {
  padding: 8px 28px 24px;
}
</style>
