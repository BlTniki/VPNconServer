package com.bitniki.VPNconServer.modules.subscription.service.impl;


import com.bitniki.VPNconServer.modules.peer.service.PeerService;
import com.bitniki.VPNconServer.modules.subscription.repository.SubscriptionRepo;
import com.bitniki.VPNconServer.modules.subscription.repository.UserSubscriptionRepo;
import com.bitniki.VPNconServer.modules.subscription.service.SubscriptionService;
import com.bitniki.VPNconServer.modules.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private SubscriptionRepo subscriptionRepo;
    @Autowired
    private UserSubscriptionRepo userSubscriptionRepo;
    @Autowired
    PeerService peerService;


    private final String notifyToPay = "Хей! Завтра сгорит твоя подписка! Думаю что стоит продлить";
    private final String notifyAboutExpire = "Твоя подписка сгорела из-за неуплаты(";


}
