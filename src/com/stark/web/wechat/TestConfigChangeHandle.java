package com.stark.web.wechat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sd4324530.fastweixin.api.config.ConfigChangeNotice;
import com.github.sd4324530.fastweixin.handle.AbstractApiConfigChangeHandle;

public class TestConfigChangeHandle extends AbstractApiConfigChangeHandle {

    private static final Logger LOG = LoggerFactory.getLogger(TestConfigChangeHandle.class);

    @Override
    public void configChange(ConfigChangeNotice notice) {
        LOG.debug("收到通知.....");
        LOG.debug(notice.toJsonString());
    }
}