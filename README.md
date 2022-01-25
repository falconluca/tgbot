# 🤖️ Telegram Bot

Telegram bot to automate my English vocabulary learning workflow

## Configuration

```yml
proxy:
  http:
    # HTTP 代理的主机地址
    host: "127.0.0.1"
    # HTTP 代理端口号
    port: 8010
    
telegram:
  bot:
    # 电报机器人的访问令牌，请向 BotFather 申请
    token: "XXX"
    # 电报机器人的唯一用户名，请向 BotFather 申请
    username: "XXX"
    # 电报机器人的创建者ID
    creatorId: 0

dingding:
  # 钉钉群聊机器人的密钥
  secret: "XXX"

eudb:
  # 欧陆词典的授权信息
  accessToken: "XXX"
  # 生效的生词本，默认使用默认生词本(dictionaryId为0)
  dictionaryId: "XXX"
```

## Feature

- [x] 实时翻译
- [x] 同步到欧陆词典
- [x] 朗文、牛津词典
- [x] 推送与单词相关的 YouTube 视频内容
- [x] SQLite 持久化
- [ ] 邮件、电报定期推送 SQLite 转 CSV 的存档
- [ ] 钉钉定时推送提醒背词
- [ ] 一键生成单词的 Anki 卡片
- [ ] 绘制并推送查词历史记录的艾宾浩斯遗忘曲线

