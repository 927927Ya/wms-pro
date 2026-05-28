package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.InboundDetails;
import com.d0dd.wms.mapper.InboundDetailsMapper;
import com.d0dd.wms.service.InboundDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

@Service
public class InboundDetailsServiceImpl implements InboundDetailsService {

    @Autowired
    private InboundDetailsMapper inboundDetailsMapper;

    @Override
    public InboundDetails getById(Long id) {
        return inboundDetailsMapper.selectById(id);
    }

    @Override
    public List<InboundDetails> list(InboundDetails inboundDetails) {
        return inboundDetailsMapper.selectList(inboundDetails);
    }
    
    @Override
    public List<InboundDetails> getByInboundId(Long inboundId) {
        return inboundDetailsMapper.selectByInboundId(inboundId);
    }

    @Override
    public int save(InboundDetails inboundDetails) {
        if (inboundDetails.getId() == null) {
            inboundDetails.setId(generateId());
        }
        return inboundDetailsMapper.insert(inboundDetails);
    }

    private long generateId() {
        long now = System.currentTimeMillis();
        int r = ThreadLocalRandom.current().nextInt(1000);
        return now * 1000 + r;
    }

    @Override
    public int update(InboundDetails inboundDetails) {
        return inboundDetailsMapper.update(inboundDetails);
    }

    @Override
    public int removeById(Long id) {
        return inboundDetailsMapper.deleteById(id);
    }
    
    @Override
    public int removeByInboundId(Long inboundId) {
        return inboundDetailsMapper.deleteByInboundId(inboundId);
    }
}
