(日本語は英語の下にある)

# Apache wicket with spring boot example application: notice board
[https://github.com/saladlam/spring-wicket-noticeboard](https://github.com/saladlam/spring-wicket-noticeboard)

## Objective
I am writing this application for studying Apache wicket MVC framework.

## Function
- Display notice on stated time
- Approval before display
- Multi language UI

## Main component used
- Apache Wicket 9.X.X
- Spring Boot 2.6.X
- Spring Security 5.3.X
- Spring JDBC 5.3.X
- Liquibase 4.5.X
- Dozer 5.5.X

## Database
Data stored in embedded H2 database. Schema and data is imported when application start and all data will be lost after application shutdown.

## Prerequisite
- Java SE Development Kit 11 or above
- Internet connection

## Compile and run
Apache Maven wrapper is included, no addition package manager is necessary.

### Compile (On Microsoft Windows)
```
mvnw package
```

### Run (On Microsoft Windows)
```
mvnw spring-boot:run
```
Press Ctrl+C to stop.

## Account
| Username | Password | Authorities |
|:-------- |:-------- |:----------- |
| user1    | user1    | USER        |
| user2    | user2    | USER        |
| admin    | admin    | USER, ADMIN |


# Apache wicket共にspring bootサンプルアプリケーション: 掲示板
[https://github.com/saladlam/spring-wicket-noticeboard](https://github.com/saladlam/spring-wicket-noticeboard)

## 目的
このアプリケーションを作るのは、Apache wicket MVCフレームワークを勉強するため。

## 機能
- 決められた時メッセージを表示すること
- メッセージを表示する前、許可すること
- 多言語ユーザーインターフェイス

## 使われる主要なコンポーネント
- Apache Wicket 9.X.X
- Spring Boot 2.6.X
- Spring Security 5.3.X
- Spring JDBC 5.3.X
- Liquibase 4.5.X
- Dozer 5.5.X

## データベース
データは埋め込み H2 データベースに記憶する。スキーマとデータはアプリケーション起動時がインポートされ、それからアプリケーション終了時全てのデータがなくなった。

## 必要なもの
- Java SE Development Kit 11以降
- インタネット接続

## コンパイルと実行
Apache Mavenラッパーがついていて、だからパッケージマネージャが用意されなくでもいい。

### コンパイルする (Microsoft Windowsの場合)
```
mvnw package
```

### 実行する (Microsoft Windowsの場合)
```
mvnw spring-boot:run
```
アプリケーションを閉じたい時、Ctrl+Cを押してください。

## アカウント
| ユーザーネーム | パスワード | 権限 |
|:-------- |:-------- |:----------- |
| user1    | user1    | USER        |
| user2    | user2    | USER        |
| admin    | admin    | USER, ADMIN |
