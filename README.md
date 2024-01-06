# 컨테이너 기반 MSA(MircroService Architecture)

## Eureka + Spring Cloud Bus
![image](https://github.com/siawase7179/eureka/assets/152139618/cbbad898-d978-440a-b7fc-b4c1922fc561)

``` java
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@EnableConfigServer
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
```

> [!NOTE]
> private git hub은 유료이기 때문에 무료 버전인 docker git을 사용하였다.

![image](https://github.com/siawase7179/eureka/assets/152139618/615e3bcc-414b-49b6-9212-15fcd84c8700)

+ configuration file 을 remote repository 에 push 한다.

+ Spring Cloud Bus 가 Message Broker 로 변경된 설정 정보에 대한 Message 를 발행한다.

+ 설정 정보가 변경되었음을 Config Server 에게 알려준다.

+ Message Broker 가 해당 메시지를 Subscribing 하고 있는 Application 들 에게 Broadcasting 한다.

+ 각각의 Application 은 Spring Cloud Bus 가 받은 설정 정보를 반영한다.

``` yaml
management:
  endpoints:
    web:
      exposure:
        include: bus-refresh

spring:
  rabbitmq:
    addresses: rabbitM:5672,rabbitS:5672  
    username: newgw
    password: pwnewgw
    virtual-host: mq_test
  cloud:
    config:
      server:
        git:
          uri: ssh://git@git-server:2222/git-server/repos/config.git
          ignore-local-ssh-settings: true
          private-key: |
            -----BEGIN RSA PRIVATE KEY-----
            MIIJKQIBAAKCAgEAwAW0gP64kcXfmeP4MWSqSof2ZcY+2mJEu2W4h5tRax+H9njc
            J7wd+Bq0vo3QkXkat29TpZ4JKiJfZhdC2wixlI0y/S4w0JSfWyVeyUx3yw4Q7DRB
            ....
            -----END RSA PRIVATE KEY-----
```

![image](https://github.com/siawase7179/eureka/assets/152139618/cdb68614-47dc-46c7-8315-2de50985d0d9)

config server에게 알려주면 각 application이 설정정보를 읽어간다.

-------

## Spring Cloud Gateway

+ Spring 생테계를 기반으로 하는 API Gateway를 제공해주는 프로젝트

+ Spring Cloud Gateway는 효율적인 방법으로 API를 라우팅하는 방법을 제공함

![image](https://github.com/siawase7179/eureka/assets/152139618/17ce84e6-7bf8-411f-8d7a-450202c7dd5d)


``` yaml
spring:
  cloud:
    gateway:
      routes:
        - id: rest-server
          uri: lb://REST-SERVER
          predicates:
            - Path=/v1/token
            - Method=POST
          filters:
            - name: AuthenticateFilter
```

> [!NOTE]
> pre 필터를 통해 라우팅이 가능

``` java
@Component
public class AuthenticateFilter extends AbstractGatewayFilterFactory<AuthenticateFilter.Config> {    

    public AuthenticateFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey("X-Client-id")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                throw new AuthenticateException(HttpStatus.BAD_REQUEST, "90003", "X-Client-Id not set");
            }

            if(!request.getHeaders().containsKey("X-Client-Password")){
                throw new AuthenticateException(HttpStatus.BAD_REQUEST,"90004", "Client-Password not set");
            }

            return chain.filter(exchange) ;
        });
    }

    public static class Config {

    }
}
```
헤더 값에 필수 값이 없는 경우 Exception처리로 실패처리 하였다.
