# 逐风马马拉松报名系统

一个功能完整的马拉松报名系统，采用 Angular + Java Spring Boot + MySQL 技术栈开发。

## 项目结构

```
Marathons/
├── backend/                # 后端 Java Spring Boot 项目
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/zhufengma/marathon/
│           │   ├── ZhufengmaApplication.java    # 启动类
│           │   ├── config/                        # 配置类
│           │   ├── controller/                    # 控制器
│           │   ├── dto/                           # 数据传输对象
│           │   ├── entity/                        # 实体类
│           │   ├── guards/                        # 路由守卫
│           │   ├── interceptors/                  # 拦截器
│           │   ├── repository/                    # 数据访问层
│           │   ├── service/                       # 业务逻辑层
│           │   └── util/                          # 工具类
│           └── resources/
│               └── application.properties          # 应用配置
├── frontend/               # 前端 Angular 项目
│   ├── package.json
│   ├── angular.json
│   ├── tsconfig.json
│   └── src/
│       ├── app/
│       │   ├── components/                          # 组件
│       │   ├── guards/                              # 路由守卫
│       │   ├── interceptors/                        # HTTP 拦截器
│       │   ├── services/                            # 服务
│       │   ├── app.module.ts
│       │   ├── app-routing.module.ts
│       │   └── app.component.ts
│       ├── environments/                            # 环境配置
│       ├── index.html
│       ├── main.ts
│       └── styles.css                               # 全局样式
└── database/               # 数据库脚本
    └── init.sql
```

## 技术栈

### 后端
- **Java 8+**
- **Spring Boot 2.7.18**
- **Spring Security** (JWT 认证)
- **Spring Data JPA** (ORM)
- **MySQL 8.0+**
- **Apache POI** (Excel 导出)
- **Lombok**

### 前端
- **Angular 15**
- **TypeScript 4.9**
- **Bootstrap 5**
- **Bootstrap Icons**
- **Chart.js**

## 功能特性

### 用户功能
- ✅ 用户注册与登录
- ✅ 查看赛事列表和详情
- ✅ 赛事报名
- ✅ 查看报名记录和审核状态
- ✅ 取消报名

### 管理员功能
- ✅ 发布/编辑/下架赛事
- ✅ 管理赛事组别
- ✅ 查看报名名单
- ✅ 手动审核通过/拒绝报名
- ✅ 批量审核报名
- ✅ 批量导出报名数据 (Excel)
- ✅ 按组别统计报名人数
- ✅ 发布赛事公告
- ✅ 发布竞赛规程
- ✅ 发布领物须知

### UI 设计
- ✅ 运动活力主题色
- ✅ 清爽简洁的界面设计
- ✅ 响应式布局
- ✅ 流畅的动画效果

## 环境要求

- **JDK 8 或更高版本**
- **Node.js 16 或更高版本**
- **MySQL 8.0 或更高版本**
- **Maven 3.6 或更高版本**
- **Angular CLI 15** (可选，用于开发)

## 启动步骤

### 第一步：配置数据库

1. **启动 MySQL 服务**

2. **创建数据库并导入初始数据**
   ```bash
   # 登录 MySQL
   mysql -u root -p

   # 执行初始化脚本
   source /path/to/Marathons/database/init.sql
   ```

   或者使用 MySQL 客户端直接执行 `database/init.sql` 文件。

3. **修改数据库连接配置** (如需要)

   编辑 `backend/src/main/resources/application.properties`：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/zhufengma?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=root
   ```

   根据你的 MySQL 配置修改用户名和密码。

### 第二步：启动后端服务

1. **进入后端目录**
   ```bash
   cd backend
   ```

2. **编译并运行**
   ```bash
   # 使用 Maven 编译并运行
   mvn spring-boot:run

   # 或者先编译，再运行 jar 包
   mvn clean package
   java -jar target/marathon-system-1.0.0.jar
   ```

3. **验证后端是否启动成功**

   访问 http://localhost:8080/api/events/public/list

   如果返回 JSON 格式的赛事列表数据，说明后端启动成功。

### 第三步：启动前端服务

1. **进入前端目录**
   ```bash
   cd frontend
   ```

2. **安装依赖**
   ```bash
   npm install
   ```

3. **启动开发服务器**
   ```bash
   npm start
   # 或者使用 ng serve
   ng serve --open
   ```

4. **访问应用**

   打开浏览器访问 http://localhost:4200

## 测试账号

系统初始化时已创建以下测试账号：

### 管理员账号
- **用户名**: `admin`
- **密码**: `admin123`

### 普通用户账号
- **用户名**: `zhangsan`
- **密码**: `user123`

- **用户名**: `lisi`
- **密码**: `user123`

## 功能验证指南

### 1. 用户功能验证

#### 注册新用户
1. 点击导航栏的「注册」按钮
2. 填写注册表单：
   - 用户名：3-20 个字符
   - 密码：6-20 个字符
   - 真实姓名
   - 身份证号：18 位
   - 手机号：11 位
   - 性别、出生日期
3. 点击「注册」按钮
4. 注册成功后会自动跳转到登录页面

#### 用户登录
1. 点击导航栏的「登录」按钮
2. 输入用户名和密码
3. 点击「登录」按钮
4. 登录成功后导航栏会显示用户信息

#### 浏览赛事
1. 点击导航栏的「赛事列表」
2. 可以使用筛选功能：
   - 全部赛事
   - 正在报名
   - 即将开始
3. 点击赛事卡片或「查看详情」查看赛事详情

#### 赛事报名
1. 进入赛事详情页面
2. 选择「赛事组别」标签
3. 点击「立即报名」按钮（需要先登录）
4. 填写报名信息：
   - T恤尺码
   - 紧急联系人（可选）
   - 紧急联系电话（可选）
   - 病史信息（可选）
5. 点击「确认报名」

#### 查看报名记录
1. 登录后点击导航栏的「我的报名」
2. 查看所有报名记录及其状态：
   - 待审核
   - 已通过
   - 已拒绝
   - 已取消
3. 对于「待审核」状态的报名，可以点击「取消」按钮取消报名

### 2. 管理员功能验证

#### 管理员登录
1. 点击导航栏的「登录」按钮
2. 使用管理员账号登录：
   - 用户名：`admin`
   - 密码：`admin123`
3. 登录成功后导航栏会显示「管理后台」入口

#### 进入管理后台
1. 点击导航栏的「管理后台」
2. 进入管理员仪表盘

#### 赛事管理
1. 点击左侧菜单的「赛事管理」
2. 查看所有赛事列表
3. 操作功能：
   - 「查看报名」：查看该赛事的报名名单
   - 「编辑」：修改赛事信息
   - 「发布」：将草稿状态的赛事发布
   - 「下架」：将已发布的赛事下架

#### 发布新赛事
1. 点击左侧菜单的「发布赛事」
2. 填写赛事信息：
   - 赛事名称
   - 赛事日期
   - 赛事时间
   - 赛事地点
   - 报名开始/截止日期
   - 赛事描述
3. 点击「创建赛事」
4. 创建后默认为「草稿」状态，需要点击「发布」才能让用户看到

#### 报名审核
1. 在赛事管理列表中点击「查看报名」
2. 查看该赛事的所有报名记录
3. 统计信息：
   - 总报名人数
   - 总容量
   - 剩余名额
   - 待审核数量
   - 各组别报名统计
4. 单个审核：
   - 点击「通过」图标通过报名
   - 点击「拒绝」图标拒绝报名
5. 批量审核：
   - 勾选要审核的报名记录
   - 选择审核结果（通过/拒绝）
   - 填写审核备注（可选）
   - 点击「批量审核」

#### 导出报名数据
1. 在报名列表页面
2. 点击右上角的「导出 Excel」按钮
3. 浏览器会自动下载 Excel 文件
4. Excel 文件包含以下信息：
   - 用户名、真实姓名
   - 身份证号（已脱敏）
   - 手机号、邮箱
   - 性别、出生日期
   - 报名组别、T恤尺码
   - 紧急联系人、联系电话
   - 报名状态、审核备注
   - 报名时间

#### 公告管理
1. 点击左侧菜单的「公告管理」
2. 创建新公告：
   - 公告标题
   - 公告内容
   - 关联赛事（可选）
   - 是否置顶
3. 保存后可以发布

#### 竞赛规程和领物须知
1. 点击左侧菜单的「竞赛规程」或「领物须知」
2. 创建新内容
3. 关联到具体赛事
4. 发布后用户可在赛事详情页面查看

## API 接口文档

### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户/管理员登录

### 赛事接口 (公开)
- `GET /api/events/public/list` - 获取所有已发布赛事
- `GET /api/events/public/active` - 获取正在报名的赛事
- `GET /api/events/public/upcoming` - 获取即将开始的赛事
- `GET /api/events/public/{id}` - 获取赛事详情
- `GET /api/events/public/{eventId}/groups` - 获取赛事组别

### 赛事接口 (管理员)
- `POST /api/events` - 创建赛事
- `PUT /api/events/{id}` - 更新赛事
- `PUT /api/events/{id}/publish` - 发布赛事
- `PUT /api/events/{id}/offline` - 下架赛事
- `DELETE /api/events/{id}` - 删除赛事
- `POST /api/events/{eventId}/groups` - 创建组别
- `PUT /api/events/groups/{groupId}` - 更新组别
- `DELETE /api/events/groups/{groupId}` - 删除组别

### 报名接口
- `POST /api/registrations` - 创建报名
- `GET /api/registrations/my` - 获取我的报名
- `PUT /api/registrations/{id}/cancel` - 取消报名
- `GET /api/registrations/event/{eventId}` - 获取赛事报名列表 (管理员)
- `PUT /api/registrations/{id}/review` - 审核报名 (管理员)
- `POST /api/registrations/batch-review` - 批量审核 (管理员)
- `GET /api/registrations/statistics/event/{eventId}` - 获取报名统计 (管理员)

### 导出接口
- `GET /api/export/registrations/event/{eventId}` - 导出报名 Excel (管理员)

### 公告接口
- `GET /api/announcements/public/list` - 获取已发布公告
- `GET /api/announcements/public/{id}` - 获取公告详情
- `POST /api/announcements` - 创建公告 (管理员)
- `PUT /api/announcements/{id}` - 更新公告 (管理员)
- `PUT /api/announcements/{id}/publish` - 发布公告 (管理员)
- `DELETE /api/announcements/{id}` - 删除公告 (管理员)

## 常见问题

### 1. 数据库连接失败
- 检查 MySQL 服务是否启动
- 检查 `application.properties` 中的数据库配置
- 确保数据库 `zhufengma` 已创建
- 检查用户名和密码是否正确

### 2. 前端无法访问后端 API
- 检查后端服务是否在 `http://localhost:8080` 运行
- 检查前端 `environment.ts` 中的 `apiUrl` 配置
- 如果前后端不在同一域名，需要配置 CORS（项目已配置允许跨域）

### 3. 登录提示用户名或密码错误
- 确保使用了正确的测试账号
- 管理员账号：`admin` / `admin123`
- 普通用户账号：`zhangsan` / `user123` 或 `lisi` / `user123`

### 4. 报名时提示"赛事未发布"
- 只有「已发布」状态的赛事才能报名
- 管理员需要在赛事管理中点击「发布」

### 5. Excel 导出失败
- 确保有报名记录
- 检查浏览器是否允许下载文件
- 后端需要有足够的内存处理大型数据

## 生产环境部署

### 后端部署
1. **编译打包**
   ```bash
   cd backend
   mvn clean package -DskipTests
   ```

2. **运行 Jar 包**
   ```bash
   java -jar target/marathon-system-1.0.0.jar --spring.profiles.active=prod
   ```

3. **使用 Docker (可选)**
   ```dockerfile
   FROM openjdk:8-jdk-alpine
   COPY target/marathon-system-1.0.0.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "/app.jar"]
   ```

### 前端部署
1. **构建生产版本**
   ```bash
   cd frontend
   ng build --prod
   ```

2. **部署到 Web 服务器**
   - 将 `dist/zhufengma-frontend/` 目录部署到 Nginx、Apache 等 Web 服务器
   - 配置反向代理，将 `/api` 请求转发到后端服务

3. **Nginx 配置示例**
   ```nginx
   server {
       listen 80;
       server_name your-domain.com;

       # 前端静态文件
       location / {
           root /path/to/frontend/dist/zhufengma-frontend;
           try_files $uri $uri/ /index.html;
       }

       # 后端 API 反向代理
       location /api/ {
           proxy_pass http://localhost:8080/api/;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
       }
   }
   ```

## 开发计划

- [ ] 添加支付功能
- [ ] 实现赛事照片上传
- [ ] 添加成绩查询功能
- [ ] 优化移动端体验
- [ ] 添加消息通知功能
- [ ] 实现多语言支持
- [ ] 添加数据分析仪表盘

## 技术支持

如有问题，请检查以下文件：
- `backend/src/main/resources/application.properties` - 后端配置
- `frontend/src/environments/environment.ts` - 前端环境配置
- `database/init.sql` - 数据库初始化脚本

## 许可证

MIT License
