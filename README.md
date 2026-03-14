# spring-tutorial-23rd
CEOS 백엔드 23기 스프링 튜토리얼

---

### 1. spring-tutorial-23rd를 완료해요!
참고: [인텔리제이 설치](https://melodyblue.tistory.com/34)

### 2. spring이 지원하는 기술들(IoC/DI, AOP, PSA 등)을 자유롭게 조사해요

Spring의 삼각형
 - IoC/DI & AOP & PSA

IoC
 - 객체의 호출을 스프링이 대신 해주는 것
 - 직접 생성 x

DI
- 빈을 스프링 컨테이너에 넣어주는 것

```
class A {

    private B b;   // 선언만

    A(B b){  // DI
        this.b = b;
    }

    void run() {
        b.hello();
    }
}
```
AOP
- 모듈별로 중복해서 나타나는 횡단 관심사를 분리
- 공통 코드를 각 클래스에 쓰지 않고 한 곳에서만 작성해서 자동으로 실행되도록

PSA
- 오라클, MySQL, MS-SQL 등과 같은 다양한 DBMS의 종류에 상관없이
  Connection, Statement, ResultSet을 이용해 공통된 방식으로 코드를 작성할 수 있음


### 3. Spring Bean이 무엇이고, Bean의 라이프사이클과 Bean Scope에 대해 조사해요
Spring Bean: 스프링에서 관리하는 객체
대표적인 빈: Controller, Service, Repository

빈 생명 주기
- 생성 -> 초기화 -> (프로그램에서 사용) -> 소멸
```
@Component
class TestBean {

    public TestBean() {
        System.out.println("생성");
    }

    @PostConstruct
    public void init(){
        System.out.println("초기화");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("소멸");
    }

}
```

빈 스코프
- 빈이 존재할 수 있는 범위

| Scope       | Description                                                                                    |
|-------------|------------------------------------------------------------------------------------------------|
| singleton   | (Default) 각 스프링 컨테이너에 대한 단일 객체 인스턴스에 대한 단일 bean definition의 범위를 지정합니다                          |
| prototype   | 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프입니다.                                            |
| request     | 웹 요청이 들어오고 나갈 때까지 유지되는 스코프입니다.                                                                 |
| session     | 웹 세션이 생성되고 종료될 때까지 유지되는 스코프입니다.                                                                |
| application | 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프입니다.                                                                |
| websocket   | 단일 bean definition 범위를 WebSocket의 라이프사이클까지 확장합니다. Spring ApplicationContext의 Context에서만 유효합니다. |


#### 3.1 어노테이션이란 무엇이며, Java에서 어떻게 구현될까요?
어노테이션이란
- 코드에 추가적인 정보를 제공하는 메타데이터
- 어노테이션은 프로그램의 컴파일, 배포, 실행 과정에서 다양한 기능을 수행하도록 영향을 줄 수 있음

어노테이션 구현
```
public @interface MyAnnotation { // 어노테이션 만들기
}
```
```
@MyAnnotation // 어노테이션 사용 -> 스프링이 실행 할 때 Test에 어노테이션이 있구나
class Test {
}
```

#### 3.2 스프링에서 어노테이션을 통해 Bean을 등록할 때, 어떤 일련의 과정이 일어나는지 탐구해보세요.
#### 3.3 `@ComponentScan` 과 같은 어노테이션을 사용하여 스프링이 컴포넌트를 어떻게 탐색하고 찾는지의 과정을 깊게 파헤쳐보세요.

1) 프로그램 시작 (SpringApplication.run)
2) ComponentScan 실행 (패키지 탐색)
3) 어노테이션 검사 (@Component, @Service 등 발견)
4) BeanDefinition 생성 (Bean 정보 저장)
5) 객체 생성 (Bean 인스턴스 생성)
6) 의존성 주입 (@Autowired)
7) 초기화 (@PostConstruct 실행)
8) 컨테이너에서 관리 (ApplicationContext가 Bean 관리)

<details>
<summary>코드 예시</summary>

```
// 1️⃣ 프로그램 시작
// Spring Boot 애플리케이션 실행
@SpringBootApplication   // 내부에 @ComponentScan 포함
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        // Spring 컨테이너(ApplicationContext) 생성
    }
}


// 2️⃣ ComponentScan 실행
// Spring이 현재 패키지와 하위 패키지를 탐색하여
// Bean으로 등록할 클래스를 찾기 시작


@Service
// 3️⃣ 어노테이션 검사
// Spring이 클래스들을 검사하다가
// @Service 발견 → Bean 후보로 판단
class UserService {

    public void login(){
        System.out.println("login");
    }
}


// 4️⃣ BeanDefinition 생성
// Spring 내부에서 UserService에 대한 BeanDefinition 생성
// (객체 설계 정보 저장)
// 예: 클래스 이름, scope(singleton), 생성 방법 등


@Service
class OrderService {

    // 6️⃣ 의존성 주입 (Dependency Injection)
    // Spring이 UserService Bean을 찾아서
    // 이 변수에 자동으로 넣어줌
    @Autowired
    UserService userService;

    public void order(){
        userService.login();
    }


    // 7️⃣ 초기화
    // Bean 생성 후 실행되는 메서드
    @PostConstruct
    public void init(){
        System.out.println("OrderService 초기화");
    }

}


// 5️⃣ 객체 생성
// Spring이 BeanDefinition을 기반으로 실제 객체 생성
// new UserService()
// new OrderService()


// 8️⃣ 컨테이너에서 관리
// 생성된 Bean들은 Spring Container(ApplicationContext)에 저장
// 이후 다른 클래스에서 @Autowired로 주입해서 사용
```

</details>

#### 3.4 (선택) 하나의 interface를 구현한 service가 여러개 있을때 어떻게 주입 해야할까요?
```
@Autowired
@Qualifier("kakaoPayService") // Qualifier로 해결
PaymentService paymentService;
```
```
@Primary // Primary로 해결
@Service 
class KakaoPayService implements PaymentService {}
```


### 4. 🔥Spring MVC를 심층 분석해요🔥
#### 4.1 MVC 패턴과 Spring MVC는 어떻게 다를까요?
Spring MVC
- MVC 패턴을 웹 환경에서 쉽게 사용할 수 있게 만든 프레임워크
- 기본 MVC는 구조만 정의하고 요청 처리 방법을 직접 구현해야 했음
#### 4.2 Servlet은 무엇이고 웹 요청이 어떻게 처리될까요?
Servlet
- Java로 웹 요청을 처리하는 서버 프로그램
```
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res) throws IOException {

        res.getWriter().println("hello");
    }
}
```
#### 4.3 톰캣이 무엇이고 WAS는 무엇일까요?
톰캣
- Java 웹 애플리케이션을 실행하는 서버
- HTTP 요청 받기 -> Servlet 실행 -> 응답 반환
WAS
- 웹 애플리케이션 실행 서버
#### 4.4 Dispatcher Servlet은 무엇이고 어떻게 동작하는지 직접 흐름을 분석해요.
DispatcherServlet
- Spring MVC의 중앙 컨트롤러
구조 
```
브라우저
   ↓
Tomcat
   ↓
DispatcherServlet
   ↓
Controller
   ↓
Service
   ↓
View
```


