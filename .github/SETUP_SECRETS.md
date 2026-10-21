# GitHub Actions 配置

## 构建

推送 tag（如 `v2.3.2`）或手动触发 workflow 即可构建 release/debug APK。

```bash
# 打 tag 触发构建
git tag v2.3.2
git push origin v2.3.2

# 或在 GitHub Actions 页面手动触发
```

## 签名密钥配置

签名密钥 (`security/scorpion.jks`) 不在代码仓库中（已在 .gitignore 排除）。
通过 GitHub Secret 传入：

1. 本地生成 base64 编码的密钥文件：
   ```bash
   base64 -w 0 security/scorpion.jks
   ```

2. 在 GitHub 仓库 Settings → Secrets and variables → Actions → New repository secret：
   - Name: `SIGNING_KEYSTORE_BASE64`
   - Value: 上一步输出的 base64 字符串

如果不配置此 Secret，CI 会自动生成一个临时 debug 密钥用于构建（仅测试用，无法覆盖安装已签名的旧版本）。
