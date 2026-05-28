package com.d0dd.wms.service.impl;

import com.d0dd.wms.dto.InboundDTO;
import com.d0dd.wms.dto.InboundQueryDto;
import com.d0dd.wms.entity.Inbound;
import com.d0dd.wms.entity.InboundDetails;
import com.d0dd.wms.mapper.InboundDetailsMapper;
import com.d0dd.wms.mapper.InboundMapper;
import com.d0dd.wms.mapper.ProdSkuMapper;
import com.d0dd.wms.service.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class InboundServiceImpl implements InboundService {

    @Autowired
    private InboundMapper inboundMapper;
    
    @Autowired
    private InboundDetailsMapper inboundDetailsMapper;

    @Autowired
    private ProdSkuMapper prodSkuMapper;

    @Override
    public Inbound getById(Long id) {
        return inboundMapper.selectById(id);
    }

    @Override
    public List<Inbound> list(Inbound inbound) {
        return inboundMapper.selectList(inbound);
    }
    
    @Override
    public List<Inbound> list(InboundQueryDto queryDto) {
        return inboundMapper.selectListByDto(queryDto);
    }

    @Override
    public int save(Inbound inbound) {
        if (inbound.getId() == null) {
            inbound.setId(generateId());
        }
        return inboundMapper.insert(inbound);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createInbound(InboundDTO inboundDTO) {
        Inbound inbound = inboundDTO.getInbound();
        if (inboundDTO.getInboundDetails() == null || inboundDTO.getInboundDetails().isEmpty()) {
            throw new RuntimeException("Inbound details cannot be empty");
        }

        if (inbound.getSupplierId() == null && inbound.getCustomerId() != null) {
            inbound.setSupplierId(inbound.getCustomerId());
        }
        if (inbound.getCustomerId() == null && inbound.getSupplierId() != null) {
            inbound.setCustomerId(inbound.getSupplierId());
        }
        
        // Initial status: Created (0)
        inbound.setInboundStatus("0"); 

        if (inbound.getId() == null) {
            inbound.setId(generateId());
        }
        
        inboundMapper.insert(inbound);
        
        // Save details
        for (InboundDetails details : inboundDTO.getInboundDetails()) {
            if (details.getId() == null) {
                details.setId(generateId());
            }
            details.setInboundId(inbound.getId());
            if (details.getToReceivedQty() == null) {
                details.setToReceivedQty(BigDecimal.ZERO);
            }
            if (details.getReceivedQty() == null) {
                details.setReceivedQty(BigDecimal.ZERO);
            }
            if (details.getWeightUnit() == null || details.getWeightUnit().trim().isEmpty()) {
                details.setWeightUnit("1");
            }
            if (details.getSkuId() != null) {
                com.d0dd.wms.entity.ProdSku sku = prodSkuMapper.selectById(details.getSkuId());
                if (sku != null && sku.getWeight() != null) {
                    details.setTotalWeight(
                            sku.getWeight()
                                    .multiply(details.getToReceivedQty())
                                    .setScale(2, RoundingMode.HALF_UP)
                    );
                }
            }
            if (details.getTotalWeight() == null) {
                details.setTotalWeight(BigDecimal.ZERO);
            }
            details.setPalletCount(null);
            inboundDetailsMapper.insert(details);
        }
        
        return 1;
    }

    private long generateId() {
        long now = System.currentTimeMillis();
        int r = ThreadLocalRandom.current().nextInt(1000);
        return now * 1000 + r;
    }

    @Override
    public int update(Inbound inbound) {
        return inboundMapper.update(inbound);
    }

    @Override
    public int removeById(Long id) {
        return inboundMapper.deleteById(id);
    }
}
