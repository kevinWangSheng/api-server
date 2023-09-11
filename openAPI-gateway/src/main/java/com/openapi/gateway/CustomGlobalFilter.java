package com.openapi.gateway;


import com.dubbo.model.GetLoginUser;
import com.dubbo.model.dto.InvokeKeyDto;
import com.dubbo.model.entity.InterfaceInfo;
import com.dubbo.model.entity.User;
import com.dubbo.model.entity.UserInterfaceInfo;
import com.dubbo.service.*;
import com.dubbo.utils.InvokeKeyUtils;
import io.netty.util.internal.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wang
 * @create 2023-2023-02-22:17
 */
@Configuration
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerUserService innerUserService;
    private int randonSaveNum = 0;
    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;

    @DubboReference
    private ParamVerify paramVerify;

    @DubboReference
    private AccessKeyPublishDelayedMessage publishDelayedMessage;
    List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        if(request!=null && request.getPath().toString().equals("/api/user/login")){
            return chain.filter(exchange);
        }

        // 如果传递进来的参数不合理
        String sourceIp = exchange.getRequest().getLocalAddress().getHostString();
        InvokeKeyDto invokeKeyDto = InvokeKeyUtils.getInvokeKeyDto(headers);
        if(!IP_WHITE_LIST.contains(sourceIp) || !paramVerify.isVaild(invokeKeyDto)){
            log.info("拒绝访问");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        String accessKey = headers.getFirst("accessKey");
        User invokeUser = innerUserService.getInvokeUser(accessKey);
        InterfaceInfo interfaceInfo = interfaceInfoService.getInterface(headers.getFirst("path"), headers.getFirst("method"));
        if(interfaceInfo==null){
            throw new RuntimeException("没有可以调用的接口，请确认你是否能够调用该接口");
        }
        if(randonSaveNum++%10==0) {
            publishDelayedMessage.sendDelayedMessage(accessKey, 400);
        }
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatusCode statusCode = originalResponse.getStatusCode();
            UserInterfaceInfo invokeInterface = innerUserInterfaceInfoService.canInvoke(interfaceInfo.getId(), invokeUser.getId());
            if(statusCode == HttpStatus.OK){
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 调用接口执行+1操作。
                                boolean result = innerUserInterfaceInfoService.invokeCount(interfaceInfo.getId(), invokeUser.getId(), invokeInterface.getVersion());
                                // 因为他调用的参数都是一样的，所以如果出现并发问题，并且只是因为并发修改调用次数的问题，那么只需要循环修改就行，否则如果返回的剩余次数 <=0,则直接抛出异常。
                                while(!result){
                                    UserInterfaceInfo userInterfaceInfo = innerUserInterfaceInfoService.canInvoke(interfaceInfo.getId(), invokeUser.getId());
                                    if(userInterfaceInfo == null || userInterfaceInfo.getLeftNum()<=0){
                                        throw new RuntimeException("你已经没有调用次数了");
                                    }
                                    result = innerUserInterfaceInfoService.invokeCount(interfaceInfo.getId(), invokeUser.getId(), userInterfaceInfo.getVersion());
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }



    @Override
    public int getOrder() {
        return -1;
    }

}
