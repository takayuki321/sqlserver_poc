# Spring MVC + MyBatis + MSSQL サンプルアプリケーション

このプロジェクトは、以下の技術スタックを使用したシンプルなWeb APIのデモです：

- **Spring Web MVC** (6.1.x)
- **MyBatis + MyBatis-Spring**
- **MSSQL (JNDI経由接続)**
- **WildFlyデプロイ対応（WARパッケージ）**

---

## 📁 プロジェクト構成

```
├── src/main/java/com/example/
│   ├── controller/HelloController.java
│   ├── service/HelloService.java
│   ├── mapper/HelloMapper.java
│   └── model/Hello.java
│
├── src/main/resources/mybatis/HelloMapper.xml
├── src/main/webapp/WEB-INF/
│   ├── applicationContext.xml
│   └── web.xml
└── pom.xml
```

---

## 🔧 ソースファイルの説明

### 1. HelloController.java

- エンドポイント: `/api/hello/{id}`
- HTTPリクエストを受け取り、サービス層へ委譲。

```java
@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    private HelloService helloService;

    @GetMapping("/{id}")
    public Hello getHello(@PathVariable("id") int id) {
        return helloService.getHello(id);
    }
}
```

---

### 2. HelloService.java

- ビジネスロジックを実行し、Mapperから取得した結果を返す。

```java
@Service
public class HelloService {
    @Autowired
    private HelloMapper helloMapper;

    public Hello getHello(int id) {
        return helloMapper.selectHello(id);
    }
}
```

---

### 3. HelloMapper.java / HelloMapper.xml

- SQLを呼び出すためのインターフェースとその定義ファイル（MyBatis）。

```java
@Mapper
public interface HelloMapper {
    Hello selectHello(int id);
}
```

```xml
<mapper namespace="com.example.mapper.HelloMapper">
    <select id="selectHello" parameterType="int" resultType="com.example.model.Hello">
        SELECT id, message FROM hello WHERE id = #{id}
    </select>
</mapper>
```

---

### 4. Hello.java

- DBの1レコードに対応するモデルクラス（DTO）。

```java
public class Hello {
    private int id;
    private String message;
    // getter/setter
}
```

---

## ⚙️ 設定ファイルの説明

### 5. applicationContext.xml

SpringのDI設定、MyBatisの構成、Mapperスキャンなどを定義。

```xml
<beans>
    <context:component-scan base-package="com.example" />

    <bean id="dataSource" class="org.springframework.jndi.JndiObjectFactoryBean">
        <property name="jndiName" value="java:/MSSQLDS"/>
    </bean>

    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="mapperLocations" value="classpath:mybatis/*.xml"/>
    </bean>

    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.example.mapper"/>
    </bean>
</beans>
```

---

### 6. web.xml

サーブレット定義およびSpring初期化設定。

```xml
<web-app>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/applicationContext.xml</param-value>
    </context-param>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/applicationContext.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/api/*</url-pattern>
    </servlet-mapping>
</web-app>
```

---

## ✅ 動作確認手順

1. WildFlyにJNDIリソース `java:/MSSQLDS` を登録。
2. WARビルドしてデプロイ。
3. 以下URLでJSONレスポンスが返れば成功：

```
GET http://localhost:8080/myapp/api/hello/1
```

---

## 🧩 使用ライブラリ（抜粋）

- spring-webmvc 6.1.x
- mybatis 3.5.x
- mybatis-spring 3.x
- jackson-databind 2.x
- mssql-jdbc 12.x

---

## 💬 補足

- JNDI接続を使わずに、`DriverManagerDataSource` で直指定も可能です（コメント参照）。
- JSONで返すために `jackson-databind` が必須。

---

## 📫 問い合わせ

改善や質問などありましたら Issue または PR でどうぞ！

