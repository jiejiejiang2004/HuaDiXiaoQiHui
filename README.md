# 项目名称：XiaoQiHui 招聘系统

## 项目介绍

XiaoQiHui是一个基于Vue 3和Spring Boot的前后端分离招聘系统，提供职位发布、简历管理、面试安排等功能，为企业和求职者搭建高效的招聘平台。

## 技术栈

### 前端
- Vue 3
- JavaScript
- 前端构建工具：Vite
- CSS预处理器：可能包含SCSS/SASS（根据项目实际使用情况）

### 后端
- Spring Boot
- Java
- MyBatis（或其他ORM框架）
- Redis（用于缓存和会话管理）
- JWT（用于身份验证）

## 环境要求

### 前端
- Node.js 14.0+ 
- npm 6.0+ 或 yarn

### 后端
- JDK 1.8+ 
- Maven 3.6+ 
- MySQL 5.7+ 
- Redis 6.0+（可选，用于缓存）

## 项目结构

```
├── frontend/           # 前端Vue 3项目
│   ├── src/            # 前端源代码
│   ├── public/         # 静态资源
│   ├── dist/           # 构建输出目录
│   └── node_modules/   # 依赖包
├── backend/            # 后端Spring Boot项目
│   ├── src/            # 后端源代码
│   ├── target/         # 构建输出目录
│   ├── uploads/        # 上传文件存储
│   └── .mvn/           # Maven包装器
└── doc/                # 项目文档
    ├── 原型设计/        # 原型设计图
    └── api.md          # API文档
```

## 环境配置与运行

### 前端配置与运行

1. **安装依赖**

   ```bash
   cd frontend
   npm install
   # 或使用yarn
   yarn install
   ```

2. **开发环境运行**

   ```bash
   npm run serve
   # 或使用yarn
   yarn serve
   ```

3. **生产环境构建**

   ```bash
   npm run build
   # 或使用yarn
   yarn build
   ```

4. **前端配置**

   - 前端API请求地址配置：请在前端项目的配置文件中修改API基础URL，指向后端服务地址
   - 环境变量配置：在`.env`文件中配置相关环境变量

### 后端配置与运行

1. **数据库配置**

   - 创建数据库：根据`backend/src/main/resources/sql/`目录下的SQL文件初始化数据库
   - 配置数据库连接：修改`backend/src/main/resources/application.yaml`文件中的数据库连接信息

2. **依赖安装**

   ```bash
   cd backend
   mvn clean install
   ```

3. **运行项目**

   ```bash
   # 方法1：使用Maven运行
   mvn spring-boot:run
   
   # 方法2：运行打包后的jar文件
   mvn clean package
   java -jar target/xiaoqihui-0.0.1-SNAPSHOT.jar
   ```

4. **后端配置**

   - **数据库连接**：在`application.yaml`中配置数据库URL、用户名和密码
   - **Redis配置**：如果使用Redis，在`application.yaml`中配置Redis连接信息
   - **JWT配置**：配置JWT密钥和过期时间
   - **文件上传路径**：配置文件上传的存储路径

   **application.yaml示例配置**：

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/xiaoqihui?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
       username: root
       password: your_password
       driver-class-name: com.mysql.cj.jdbc.Driver
     
   # Redis配置（可选）
   redis:
     host: localhost
     port: 6379
     password: 
     database: 0
   
   # JWT配置
   jwt:
     secret: your_jwt_secret_key
     expiration: 86400 # 24小时
   
   # 文件上传配置
   upload:
     path: ./uploads
   
   # 服务器配置
   server:
     port: 8081
     servlet:
       context-path: /api
   ```

   **注意**：请根据实际环境修改上述配置中的数据库连接信息、JWT密钥等敏感信息。

## 注意事项

1. **数据库初始化**：首次运行前请确保已执行`backend/src/main/resources/sql/`目录下的SQL文件初始化数据库

2. **环境变量**：根据实际环境配置`.env`文件（前端）和`application.yaml`文件（后端）

3. **端口冲突**：默认情况下，前端运行在`http://localhost:8080`，后端运行在`http://localhost:8081`，如有端口冲突请修改配置

4. **文件上传**：确保后端的上传目录有正确的读写权限

5. **跨域配置**：后端已配置跨域支持，前端可直接访问后端API

## 开发建议

1. **代码风格**：遵循项目现有的代码风格和命名规范

2. **分支管理**：建议使用Git分支进行功能开发和Bug修复

3. **提交规范**：提交代码时请使用清晰的提交信息，描述变更内容

4. **测试**：在提交代码前请确保所有功能正常运行

## 联系方式

如有问题或建议，请联系项目维护人员。

---

**备注**：本项目为招聘系统，包含企业端和个人端功能，具体功能请参考API文档和原型设计。