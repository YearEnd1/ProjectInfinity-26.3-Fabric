# Infinite Dimensions — 26.3 Fabric 社区移植版

**无限维度** 的 Minecraft **26.3 / Fabric** 非官方移植，由 [YearEnd1](https://github.com/YearEnd1) 维护。

本项目基于 [ProjectInfinity](https://tangled.org/codexarchonic.nekoweb.org/ProjectInfinity)，保留上游 Git 历史、原作者署名及 **LGPL-3.0-or-later** 许可证。原作者为 **ArchonicCinnamon** 与 **cassiancc**；上游介绍和致谢见 [README.upstream.md](README.upstream.md)。

Unofficial Minecraft 26.3 Fabric port of Infinite Dimensions, with Simplified Chinese localization, optional Waystones integration, and world-generation compatibility fixes. This repository is independent of the upstream project.

## 下载与安装

从本仓库的 [Releases](https://github.com/YearEnd1/ProjectInfinity-26.3-Fabric/releases) 下载运行版 JAR，放入实例的 `mods` 目录。同一实例只保留一个 Infinite Dimensions JAR。

| 环境 | 要求 |
| --- | --- |
| Minecraft | 26.3 |
| 加载器 | Fabric Loader 0.20.1 或更新的兼容版本 |
| Java | 25 |
| 必需前置 | Fabric API、Architectury API |
| 可选配置界面 | Cloth Config、Mod Menu |
| 可选石碑联动 | Waystones 及其前置 Balm、Shogi |

本地验证使用 Fabric API `0.166.2+26.3`、Architectury API `22.0.5`、Waystones `26.3.0.1`、Balm 和 Shogi `26.3.0.3`。请使用对应 Minecraft 版本的前置。

将书本文本投入下界传送门，即可探索由文本决定的随机维度。完整原版玩法说明见 [上游文档](https://moddedmc.org/en/mod/infinite-dimensions/docs/Configuring-the-mod)。

## 此移植版包含的改动

- 适配 26.3 的地物、地形规则、密度函数和环境属性数据格式。
- 修复随机维度加载、方块状态转换、地物排序循环和植被重复偏移导致的越界读取。
- 迁移维度时钟、时间线、天空颜色和群系雾色，恢复普通天空的昼夜变化与时间指令。
- 内置简体中文，随机群系显示为“随机群系 #编号”；支持资源包覆盖具体群系名称。
- 可选 Waystones 联动：新区块稀疏生成石碑，玩家首次进入 Infinity 维度时在附近放置入口石碑，虚空入口可补小平台。生成记录持久化，重复进入不会重复生成。

石碑的激活方式、费用及传送规则沿用 Waystones 配置。未安装 Waystones 时不启用此联动。原本固定时间、下界或末地风格的维度会保留相应设定。

## 构建

安装 JDK 25，在仓库根目录执行：

```powershell
.\gradlew.bat :26.3-fabric:build --no-daemon
```

Linux / macOS：

```sh
./gradlew :26.3-fabric:build --no-daemon
```

产物位于 `versions/26.3-fabric/build/libs/`。无 `-sources` 后缀的 JAR 用于运行，`-sources.jar` 为源码附件。首次构建需要联网下载 Gradle、Minecraft 和开发依赖。

当前 Stonecutter 配置只构建 `26.3-fabric`。仓库保留了上游其他版本文件，但不代表本分支验证或支持了这些构建目标。

## 验证与已知限制

移植过程中已做原生服务端和开发客户端回归，覆盖随机维度解码、部分实际区块生成、地物排序、石碑注册与重启去重、昼夜光照、时间指令和动态群系翻译。

随机维度组合及第三方模组数量很大，测试未覆盖所有组合。旧版自定义天空渲染及部分渲染效果尚未完整移植；基础天空与时钟已适配。报告问题时，请提供 Minecraft、加载器和模组版本及相关日志，并先移除日志中的访问令牌等登录信息。

本分支的问题请提交到 [本仓库 Issues](https://github.com/YearEnd1/ProjectInfinity-26.3-Fabric/issues)。

## 来源与许可证

上游起点：[`c26f116e3f2ee57fa4207c0bce00d61a00d8d7f9`](https://tangled.org/codexarchonic.nekoweb.org/ProjectInfinity/commit/c26f116e3f2ee57fa4207c0bce00d61a00d8d7f9)。

本分支沿用 **LGPL-3.0-or-later**，未改用其他许可证。参见 [LICENSE](LICENSE)、其引用的 [GPL 正文](COPYING) 和 [NOTICE](NOTICE)。原作者与第三方贡献者的署名和上游致谢均予保留。
