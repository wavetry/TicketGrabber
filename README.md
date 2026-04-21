# 12306 抢票助手 Android App

一个基于 Android 平台的 12306 火车票抢票应用，采用现代化的技术栈和架构设计。

> ⚠️ **重要提示**：本项目仅供学习研究使用。12306 官方不提供公开 API，使用抢票工具可能违反服务协议。请遵守相关法律法规。

---

## 功能特性

### 核心功能
- 🔐 **用户认证** - 支持 12306 账号登录、验证码处理、Session 管理
- 🔍 **车次查询** - 实时查询车次信息、余票查询、车站选择
- 🎯 **自动抢票** - 后台自动抢票、多车次监控、智能重试
- 📋 **订单管理** - 订单查询、取消订单、支付入口
- 🔔 **通知推送** - 抢票成功通知、状态变更提醒

### 技术亮点
- 采用 **MVVM + Repository** 架构模式
- 使用 **Jetpack Compose** 构建现代化 UI
- **WorkManager** 实现后台抢票任务
- **Room** 数据库本地缓存
- **Hilt** 依赖注入
- **Coroutines + Flow** 异步处理

---

## 技术栈

| 层级 | 技术 |
|------|------|
| 编程语言 | Kotlin |
| UI 框架 | Jetpack Compose + Material 3 |
| 架构模式 | MVVM + Repository |
| 依赖注入 | Hilt |
| 网络请求 | Retrofit + OkHttp |
| 本地存储 | Room |
| 后台任务 | WorkManager |
| 异步处理 | Coroutines + Flow |

---

## 项目结构

```
TicketGrabber/
├── app/src/main/kotlin/com/ticket/grabber/
│   ├── base/                    # 基础类
│   │   ├── BaseRepository.kt
│   │   ├── BaseViewModel.kt
│   │   └── UiState.kt
│   │
│   ├── core/                    # 核心工具
│   │   ├── constants/          # 常量定义
│   │   ├── database/           # 数据库辅助
│   │   ├── network/            # 网络工具
│   │   └── utils/              # 工具类
│   │
│   ├── data/                    # 数据层
│   │   ├── api/                # API 接口
│   │   │   ├── TicketApi.kt    # 主 API 定义
│   │   │   ├── auth/           # 认证相关
│   │   │   └── interceptors/   # 网络拦截器
│   │   │
│   │   ├── local/              # Room 数据库
│   │   │   ├── TicketDatabase.kt
│   │   │   ├── AuthDao.kt
│   │   │   ├── StationDao.kt
│   │   │   ├── TrainDao.kt
│   │   │   ├── OrderDao.kt
│   │   │   ├── PassengerDao.kt
│   │   │   ├── PaymentDao.kt
│   │   │   └── GrabTaskDao.kt
│   │   │
│   │   ├── model/              # 数据模型
│   │   │   ├── Auth.kt         # 认证模型
│   │   │   ├── Station.kt      # 车站模型
│   │   │   ├── Train.kt        # 车次模型
│   │   │   ├── Order.kt        # 订单模型
│   │   │   ├── Passenger.kt    # 乘客模型
│   │   │   ├── Payment.kt      # 支付模型
│   │   │   └── GrabTask.kt     # 抢票任务模型
│   │   │
│   │   └── repository/         # 仓库层
│   │       ├── local/          # 本地仓库
│   │       ├── remote/         # 远程仓库
│   │       ├── AuthRepository.kt
│   │       ├── TicketRepository.kt
│   │       ├── OrderRepository.kt
│   │       └── GrabTaskRepository.kt
│   │
│   ├── di/                      # 依赖注入
│   │   ├── DataModule.kt
│   │   ├── NetworkModule.kt
│   │   └── WorkerModule.kt
│   │
│   ├── ui/                      # UI 层
│   │   ├── screen/             # 界面
│   │   │   ├── MainActivity.kt
│   │   │   ├── HomeScreen.kt
│   │   │   ├── LoginScreen.kt
│   │   │   ├── SearchScreen.kt
│   │   │   ├── GrabScreen.kt
│   │   │   ├── OrderScreen.kt
│   │   │   └── ...
│   │   │
│   │   ├── viewmodel/          # ViewModel
│   │   │   ├── LoginViewModel.kt
│   │   │   ├── SearchViewModel.kt
│   │   │   ├── GrabViewModel.kt
│   │   │   └── OrderViewModel.kt
│   │   │
│   │   ├── theme/              # 主题
│   │   └── composable/         # 可复用组件
│   │
│   ├── worker/                  # 后台任务
│   │   ├── GrabWorker.kt       # 抢票 Worker
│   │   ├── TicketWorkers.kt
│   │   └── NotificationWorker.kt
│   │
│   └── TicketGrabberApplication.kt
│
├── gradle/                      # Gradle 配置
├── build.gradle.kts            # 项目构建配置
└── settings.gradle.kts         # 项目设置
```

---

## 快速开始

### 环境要求

- Android Studio Arctic Fox (2020.3.1) 或更高版本
- JDK 11 或更高版本
- Android SDK 26+ (Android 8.0)
- Kotlin 1.9+

### 构建步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd TicketGrabber
   ```

2. **使用 Android Studio 打开**
   - 打开 Android Studio
   - 选择 "Open an existing Android Studio project"
   - 选择 `TicketGrabber` 文件夹

3. **同步 Gradle**
   - 点击 "Sync Now" 或运行：
   ```bash
   ./gradlew build
   ```

4. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击 "Run" 按钮 (Shift + F10)

---

## 使用指南

### 1. 登录

1. 打开应用，进入登录页面
2. 输入 12306 账号和密码
3. 输入验证码（如有）
4. 点击登录按钮

### 2. 查询车次

1. 进入"查询"页面
2. 选择出发站和到达站
3. 选择出发日期
4. 点击"查询"按钮
5. 查看车次列表和余票信息

### 3. 创建抢票任务

1. 在查询结果中选择目标车次
2. 点击"抢票"按钮
3. 选择席别类型（可多选）
4. 选择乘客
5. 设置抢票参数（重试次数、查询间隔）
6. 点击"开始抢票"

### 4. 管理抢票任务

1. 进入"抢票"页面
2. 查看所有抢票任务列表
3. 任务状态：
   - 🟡 待启动
   - 🔵 抢票中
   - 🟢 已抢到
   - 🔴 已失败
   - ⚪ 已取消
4. 可操作：开始、暂停、取消、删除任务

### 5. 查看订单

1. 进入"订单"页面
2. 查看所有订单列表
3. 点击订单查看详情
4. 可进行支付或取消操作

### 6. 后台抢票

- 抢票任务会在后台运行
- 抢到票后会发送通知提醒
- 即使关闭应用，Worker 也会继续运行

---

## 核心功能说明

### 用户认证模块

```kotlin
// 登录流程
1. 获取验证码 -> getCaptchaImage()
2. 提交登录 -> login(username, password, captcha)
3. 保存 Session -> saveAuthSession()
4. 自动刷新 Token -> AuthInterceptor
```

**关键文件：**
- `data/api/auth/AuthRepository.kt` - 认证仓库
- `data/local/AuthDao.kt` - 认证数据访问
- `ui/viewmodel/LoginViewModel.kt` - 登录逻辑
- `ui/screen/LoginScreen.kt` - 登录界面

### 车次查询模块

```kotlin
// 查询流程
1. 选择车站 -> StationRepository.getStations()
2. 查询车次 -> TicketRepository.queryTrains(from, to, date)
3. 显示结果 -> SearchScreen
4. 缓存数据 -> Room Database
```

**关键文件：**
- `data/repository/TicketRepository.kt` - 车次仓库
- `ui/viewmodel/SearchViewModel.kt` - 查询逻辑
- `ui/screen/SearchScreen.kt` - 查询界面

### 抢票核心模块

```kotlin
// 抢票流程
1. 创建任务 -> GrabTaskRepository.createTask()
2. 启动 Worker -> GrabWorker.start()
3. 循环查询 -> checkTicketAvailability()
4. 提交订单 -> submitOrder()
5. 通知用户 -> NotificationManager
```

**关键文件：**
- `data/repository/GrabTaskRepository.kt` - 任务仓库
- `worker/GrabWorker.kt` - 后台抢票 Worker
- `ui/viewmodel/GrabViewModel.kt` - 抢票逻辑
- `ui/screen/GrabScreen.kt` - 抢票界面

### 订单管理模块

```kotlin
// 订单流程
1. 查询订单 -> OrderRepository.getOrders()
2. 查看详情 -> getOrderDetail(orderNo)
3. 取消订单 -> cancelOrder(orderNo)
4. 支付订单 -> createPayment()
```

**关键文件：**
- `data/repository/OrderRepository.kt` - 订单仓库
- `ui/viewmodel/OrderViewModel.kt` - 订单逻辑
- `ui/screen/OrderScreen.kt` - 订单界面

---

## 数据模型

### 抢票任务 (GrabTask)

```kotlin
@Entity(tableName = "grab_tasks")
data class GrabTask(
    @PrimaryKey val taskId: String,
    val trainNo: String,           // 车次号
    val trainCode: String,         // 车次代码
    val fromStation: String,         // 出发站
    val toStation: String,           // 到达站
    val departureDate: String,       // 出发日期
    val seatTypes: String,           // 席别列表
    val passengers: String,          // 乘客列表
    val status: Int,                 // 任务状态
    val priority: Int,               // 优先级
    val retryCount: Int,             // 重试次数
    val maxRetryCount: Int,          // 最大重试次数
    val queryInterval: Long,         // 查询间隔
    val orderNo: String?,            // 订单号（抢到后）
    val errorMessage: String?,       // 错误信息
    val createTime: Long,
    val updateTime: Long
)
```

### 订单 (Order)

```kotlin
@Entity(tableName = "orders")
data class Order(
    @PrimaryKey val orderNo: String,
    val trainNo: String,
    val trainCode: String,
    val fromStation: String,
    val toStation: String,
    val departureDate: String,
    val departureTime: String,
    val arrivalTime: String,
    val passengers: String,          // JSON 格式
    val seats: String,               // JSON 格式
    val totalPrice: Double,
    val status: Int,                 // 0:待支付, 1:已支付, 2:已取消, 3:已出行, 4:已退票
    val paymentNo: String?,
    val paymentTime: Long?,
    val createTime: Long,
    val updateTime: Long
)
```

---

## 自定义配置

### 修改 API 地址

编辑 `data/api/TicketApi.kt`：

```kotlin
interface TicketApi {
    // 修改 baseUrl
    companion object {
        const val BASE_URL = "https://kyfw.12306.cn/"
    }
    
    // API 端点...
}
```

### 调整抢票参数

编辑 `data/model/GrabTask.kt`：

```kotlin
data class CreateGrabTaskRequest(
    // 默认查询间隔（毫秒）
    val queryInterval: Long = 3000,
    // 默认最大重试次数
    val maxRetryCount: Int = 100,
    // 优先级
    val priority: Int = 0
)
```

### 自定义通知

编辑 `worker/GrabWorker.kt`：

```kotlin
private fun showNotification(title: String, message: String) {
    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(R.drawable.ic_notification)  // 自定义图标
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()
}
```

---

## 常见问题

### Q: 为什么登录失败？

A: 请检查：
1. 网络连接是否正常
2. 账号密码是否正确
3. 验证码是否输入正确
4. 12306 是否要求额外的安全验证

### Q: 抢票任务为什么自动停止？

A: 可能原因：
1. 达到最大重试次数
2. 登录 Session 过期
3. 网络连接中断
4. 用户手动取消

### Q: 抢到票后如何支付？

A: 应用会跳转到 12306 官方支付页面，或提供支付链接。

### Q: 后台抢票会消耗多少电量？

A: WorkManager 会自动优化电池使用，但持续网络请求会有一定耗电。建议在充电时运行抢票任务。

### Q: 可以同时抢多个车次吗？

A: 可以，创建多个抢票任务即可。系统会并行处理。

---

## 开发计划

- [x] 基础架构搭建
- [x] 用户认证模块
- [x] 车次查询模块
- [x] 抢票核心模块
- [x] 订单管理模块
- [x] 后台服务
- [ ] 验证码自动识别
- [ ] 智能抢票策略
- [ ] 多账号支持
- [ ] 数据分析统计

---

## 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

---

## 许可证

本项目仅供学习研究使用，遵循 MIT 许可证。

**免责声明**：使用本软件产生的任何后果由用户自行承担。请遵守 12306 平台规则和相关法律法规。

---

## 联系方式

如有问题或建议，欢迎通过以下方式联系：

- 提交 Issue
- 发送邮件

---

**感谢使用 12306 抢票助手！祝您抢票成功！** 🎉
