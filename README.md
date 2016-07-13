﻿带有社交功能的android文章阅读软件
后台使用thinkphp

1.1.1文章阅读模块
负责显示文章，上下滑动，字体放缩。
1.1.2好友模块
负责添加好友和查看好友收藏的文章。
1.1.3其他模块(文章交互模块)
负责筛选文章，点赞文章，收藏文章，分享文章。
1.1.4用户登录模块
由注册模块和登录模块组层，注册模块负责将用户的注册信息保存到数据库，同时进行错误验证功能，登录模块负责检查用户提交的登录信息是否正确。
1.1.5服务器端模块
服务器端模块负责管理员的登陆和管理员对用户数据的修改。
1.2 模块层次结构

1.3 模块之间的关系	
1.3.1.依赖关系：
用户登录模块依赖于服务器模块提供的数据查询与新增功能。
其他模块依赖于文章阅读模块。
1.3.2.调用关系：
好友模块调用文章阅读模块和其他模块。
1.4 通信机制描述
在小道文章中，主要涉及的就是客户端和服务器端之间的通信。客户端请求指定的URL,并携带相关参数，服务器端收到请求，进行相关业务逻辑处理，之后将JSON格式的数据返回给客户端进行处理。
1.5 模块的核心接口
在小道文章客户端中，文章阅读模块通过调用一个工具类里面封装好的网络请求方法，请求指定网络地址，最后处理返回数据。
1.6 处理方式设计
在小道文章客户端中，每一次向服务器端请求数据时，服务器端都会在返回的JSON数据中加一个status字段来判断当前操作是否正确，然后客户端根据这个字段再执行页面跳转或者文章阅读等事务。