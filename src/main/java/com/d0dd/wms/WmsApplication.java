package com.d0dd.wms;

import org.apache.shiro.spring.boot.autoconfigure.ShiroAnnotationProcessorAutoConfiguration;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.apache.shiro.spring.boot.autoconfigure.ShiroBeanAutoConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebMvcAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Locale;

@SpringBootApplication(exclude = {
        ShiroAutoConfiguration.class,
        ShiroBeanAutoConfiguration.class,
        ShiroAnnotationProcessorAutoConfiguration.class,
        ShiroWebAutoConfiguration.class,
        ShiroWebFilterConfiguration.class,
        ShiroWebMvcAutoConfiguration.class
})
public class WmsApplication {
    private static final Logger log = LoggerFactory.getLogger(WmsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WmsApplication.class, args);
    }

    @Bean
    public CommandLineRunner schemaMigrationRunner(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                String product = connection.getMetaData().getDatabaseProductName();
                String productLower = product == null ? "" : product.toLowerCase(Locale.ROOT);
                if (!(productLower.contains("mysql") || productLower.contains("mariadb"))) {
                    return;
                }

                Integer exists = jdbcTemplate.queryForObject(
                        "select count(*) from information_schema.COLUMNS " +
                                "where TABLE_SCHEMA = DATABASE() and TABLE_NAME = 'outbound' and COLUMN_NAME = 'outbound_status'",
                        Integer.class
                );

                if (exists != null && exists == 0) {
                    jdbcTemplate.execute(
                            "alter table outbound " +
                                    "add column outbound_status varchar(10) null comment '出库状态，参考sys_dict表' after outbound_type"
                    );
                    jdbcTemplate.update("update outbound set outbound_status = '0' where outbound_status is null");
                    log.info("Schema migration applied: outbound.outbound_status added");
                }

                jdbcTemplate.execute(
                        "create table if not exists authorities (" +
                                "authority_id bigint(20) not null auto_increment comment '权限ID'," +
                                "authority_name varchar(100) not null comment '权限名称'," +
                                "authority_key varchar(100) default null comment '权限标识'," +
                                "parent_id bigint(20) default 0 comment '父级ID'," +
                                "order_num int(11) default 0 comment '排序'," +
                                "status char(1) default '0' comment '状态（0正常 1停用）'," +
                                "create_by varchar(64) default '' comment '创建者'," +
                                "create_time datetime default null comment '创建时间'," +
                                "update_by varchar(64) default '' comment '更新者'," +
                                "update_time datetime default null comment '更新时间'," +
                                "remark varchar(500) default null comment '备注'," +
                                "primary key (authority_id)" +
                                ") engine=InnoDB default charset=utf8mb4 comment='权限表'"
                );

                jdbcTemplate.execute(
                        "create table if not exists auth_role (" +
                                "role_id bigint(20) not null comment '角色ID'," +
                                "authority_id bigint(20) not null comment '权限ID'," +
                                "primary key (role_id, authority_id)" +
                                ") engine=InnoDB default charset=utf8mb4 comment='角色与权限关联表'"
                );

                jdbcTemplate.update(
                        "insert ignore into authorities " +
                                "(authority_id, authority_name, authority_key, parent_id, order_num, status, create_by, create_time, update_by, update_time, remark) values " +
                                "(1, '超级权限', '*', 0, 1, '0', 'admin', now(), 'admin', now(), '管理员全权限')," +
                                "(2, '仓库模块', null, 0, 10, '0', 'admin', now(), 'admin', now(), null)," +
                                "(3, '仓库管理', 'warehouse:manage', 2, 1, '0', 'admin', now(), 'admin', now(), null)," +
                                "(4, '产品模块', null, 0, 20, '0', 'admin', now(), 'admin', now(), null)," +
                                "(5, '产品管理', 'prod:manage', 4, 1, '0', 'admin', now(), 'admin', now(), null)," +
                                "(6, '库存模块', null, 0, 30, '0', 'admin', now(), 'admin', now(), null)," +
                                "(7, '库存查看', 'inventory:view', 6, 1, '0', 'admin', now(), 'admin', now(), null)," +
                                "(8, '入库模块', null, 0, 40, '0', 'admin', now(), 'admin', now(), null)," +
                                "(9, '入库单管理', 'inbound:manage', 8, 1, '0', 'admin', now(), 'admin', now(), null)," +
                                "(10, '收货任务', 'inbound:receive', 8, 2, '0', 'admin', now(), 'admin', now(), null)," +
                                "(11, '出库模块', null, 0, 50, '0', 'admin', now(), 'admin', now(), null)," +
                                "(12, '出库单管理', 'outbound:manage', 11, 1, '0', 'admin', now(), 'admin', now(), null)," +
                                "(13, '拣货任务', 'outbound:pick', 11, 2, '0', 'admin', now(), 'admin', now(), null)," +
                                "(14, '客户模块', null, 0, 60, '0', 'admin', now(), 'admin', now(), null)," +
                                "(15, '客户管理', 'customer:manage', 14, 1, '0', 'admin', now(), 'admin', now(), null)," +
                                "(16, '系统模块', null, 0, 70, '0', 'admin', now(), 'admin', now(), null)," +
                                "(17, '用户管理', 'sys:user:manage', 16, 1, '0', 'admin', now(), 'admin', now(), null)," +
                                "(18, '用户新增', 'sys:user:add', 17, 1, '0', 'admin', now(), 'admin', now(), null)," +
                                "(19, '用户编辑', 'sys:user:edit', 17, 2, '0', 'admin', now(), 'admin', now(), null)," +
                                "(20, '用户删除', 'sys:user:remove', 17, 3, '0', 'admin', now(), 'admin', now(), null)," +
                                "(21, '角色管理', 'sys:role:manage', 16, 2, '0', 'admin', now(), 'admin', now(), null)," +
                                "(22, '角色新增', 'sys:role:add', 21, 1, '0', 'admin', now(), 'admin', now(), null)," +
                                "(23, '角色编辑', 'sys:role:edit', 21, 2, '0', 'admin', now(), 'admin', now(), null)," +
                                "(24, '角色删除', 'sys:role:remove', 21, 3, '0', 'admin', now(), 'admin', now(), null)"
                );

                Long adminRoleId = ensureRole(jdbcTemplate, "管理员", "admin", 1, "系统管理员");
                Long productManagerRoleId = ensureRole(jdbcTemplate, "产品管理员", "product_manager", 2, "负责产品与基础资料维护");
                Long assetManagerRoleId = ensureRole(jdbcTemplate, "仓库管理员", "asset_manager", 3, "负责库存与出入库单管理");
                Long pickingClerkRoleId = ensureRole(jdbcTemplate, "拣货员", "picking_clerk", 4, "负责完成出库拣货");
                Long receivingClerkRoleId = ensureRole(jdbcTemplate, "收货员", "receiving_clerk", 5, "负责完成入库收货");

                Long warehouseManageId = getAuthorityId(jdbcTemplate, "warehouse:manage");
                Long prodManageId = getAuthorityId(jdbcTemplate, "prod:manage");
                Long inventoryViewId = getAuthorityId(jdbcTemplate, "inventory:view");
                Long inboundManageId = getAuthorityId(jdbcTemplate, "inbound:manage");
                Long inboundReceiveId = getAuthorityId(jdbcTemplate, "inbound:receive");
                Long outboundManageId = getAuthorityId(jdbcTemplate, "outbound:manage");
                Long outboundPickId = getAuthorityId(jdbcTemplate, "outbound:pick");
                Long customerManageId = getAuthorityId(jdbcTemplate, "customer:manage");
                Long sysUserManageId = getAuthorityId(jdbcTemplate, "sys:user:manage");
                Long sysUserAddId = getAuthorityId(jdbcTemplate, "sys:user:add");
                Long sysUserEditId = getAuthorityId(jdbcTemplate, "sys:user:edit");
                Long sysUserRemoveId = getAuthorityId(jdbcTemplate, "sys:user:remove");
                Long sysRoleManageId = getAuthorityId(jdbcTemplate, "sys:role:manage");
                Long sysRoleAddId = getAuthorityId(jdbcTemplate, "sys:role:add");
                Long sysRoleEditId = getAuthorityId(jdbcTemplate, "sys:role:edit");
                Long sysRoleRemoveId = getAuthorityId(jdbcTemplate, "sys:role:remove");

                linkRoleAuthority(jdbcTemplate, adminRoleId, warehouseManageId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, prodManageId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, inventoryViewId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, inboundManageId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, inboundReceiveId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, outboundManageId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, outboundPickId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, customerManageId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, sysUserManageId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, sysUserAddId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, sysUserEditId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, sysUserRemoveId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, sysRoleManageId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, sysRoleAddId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, sysRoleEditId);
                linkRoleAuthority(jdbcTemplate, adminRoleId, sysRoleRemoveId);

                linkRoleAuthority(jdbcTemplate, productManagerRoleId, prodManageId);

                linkRoleAuthority(jdbcTemplate, assetManagerRoleId, inventoryViewId);
                linkRoleAuthority(jdbcTemplate, assetManagerRoleId, inboundManageId);
                linkRoleAuthority(jdbcTemplate, assetManagerRoleId, outboundManageId);

                unlinkRoleAuthority(jdbcTemplate, pickingClerkRoleId, inboundReceiveId);
                unlinkRoleAuthority(jdbcTemplate, receivingClerkRoleId, outboundPickId);

                linkRoleAuthority(jdbcTemplate, pickingClerkRoleId, outboundPickId);
                linkRoleAuthority(jdbcTemplate, receivingClerkRoleId, inboundReceiveId);
            } catch (Exception e) {
                log.warn("Schema migration skipped/failed", e);
            }
        };
    }

    private static Long getAuthorityId(JdbcTemplate jdbcTemplate, String authorityKey) {
        return queryLongOrNull(
                jdbcTemplate,
                "select authority_id from authorities where authority_key = ? limit 1",
                authorityKey
        );
    }

    private static Long ensureRole(JdbcTemplate jdbcTemplate, String roleName, String roleKey, int roleSort, String remark) {
        Long existing = queryLongOrNull(
                jdbcTemplate,
                "select role_id from sys_role where role_key = ? limit 1",
                roleKey
        );
        if (existing != null) {
            return existing;
        }
        jdbcTemplate.update(
                "insert into sys_role (role_name, role_key, role_sort, status, del_flag, create_by, create_time, update_by, update_time, remark) " +
                        "values (?, ?, ?, '0', '0', 'admin', now(), 'admin', now(), ?)",
                roleName, roleKey, roleSort, remark
        );
        return queryLongOrNull(
                jdbcTemplate,
                "select role_id from sys_role where role_key = ? limit 1",
                roleKey
        );
    }

    private static void linkRoleAuthority(JdbcTemplate jdbcTemplate, Long roleId, Long authorityId) {
        if (roleId == null || authorityId == null) {
            return;
        }
        jdbcTemplate.update(
                "insert ignore into auth_role (role_id, authority_id) values (?, ?)",
                roleId, authorityId
        );
    }

    private static void unlinkRoleAuthority(JdbcTemplate jdbcTemplate, Long roleId, Long authorityId) {
        if (roleId == null || authorityId == null) {
            return;
        }
        jdbcTemplate.update(
                "delete from auth_role where role_id = ? and authority_id = ?",
                roleId, authorityId
        );
    }

    private static Long queryLongOrNull(JdbcTemplate jdbcTemplate, String sql, Object arg) {
        java.util.List<Long> rows = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong(1), arg);
        if (rows == null || rows.isEmpty()) {
            return null;
        }
        return rows.get(0);
    }
}
