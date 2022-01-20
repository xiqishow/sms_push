# SMS Push

SMS Push是一款短信转发的应用，可以将android系统接收的短信以SMTP方式发送到指定的邮箱。

### 背景
我有两部手机，一部iPhone 12作为主力机，另一部是android的小米9Se，其中小米9se的两个号码用于接收各种物流、营销或其他不重要的电话或短信。虽然电话可以通过“呼叫转移”转至iPhone，但短信缺一直没有特别好的方式转移，每次接收验证码还需要从包里翻出来查看。

### 解决问题
通过这个应用，将短信发送到自己的邮箱，并在iPhone 邮件应用或微信（如QQ邮箱、企业邮箱）或其它第三方的邮件应用配置接收邮箱，能做到及时的短信推送到iPhone。
在推送短信的时候，也携带了电量信息，方便及时提醒对手机进行充电，防止长时间忽略导致备用机缺电关机。并且在系统提示电量低的时候（MIUI为15%）及时发送邮件提示充电。

### 使用和编译
直接在Anroid Studio中导入项目代码，连接设备运行即可。

### 注意事项

* 开启应用自启动（MIUI为“应用信息”->“自启动”）
* 授予应用“读取短信”的权限，在MIUI系统中也许给予“通知类短信”，否则一些验证码短信无法读取
* 进入应用信息的“省电策略”，请在后台配置中选择“无限制”，防止应用进程被系统杀死
* 请将应用锁定在任务管理器中，防止关闭所有应用时将应用杀死
* 一些系统还需要给予应用通知栏显示的权限，否则应用前台服务可能无法运行
* 本人仅在MIUI 12.5上使用和测试

### 分发和运行
由于短信是比较敏感的个人信息，因此不建议使用二进制版本的方式进行分发，请自行打包运行。另外短信完全是通过SMTP发送邮件进行转发，因此请保证收发邮箱和服务是安全的，防止造成安全信息泄漏。项目中也提供了二进制APK，请自行考虑安装

License
-------

    Copyright 2021 xiqishow

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.