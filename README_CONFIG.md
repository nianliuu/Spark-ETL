# Milvus Spark Connector Configuration Guide

## 📁 Configuration Files

配置文件支持两种位置：
1. `config/application.json` (推荐)
2. `src/main/scala/zilliz/config.json` (兼容旧版本)

## 📋 Configuration Format

```json
{
  "milvus": {
    "uri": "https://your-milvus-endpoint.com:19541",
    "token": "your-milvus-token",
    "collectionName": "your_collection"
  },
  "s3": {
    "endpoint": "https://s3.us-west-2.amazonaws.com",
    "bucketName": "your-bucket-name",
    "rootPath": "your-root-path/",
    "accessKey": "YOUR_ACCESS_KEY",
    "secretKey": "YOUR_SECRET_KEY"
  },
  "spark": {
    "appName": "MergeDataDemo",
    "master": "local[*]",
    "driverMemory": "4g",
    "executorMemory": "2g"
  },
  "schema": {
    "idField": "id"
  }
}
```

## 🚀 Usage

### 1. 使用默认配置文件运行
```bash
sbt run
```

### 2. 使用自定义配置文件
```bash
sbt "run config/custom.json"
```

### 3. 在 Docker 中运行
```bash
# 使用默认配置
docker run -it --rm milvus-spark-app:latest ./spark-submit-wrapper.sh --class "zilliz.MergeData" ./lib/milvus-spark-connector-example_2.13-0.1.0-SNAPSHOT.jar

# 使用自定义配置文件（需要挂载配置文件）
docker run -it --rm -v $(pwd)/config:/app/config milvus-spark-app:latest ./spark-submit-wrapper.sh --class "zilliz.MergeData" ./lib/milvus-spark-connector-example_2.13-0.1.0-SNAPSHOT.jar config/application.json
```

## 🔧 Configuration Structure

### MilvusConfig
- `uri`: Milvus 服务器地址
- `token`: 认证令牌
- `collectionName`: 集合名称

### S3Config
- `endpoint`: S3 端点
- `bucketName`: 存储桶名称
- `rootPath`: 根路径
- `accessKey`: 访问密钥
- `secretKey`: 秘密密钥

### SparkConfig
- `appName`: Spark 应用名称
- `master`: Spark 主节点配置
- `driverMemory`: 驱动程序内存
- `executorMemory`: 执行器内存

### SchemaConfig
- `idField`: ID 字段名称

## 🔐 Security Best Practices

1. **不要提交包含真实凭据的配置文件到版本控制**
2. **使用环境变量或密钥管理系统**
3. **为不同环境创建不同的配置文件**

## 🌍 Environment-specific Configs

```bash
# 开发环境
config/dev.json

# 测试环境
config/test.json

# 生产环境
config/prod.json
```

## ⚠️ 故障排除

如果遇到配置加载错误：
1. 检查配置文件路径是否正确
2. 验证 JSON 格式是否有效
3. 确保所有必需字段都已填写
4. 检查文件权限

## 📝 Examples

查看 `config/application.json` 获取完整的配置示例。 