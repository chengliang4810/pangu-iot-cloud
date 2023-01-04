package com.ruoyi.common.loadbalance.core;

import com.ruoyi.common.loadbalance.constant.LoadBalancerConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 自定义 Dubbo 负载均衡算法
 *
 * @author Lion Li
 */
@Slf4j
public class CustomDubboLoadBalancer extends AbstractLoadBalance {

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        for (Invoker<T> invoker : invokers) {
            if (invoker.getUrl().getHost().equals(LoadBalancerConstant.getHost())) {
                return invoker;
            }
        }
        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }
}
