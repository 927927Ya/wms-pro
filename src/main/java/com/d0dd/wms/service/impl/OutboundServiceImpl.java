package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.OutboundDTO;
import com.d0dd.wms.dto.OutboundOrderQueryDto;
import com.d0dd.wms.entity.Outbound;
import com.d0dd.wms.entity.OutboundDetails;
import com.d0dd.wms.entity.OutboundPickingTask;
import com.d0dd.wms.mapper.OutboundDetailsMapper;
import com.d0dd.wms.mapper.OutboundMapper;
import com.d0dd.wms.mapper.OutboundPickingTaskMapper;
import com.d0dd.wms.service.OutboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OutboundServiceImpl implements OutboundService {

    @Autowired
    private OutboundMapper outboundMapper;
    
    @Autowired
    private OutboundDetailsMapper outboundDetailsMapper;

    @Autowired
    private OutboundPickingTaskMapper outboundPickingTaskMapper;

    @Override
    public Outbound getById(Long id) {
        Outbound outbound = outboundMapper.selectById(id);
        if (outbound == null || outbound.getId() == null) {
            return outbound;
        }

        if (!"2".equals(outbound.getOutboundStatus()) && isCompleted(outbound.getId())) {
            outbound.setOutboundStatus("2");
            outboundMapper.update(outbound);
        }

        return outbound;
    }

    @Override
    public List<Outbound> list(OutboundOrderQueryDto queryDto) {
        List<Outbound> outbounds = outboundMapper.selectList(queryDto);
        if (outbounds == null || outbounds.isEmpty()) {
            return outbounds;
        }

        for (Outbound outbound : outbounds) {
            if (outbound == null || outbound.getId() == null) {
                continue;
            }
            if ("2".equals(outbound.getOutboundStatus())) {
                continue;
            }
            if (isCompleted(outbound.getId())) {
                outbound.setOutboundStatus("2");
                outboundMapper.update(outbound);
            }
        }

        return outbounds;
    }

    private boolean isCompleted(Long outboundId) {
        List<OutboundDetails> detailsList = outboundDetailsMapper.selectByOutboundId(outboundId);
        if (detailsList == null || detailsList.isEmpty()) {
            return false;
        }

        for (OutboundDetails details : detailsList) {
            List<OutboundPickingTask> tasks = outboundPickingTaskMapper.selectByOutboundDetailsId(details.getId());
            if (tasks == null || tasks.isEmpty()) {
                return false;
            }

            boolean hasNonVoid = false;
            for (OutboundPickingTask task : tasks) {
                Integer taskStatus = task.getTaskStatus();
                if (taskStatus == null || taskStatus != 3) {
                    hasNonVoid = true;
                    if (!Integer.valueOf(1).equals(taskStatus)) {
                        return false;
                    }
                }
            }

            if (!hasNonVoid) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int save(Outbound outbound) {
        return outboundMapper.insert(outbound);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createOutbound(OutboundDTO outboundDTO) {
        Outbound outbound = outboundDTO.getOutbound();
        if (outboundDTO.getOutboundDetails() == null || outboundDTO.getOutboundDetails().isEmpty()) {
            throw new RuntimeException("Outbound details cannot be empty");
        }
        
        outbound.setOutboundStatus("0");
        outboundMapper.insert(outbound);
        
        for (OutboundDetails details : outboundDTO.getOutboundDetails()) {
            details.setOutboundId(outbound.getId());
            outboundDetailsMapper.insert(details);
        }
        
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOutbound(OutboundDTO outboundDTO) {
        Outbound outbound = outboundDTO.getOutbound();
        if (outbound == null || outbound.getId() == null) {
            throw new RuntimeException("Outbound order id is required");
        }
        outboundMapper.update(outbound);
        outboundDetailsMapper.deleteByOutboundId(outbound.getId());
        if (outboundDTO.getOutboundDetails() != null) {
            for (OutboundDetails details : outboundDTO.getOutboundDetails()) {
                details.setOutboundId(outbound.getId());
                outboundDetailsMapper.insert(details);
            }
        }
        return 1;
    }

    @Override
    public int update(Outbound outbound) {
        return outboundMapper.update(outbound);
    }

    @Override
    public int removeById(Long id) {
        return outboundMapper.deleteById(id);
    }
}
