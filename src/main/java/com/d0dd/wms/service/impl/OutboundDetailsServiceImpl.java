package com.d0dd.wms.service.impl;

import com.d0dd.wms.entity.OutboundDetails;
import com.d0dd.wms.mapper.OutboundDetailsMapper;
import com.d0dd.wms.service.OutboundDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboundDetailsServiceImpl implements OutboundDetailsService {

    @Autowired
    private OutboundDetailsMapper outboundDetailsMapper;

    @Override
    public OutboundDetails getById(Long id) {
        return outboundDetailsMapper.selectById(id);
    }

    @Override
    public List<OutboundDetails> list(OutboundDetails outboundDetails) {
        return outboundDetailsMapper.selectList(outboundDetails);
    }
    
    @Override
    public List<OutboundDetails> getByOutboundId(Long outboundId) {
        return outboundDetailsMapper.selectByOutboundId(outboundId);
    }

    @Override
    public int save(OutboundDetails outboundDetails) {
        return outboundDetailsMapper.insert(outboundDetails);
    }

    @Override
    public int update(OutboundDetails outboundDetails) {
        return outboundDetailsMapper.update(outboundDetails);
    }

    @Override
    public int removeById(Long id) {
        return outboundDetailsMapper.deleteById(id);
    }
    
    @Override
    public int removeByOutboundId(Long outboundId) {
        return outboundDetailsMapper.deleteByOutboundId(outboundId);
    }
}
